package co.astrada.client.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Retries of idempotent calls after 429 and 5xx, against a local HTTP server. Hand-written. */
class RetryInterceptorTest {
    private MockWebServer server;
    private final List<Long> waits = new ArrayList<>();

    private final RetryInterceptor retry = new RetryInterceptor(2) {
        @Override
        protected void sleep(long millis) {
            waits.add(millis);
        }
    };

    @BeforeEach
    void start() throws Exception {
        server = new MockWebServer();
        server.start();
    }

    @AfterEach
    void stop() throws Exception {
        server.shutdown();
    }

    private Response call(String method) throws Exception {
        OkHttpClient http = new OkHttpClient.Builder().addInterceptor(retry).build();
        RequestBody body = "GET".equals(method) || "DELETE".equals(method)
                ? null : RequestBody.create("{}", MediaType.get("application/json"));
        return http.newCall(new Request.Builder().url(server.url("/x")).method(method, body).build()).execute();
    }

    @Test
    void retriesAGetAfter503And429() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(503));
        server.enqueue(new MockResponse().setResponseCode(429).setHeader("Retry-After", "3"));
        server.enqueue(new MockResponse().setResponseCode(200).setBody("ok"));

        try (Response response = call("GET")) {
            assertEquals(200, response.code());
        }
        assertEquals(3, server.getRequestCount());
        assertEquals(2, waits.size());
        assertTrue(waits.get(0) >= 250 && waits.get(0) <= 500, "backoff " + waits.get(0));
        assertEquals(3000L, waits.get(1), "Retry-After seconds");
    }

    @Test
    void stopsAfterMaxRetries() throws Exception {
        for (int i = 0; i < 4; i++) {
            server.enqueue(new MockResponse().setResponseCode(500));
        }
        try (Response response = call("DELETE")) {
            assertEquals(500, response.code());
        }
        assertEquals(3, server.getRequestCount());
    }

    @Test
    void neverRetriesPostOrPatch() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(503));
        server.enqueue(new MockResponse().setResponseCode(503));
        try (Response response = call("POST")) {
            assertEquals(503, response.code());
        }
        try (Response response = call("PATCH")) {
            assertEquals(503, response.code());
        }
        assertEquals(2, server.getRequestCount());
        assertTrue(waits.isEmpty());
    }

    @Test
    void doesNotRetryOtherClientErrors() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(404));
        try (Response response = call("GET")) {
            assertEquals(404, response.code());
        }
        assertEquals(1, server.getRequestCount());
    }

    @Test
    void returnsATooLongRetryAfterInsteadOfWaiting() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(429).setHeader("Retry-After", "3600"));
        try (Response response = call("GET")) {
            assertEquals(429, response.code());
        }
        assertEquals(1, server.getRequestCount());
        assertTrue(waits.isEmpty());
    }

    @Test
    void readsRetryAfterAsSecondsOrAnHttpDate() {
        assertEquals(120_000L, RetryInterceptor.retryAfterMillis("120"));
        String inTenSeconds = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now().plusSeconds(10));
        long millis = RetryInterceptor.retryAfterMillis(inTenSeconds);
        assertTrue(millis > 8_000 && millis <= 10_000, "date " + millis);
        assertNull(RetryInterceptor.retryAfterMillis("soon"));
        assertNull(RetryInterceptor.retryAfterMillis(null));
    }

    @Test
    void retriesAFailedConnectionButNotAReadTimeout() throws Exception {
        int port = server.getPort();
        server.shutdown(); // nothing listens now: connection refused
        OkHttpClient http = new OkHttpClient.Builder().addInterceptor(retry).build();
        assertThrows(java.io.IOException.class,
                () -> http.newCall(new Request.Builder().url("http://127.0.0.1:" + port + "/x").build()).execute());
        assertEquals(2, waits.size(), "two retries after refused connections");

        server = new MockWebServer();
        server.start();
        waits.clear();
        server.enqueue(new MockResponse().setBody("late").setHeadersDelay(2, java.util.concurrent.TimeUnit.SECONDS));
        OkHttpClient impatient = new OkHttpClient.Builder().addInterceptor(retry)
                .readTimeout(java.time.Duration.ofMillis(200)).build();
        assertThrows(java.net.SocketTimeoutException.class,
                () -> impatient.newCall(new Request.Builder().url(server.url("/x")).build()).execute());
        assertEquals(1, server.getRequestCount());
        assertTrue(waits.isEmpty());
    }

    @Test
    void retriesALoginOnlyWhenTheTokenEndpointAskedForIt() {
        assertTrue(RetryInterceptor.isRetryable(new co.astrada.client.oauth.ClientCredentials.AuthenticationException(503, "x")));
        assertTrue(RetryInterceptor.isRetryable(new co.astrada.client.oauth.ClientCredentials.AuthenticationException(429, "x")));
        assertFalse(RetryInterceptor.isRetryable(new co.astrada.client.oauth.ClientCredentials.AuthenticationException(401, "x")));
        assertFalse(RetryInterceptor.isRetryable(new co.astrada.client.oauth.ClientCredentials.AuthenticationException(400, "x")));
        assertTrue(RetryInterceptor.isRetryable(new java.net.ConnectException("refused")));
        assertTrue(RetryInterceptor.isRetryable(new java.net.SocketTimeoutException("Connect timed out")));
        assertFalse(RetryInterceptor.isRetryable(new java.net.SocketTimeoutException("timeout")));
        assertFalse(RetryInterceptor.isRetryable(new java.io.IOException("unexpected end of stream")));
    }
}
