package co.astrada.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.astrada.client.api.BankLinksApi;
import co.astrada.client.api.SubaccountsApi;
import co.astrada.client.model.BankLink;
import co.astrada.client.model.EnrollmentLinkConflictProblem;
import co.astrada.client.model.Subaccount;
import co.astrada.client.model.SubaccountList;
import co.astrada.client.pagination.Pages;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * One client for the card APIs and the Banking API, against a local HTTP server: login, cursor
 * pagination, lenient reading of new fields and enum values, errors and debug logging.
 * Hand-written.
 */
class AstradaClientTest {
    private static final String TOKEN = "{\"access_token\":\"token-1\",\"token_type\":\"Bearer\",\"expires_in\":300}";
    private static final String SUB_ID = "2f8a3c1e-5b6d-4e7f-8a9b-0c1d2e3f4a5b";

    private MockWebServer server;
    private ApiClient client;

    @BeforeEach
    void start() throws Exception {
        server = new MockWebServer();
        server.start();
        client = Astrada.builder()
                .baseUrl(server.url("/").toString()) // trailing slash on purpose
                .credentials("acct-1", "client-1", "secret-1")
                .maxRetries(0)
                .build();
    }

    @AfterEach
    void stop() throws Exception {
        server.shutdown();
    }

    private static MockResponse json(int status, String body) {
        return new MockResponse().setResponseCode(status).setHeader("Content-Type", "application/json").setBody(body);
    }

    private static String subaccount(String id, String extra) {
        return "{\"_links\":{\"self\":{\"href\":\"/subaccounts/" + id + "\"}},\"accountId\":\"" + UUID.randomUUID()
                + "\",\"id\":\"" + id + "\",\"name\":\"Shop " + id.substring(0, 4) + "\","
                + "\"createdAt\":\"2026-01-01T00:00:00Z\",\"updatedAt\":\"2026-01-01T00:00:00Z\",\"configurations\":{}"
                + extra + "}";
    }

    private static String subaccountPage(String id, String next) {
        String nextLink = next == null ? "" : ",\"next\":{\"href\":\"/subaccounts?cursor=" + next + "&limit=1\"}";
        return "{\"_embedded\":{\"subaccounts\":[" + subaccount(id, "") + "]},"
                + "\"_links\":{\"self\":{\"href\":\"/subaccounts\"}" + nextLink + "}}";
    }

    @Test
    void walksEveryPageWithAStringCursor() throws Exception {
        String second = UUID.randomUUID().toString();
        server.enqueue(json(200, TOKEN));
        server.enqueue(json(200, subaccountPage(SUB_ID, "eyJwayI6IjEifQ%3D%3D")));
        server.enqueue(json(200, subaccountPage(second, null)));

        SubaccountsApi api = new SubaccountsApi(client);
        List<UUID> ids = new ArrayList<>();
        for (Subaccount s : Pages.iterate(cursor -> api.listSubaccounts(cursor, 1),
                page -> page.getEmbedded().getSubaccounts())) {
            ids.add(s.getId());
        }

        assertEquals(List.of(UUID.fromString(SUB_ID), UUID.fromString(second)), ids);
        assertEquals("/auth/realms/acct-1/protocol/openid-connect/token", server.takeRequest().getPath());
        RecordedRequest first = server.takeRequest();
        assertEquals("/subaccounts?limit=1", first.getPath());
        assertEquals("Bearer token-1", first.getHeader("Authorization"));
        assertEquals("/subaccounts?cursor=eyJwayI6IjEifQ%3D%3D&limit=1", server.takeRequest().getPath());
    }

