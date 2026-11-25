package co.astrada.client.oauth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Logs in to the Astrada API with OAuth2 client credentials. Hand-written; not generated.
 *
 * <p>An OkHttp interceptor that requests a token from
 * {@code <basePath>/auth/realms/<accountId>/protocol/openid-connect/token}, adds it to every
 * request as {@code Authorization: Bearer <token>}, reuses it until 30 seconds before it expires (halfway, for a token that lives 60 s or less),
 * and on a 401 requests a new token and retries once. {@code Astrada.builder().credentials(...)}
 * sets it up; to add it to an {@code ApiClient} yourself:
 *
 * <pre>{@code
 * ClientCredentials auth = new ClientCredentials(Environment.SANDBOX, accountId, clientId, clientSecret);
 * client.setHttpClient(auth.addTo(client.getHttpClient()));
 * }</pre>
 *
 * <p>The token request uses its own HTTP client, so debug logging never shows the token response.
 */
public class ClientCredentials implements Interceptor {
    public static final String PRODUCTION = "https://api.astrada.co";
    public static final String SANDBOX = "https://api.sandbox.astrada.co";

    private static final long EXPIRY_MARGIN_MILLIS = 30_000L;

    private final String tokenUrl;
    private final String clientId;
    private final String clientSecret;
    private final OkHttpClient tokenClient;

    private String accessToken;
    private String grantedScope;
    private long expiresAtMillis;

    /** Logs in to the given environment's token URL. */
    public ClientCredentials(co.astrada.client.Environment environment, String accountId, String clientId,
                             String clientSecret) {
        this(environment.getBaseUrl(), accountId, clientId, clientSecret);
    }

    public ClientCredentials(String basePath, String accountId, String clientId, String clientSecret) {
        this(basePath, accountId, clientId, clientSecret, new OkHttpClient());
    }

    /** Same as the four-argument constructor, with the HTTP client used for token requests. */
    public ClientCredentials(String basePath, String accountId, String clientId, String clientSecret,
                             OkHttpClient tokenClient) {
        if (basePath == null || accountId == null || clientId == null || clientSecret == null) {
            throw new IllegalArgumentException("basePath, accountId, clientId and clientSecret are required");
        }
        this.tokenUrl = basePath.replaceAll("/+$", "") + "/auth/realms/" + accountId
                + "/protocol/openid-connect/token";
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        // Never follow a redirect: OkHttp would send the client secret to the new address.
        this.tokenClient = tokenClient.newBuilder().followRedirects(false).followSslRedirects(false).build();
    }

    /**
     * The scopes the token endpoint said the last token carries (its {@code scope} field,
     * space-separated), or null when it did not say or no token was requested yet.
     */
    public synchronized String getGrantedScope() {
        return grantedScope;
    }

    /** The API client's id. */
    public String getClientId() {
        return clientId;
    }

    /** Returns a copy of {@code httpClient} that logs in through this interceptor. */
    public OkHttpClient addTo(OkHttpClient httpClient) {
        return httpClient.newBuilder().addInterceptor(this).build();
    }

    /** The token URL this instance logs in at. */
    public String getTokenUrl() {
        return tokenUrl;
    }

    /** Returns a valid access token, requesting a new one when the cached one is about to expire. */
    public synchronized String getAccessToken() throws IOException {
        if (accessToken == null || System.currentTimeMillis() >= expiresAtMillis) {
            requestToken();
        }
        return accessToken;
    }

    /** Forgets the cached token, so the next request logs in again. */
    public synchronized void clear() {
        accessToken = null;
        expiresAtMillis = 0;
    }

    /**
     * Forgets the cached token only if it is {@code refused}: a token another thread fetched after
     * the refused one was sent is kept, so concurrent 401s on one stale token cause one new login.
     */
    public synchronized void clear(String refused) {
        if (refused != null && refused.equals(accessToken)) {
            clear();
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (request.header("Authorization") != null) {
            return chain.proceed(request);
        }
        String token = getAccessToken();
        Response response = chain.proceed(withToken(request, token));
        if (response.code() != 401) {
            return response;
        }
        response.close();
        clear(token);
        return chain.proceed(withToken(request, getAccessToken()));
    }

    private static Request withToken(Request request, String token) {
        return request.newBuilder().header("Authorization", "Bearer " + token).build();
    }

    private void requestToken() throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", clientId)
                .add("client_secret", clientSecret);
        FormBody form = formBuilder.build();
        Request request = new Request.Builder().url(tokenUrl).post(form).build();
        try (Response response = tokenClient.newCall(request).execute()) {
            ResponseBody body = response.body();
            String text = body == null ? "" : body.string();
            if (response.isRedirect() || (response.code() >= 300 && response.code() < 400)) {
                // The origin only: a redirect's query string can carry tokens.
                String location = response.header("Location");
                okhttp3.HttpUrl resolved = location == null ? null : request.url().resolve(location);
                String target = resolved == null ? "another address"
                        : resolved.scheme() + "://" + resolved.host() + ":" + resolved.port();
                throw new AuthenticationException(response.code(), "Authentication failed: the login answered with a redirect to "
                        + target + ". The SDK does not send the client secret there; "
                        + "check the environment or base URL (" + tokenUrl + ")");
            }
            JsonObject json = parse(text);
            if (!response.isSuccessful() || json == null || !json.has("access_token")) {
                String reason = json != null && json.has("error_description")
                        ? json.get("error_description").getAsString()
                        : "status " + response.code();
                throw new AuthenticationException(response.code(), "Authentication failed: " + reason);
            }
            accessToken = json.get("access_token").getAsString();
            grantedScope = json.has("scope") && !json.get("scope").isJsonNull() ? json.get("scope").getAsString() : null;
            long expiresIn = json.has("expires_in") ? json.get("expires_in").getAsLong() : 60L;
            // Renew 30 s early, or halfway through a token that lives 60 s or less, so a short-lived
            // token is still reused instead of a login on every request.
            long lifetime = expiresIn * 1000L;
            expiresAtMillis = System.currentTimeMillis() + lifetime - Math.min(EXPIRY_MARGIN_MILLIS, lifetime / 2);
        }
    }

    private static JsonObject parse(String text) {
        try {
            JsonElement element = JsonParser.parseString(text);
            return element.isJsonObject() ? element.getAsJsonObject() : null;
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * The token request was refused (wrong credentials, wrong account, or no access), or the token
     * endpoint answered 429 or a 5xx; {@link #getStatus()} says which.
     */
    public static class AuthenticationException extends IOException {
        private final int status;

        public AuthenticationException(int status, String message) {
            super(message);
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }
}
