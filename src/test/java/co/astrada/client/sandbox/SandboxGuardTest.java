package co.astrada.client.sandbox;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.api.SandboxApi;
import co.astrada.client.model.SimulateBulkFeedFileParams;
import co.astrada.client.model.SimulateTransactionScenarioRequest;
import java.util.UUID;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * Sandbox-only calls are refused before any request unless the client is set to the sandbox and
 * points at the sandbox API or at a base URL marked as a sandbox. Hand-written.
 */
class SandboxGuardTest {
    private MockWebServer server;

    @BeforeEach
    void start() throws Exception {
        server = new MockWebServer();
        server.start();
    }

    @AfterEach
    void stop() throws Exception {
        server.shutdown();
    }

    private String mockUrl() {
        return server.url("/").toString();
    }

    private static SimulateTransactionScenarioRequest scenario() {
        return new SimulateTransactionScenarioRequest()
                .scenario(SimulateTransactionScenarioRequest.ScenarioEnum.SIMPLE_PURCHASE)
                .cardId(UUID.randomUUID())
                .subaccountId(UUID.randomUUID());
    }

    private void assertRefusedWithoutARequest(Executable call) {
        SandboxOnlyException e = assertThrows(SandboxOnlyException.class, call);
        assertTrue(e instanceof ApiException);
        assertEquals(0, e.getCode());
        assertEquals(0, server.getRequestCount(), "no request, not even the login");
    }

    @Test
    void refusesAProductionClient() {
        ApiClient production = Astrada.builder().environment(Environment.PRODUCTION)
                .credentials("a", "b", "c").build();
        assertRefusedWithoutARequest(() -> production.sandbox().simulateTransactionScenario(scenario()));
    }

    @Test
    void refusesTheSandboxDeletesOnAProductionClient() {
        // The mock's URL, so a guard that let a delete through would be seen here, not in production.
        ApiClient production = Astrada.builder().baseUrl(mockUrl()).credentials("a", "b", "c").build();
        assertRefusedWithoutARequest(() -> production.sandbox().deleteSubaccount(UUID.randomUUID()));
        assertRefusedWithoutARequest(() -> production.sandbox().deleteCardSubscription(UUID.randomUUID()));
        assertRefusedWithoutARequest(() -> production.sandbox().deleteNetworkBulkFeed(UUID.randomUUID()));
    }

    @Test
    void refusesAProductionClientEvenWithTheMockUrl() {
        ApiClient client = Astrada.builder().baseUrl(mockUrl()).credentials("a", "b", "c").build();
        assertRefusedWithoutARequest(() -> client.sandbox().simulateTransactionScenario(scenario()));
    }

    @Test
    void refusesASandboxClientWithAnUnmarkedBaseUrl() {
        ApiClient client = Astrada.builder().environment(Environment.SANDBOX).baseUrl(mockUrl())
                .credentials("a", "b", "c").build();
        assertRefusedWithoutARequest(() -> client.sandbox().simulateTransactionScenario(scenario()));
        assertRefusedWithoutARequest(() -> client.sandbox().simulateTransactionScenarioAsync(scenario(), null));
        assertRefusedWithoutARequest(() -> new SandboxApi(client).simulateTransactionScenarioWithHttpInfo(scenario()));
    }

    @Test
    void refusesAClientMovedOffTheMarkedUrl() {
        ApiClient client = Astrada.builder().sandboxBaseUrl(mockUrl()).credentials("a", "b", "c").build();
        client.setBasePath("https://api.astrada.co");
        assertRefusedWithoutARequest(() -> client.sandbox().simulateTransactionScenario(scenario()));
    }

    @Test
    void refusesAServerIndexOrCustomBaseUrlThatIsNotTheSandbox() {
        ApiClient indexed = Astrada.builder().environment(Environment.SANDBOX).credentials("a", "b", "c").build();
        indexed.setServerIndex(0); // the first server in the definition is production
        assertRefusedWithoutARequest(() -> indexed.sandbox().simulateTransactionScenario(scenario()));

        ApiClient marked = Astrada.builder().sandboxBaseUrl(mockUrl()).credentials("a", "b", "c").build();
        SandboxApi api = marked.sandbox();
        api.setCustomBaseUrl("https://api.astrada.co");
        assertRefusedWithoutARequest(() -> api.simulateTransactionScenario(scenario()));
    }

    @Test
    void refusesAHandBuiltClient() {
        ApiClient client = new ApiClient();
        client.setBasePath("https://api.sandbox.astrada.co");
        assertThrows(SandboxOnlyException.class, () -> SandboxGuard.check(client, null)); // no environment
        client.setEnvironment(Environment.SANDBOX);
        assertDoesNotThrow(() -> SandboxGuard.check(client, null));
    }

