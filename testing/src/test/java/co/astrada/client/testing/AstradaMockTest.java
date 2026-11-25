package co.astrada.client.testing;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.JSON;
import co.astrada.client.api.BankLinksApi;
import co.astrada.client.api.NetworkBulkEnrollmentsApi;
import co.astrada.client.api.SubaccountsApi;
import co.astrada.client.model.SimulateTransactionScenarioRequest;
import co.astrada.client.model.SubaccountList;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** The shipped WireMock stubs work with the SDK, and a test can override one operation. Hand-written. */
class AstradaMockTest {
    private WireMockServer server;
    private ApiClient client;

    @BeforeEach
    void start() {
        server = new WireMockServer(options().dynamicPort());
        server.start();
        AstradaMock.register(server);
        client = AstradaMock.client(server.baseUrl());
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    void everyExampleBodyReadsAsItsModel() throws Exception {
        List<String> failures = new ArrayList<>();
        int checked = 0;
        for (String name : AstradaMock.names()) {
            JsonObject mapping;
            try (InputStream in = getClass().getClassLoader().getResourceAsStream(AstradaMock.MAPPINGS + name + ".json")) {
                mapping = JsonParser.parseString(new String(in.readAllBytes(), StandardCharsets.UTF_8)).getAsJsonObject();
            }
            JsonObject metadata = mapping.getAsJsonObject("metadata");
            if (!metadata.has("bodyModel") || metadata.get("bodyModel").isJsonNull()) {
                continue;
            }
            String model = metadata.get("bodyModel").getAsString();
            try {
                Class<?> type = Class.forName("co.astrada.client.model." + model);
                assertNotNull(JSON.getGson().fromJson(mapping.getAsJsonObject("response").get("jsonBody"), type));
                checked++;
            } catch (ClassNotFoundException | RuntimeException e) {
                failures.add(name + " as " + model + ": " + e.getMessage());
            }
        }
        assertEquals(List.of(), failures);
        assertFalse(checked < 50, "checked only " + checked);
    }

    @Test
    void listsSubaccountsFromTheExample() {
        SubaccountList page = new SubaccountsApi(client).listSubaccounts(null, 10);
        assertFalse(page.getEmbedded().getSubaccounts().isEmpty());
    }

    @Test
    void anOverrideWinsForThatOperationOnly() {
        AstradaMock.override(server, "GetSubaccount", aResponse().withStatus(404)
                .withHeader("Content-Type", "application/problem+json")
                .withBody("{\"title\":\"Not Found\",\"detail\":\"Subaccount not found\"}"));

        ApiException e = assertThrows(ApiException.class,
                () -> new SubaccountsApi(client).getSubaccount(UUID.randomUUID()));
        assertEquals(404, e.getCode());
        assertEquals("Subaccount not found", e.getDetail());
        assertFalse(new SubaccountsApi(client).listSubaccounts(null, 10).getEmbedded().getSubaccounts().isEmpty());
    }

    @Test
    void aLiteralPathBeatsATemplatedOne() {
        // GET /network-bulk-enrollments/open must not be answered by /network-bulk-enrollments/{id}.
        assertNotNull(new NetworkBulkEnrollmentsApi(client)
                .getOpenNetworkBulkEnrollment(UUID.randomUUID(), "VISA", "411111").getOpen());
    }

    @Test
    void callsTheBankingApi() {
        assertNotNull(new BankLinksApi(client).listBankLinks(null, null, 10, null).getItems());
    }

    @Test
    void sandboxCallsWorkAgainstTheMock() {
        assertNotNull(client.sandbox().simulateTransactionScenario(new SimulateTransactionScenarioRequest()
                .scenario(SimulateTransactionScenarioRequest.ScenarioEnum.SIMPLE_PURCHASE)
                .cardId(UUID.randomUUID()).subaccountId(UUID.randomUUID())).getTransactions());
    }
}
