package co.astrada.client.http;

import co.astrada.client.oauth.ClientCredentials;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Retries idempotent requests after a 429, a 5xx or a failed connection (refused, unreachable,
 * unknown host, or the connect timeout). A read timeout is not retried: the server may still be
 * working on the request, and retrying would multiply the wait. A refused login (the token
 * request answered 4xx other than 429) is never retried, so a wrong secret makes one token
 * request, not one per retry. Hand-written; not
 * generated. {@code Astrada.builder()} adds it; add it yourself with
 * {@code httpClient.newBuilder().addInterceptor(new RetryInterceptor(2))}.
 *
 * <p>Only GET, HEAD, PUT, DELETE and OPTIONS are retried: sending a POST or PATCH again could
 * create or change something twice. The wait before a retry is the response's
 * {@code Retry-After} (seconds or an HTTP date) when it has one, otherwise an exponential backoff
 * with jitter (about 0.5, 1, 2 seconds and so on, at most 8). A {@code Retry-After} longer than
 * {@link #MAX_RETRY_AFTER} is not waited out: the response is returned as it is.
 */
public class RetryInterceptor implements Interceptor {
    public static final int DEFAULT_MAX_RETRIES = 2;
    /** The longest {@code Retry-After} the interceptor waits for. */
    public static final Duration MAX_RETRY_AFTER = Duration.ofSeconds(60);

    private static final long BASE_DELAY_MILLIS = 500;
    private static final long MAX_BACKOFF_MILLIS = 8_000;

    private final int maxRetries;

    public RetryInterceptor(int maxRetries) {
        if (maxRetries < 0) {
            throw new IllegalArgumentException("maxRetries must be 0 or more");
        }
        this.maxRetries = maxRetries;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!isIdempotent(request.method())) {
            return chain.proceed(request);
        }
        for (int attempt = 0; ; attempt++) {
            Response response;
            try {
                response = chain.proceed(request);
            } catch (IOException e) {
                if (attempt >= maxRetries || chain.call().isCanceled() || !isRetryable(e)) {
                    throw e;
                }
                sleep(backoffMillis(attempt));
                continue;
            }
            if (attempt >= maxRetries || !isRetryable(response.code())) {
                return response;
            }
            Long retryAfter = retryAfterMillis(response.header("Retry-After"));
            if (retryAfter != null && retryAfter > MAX_RETRY_AFTER.toMillis()) {
                return response;
            }
            response.close();
            sleep(retryAfter != null ? retryAfter : backoffMillis(attempt));
        }
    }

    /** Whether a request with this method may be sent again without changing its effect. */
    public static boolean isIdempotent(String method) {
        switch (method.toUpperCase(Locale.ROOT)) {
            case "GET":
            case "HEAD":
            case "PUT":
            case "DELETE":
            case "OPTIONS":
                return true;
            default:
                return false;
        }
    }

    /** 429 Too Many Requests and every 5xx. */
    public static boolean isRetryable(int status) {
        return status == 429 || status >= 500 && status <= 599;
    }

    /**
     * The wait a {@code Retry-After} value asks for, in milliseconds: a number of seconds or an
     * HTTP date. Null when the value is missing or not readable.
     */
    public static Long retryAfterMillis(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        String text = value.trim();
        try {
            long seconds = Long.parseLong(text);
            return seconds < 0 ? null : seconds * 1000L;
        } catch (NumberFormatException notSeconds) {
            try {
                ZonedDateTime at = ZonedDateTime.parse(text, DateTimeFormatter.RFC_1123_DATE_TIME);
                return Math.max(0L, Duration.between(ZonedDateTime.now(at.getZone()), at).toMillis());
            } catch (DateTimeParseException notADate) {
                return null;
            }
        }
    }

    static long backoffMillis(int attempt) {
        long ceiling = Math.min(MAX_BACKOFF_MILLIS, BASE_DELAY_MILLIS << Math.min(attempt, 20));
        return ceiling / 2 + ThreadLocalRandom.current().nextLong(ceiling / 2 + 1);
    }

    /**
     * Whether a failed request may be sent again: a connection that could not be made, or a login
     * the token endpoint answered with 429 or a 5xx. Not a refused login, a read timeout or a
     * dropped response.
     */
    public static boolean isRetryable(IOException e) {
        if (e instanceof ClientCredentials.AuthenticationException) {
            return isRetryable(((ClientCredentials.AuthenticationException) e).getStatus());
        }
        if (e instanceof SocketTimeoutException) {
            String message = e.getMessage() == null ? "" : e.getMessage().toLowerCase(Locale.ROOT);
            return message.contains("connect");
        }
        return e instanceof ConnectException || e instanceof NoRouteToHostException
                || e instanceof UnknownHostException;
    }

    /** Waits; protected so tests can skip the wait. */
    protected void sleep(long millis) throws IOException {
        if (millis <= 0) {
            return;
        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            InterruptedIOException interrupted = new InterruptedIOException("Interrupted while waiting to retry");
            interrupted.initCause(e);
            throw interrupted;
        }
    }
}
