package co.astrada.client.sandbox;

import co.astrada.client.ApiClient;
import co.astrada.client.Environment;
import co.astrada.client.http.RetryInterceptor;
import java.net.URI;
import java.time.Duration;
import java.util.Locale;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * The check every sandbox-only call makes before it sends anything. Hand-written; the generated
 * SandboxApi calls it (the api template does so for each operation marked x-sandbox-only).
 *
 * <p>A call passes only when both hold:
 * <ul>
 *   <li>the client's environment is {@link Environment#SANDBOX}; and</li>
 *   <li>the URL the call would go to is the sandbox API ({@code https://api.sandbox.astrada.co}),
 *       or exactly the base URL marked with {@code ApiClient.markSandboxBaseUrl} (what
 *       {@code Astrada.builder().sandboxBaseUrl(...)} does, for a local mock or a proxy). A marked
 *       URL on any other Astrada host (production included) is refused.</li>
 * </ul>
 * Otherwise it throws {@link SandboxOnlyException}, so a simulation can never reach production.
 */
public final class SandboxGuard {
    private static final URI SANDBOX = URI.create(Environment.SANDBOX.getBaseUrl());

    /**
     * The read timeout of the sandbox-only deletes, unless the client's is longer or off. They answer
     * only when everything under the deleted resource is gone, which can take minutes.
     */
    public static final Duration DELETE_TIMEOUT = Duration.ofMinutes(5);

    /** The most times one sandbox delete reaches the network: once, plus resends it never ran for. */
    public static final int MAX_DELETE_SENDS = 3;

    /**
     * Thrown instead of sending a sandbox delete again. A {@link java.net.ProtocolException}, which
     * OkHttp treats as final, so it does not try again with another route or connection.
     */
    public static final class NotSentAgainException extends java.net.ProtocolException {
        private static final long serialVersionUID = 1L;

        NotSentAgainException(String message) {
            super(message);
        }
    }

    private SandboxGuard() {}

    /**
     * The call for a sandbox-only delete: the client's settings without {@link RetryInterceptor},
     * with the request sent once, or again only when an answer shows it never ran (a 401 before a
     * new login, an HTTP/2 REFUSED_STREAM, or a connection shut down (GOAWAY) before the stream was
     * accepted), never more than {@link #MAX_DELETE_SENDS} times, and with a read and call timeout of
     * at least {@link #DELETE_TIMEOUT}. A second delete while the first may still run is not safe:
     * read the resource every few seconds (404 means it finished); a delete runs for at most about 5
     * minutes, so after 6 minutes calling it again is safe. The generated SandboxApi and
     * {@code ApiClient.request} use it for each sandbox-only delete.
     *
     * <p>OkHttp still tries a host's other addresses when a connection cannot be made (nothing was
     * sent yet). Its own resends of a request already sent (after the connection drops, or a 408)
     * are refused by a network interceptor, which throws {@link NotSentAgainException} instead, and a
     * 503's {@code Retry-After} is removed so OkHttp does not resend on {@code Retry-After: 0}; the
     * {@link co.astrada.client.ApiException} for that 503 therefore has no {@code Retry-After}.
     */
    public static Call deleteCall(ApiClient client, Request request) {
        OkHttpClient base = client.getHttpClient();
        OkHttpClient.Builder builder = base.newBuilder();
        builder.interceptors().removeIf(i -> i instanceof RetryInterceptor);
        builder.networkInterceptors().removeIf(i -> i instanceof RetryInterceptor);
        // One client per call, so this state is per call. The request reaches the network once. It is
        // sent again only when the first answer proves the delete never ran: a 401 (the token was
        // refused; the login sends it again with a new one) or an HTTP/2 REFUSED_STREAM (the server
        // turned the stream down unprocessed; OkHttp sends it again). At most MAX_DELETE_SENDS sends
        // in all, then NotSentAgainException, which OkHttp does not retry.
        java.util.concurrent.atomic.AtomicInteger sends = new java.util.concurrent.atomic.AtomicInteger();
        java.util.concurrent.atomic.AtomicBoolean neverRan = new java.util.concurrent.atomic.AtomicBoolean();
        builder.addNetworkInterceptor(chain -> {
            int send = sends.incrementAndGet();
            if (send > 1 && !(neverRan.get() && send <= MAX_DELETE_SENDS)) {
                // The text follows the last answer: only a delete that never ran was refused unprocessed.
                throw new NotSentAgainException(neverRan.get()
                        ? "A sandbox delete was refused unprocessed " + (send - 1) + " times; not sending it again"
                        : "A sandbox delete is not sent again: it was sent and may still be running. Read the "
                                + "resource every few seconds: 404 means it finished. A delete runs for at most about "
                                + "5 minutes, so after 6 minutes calling it again is safe.");
            }
            neverRan.set(false);
            okhttp3.Response response;
            try {
                response = chain.proceed(chain.request());
            } catch (okhttp3.internal.http2.StreamResetException e) {
                neverRan.set(e.errorCode == okhttp3.internal.http2.ErrorCode.REFUSED_STREAM);
                throw e;
            } catch (okhttp3.internal.http2.ConnectionShutdownException e) {
                // HTTP/2 GOAWAY before this stream was accepted: it never reached the server.
                neverRan.set(true);
                throw e;
            }
            neverRan.set(response.code() == 401);
            return response;
        });
        builder.addNetworkInterceptor(chain -> {
            okhttp3.Response response = chain.proceed(chain.request());
            return response.code() == 503 && response.header("Retry-After") != null
                    ? response.newBuilder().removeHeader("Retry-After").build()
                    : response;
        });
        if (base.readTimeoutMillis() != 0 && base.readTimeoutMillis() < DELETE_TIMEOUT.toMillis()) {
            builder.readTimeout(DELETE_TIMEOUT);
        }
        if (base.callTimeoutMillis() != 0 && base.callTimeoutMillis() < DELETE_TIMEOUT.toMillis()) {
            builder.callTimeout(DELETE_TIMEOUT);
        }
        return builder.build().newCall(request);
    }

    /**
     * Throws IllegalArgumentException unless {@code baseUrl} may be marked as a sandbox stand-in:
     * a readable http(s) URL on a host that is not Astrada's (a local mock, a proxy).
     */
    public static void requireMarkable(String baseUrl) {
        if (baseUrl == null || HttpUrl.parse(baseUrl.trim()) == null) {
            throw new IllegalArgumentException("\"" + baseUrl + "\" is not a readable http(s) URL, so it "
                    + "cannot be marked as a sandbox");
        }
        if (isOtherAstradaHost(baseUrl)) {
            throw new IllegalArgumentException(baseUrl + " is an Astrada host other than the sandbox API; "
                    + "only a non-Astrada host (a local mock, a proxy) can be marked as a sandbox "
                    + "(use environment(SANDBOX) for the sandbox)");
        }
    }

    /**
     * @param client the client making the call
     * @param operationBasePath the base URL the call chose itself (a custom base URL set on the API
     *     class), or null when it uses the client's
     */
    public static void check(ApiClient client, String operationBasePath) {
        if (client == null || client.getEnvironment() != Environment.SANDBOX) {
            throw new SandboxOnlyException("Sandbox-only call refused: the client's environment is "
                    + (client == null ? null : client.getEnvironment()) + ", not SANDBOX");
        }
        String target = strip(effectiveBaseUrl(client, operationBasePath));
        if (isSandboxApi(target)) {
            return;
        }
        String marked = strip(client.getMarkedSandboxBaseUrl());
        if (marked != null && marked.equals(target) && !isOtherAstradaHost(target)) {
            return;
        }
        throw new SandboxOnlyException("Sandbox-only call refused: " + target + " is not the sandbox API ("
                + SANDBOX + ") and was not marked as a sandbox (Astrada.builder().sandboxBaseUrl(...))");
    }

    /** The base URL the generated buildCall would use. */
    private static String effectiveBaseUrl(ApiClient client, String operationBasePath) {
        if (operationBasePath != null) {
            return operationBasePath;
        }
        Integer index = client.getServerIndex();
        if (index != null) {
            if (index < 0 || index >= client.getServers().size()) {
                return null;
            }
            return client.getServers().get(index).URL(client.getServerVariables());
        }
        return client.getBasePath();
    }

    static boolean isSandboxApi(String baseUrl) {
        if (baseUrl == null) {
            return false;
        }
        URI uri;
        try {
            uri = URI.create(baseUrl);
        } catch (IllegalArgumentException e) {
            return false;
        }
        String path = uri.getRawPath() == null ? "" : uri.getRawPath();
        return "https".equalsIgnoreCase(uri.getScheme())
                && uri.getHost() != null
                && SANDBOX.getHost().equals(uri.getHost().toLowerCase(Locale.ROOT))
                && (uri.getPort() == -1 || uri.getPort() == 443)
                && uri.getRawUserInfo() == null
                && path.isEmpty();
    }

    /**
     * Whether the URL is on an Astrada host other than the sandbox API (production, for one).
     * Marking a URL as a sandbox is for hosts that are not Astrada's, such as a local mock; an
     * unreadable URL counts as Astrada's, so it is refused too. The URL is read as OkHttp reads it,
     * so a Docker Compose host such as {@code astrada_mock} is readable.
     */
    public static boolean isOtherAstradaHost(String baseUrl) {
        if (baseUrl == null) {
            return true;
        }
        HttpUrl url = HttpUrl.parse(baseUrl.trim());
        if (url == null) {
            return true;
        }
        String host = url.host().toLowerCase(Locale.ROOT).replaceAll("\\.+$", "");
        boolean astrada = host.equals("astrada.co") || host.endsWith(".astrada.co");
        return astrada && !isSandboxApi(baseUrl.trim());
    }

    private static String strip(String url) {
        return url == null ? null : url.replaceAll("/+$", "");
    }
}
