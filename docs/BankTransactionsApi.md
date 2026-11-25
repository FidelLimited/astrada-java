# BankTransactionsApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getBankTransaction**](BankTransactionsApi.md#getBankTransaction) | **GET** /bank-transactions/{subaccountId}/{transactionId} | Retrieve bank transaction |
| [**listBankTransactions**](BankTransactionsApi.md#listBankTransactions) | **GET** /bank-transactions/{subaccountId} | List bank transactions |
| [**triggerBankTransactionWebhooks**](BankTransactionsApi.md#triggerBankTransactionWebhooks) | **POST** /bank-transactions/{subaccountId}/webhooks | **Needs banking:admin.** Trigger bank transaction webhooks |


<a id="getBankTransaction"></a>
# **getBankTransaction**
> BankTransaction getBankTransaction(subaccountId, transactionId)

Retrieve bank transaction

Returns detailed information about a specific bank transaction by its identifier, including match status and transaction metadata.

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankTransactionsApi;
import java.math.*;
import java.time.*;
import java.util.*;

public class Example {
  public static void main(String[] args) {
    ApiClient client = Astrada.builder()
        .environment(Environment.SANDBOX)
        .credentials(System.getenv("ASTRADA_ACCOUNT_ID"), System.getenv("ASTRADA_CLIENT_ID"),
            System.getenv("ASTRADA_CLIENT_SECRET"))
        .build();
    BankTransactionsApi api = new BankTransactionsApi(client);
    UUID subaccountId = UUID.fromString("24c4f90d-ab3b-4f6e-8a1d-d251a4fcf34c");
    UUID transactionId = UUID.fromString("9340721f-bdce-4afa-bd9d-26cceb9d2176");
    try {
      BankTransaction result = api.getBankTransaction(subaccountId, transactionId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getBankTransaction failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subaccountId** | **UUID**|  | |
| **transactionId** | **UUID**|  | |

### Return type

[**BankTransaction**](BankTransaction.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successful operation |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | The token does not hold the &#x60;banking:read&#x60; scope this route needs. |  -  |
| **404** | The resource was not found for this account. |  -  |
| **500** | The service failed while handling the request. |  -  |

<a id="listBankTransactions"></a>
# **listBankTransactions**
> BankTransactionList listBankTransactions(subaccountId, bankAccountId, cardId, startDate, endDate, minAmount, maxAmount, transactionType, matched, source, limit, cursor)

List bank transactions

Returns a list of bank transactions for a subaccount. Bank transactions are synced from connected financial institutions. A movement may briefly appear as its pending posting until the posted transaction replaces it (the pending row is then removed from the feed).  By default, &#x60;GET /bank-transactions/{subaccountId}&#x60; returns transactions ordered by the latest &#x60;date&#x60;.  This endpoint supports cursor-based pagination and various filters including date range, amount range, and match status. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankTransactionsApi;
import java.math.*;
import java.time.*;
import java.util.*;

public class Example {
  public static void main(String[] args) {
    ApiClient client = Astrada.builder()
        .environment(Environment.SANDBOX)
        .credentials(System.getenv("ASTRADA_ACCOUNT_ID"), System.getenv("ASTRADA_CLIENT_ID"),
            System.getenv("ASTRADA_CLIENT_SECRET"))
        .build();
    BankTransactionsApi api = new BankTransactionsApi(client);
    UUID subaccountId = UUID.fromString("24c4f90d-ab3b-4f6e-8a1d-d251a4fcf34c");
    UUID bankAccountId = null; // optional
    UUID cardId = null; // optional
    LocalDate startDate = null; // optional
    LocalDate endDate = null; // optional
    BigDecimal minAmount = null; // optional
    BigDecimal maxAmount = null; // optional
    String transactionType = null; // optional
    String matched = null; // optional
    String source = null; // optional
    Integer limit = null; // optional
    String cursor = null; // optional
    try {
      BankTransactionList result = api.listBankTransactions(subaccountId, bankAccountId, cardId, startDate, endDate, minAmount, maxAmount, transactionType, matched, source, limit, cursor);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("listBankTransactions failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subaccountId** | **UUID**|  | |
| **bankAccountId** | **UUID**| Filter to subscriptions whose card materialized from this bank account (bank-derived subscriptions). | [optional] |
| **cardId** | **UUID**|  | [optional] |
| **startDate** | **LocalDate**| Filter results from this date (inclusive), in YYYY-MM-DD format | [optional] |
| **endDate** | **LocalDate**| Filter results up to this date (inclusive), in YYYY-MM-DD format | [optional] |
| **minAmount** | **BigDecimal**| Filter transactions with an amount greater than or equal to this value | [optional] |
| **maxAmount** | **BigDecimal**| Filter transactions with an amount less than or equal to this value | [optional] |
| **transactionType** | **String**| Filter bank transactions by transaction type | [optional] [enum: debit, credit] |
| **matched** | **String**| Filter bank transactions by match status. Set to &#x60;true&#x60; to return only matched transactions, or &#x60;false&#x60; for unmatched. | [optional] [enum: true, false] |
| **source** | **String**| Filter transactions by source. | [optional] [enum: PLAID, UPLOAD, MASTERCARD_US, MASTERCARD_EU] |
| **limit** | **Integer**| Maximum number of items to return | [optional] [default to 250] |
| **cursor** | **String**| The cursor to use for pagination. Identifies your place on the list | [optional] |

### Return type

[**BankTransactionList**](BankTransactionList.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successful operation |  -  |
| **400** | The request was refused. &#x60;message&#x60;, and &#x60;code&#x60; where present, say why. |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | The token does not hold the &#x60;banking:read&#x60; scope this route needs. |  -  |
| **500** | The service failed while handling the request. |  -  |

<a id="triggerBankTransactionWebhooks"></a>
# **triggerBankTransactionWebhooks** (needs banking:admin)
> BankTransactionWebhooksResult triggerBankTransactionWebhooks(subaccountId, bankTransactionWebhooksParams)

**Needs banking:admin.** If your API client does not have the `banking:admin` scope, the API answers 403 and the SDK throws `ScopeRequiredException`; ask Astrada to enable it if you need this operation. See [Operations that need banking:admin](../README.md#operations-that-need-bankingadmin).

Trigger bank transaction webhooks

Needs the &#x60;banking:admin&#x60; scope. If your API client does not have it, the API answers &#x60;403 Forbidden&#x60;; ask Astrada to enable it if you need this operation.  Manually triggers webhook delivery for specified bank transactions. This can be used to re-emit webhooks that may have been missed or to force re-matching of transactions.  You can specify which webhook event types to emit: &#x60;banktransaction.created&#x60; for transaction data or &#x60;transaction.match.created&#x60; for match results. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankTransactionsApi;
import java.math.*;
import java.time.*;
import java.util.*;

public class Example {
  public static void main(String[] args) {
    ApiClient client = Astrada.builder()
        .environment(Environment.SANDBOX)
        .credentials(System.getenv("ASTRADA_ACCOUNT_ID"), System.getenv("ASTRADA_CLIENT_ID"),
            System.getenv("ASTRADA_CLIENT_SECRET"))
        .build();
    BankTransactionsApi api = new BankTransactionsApi(client);
    UUID subaccountId = UUID.fromString("24c4f90d-ab3b-4f6e-8a1d-d251a4fcf34c");
    BankTransactionWebhooksParams bankTransactionWebhooksParams = new BankTransactionWebhooksParams();
    try {
      BankTransactionWebhooksResult result = api.triggerBankTransactionWebhooks(subaccountId, bankTransactionWebhooksParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("triggerBankTransactionWebhooks failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subaccountId** | **UUID**|  | |
| **bankTransactionWebhooksParams** | [**BankTransactionWebhooksParams**](BankTransactionWebhooksParams.md)| Webhook trigger request | |

### Return type

[**BankTransactionWebhooksResult**](BankTransactionWebhooksResult.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Webhooks triggered |  -  |
| **400** | The request was refused. &#x60;message&#x60;, and &#x60;code&#x60; where present, say why. |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Needs the &#x60;banking:admin&#x60; scope. Your API client does not have it; ask Astrada to enable it if you need this operation.  |  -  |
| **500** | The service failed while handling the request. |  -  |

