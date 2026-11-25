package co.astrada.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.astrada.client.api.BankLinksApi;
import co.astrada.client.api.BankSubscriptionsApi;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Banking methods that need a scope turn a 403 into ScopeRequiredException. Hand-written. */
class ScopeRequiredTest {
    private static final String TOKEN = "{\"access_token\":\"t\",\"token_type\":\"Bearer\",\"expires_in\":300}";
    private static final String FORBIDDEN = "{\"title\":\"Forbidden\",\"detail\":\"Missing scope\"}";

    private MockWebServer server;
    private ApiClient client;

    @BeforeEach
    void start() throws Exception {
        server = new MockWebServer();
        server.start();
        client = Astrada.builder().baseUrl(server.url("/").toString()).credentials("a", "b", "c").maxRetries(0).build();
    }

    @AfterEach
    void stop() throws Exception {
        server.shutdown();
    }

    private static MockResponse json(int status, String body) {
        return new MockResponse().setResponseCode(status).setHeader("Content-Type", "application/json").setBody(body);
    }

    @Test
    void a403OnAMethodThatNeedsAScopeNamesTheScope() {
        server.enqueue(json(200, TOKEN));
        server.enqueue(json(403, FORBIDDEN));

        ScopeRequiredException e = assertThrows(ScopeRequiredException.class,
                () -> new BankLinksApi(client).syncBankLink("bl-1", null));

        assertEquals(403, e.getCode());
        assertEquals("syncBankLink", e.getOperationId());
        assertEquals("banking:admin", e.getRequiredScope());
        assertEquals("Missing scope", e.getDetail());
        assertTrue(e.getMessage().startsWith(
                "(403) syncBankLink needs the `banking:admin` scope. The likely cause is that your API client "
                + "does not have it; if so, ask Astrada to enable it. A 403 can also mean the resource is not "
                + "this account's."),
                e.getMessage());
    }

    @Test
    void otherErrorsStayPlainApiExceptions() {
        server.enqueue(json(200, TOKEN));
        server.enqueue(json(404, "{\"statusCode\":404,\"message\":\"Bank subscription not found\"}"));

        ApiException e = assertThrows(ApiException.class,
                () -> new BankSubscriptionsApi(client).getBankSubscription(java.util.UUID.randomUUID()));

        assertFalse(e instanceof ScopeRequiredException);
        assertEquals(404, e.getCode());
    }

    @Test
    void aCustomerMethodKeepsItsPlain403() {
        server.enqueue(json(200, TOKEN));
        server.enqueue(json(403, FORBIDDEN));

        ApiException e = assertThrows(ApiException.class,
                () -> new BankLinksApi(client).listBankLinks(null, null, 10, null));

        assertFalse(e instanceof ScopeRequiredException);
    }

    @Test
    void theAsyncCallbackGetsItToo() throws Exception {
        server.enqueue(json(200, TOKEN));
        server.enqueue(json(403, FORBIDDEN));
        CompletableFuture<ApiException> failure = new CompletableFuture<>();

        new BankLinksApi(client).deleteBankLinkAsync("bl-1", new ApiCallback<Void>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                failure.complete(e);
            }

            @Override
            public void onSuccess(Void result, int statusCode, Map<String, List<String>> responseHeaders) {
                failure.complete(null);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {}

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {}
        });

        ApiException e = failure.get(10, TimeUnit.SECONDS);
        assertTrue(e instanceof ScopeRequiredException, String.valueOf(e));
        assertEquals("banking:admin", ((ScopeRequiredException) e).getRequiredScope());
    }
}