    @Test
    void callsTheBankingApiWithTheSameClient() throws Exception {
        String link = "{\"_links\":{\"self\":{\"href\":\"/bank-links/bl-1\"}},\"id\":\"bl-1\",\"accountId\":\""
                + UUID.randomUUID() + "\",\"subaccountId\":\"" + SUB_ID + "\",\"state\":\"pending\","
                + "\"hostedLink\":\"https://link.example\",\"linkExpiresAt\":\"2026-01-01T00:00:00Z\","
                + "\"createdAt\":\"2026-01-01T00:00:00Z\"}";
        server.enqueue(json(200, TOKEN));
        server.enqueue(json(200, "{\"items\":[" + link + "],\"pagination\":{\"cursor\":\"c2\",\"hasMore\":true}}"));
        server.enqueue(json(200, "{\"items\":[" + link.replace("bl-1", "bl-2") + "],\"pagination\":{\"hasMore\":false}}"));

        BankLinksApi bankLinks = new BankLinksApi(client);
        List<String> ids = new ArrayList<>();
        for (BankLink l : Pages.iterate(cursor -> bankLinks.listBankLinks(null, null, 1, cursor),
                page -> page.getItems())) {
            ids.add(l.getId());
        }

        assertEquals(List.of("bl-1", "bl-2"), ids);
        server.takeRequest();
        assertEquals("/bank-links?limit=1", server.takeRequest().getPath());
        RecordedRequest second = server.takeRequest();
        assertEquals("/bank-links?limit=1&cursor=c2", second.getPath());
        assertEquals("Bearer token-1", second.getHeader("Authorization"));
    }

    @Test
    void keepsUnknownFieldsAndEnumValues() throws Exception {
        String body = "{\"_embedded\":{\"subaccounts\":[" + subaccount(SUB_ID, ",\"newField\":{\"a\":1}") + "]},"
                + "\"_links\":{\"self\":{\"href\":\"/subaccounts\"}},\"newTopLevel\":\"x\"}";
        server.enqueue(json(200, TOKEN));
        server.enqueue(json(200, body));

        SubaccountList page = new SubaccountsApi(client).listSubaccounts(null, 10);

        assertEquals("x", page.getAdditionalProperty("newTopLevel"));
        Subaccount s = page.getEmbedded().getSubaccounts().get(0);
        assertTrue(s.getAdditionalProperties().containsKey("newField"));
        assertNull(page.nextCursor());
    }

    @Test
    void readsAnUnknownEnumValueAsUnknownAndKeepsIt() throws Exception {
        String body = "{\"_links\":{\"self\":{\"href\":\"/bank-links/bl-1\"}},\"id\":\"bl-1\",\"accountId\":\""
                + UUID.randomUUID() + "\",\"subaccountId\":\"" + SUB_ID + "\",\"state\":\"paused_by_bank\","
                + "\"hostedLink\":\"https://link.example\",\"linkExpiresAt\":\"2026-01-01T00:00:00Z\","
                + "\"createdAt\":\"2026-01-01T00:00:00Z\"}";
        server.enqueue(json(200, TOKEN));
        server.enqueue(json(200, body));

        BankLink link = new BankLinksApi(client).getBankLink("bl-1");

        assertEquals(BankLink.StateEnum.UNKNOWN_DEFAULT_OPEN_API, link.getState());
        assertEquals("paused_by_bank", link.getAdditionalProperty("state"));
        assertTrue(link.toJson().contains("\"state\":\"paused_by_bank\""), link.toJson());
    }

    @Test
    void throwsApiExceptionWithTheParsedErrorBody() throws Exception {
        server.enqueue(json(200, TOKEN));
        server.enqueue(json(409, "{\"title\":\"Conflict\",\"detail\":\"Enrollment links are off\","
                + "\"code\":\"ENROLLMENT_LINKS_NOT_ENABLED\"}").setHeader("Content-Type", "application/problem+json"));

        ApiException e = assertThrows(ApiException.class,
                () -> new SubaccountsApi(client).getSubaccount(UUID.fromString(SUB_ID)));

        assertEquals(409, e.getCode());
        assertEquals("Conflict", e.getTitle());
        assertEquals("Enrollment links are off", e.getDetail());
        assertEquals("ENROLLMENT_LINKS_NOT_ENABLED", e.getErrorCode());
        assertEquals("HTTP 409 Conflict: Enrollment links are off (ENROLLMENT_LINKS_NOT_ENABLED)", e.getMessage());
        EnrollmentLinkConflictProblem problem = e.getErrorBody(EnrollmentLinkConflictProblem.class);
        assertEquals(EnrollmentLinkConflictProblem.CodeEnum.ENROLLMENT_LINKS_NOT_ENABLED, problem.getCode());
    }

