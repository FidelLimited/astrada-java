package co.astrada.client.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.api.SubaccountsApi;
import co.astrada.client.model.SimulateTransactionScenarioRequest;
import co.astrada.client.model.SubaccountUpdateParams;
import java.util.UUID;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** A POST or PATCH is never sent twice, not even by OkHttp's own retry on a dropped connection or a 408. */
class NoResendTest {
    private static final String TOKEN = "{\"access_token\":\"t\",\"token_type\":\"Bearer\",\"expires_in\":300}";
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

    private ApiClient client() {
        return Astrada.builder().sandboxBaseUrl(server.url("/").toString()).credentials("a", "b", "c").build();
    }

    private static SimulateTransactionScenarioRequest scenario() {
        return new SimulateTransactionScenarioRequest()
                .scenario(SimulateTransactionScenarioRequest.ScenarioEnum.SIMPLE_PURCHASE)
                .cardId(UUID.randomUUID()).subaccountId(UUID.randomUUID());
    }

    @Test
    void aPostOnADroppedConnectionIsNotSentAgain() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        // The connection drops once the POST is in; OkHttp's retryOnConnectionFailure would resend it.
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST));
        server.enqueue(new MockResponse().setResponseCode(202).setHeader("Content-Type", "application/json")
                .setBody("{\"transactions\":[]}"));
        ApiClient client = client();

        assertThrows(ApiException.class, () -> client.sandbox().simulateTransactionScenario(scenario()));
        assertEquals(2, server.getRequestCount(), "the login and one POST");
    }

    @Test
    void aPostAnswered408IsNotSentAgain() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        server.enqueue(new MockResponse().setResponseCode(408).setHeader("Content-Type", "application/json")
                .setBody("{\"title\":\"Request Timeout\"}"));
        server.enqueue(new MockResponse().setResponseCode(202).setHeader("Content-Type", "application/json")
                .setBody("{\"transactions\":[]}"));
        ApiClient client = client();

        ApiException e = assertThrows(ApiException.class, () -> client.sandbox().simulateTransactionScenario(scenario()));
        assertEquals(408, e.getCode());
        assertEquals(2, server.getRequestCount(), "the login and one POST");
    }

    private ApiClient plainClient() {
        return Astrada.builder().baseUrl(server.url("/").toString()).credentials("a", "b", "c").build();
    }

    @Test
    void aPatchOnADroppedConnectionIsNotSentAgain() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST));
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody("{}"));
        SubaccountsApi api = new SubaccountsApi(plainClient());

        assertThrows(ApiException.class, () -> api.updateSubaccount(UUID.randomUUID(), new SubaccountUpdateParams()));
        assertEquals(2, server.getRequestCount(), "the login and one PATCH");
    }

    @Test
    void aPostAnswered503RetryAfterZeroIsNotSentAgain() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        server.enqueue(new MockResponse().setResponseCode(503).setHeader("Retry-After", "0"));
        server.enqueue(new MockResponse().setResponseCode(202).setHeader("Content-Type", "application/json")
                .setBody("{\"transactions\":[]}"));
        ApiClient client = client();

        ApiException e = assertThrows(ApiException.class, () -> client.sandbox().simulateTransactionScenario(scenario()));
        assertEquals(503, e.getCode());
        assertEquals(2, server.getRequestCount(), "the login and one POST");
    }

    @Test
    void aPostRefusedWith401IsSentOnceMoreWithANewToken() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        server.enqueue(new MockResponse().setResponseCode(401));
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        server.enqueue(new MockResponse().setResponseCode(202).setHeader("Content-Type", "application/json")
                .setBody("{\"transactions\":[]}"));
        ApiClient client = client();

        client.sandbox().simulateTransactionScenario(scenario());
        assertEquals(4, server.getRequestCount(), "login, 401, new login, the POST again");
    }

    @Test
    void aBodyReplacedByACustomerInterceptorIsStillNotSentAgain() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST));
        server.enqueue(new MockResponse().setResponseCode(202).setHeader("Content-Type", "application/json")
                .setBody("{\"transactions\":[]}"));
        // Like a compression interceptor: replaces every body with a new (not one-shot) one.
        okhttp3.OkHttpClient custom = new okhttp3.OkHttpClient.Builder().addInterceptor(chain -> {
            okhttp3.Request request = chain.request();
            if (request.body() == null) {
                return chain.proceed(request);
            }
            okio.Buffer copy = new okio.Buffer();
            request.body().writeTo(copy);
            okhttp3.RequestBody replaced = okhttp3.RequestBody.create(copy.readByteArray(), request.body().contentType());
            return chain.proceed(request.newBuilder().method(request.method(), replaced).build());
        }).build();
        ApiClient client = Astrada.builder().httpClient(custom).sandboxBaseUrl(server.url("/").toString())
                .credentials("a", "b", "c").build();

        assertThrows(ApiException.class, () -> client.sandbox().simulateTransactionScenario(scenario()));
        assertEquals(2, server.getRequestCount(), "the login and one POST");
    }

    @Test
    void aLoginOnADroppedPooledConnectionIsRecoveredAndTheGetSucceeds() throws Exception {
        // A token that lives 2 s (renewed after 1 s), so the second GET logs in again on the pooled connection.
        String shortToken = "{\"access_token\":\"t\",\"token_type\":\"Bearer\",\"expires_in\":2}";
        String page = "{\"_embedded\":{\"subaccounts\":[]},\"_links\":{\"self\":{\"href\":\"/subaccounts\"}}}";
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(shortToken));
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(page));
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST)); // the next login
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(shortToken));
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(page));
        SubaccountsApi api = new SubaccountsApi(plainClient());

        api.listSubaccounts(null, 10);
        Thread.sleep(1100);
        api.listSubaccounts(null, 10);
        assertEquals(5, server.getRequestCount(), "login, GET, dropped login, login again, GET");
    }

    @Test
    void bodyLoggingStillLogsAPostBodyAndRedactsTheToken() throws Exception {
        java.util.List<String> lines = new java.util.ArrayList<>();
        java.util.logging.Logger okhttp = java.util.logging.Logger.getLogger("okhttp3.OkHttpClient");
        java.util.logging.Handler handler = new java.util.logging.Handler() {
            @Override public void publish(java.util.logging.LogRecord record) { lines.add(record.getMessage()); }
            @Override public void flush() {}
            @Override public void close() {}
        };
        okhttp.addHandler(handler);
        java.util.logging.Level level = okhttp.getLevel();
        okhttp.setLevel(java.util.logging.Level.ALL);
        try {
            server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
            server.enqueue(new MockResponse().setResponseCode(202).setHeader("Content-Type", "application/json")
                    .setBody("{\"transactions\":[]}"));
            ApiClient client = client();
            client.setDebugging(okhttp3.logging.HttpLoggingInterceptor.Level.BODY);
            client.sandbox().simulateTransactionScenario(scenario());
        } finally {
            okhttp.removeHandler(handler);
            okhttp.setLevel(level);
        }
        String log = String.join("\n", lines);
        org.junit.jupiter.api.Assertions.assertTrue(log.contains("\"scenario\""), log);
        org.junit.jupiter.api.Assertions.assertFalse(log.contains("one-shot body omitted"), log);
        org.junit.jupiter.api.Assertions.assertTrue(log.contains("Authorization: \u2588\u2588"), log);
    }

    @Test
    void mintSdkAccessTokenMintsWithTheSdkClientAndNeverReturnsTheBackendToken() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody("{}"));
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json")
                .setBody("{\"access_token\":\"sdk-token\",\"token_type\":\"Bearer\",\"expires_in\":300,"
                        + "\"scope\":\"subaccounts:read card-subscriptions:write\"}"));
        ApiClient client = plainClient();
        client.request("GET", "/subaccounts", null, null);
        server.takeRequest(); // the backend login, token "t"
        assertEquals("Bearer t", server.takeRequest().getHeader("Authorization")); // the list
        assertEquals("sdk-token", client.mintSdkAccessToken("sdk-id", "sdk-secret",
                java.util.Arrays.asList("subaccounts:read", "card-subscriptions:write")));
        String form = server.takeRequest().getBody().readUtf8();
        org.junit.jupiter.api.Assertions.assertTrue(form.contains("client_id=sdk-id"), form);
        org.junit.jupiter.api.Assertions.assertFalse(form.contains("scope="), form);
    }

    @Test
    void mintSdkAccessTokenRefusesATokenWithMoreScopesThanAllowed() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json")
                .setBody("{\"access_token\":\"sdk-token\",\"expires_in\":300,\"scope\":\"subaccounts:read banking:admin\"}"));
        ApiClient client = plainClient();
        co.astrada.client.oauth.UnexpectedScopesException e = assertThrows(co.astrada.client.oauth.UnexpectedScopesException.class,
                () -> client.mintSdkAccessToken("sdk-id", "sdk-secret", java.util.Collections.singleton("subaccounts:read")));
        assertEquals(java.util.Collections.singletonList("banking:admin"), e.getExtraScopes());
    }

    @Test
    void mintSdkAccessTokenRefusesTheBackendClientUnlessItCallsTheSandbox() {
        ApiClient production = plainClient();
        assertThrows(IllegalArgumentException.class, () -> production.mintSdkAccessToken("b", "c", null));
        // The label says sandbox, the host is production: refused too.
        ApiClient mislabelled = Astrada.builder().environment(co.astrada.client.Environment.SANDBOX)
                .baseUrl("https://api.astrada.co").credentials("a", "b", "c").build();
        assertThrows(IllegalArgumentException.class, () -> mislabelled.mintSdkAccessToken("b", "c", null));
        assertThrows(IllegalArgumentException.class, () -> production.mintSdkAccessToken("", "c", null));
        assertThrows(IllegalArgumentException.class, () -> production.mintSdkAccessToken("sdk", null, null));
        assertEquals(0, server.getRequestCount(), "refused before any request");
        assertThrows(IllegalStateException.class, () -> new ApiClient().mintSdkAccessToken("x", "y", null));
    }

    @Test
    void mintSdkAccessTokenAllowsOneClientInTheSandbox() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        assertEquals("t", client().mintSdkAccessToken("b", "c", null));
    }

    @Test
    void requestSendsTheSandboxOnlyDeletesOnceAndOnlyInTheSandbox() throws Exception {
        ApiClient production = plainClient();
        for (String path : new String[] {"/subaccounts/s1", "/card-subscriptions/c1", "/network-bulk-feeds/f1", "/x/../subaccounts/s1"}) {
            assertThrows(co.astrada.client.sandbox.SandboxOnlyException.class, () -> production.request("DELETE", path, null, null));
        }
        assertEquals(0, server.getRequestCount());
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        server.enqueue(new MockResponse().setResponseCode(503).setHeader("Retry-After", "0"));
        server.enqueue(new MockResponse().setResponseCode(204));
        ApiException e = assertThrows(ApiException.class, () -> client().request("DELETE", "/subaccounts/s1", null, null));
        assertEquals(503, e.getCode());
        assertEquals(2, server.getRequestCount(), "the login and one delete");
    }

    @Test
    void requestLogsInRetriesOnlyIdempotentCallsAndThrowsOnErrors() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        server.enqueue(new MockResponse().setResponseCode(503).setHeader("Retry-After", "0"));
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody("{\"items\":[1]}"));
        server.enqueue(new MockResponse().setResponseCode(503).setHeader("Retry-After", "0"));
        server.enqueue(new MockResponse().setResponseCode(404).setHeader("Content-Type", "application/problem+json")
                .setBody("{\"status\":404,\"title\":\"Not Found\",\"detail\":\"no such widget\"}"));
        ApiClient client = plainClient();
        java.util.Map<String, Object> query = new java.util.HashMap<>();
        query.put("limit", 1);
        com.google.gson.JsonElement page = client.request("GET", "/widgets", query, null);
        assertEquals(1, page.getAsJsonObject().get("items").getAsJsonArray().size());
        server.takeRequest(); // the login
        okhttp3.mockwebserver.RecordedRequest first = server.takeRequest();
        assertEquals("Bearer t", first.getHeader("Authorization"));
        assertEquals("/widgets?limit=1", first.getPath());
        server.takeRequest(); // the retry
        ApiException post = assertThrows(ApiException.class,
                () -> client.request("POST", "/widgets", null, java.util.Collections.singletonMap("name", "x")));
        assertEquals(503, post.getCode());
        assertEquals("{\"name\":\"x\"}", server.takeRequest().getBody().readUtf8());
        ApiException missing = assertThrows(ApiException.class, () -> client.request("GET", "/widgets/w1", null, null));
        assertEquals(404, missing.getCode());
        assertEquals(5, server.getRequestCount(), "the login, GET twice, POST once, the 404");
        assertThrows(IllegalArgumentException.class, () -> client.request("GET", "widgets", null, null));
        assertThrows(co.astrada.client.sandbox.SandboxOnlyException.class,
                () -> client.request("POST", "/x/../%73imulate/transactions", null, null));
        assertEquals(5, server.getRequestCount());
    }

    @Test
    void theReadmeAllowedScopesTakeTheSandboxSdkClientAndRefuseABackendScope() throws Exception {
        java.util.List<String> readme = java.util.Arrays.asList("subaccounts:read", "card-subscriptions:read",
                "card-subscriptions:write", "card-verifications:read", "card-verifications:write",
                "enrollment-methods:write", "banking:read", "banking:write");
        // The scopes the sandbox SDK client's token carried on 2026-11-25.
        String sandboxSdk = "enrollment-methods:write card-verifications:read card-verifications:write banking:read "
                + "banking:write subaccounts:read card-subscriptions:read card-subscriptions:write";
        for (String extra : new String[] {"", " banking:admin", " webhooks:write"}) {
            server.enqueue(new MockResponse().setHeader("Content-Type", "application/json")
                    .setBody("{\"access_token\":\"sdk-token\",\"expires_in\":300,\"scope\":\"" + sandboxSdk + extra + "\"}"));
            ApiClient client = plainClient();
            if (extra.isEmpty()) {
                assertEquals("sdk-token", client.mintSdkAccessToken("sdk-id", "sdk-secret", readme));
            } else {
                assertThrows(co.astrada.client.oauth.UnexpectedScopesException.class, () -> client.mintSdkAccessToken("sdk-id", "sdk-secret", readme));
            }
        }
    }

    @Test
    void aLoginThatStatesNoScopeIsItsOwnError() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        co.astrada.client.oauth.UnexpectedScopesException e = assertThrows(co.astrada.client.oauth.UnexpectedScopesException.class,
                () -> plainClient().mintSdkAccessToken("sdk-id", "sdk-secret", java.util.Collections.singleton("subaccounts:read")));
        assertEquals(java.util.Collections.emptyList(), e.getExtraScopes());
    }

    @Test
    void theRequestPathRuleRefusesAndNormalises() {
        assertEquals("/subaccounts/s1", ApiClient.normaliseRequestPath("/x/..//subaccounts/s1"));
        assertEquals("/subaccounts/s1", ApiClient.normaliseRequestPath("/subaccounts//s1/"));
        for (String bad : new String[] {"/subaccounts/s1#/x", "/subaccounts/s1?x=1", "/subaccounts%2Fs1", "/a%2e%2e/b",
                "/x\\y", "/x%zz", "subaccounts", "/simu\tlate/transactions", "/x/.\t./subaccounts/s1", "/x\r\ny",
                "/x\ny#", "/x%0Ay", "/x%5Cy", "/x%3Fy", "/x%23y", "/simulate ", "/subaccounts/s1/. ", "/x y",
                "/x%00", "/x%1f", "/x%7F"}) {
            assertThrows(IllegalArgumentException.class, () -> ApiClient.normaliseRequestPath(bad), bad);
        }
        ApiClient production = plainClient();
        String[][] guarded = {{"DELETE", "/x/..//subaccounts/s1"}, {"DELETE", "/subaccounts//s1/"}, {"delete", "/Card-Subscriptions/c1"},
                {"POST", "//simulate/transactions"}, {"POST", "/simulate/transactions/t1"}, {"DELETE", "/network-bulk-feeds/f1"}};
        for (String[] call : guarded) {
            assertThrows(co.astrada.client.sandbox.SandboxOnlyException.class, () -> production.request(call[0], call[1], null, null),
                    call[0] + " " + call[1]);
        }
        assertThrows(IllegalArgumentException.class, () -> production.request("DELETE", "/subaccounts/s1#/x", null, null));
        assertEquals(0, server.getRequestCount());
    }

    @Test
    void aBlankScopeIsNotStated() throws Exception {
        for (String scope : new String[] {"", "   "}) {
            server.enqueue(new MockResponse().setHeader("Content-Type", "application/json")
                    .setBody("{\"access_token\":\"sdk-token\",\"expires_in\":300,\"scope\":\"" + scope + "\"}"));
            co.astrada.client.oauth.UnexpectedScopesException e = assertThrows(co.astrada.client.oauth.UnexpectedScopesException.class,
                    () -> plainClient().mintSdkAccessToken("sdk-id", "sdk-secret", java.util.Collections.singleton("subaccounts:read")));
            assertEquals(java.util.Collections.emptyList(), e.getExtraScopes());
        }
    }

    @Test
    void theOwnClientCheckFollowsTheBaseUrlTheTokenIsMintedAt() {
        ApiClient production = plainClient();
        production.setBasePath("https://api.sandbox.astrada.co");
        assertThrows(IllegalArgumentException.class, () -> production.mintSdkAccessToken("b", "c", null));
        assertEquals(0, server.getRequestCount());
    }

    @Test
    void refusedPathMessagesNameControlCharactersAndNeverEchoThem() {
        IllegalArgumentException raw = assertThrows(IllegalArgumentException.class,
                () -> ApiClient.normaliseRequestPath("/x\r\nforged: line"));
        org.junit.jupiter.api.Assertions.assertFalse(raw.getMessage().contains("\n"), raw.getMessage());
        org.junit.jupiter.api.Assertions.assertTrue(raw.getMessage().contains("\\u000d\\u000a"), raw.getMessage());
        IllegalArgumentException encoded = assertThrows(IllegalArgumentException.class, () -> ApiClient.normaliseRequestPath("/x%0A"));
        org.junit.jupiter.api.Assertions.assertTrue(encoded.getMessage().contains("control character"), encoded.getMessage());
        assertEquals("/x%20y", ApiClient.normaliseRequestPath("/x%20y"));
        for (String separator : new String[] {"\u0085", "\u2028", "\u2029"}) {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> ApiClient.normaliseRequestPath("x" + separator + "forged"));
            org.junit.jupiter.api.Assertions.assertFalse(e.getMessage().contains(separator), e.getMessage());
        }
    }

    @Test
    void requestSendsQueryCollectionsAndArraysAsRepeatedPairs() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody(TOKEN));
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody("{}"));
        java.util.Map<String, Object> query = new java.util.LinkedHashMap<>();
        query.put("status", java.util.Arrays.asList("a", null, "b"));
        query.put("network", new String[] {"VISA"});
        query.put("gone", null);
        query.put("limit", 5);
        plainClient().request("GET", "/cards", query, null);
        server.takeRequest(); // the login
        assertEquals("/cards?status=a&status=b&network=VISA&limit=5", server.takeRequest().getPath());
    }
}

