package co.astrada.client;

import co.astrada.client.http.RetryInterceptor;
import co.astrada.client.oauth.ClientCredentials;
import co.astrada.client.oauth.UnexpectedScopesException;
import java.time.Duration;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Builds an {@link ApiClient} that is ready to use: environment, login, timeouts and retries.
 * Hand-written; not generated.
 *
 * <pre>{@code
 * ApiClient client = Astrada.builder()
 *     .environment(Environment.SANDBOX)
 *     .credentials(accountId, clientId, clientSecret)
 *     .build();
 * new SubaccountsApi(client).listSubaccounts(null, 10);
 * new BankLinksApi(client).listBankLinks(null, null, 10, null);
 * client.sandbox().simulateTransactionScenario(...); // sandbox only, refused elsewhere
 * }</pre>
 *
 * <p>One {@code ApiClient} serves every API class, the Banking API ones included, and is safe to
 * share between threads: build it once and reuse it. Defaults: production, 10 second connect
 * timeout, 30 second read and write timeouts, 2 retries.
 */
public final class Astrada {
    private Astrada() {}

    /** Starts a new builder. */
    public static Builder builder() {
        return new Builder();
    }

    /** Settings for {@link Astrada#builder()}. */
    public static final class Builder {
        private Environment environment = Environment.PRODUCTION;
        private String baseUrl;
        private boolean baseUrlIsSandbox;
        private String accountId;
        private String clientId;
        private String clientSecret;
        private ClientCredentials credentials;
        private Duration connectTimeout = Duration.ofSeconds(10);
        private Duration readTimeout = Duration.ofSeconds(30);
        private Duration writeTimeout = Duration.ofSeconds(30);
        private int maxRetries = RetryInterceptor.DEFAULT_MAX_RETRIES;
        private OkHttpClient httpClient;

        private Builder() {}

        /** {@link Environment#SANDBOX} or {@link Environment#PRODUCTION} (the default). */
        public Builder environment(Environment environment) {
            this.environment = Objects.requireNonNull(environment, "environment");
            return this;
        }

        /**
         * A base URL other than the two environments' (a proxy, for example). Sandbox-only calls
         * refuse it; for a sandbox proxy or a local mock use {@link #sandboxBaseUrl(String)}.
         */
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            this.baseUrlIsSandbox = false;
            return this;
        }

        /**
         * A base URL that stands for the sandbox (a local mock such as WireMock or Prism, or a
         * proxy). Sets the environment to {@link Environment#SANDBOX} and marks the URL, so
         * {@code client.sandbox()} calls may go to it.
         *
         * @throws IllegalArgumentException for an Astrada host other than the sandbox API
         *     (production, for one)
         */
        public Builder sandboxBaseUrl(String baseUrl) {
            Objects.requireNonNull(baseUrl, "baseUrl");
            co.astrada.client.sandbox.SandboxGuard.requireMarkable(baseUrl);
            this.baseUrl = baseUrl;
            this.baseUrlIsSandbox = true;
            this.environment = Environment.SANDBOX;
            return this;
        }

        /**
         * The API client credentials and the account they belong to. The SDK requests an OAuth2
         * token, reuses it until shortly before it expires, and logs in again after a 401.
         */
        public Builder credentials(String accountId, String clientId, String clientSecret) {
            this.accountId = accountId;
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            return this;
        }

        /** A login you made yourself, for example to share one token cache between clients. */
        public Builder credentials(ClientCredentials credentials) {
            this.credentials = credentials;
            return this;
        }

        /** How long to wait for a connection (default 10 seconds). */
        public Builder connectTimeout(Duration timeout) {
            this.connectTimeout = Objects.requireNonNull(timeout, "timeout");
            return this;
        }

        /** How long to wait for response data (default 30 seconds). */
        public Builder readTimeout(Duration timeout) {
            this.readTimeout = Objects.requireNonNull(timeout, "timeout");
            return this;
        }

        /** How long to wait while sending a request body (default 30 seconds). */
        public Builder writeTimeout(Duration timeout) {
            this.writeTimeout = Objects.requireNonNull(timeout, "timeout");
            return this;
        }

        /**
         * How many times to retry a GET, HEAD, PUT, DELETE or OPTIONS request that got a 429 or a
         * 5xx response or failed to connect (default 2; 0 turns retries off). POST and PATCH are
         * never retried. See {@link RetryInterceptor}.
         */
        public Builder maxRetries(int maxRetries) {
            if (maxRetries < 0) {
                throw new IllegalArgumentException("maxRetries must be 0 or more");
            }
            this.maxRetries = maxRetries;
            return this;
        }