    @Test
    void acceptsOnlyTheSandboxApiHost() {
        assertTrue(SandboxGuard.isSandboxApi("https://api.sandbox.astrada.co"));
        assertTrue(SandboxGuard.isSandboxApi("https://API.Sandbox.Astrada.co:443"));
        assertFalse(SandboxGuard.isSandboxApi("http://api.sandbox.astrada.co"));
        assertFalse(SandboxGuard.isSandboxApi("https://api.sandbox.astrada.co.example.com"));
        assertFalse(SandboxGuard.isSandboxApi("https://api.sandbox.astrada.co:8443"));
        assertFalse(SandboxGuard.isSandboxApi("https://user@api.sandbox.astrada.co"));
        assertFalse(SandboxGuard.isSandboxApi("https://api.sandbox.astrada.co/proxy"));
        assertFalse(SandboxGuard.isSandboxApi("https://api.astrada.co"));
        assertFalse(SandboxGuard.isSandboxApi(null));
    }

    @Test
    void refusesABulkFeedFileOutsideTheSandbox() {
        ApiClient production = Astrada.builder().baseUrl(mockUrl()).credentials("a", "b", "c").build();
        for (BulkFeedType type : BulkFeedType.values()) {
            assertRefusedWithoutARequest(() -> production.sandbox().simulateBulkFeedFile(type,
                    new SimulateBulkFeedFileParams().networkReference("R12345")));
        }
    }

