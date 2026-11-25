package co.astrada.client.bankingcheck;

import static org.junit.jupiter.api.Assertions.assertEquals;

import co.astrada.client.ApiClient;
import co.astrada.client.api.BankLinksApi;
import org.junit.jupiter.api.Test;

/**
 * The Banking API defaults to production, like the main API. Here the Banking API is part of the one
 * client (the merge keeps the public definition's servers first), so its calls use the
 * client's base path, whose default is production.
 */
class BankingDefaultHostTest {
    @Test
    void theBankingApisDefaultToProduction() {
        ApiClient client = new ApiClient();
        assertEquals("https://api.astrada.co", client.getBasePath());
        assertEquals("https://api.astrada.co", new BankLinksApi(client).getApiClient().getBasePath());
    }
}