        /** An OkHttp client to start from (its connection pool, proxy or TLS settings). */
        public Builder httpClient(OkHttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        /**
         * Builds the client. Logging in happens on the first request, not here.
         *
         * <p>One OkHttp client (yours from {@link #httpClient}, or a new one) with these timeouts
         * carries both the API calls and the token requests, so a proxy, TLS settings or your own
         * interceptors apply to the login too; the token requests leave out any OkHttp logging
         * interceptor, so the client secret is never logged.
         */
        public ApiClient build() {
            String base = baseUrl != null ? baseUrl : environment.getBaseUrl();
            ApiClient client = new ApiClient();
            client.setEnvironment(environment);
            client.setBasePath(base);
            if (baseUrlIsSandbox) {
                client.markSandboxBaseUrl(base);
            }
            OkHttpClient.Builder sharedBuilder = (httpClient != null ? httpClient : client.getHttpClient()).newBuilder()
                    .connectTimeout(connectTimeout)
                    .readTimeout(readTimeout)
                    .writeTimeout(writeTimeout);
            OkHttpClient shared = sharedBuilder.build();
            ClientCredentials login = credentials;
            if (login == null) {
                if (accountId == null || clientId == null || clientSecret == null) {
                    throw new IllegalStateException(
                            "Set credentials(accountId, clientId, clientSecret) before build()");
                }
                login = new ClientCredentials(base, accountId, clientId, clientSecret, tokenClient(shared));
            }
            OkHttpClient.Builder http = shared.newBuilder();
            // Retries wrap the login, so each attempt carries a valid token; a refused login is
            // not retried (RetryInterceptor.isRetryable).
            if (maxRetries > 0) {
                http.addInterceptor(new RetryInterceptor(maxRetries));
            }
            http.addInterceptor(login);
            final String backendClientId = login.getClientId();
            final String mintBase = base;
            final String mintAccount = accountId;
            final OkHttpClient mintHttp = tokenClient(shared);
            // Decided here, for the base URL the token is minted at (a later setBasePath does not
            // change where it is minted).
            boolean mintsInSandbox;
            try {
                co.astrada.client.sandbox.SandboxGuard.check(client, null);
                mintsInSandbox = true;
            } catch (co.astrada.client.sandbox.SandboxOnlyException e) {
                mintsInSandbox = false;
            }
            final boolean sandboxMint = mintsInSandbox;
            client.setSdkTokenMinter((sdkClientId, sdkClientSecret, allowedScopes) -> {
                if (sdkClientId == null || sdkClientId.isEmpty() || sdkClientSecret == null || sdkClientSecret.isEmpty()) {
                    throw new IllegalArgumentException("mintSdkAccessToken needs your SDK client's id and secret "
                            + "(one of them is empty)");
                }
                if (sdkClientId.equals(backendClientId) && !sandboxMint) {
                    // Keyed on the host the token is minted at, not the environment label.
                    throw new IllegalArgumentException("mintSdkAccessToken takes your SDK client's credentials, not "
                            + "this backend client's: its token would carry every scope the backend client has");
                }
                if (mintAccount == null) {
                    throw new IllegalStateException("mintSdkAccessToken needs credentials(accountId, clientId, clientSecret)");
                }
                ClientCredentials sdkLogin = new ClientCredentials(mintBase, mintAccount, sdkClientId, sdkClientSecret, mintHttp);
                String token = sdkLogin.getAccessToken();
                if (allowedScopes != null) {
                    // The check covers what the login states in its scope field.
                    String granted = sdkLogin.getGrantedScope();
                    if (granted == null || granted.trim().isEmpty()) {
                        throw new UnexpectedScopesException(java.util.Collections.<String>emptyList(),
                                "The login did not state the scopes of the SDK client's token, so allowedScopes "
                                        + "cannot be checked (the token is not returned).");
                    }
                    java.util.Set<String> extra = new java.util.TreeSet<>();
                    for (String scope : granted.trim().split("\\s+")) {
                        if (!scope.isEmpty() && !allowedScopes.contains(scope)) {
                            extra.add(scope);
                        }
                    }
                    if (!extra.isEmpty()) {
                        throw new UnexpectedScopesException(new java.util.ArrayList<>(extra),
                                "The SDK client's token carries scopes outside allowedScopes: " + String.join(", ", extra)
                                        + ". Remove them from the SDK client (the token is not returned).");
                    }
                }
                return token;
            });
            // Last, so it is the innermost application interceptor: the body OkHttp sends is the one
            // it marks one-shot, even when an interceptor of httpClient(...) replaces the body. Not
            // on the login client: the token request is safe to send again.
            http.addInterceptor(new co.astrada.client.http.SendOnce());
            client.setHttpClient(http.build());
            return client;
        }

        private static OkHttpClient tokenClient(OkHttpClient shared) {
            OkHttpClient.Builder token = shared.newBuilder();
            token.interceptors().removeIf(i -> i instanceof HttpLoggingInterceptor);
            token.networkInterceptors().removeIf(i -> i instanceof HttpLoggingInterceptor);
            return token.build();
        }
    }
}