    @Test
    void readsTheBankingErrorBody() throws Exception {
        server.enqueue(json(200, TOKEN));
        server.enqueue(json(404, "{\"statusCode\":404,\"message\":\"Bank link not found\",\"code\":\"NOT_FOUND\"}"));

        ApiException e = assertThrows(ApiException.class, () -> new BankLinksApi(client).getBankLink("bl-9"));

        assertEquals(404, e.getCode());
        assertEquals("Bank link not found", e.getDetail());
        assertEquals("NOT_FOUND", e.getErrorCode());
    }

    @Test
    void aBodyThatDoesNotFitIsAnApiExceptionToo() throws Exception {
        server.enqueue(json(200, TOKEN));
        server.enqueue(json(200, "{\"_embedded\":[]}"));

        ApiException e = assertThrows(ApiException.class, () -> new SubaccountsApi(client).listSubaccounts(null, 1));

        assertEquals(200, e.getCode());
    }

    @Test
    void aRefusedLoginIsAnApiException() throws Exception {
        server.enqueue(json(401, "{\"error\":\"unauthorized_client\",\"error_description\":\"Invalid client\"}"));

        ApiException e = assertThrows(ApiException.class, () -> new SubaccountsApi(client).listSubaccounts(null, 1));

        assertTrue(e.getMessage().contains("Invalid client"), e.getMessage());
    }

    @Test
    void debugLoggingNeverShowsTheToken() throws Exception {
        List<String> lines = new ArrayList<>();
        Handler capture = new Handler() {
            @Override
            public void publish(LogRecord record) {
                lines.add(record.getMessage());
            }

            @Override
            public void flush() {}

            @Override
            public void close() {}
        };
        Logger okhttp = Logger.getLogger("okhttp3.OkHttpClient");
        okhttp.addHandler(capture);
        okhttp.setLevel(Level.ALL);
        try {
            client.setDebugging(okhttp3.logging.HttpLoggingInterceptor.Level.BODY);
            server.enqueue(json(200, TOKEN));
            server.enqueue(json(200, subaccountPage(SUB_ID, null)));

            new SubaccountsApi(client).listSubaccounts(null, 1);

            String log = String.join("\n", lines);
            assertTrue(log.contains("Authorization: ██"), log);
            assertFalse(log.contains("token-1"), log);
            assertFalse(log.contains("secret-1"), log);
        } finally {
            okhttp.removeHandler(capture);
        }
    }

    @Test
    void environmentsPointAtTheirHosts() {
        assertEquals("https://api.sandbox.astrada.co", Environment.SANDBOX.getBaseUrl());
        assertEquals("https://api.astrada.co", Environment.PRODUCTION.getBaseUrl());
        ApiClient sandbox = Astrada.builder().environment(Environment.SANDBOX).credentials("a", "b", "c").build();
        assertEquals("https://api.sandbox.astrada.co", sandbox.getBasePath());
        assertThrows(IllegalStateException.class, () -> Astrada.builder().build());
    }

    @Test
    void aRefusedLoginIsNotRetriedWithDefaultRetries() throws Exception {
        ApiClient defaults = Astrada.builder().baseUrl(server.url("/").toString())
                .credentials("acct-1", "client-1", "wrong").build(); // default maxRetries (2)
        server.enqueue(json(401, "{\"error\":\"unauthorized_client\",\"error_description\":\"Invalid client\"}"));
        server.enqueue(json(401, "{}"));
        server.enqueue(json(401, "{}"));

        ApiException e = assertThrows(ApiException.class, () -> new SubaccountsApi(defaults).listSubaccounts(null, 1));

        assertTrue(e.getMessage().contains("Invalid client"), e.getMessage());
        assertEquals(1, server.getRequestCount(), "one token request, no retries");
    }