    @Test
    void sendsEachBulkFeedFileToItsRoute() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json")
                .setBody("{\"access_token\":\"t\",\"token_type\":\"Bearer\",\"expires_in\":300}"));
        for (int i = 0; i < 4; i++) {
            server.enqueue(new MockResponse().setResponseCode(202));
        }
        ApiClient client = Astrada.builder().sandboxBaseUrl(mockUrl()).credentials("a", "b", "c").build();
        for (BulkFeedType type : BulkFeedType.values()) {
            client.sandbox().simulateBulkFeedFile(type,
                    new SimulateBulkFeedFileParams().networkReference("ref-1").cardCount(3));
        }
        server.takeRequest();
        assertEquals("/simulate/ingestion/amex-gl", server.takeRequest().getPath());
        assertEquals("/simulate/ingestion/smartdata", server.takeRequest().getPath());
        assertEquals("/simulate/ingestion/vbds", server.takeRequest().getPath());
        okhttp3.mockwebserver.RecordedRequest vcf = server.takeRequest();
        assertEquals("/simulate/ingestion/vcf", vcf.getPath());
        assertEquals("{\"networkReference\":\"ref-1\",\"cardCount\":3}", vcf.getBody().readUtf8());
    }

    @Test
    void sendsTheCallWhenTheUrlIsMarkedAsASandbox() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json")
                .setBody("{\"access_token\":\"t\",\"token_type\":\"Bearer\",\"expires_in\":300}"));
        server.enqueue(new MockResponse().setResponseCode(202).setHeader("Content-Type", "application/json")
                .setBody("{\"transactions\":[{\"messageIds\":[\"" + UUID.randomUUID() + "\"]}]}"));
        ApiClient client = Astrada.builder().sandboxBaseUrl(mockUrl()).credentials("a", "b", "c").build();

        assertEquals(Environment.SANDBOX, client.getEnvironment());
        assertEquals(1, client.sandbox().simulateTransactionScenario(scenario()).getTransactions().size());
        server.takeRequest();
        assertEquals("/simulate/transaction/scenario", server.takeRequest().getPath());
    }

    @Test
    void neverMarksAnAstradaHostOtherThanTheSandbox() {
        for (String url : new String[] {"https://api.astrada.co", "https://API.ASTRADA.CO:443/", "https://api.astrada.co/v1",
                "http://api.astrada.co", "https://other.astrada.co", "https://astrada.co", "https://api.astrada.co."}) {
            assertThrows(IllegalArgumentException.class, () -> Astrada.builder().sandboxBaseUrl(url), url);
            assertThrows(IllegalArgumentException.class, () -> new ApiClient().markSandboxBaseUrl(url), url);
            assertTrue(SandboxGuard.isOtherAstradaHost(url), url);
        }
        assertFalse(SandboxGuard.isOtherAstradaHost("https://api.sandbox.astrada.co"));
        assertFalse(SandboxGuard.isOtherAstradaHost("http://127.0.0.1:4010"));
        assertFalse(SandboxGuard.isOtherAstradaHost("https://astrada.co.example.com"));
        assertDoesNotThrow(() -> Astrada.builder().sandboxBaseUrl("https://api.sandbox.astrada.co"));
    }

    @Test
    void theGuardRefusesAMarkedProductionUrlEvenIfMarkingWasBypassed() throws Exception {
        ApiClient client = Astrada.builder().sandboxBaseUrl(mockUrl()).credentials("a", "b", "c").build();
        java.lang.reflect.Field marked = ApiClient.class.getDeclaredField("markedSandboxBaseUrl");
        marked.setAccessible(true);
        marked.set(client, "https://api.astrada.co");
        client.setBasePath("https://api.astrada.co");
        assertRefusedWithoutARequest(() -> client.sandbox().simulateTransactionScenario(scenario()));
    }

    private static final String TOKEN = "{\"access_token\":\"t\",\"token_type\":\"Bearer\",\"expires_in\":300}";

    @Test
    void aSandboxDeleteWaitsLongerThanTheReadTimeoutAndIsNeverRetried() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        // Slower than the client's read timeout, inside SandboxGuard.DELETE_TIMEOUT: done.
        server.enqueue(new MockResponse().setResponseCode(204).setHeadersDelay(1500, java.util.concurrent.TimeUnit.MILLISECONDS));
        server.enqueue(new MockResponse().setResponseCode(502).setHeader("Content-Type", "application/json")
                .setBody("{\"title\":\"Bad Gateway\",\"detail\":\"a step failed\"}"));
        server.enqueue(new MockResponse().setResponseCode(204));
        ApiClient client = Astrada.builder().sandboxBaseUrl(mockUrl()).credentials("a", "b", "c")
                .readTimeout(java.time.Duration.ofSeconds(1)).maxRetries(2).build();

        UUID id = UUID.randomUUID();
        client.sandbox().deleteSubaccount(id);
        ApiException failed = assertThrows(ApiException.class, () -> client.sandbox().deleteSubaccount(id));
        assertEquals(502, failed.getCode());

        server.takeRequest(); // the login
        assertEquals("DELETE", server.takeRequest().getMethod());
        assertEquals("DELETE", server.takeRequest().getMethod());
        assertEquals(3, server.getRequestCount(), "the 502 was not sent again");
        assertTrue(SandboxGuard.DELETE_TIMEOUT.toMinutes() >= 5);
    }

    @Test
    void aMockHostWithAnUnderscoreCanBeMarkedAndAnUnreadableUrlSaysSo() {
        assertDoesNotThrow(() -> Astrada.builder().sandboxBaseUrl("http://astrada_mock:8080").credentials("a", "b", "c").build());
        IllegalArgumentException unreadable = assertThrows(IllegalArgumentException.class,
                () -> Astrada.builder().sandboxBaseUrl("not a url"));
        assertTrue(unreadable.getMessage().contains("not a readable http(s) URL"), unreadable.getMessage());
        IllegalArgumentException production = assertThrows(IllegalArgumentException.class,
                () -> Astrada.builder().sandboxBaseUrl("https://api.astrada.co"));
        assertTrue(production.getMessage().contains("is an Astrada host"), production.getMessage());
    }

    @Test
    void okHttpDoesNotResendASandboxDeleteEither() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        server.enqueue(new MockResponse().setResponseCode(204)); // warms a pooled connection
        // The connection drops once the request is in: OkHttp would resend on another connection.
        server.enqueue(new MockResponse().setSocketPolicy(okhttp3.mockwebserver.SocketPolicy.DISCONNECT_AFTER_REQUEST));
        // A 503 with Retry-After: 0, which OkHttp itself would send again.
        server.enqueue(new MockResponse().setResponseCode(503).setHeader("Retry-After", "0"));
        server.enqueue(new MockResponse().setResponseCode(204));
        ApiClient client = Astrada.builder().sandboxBaseUrl(mockUrl()).credentials("a", "b", "c").build();

        UUID id = UUID.randomUUID();
        client.sandbox().deleteSubaccount(id);
        Exception dropped = null;
        try {
            client.sandbox().deleteSubaccount(id);
        } catch (Exception e) {
            dropped = e;
        }
        assertEquals(3, server.getRequestCount(), "the dropped delete was not sent again");
        assertTrue(dropped instanceof ApiException, String.valueOf(dropped));
        ApiException unavailable = assertThrows(ApiException.class, () -> client.sandbox().deleteSubaccount(id));
        assertEquals(503, unavailable.getCode());
        assertEquals(4, server.getRequestCount(), "the 503 was not sent again");
    }

    @Test
    void aSandboxDeleteRefusedWith401IsSentOnceMoreWithANewToken() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        server.enqueue(new MockResponse().setResponseCode(401).setHeader("Content-Type", "application/json")
                .setBody("{\"title\":\"Unauthorized\"}"));
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        server.enqueue(new MockResponse().setResponseCode(204));
        ApiClient client = Astrada.builder().sandboxBaseUrl(mockUrl()).credentials("a", "b", "c").build();

        client.sandbox().deleteSubaccount(UUID.randomUUID());
        assertEquals(4, server.getRequestCount(), "login, 401, new login, the delete again");
    }

    @Test
    void aSandboxDeleteIsNotSentAgainAfterA401AndThenA503() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        server.enqueue(new MockResponse().setResponseCode(401));
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        server.enqueue(new MockResponse().setResponseCode(503).setHeader("Retry-After", "0"));
        server.enqueue(new MockResponse().setResponseCode(204));
        ApiClient client = Astrada.builder().sandboxBaseUrl(mockUrl()).credentials("a", "b", "c").build();

        ApiException e = assertThrows(ApiException.class, () -> client.sandbox().deleteSubaccount(UUID.randomUUID()));
        assertEquals(503, e.getCode());
        assertEquals(4, server.getRequestCount(), "the 503 was not sent again");
    }

    @Test
    void aSandboxDeleteRefusedOverHttp2StopsAfterABoundedNumberOfSends() throws Exception {
        // HTTP/2 without TLS (prior knowledge) on the mock; every delete stream is reset with
        // REFUSED_STREAM, which OkHttp itself would retry without end.
        server.shutdown();
        server = new MockWebServer();
        server.setProtocols(java.util.Collections.singletonList(okhttp3.Protocol.H2_PRIOR_KNOWLEDGE));
        server.start();
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        for (int i = 0; i < 20; i++) {
            server.enqueue(new MockResponse().setSocketPolicy(okhttp3.mockwebserver.SocketPolicy.RESET_STREAM_AT_START)
                    .setHttp2ErrorCode(okhttp3.internal.http2.ErrorCode.REFUSED_STREAM.getHttpCode()));
        }
        okhttp3.OkHttpClient h2 = new okhttp3.OkHttpClient.Builder()
                .protocols(java.util.Collections.singletonList(okhttp3.Protocol.H2_PRIOR_KNOWLEDGE)).build();
        ApiClient client = Astrada.builder().httpClient(h2).sandboxBaseUrl(mockUrl()).credentials("a", "b", "c").build();

        ApiException e = org.junit.jupiter.api.Assertions.assertTimeoutPreemptively(java.time.Duration.ofSeconds(10),
                () -> assertThrows(ApiException.class, () -> client.sandbox().deleteSubaccount(UUID.randomUUID())));
        Throwable cause = e;
        while (cause != null && !(cause instanceof SandboxGuard.NotSentAgainException)) {
            cause = cause.getCause();
        }
        assertTrue(cause != null, String.valueOf(e));
        assertEquals(1 + SandboxGuard.MAX_DELETE_SENDS, server.getRequestCount(), "the login and at most three sends");
    }

    @Test
    void aDeleteRefusedTwiceThenAnswered408SaysItMayStillBeRunning() throws Exception {
        server.shutdown();
        server = new MockWebServer();
        server.setProtocols(java.util.Collections.singletonList(okhttp3.Protocol.H2_PRIOR_KNOWLEDGE));
        server.start();
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        for (int i = 0; i < 2; i++) {
            server.enqueue(new MockResponse().setSocketPolicy(okhttp3.mockwebserver.SocketPolicy.RESET_STREAM_AT_START)
                    .setHttp2ErrorCode(okhttp3.internal.http2.ErrorCode.REFUSED_STREAM.getHttpCode()));
        }
        server.enqueue(new MockResponse().setResponseCode(408));
        server.enqueue(new MockResponse().setResponseCode(204));
        okhttp3.OkHttpClient h2 = new okhttp3.OkHttpClient.Builder()
                .protocols(java.util.Collections.singletonList(okhttp3.Protocol.H2_PRIOR_KNOWLEDGE)).build();
        ApiClient client = Astrada.builder().httpClient(h2).sandboxBaseUrl(mockUrl()).credentials("a", "b", "c").build();

        ApiException e = assertThrows(ApiException.class, () -> client.sandbox().deleteSubaccount(UUID.randomUUID()));
        String messages = "";
        for (Throwable t = e; t != null; t = t.getCause()) {
            messages += t.getMessage() + " | ";
        }
        // OkHttp sends the 408 again by itself; the guard stops that fourth send.
        assertEquals(4, server.getRequestCount(), "the login, two refused sends and the 408");
        assertTrue(messages.contains("may still be running"), messages);
        assertTrue(!messages.contains("refused unprocessed"), messages);
    }
}
