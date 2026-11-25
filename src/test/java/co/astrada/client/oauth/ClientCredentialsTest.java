package co.astrada.client.oauth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.astrada.client.ApiClient;
import co.astrada.client.api.SubaccountsApi;
import java.util.UUID;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Checks the hand-written client-credentials login against a local HTTP server. */
class ClientCredentialsTest {
    private static final UUID SUBACCOUNT = UUID.fromString("2f8a3c1e-5b6d-4e7f-8a9b-0c1d2e3f4a5b");

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

    private String basePath() {
        return server.url("/").toString().replaceAll("/$", "");
    }

    private static MockResponse json(int status, String body) {
        return new MockResponse().setResponseCode(status).setHeader("Content-Type", "application/json").setBody(body);
    }

    private static MockResponse token(String value) {
        return json(200, "{\"access_token\":\"" + value + "\",\"token_type\":\"Bearer\",\"expires_in\":300}");
    }

    private SubaccountsApi api(ClientCredentials auth) {
        ApiClient client = new ApiClient();
        client.setBasePath(basePath());
        client.setHttpClient(auth.addTo(client.getHttpClient()));
        return new SubaccountsApi(client);
    }

    @Test
    void defaultsToProduction() {
        assertEquals(ClientCredentials.PRODUCTION, new ApiClient().getBasePath());
        assertEquals("https://api.sandbox.astrada.co/auth/realms/acct-1/protocol/openid-connect/token",
                new ClientCredentials(ClientCredentials.SANDBOX, "acct-1", "c", "s").getTokenUrl());
    }

    @Test
    void logsInWithClientCredentialsAndSendsTheBearerToken() throws Exception {
        server.enqueue(token("token-1"));
        server.enqueue(json(200, "{\"id\":\"" + SUBACCOUNT + "\"}"));

        ClientCredentials auth = new ClientCredentials(basePath(), "acct-1", "client-1", "secret-1");
        try (Response response = api(auth).getSubaccountCall(SUBACCOUNT, null).execute()) {
            assertEquals(200, response.code());
        }

        RecordedRequest login = server.takeRequest();
        assertEquals("POST", login.getMethod());
        assertEquals("/auth/realms/acct-1/protocol/openid-connect/token", login.getPath());
        String form = login.getBody().readUtf8();
        assertTrue(form.contains("grant_type=client_credentials"), form);
        assertTrue(form.contains("client_id=client-1"), form);
        assertTrue(form.contains("client_secret=secret-1"), form);

        RecordedRequest call = server.takeRequest();
        assertEquals("/subaccounts/" + SUBACCOUNT, call.getPath());
        assertEquals("Bearer token-1", call.getHeader("Authorization"));
    }

    @Test
    void reusesTheTokenAndLogsInAgainAfterA401() throws Exception {
        server.enqueue(token("token-1"));
        server.enqueue(json(200, "{}"));
        server.enqueue(json(200, "{}"));
        server.enqueue(json(401, "{}"));
        server.enqueue(token("token-2"));
        server.enqueue(json(200, "{}"));

        SubaccountsApi api = api(new ClientCredentials(basePath(), "acct-1", "client-1", "secret-1"));
        for (int i = 0; i < 3; i++) {
            try (Response response = api.getSubaccountCall(SUBACCOUNT, null).execute()) {
                assertEquals(200, response.code());
            }
        }

        assertEquals(6, server.getRequestCount());
        server.takeRequest();
        assertEquals("Bearer token-1", server.takeRequest().getHeader("Authorization"));
        assertEquals("Bearer token-1", server.takeRequest().getHeader("Authorization"));
        assertEquals("Bearer token-1", server.takeRequest().getHeader("Authorization"));
        assertTrue(server.takeRequest().getPath().endsWith("/openid-connect/token"));
        assertEquals("Bearer token-2", server.takeRequest().getHeader("Authorization"));
    }

    @Test
    void aRefusedLoginRaisesAuthenticationException() {
        server.enqueue(json(401, "{\"error\":\"unauthorized_client\",\"error_description\":\"Invalid client credentials\"}"));

        ClientCredentials auth = new ClientCredentials(basePath(), "acct-1", "client-1", "wrong");
        ClientCredentials.AuthenticationException error =
                assertThrows(ClientCredentials.AuthenticationException.class, auth::getAccessToken);
        assertEquals(401, error.getStatus());
        assertTrue(error.getMessage().contains("Invalid client credentials"));
    }

    @Test
    void aLoginRedirectIsNotFollowedSoTheSecretGoesNowhereElse() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(307).setHeader("Location", "/elsewhere?code=secret-code"));
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json")
                .setBody("{\"access_token\":\"stolen\",\"token_type\":\"Bearer\",\"expires_in\":300}"));
        SubaccountsApi api = api(new ClientCredentials(basePath(), "acct-1", "client-1", "secret-1"));

        Exception e = assertThrows(Exception.class, () -> api.listSubaccounts(null, 10));
        String message = String.valueOf(e.getMessage()) + " " + (e.getCause() == null ? "" : e.getCause().getMessage());
        assertTrue(message.contains("redirect to http://" + server.getHostName() + ":" + server.getPort() + "."), message);
        assertTrue(!message.contains("secret-code") && !message.contains("elsewhere"), message);
        assertEquals(1, server.getRequestCount(), "the secret was not sent to the redirect target");
    }

    @Test
    void clearingARefusedTokenKeepsANewerOne() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json")
                .setBody("{\"access_token\":\"first\",\"token_type\":\"Bearer\",\"expires_in\":300}"));
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json")
                .setBody("{\"access_token\":\"second\",\"token_type\":\"Bearer\",\"expires_in\":300}"));
        ClientCredentials auth = new ClientCredentials(basePath(), "acct-1", "client-1", "secret-1");
        assertEquals("first", auth.getAccessToken());
        auth.clear("first");
        assertEquals("second", auth.getAccessToken());
        // A 401 on "first" seen late (another thread already logged in again) keeps "second".
        auth.clear("first");
        assertEquals("second", auth.getAccessToken());
        assertEquals(2, server.getRequestCount());
    }

    @Test
    void aShortLivedTokenIsReusedInsteadOfALoginOnEveryRequest() throws Exception {
        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json")
                .setBody("{\"access_token\":\"short\",\"token_type\":\"Bearer\",\"expires_in\":20}"));
        ClientCredentials auth = new ClientCredentials(basePath(), "acct-1", "client-1", "secret-1");
        assertEquals("short", auth.getAccessToken());
        assertEquals("short", auth.getAccessToken());
        assertEquals(1, server.getRequestCount(), "renewed halfway through its 20 s, not at once");
    }
}