    @Test
    void aTokenEndpoint503IsRetried() throws Exception {
        ApiClient defaults = Astrada.builder().baseUrl(server.url("/").toString())
                .credentials("acct-1", "client-1", "secret-1").build();
        server.enqueue(json(503, "{}"));
        server.enqueue(json(200, TOKEN));
        server.enqueue(json(200, subaccountPage(SUB_ID, null)));

        new SubaccountsApi(defaults).listSubaccounts(null, 1);

        assertEquals(3, server.getRequestCount());
    }

    @Test
    void theLoginUsesTheBuildersHttpClientWithoutItsLogging() throws Exception {
        List<String> logged = new ArrayList<>();
        okhttp3.logging.HttpLoggingInterceptor logging = new okhttp3.logging.HttpLoggingInterceptor(logged::add);
        logging.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY);
        okhttp3.OkHttpClient base = new okhttp3.OkHttpClient.Builder()
                .addInterceptor(chain -> chain.proceed(chain.request().newBuilder().header("X-Proxy-Auth", "p1").build()))
                .addInterceptor(logging)
                .build();
        ApiClient custom = Astrada.builder().baseUrl(server.url("/").toString()).httpClient(base)
                .credentials("acct-1", "client-1", "secret-1").maxRetries(0).build();
        server.enqueue(json(200, TOKEN));
        server.enqueue(json(200, subaccountPage(SUB_ID, null)));

        new SubaccountsApi(custom).listSubaccounts(null, 1);

        RecordedRequest login = server.takeRequest();
        assertEquals("/auth/realms/acct-1/protocol/openid-connect/token", login.getPath());
        assertEquals("p1", login.getHeader("X-Proxy-Auth"), "the login went through the given client");
        assertEquals("p1", server.takeRequest().getHeader("X-Proxy-Auth"));
        String log = String.join("\n", logged);
        assertTrue(log.contains("/subaccounts"), log);
        assertFalse(log.contains("secret-1"), log);
        assertFalse(log.contains("openid-connect"), log);
    }

    @Test
    void anUnknownNullFieldDoesNotBreakWriting() throws Exception {
        String body = "{\"_embedded\":{\"subaccounts\":[]},\"_links\":{\"self\":{\"href\":\"/subaccounts\"}},"
                + "\"newNullable\":null}";
        server.enqueue(json(200, TOKEN));
        server.enqueue(json(200, body));

        SubaccountList page = new SubaccountsApi(client).listSubaccounts(null, 10);

        // It used to throw (JsonNull is not an object). Gson leaves null fields out when writing.
        assertTrue(page.getAdditionalProperties().containsKey("newNullable"));
        assertNull(page.getAdditionalProperty("newNullable"));
        assertEquals(page.toJson(), SubaccountList.fromJson(page.toJson()).toJson());
    }

    @Test
    void aKnownEnumValueSetLaterWinsOverTheKeptRawValue() throws Exception {
        String body = "{\"_links\":{\"self\":{\"href\":\"/bank-links/bl-1\"}},\"id\":\"bl-1\",\"accountId\":\""
                + UUID.randomUUID() + "\",\"subaccountId\":\"" + SUB_ID + "\",\"state\":\"paused_by_bank\","
                + "\"hostedLink\":\"https://link.example\",\"linkExpiresAt\":\"2026-01-01T00:00:00Z\","
                + "\"createdAt\":\"2026-01-01T00:00:00Z\"}";
        server.enqueue(json(200, TOKEN));
        server.enqueue(json(200, body));

        BankLink link = new BankLinksApi(client).getBankLink("bl-1");
        link.setState(BankLink.StateEnum.COMPLETED);

        assertTrue(link.toJson().contains("\"state\":\"completed\""), link.toJson());
        assertFalse(link.toJson().contains("paused_by_bank"), link.toJson());
    }
}
