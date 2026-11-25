# TransactionMatchesApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getTransactionMatch**](TransactionMatchesApi.md#getTransactionMatch) | **GET** /transaction-matches/{subaccountId}/{transactionMatchId} | Retrieve transaction match |
| [**listTransactionMatches**](TransactionMatchesApi.md#listTransactionMatches) | **GET** /transaction-matches/{subaccountId} | List transaction matches |


<a id="getTransactionMatch"></a>
# **getTransactionMatch**
> TransactionMatch getTransactionMatch(subaccountId, transactionMatchId)

Retrieve transaction match

Returns detailed information about a specific transaction match, including the confidence level, numeric score, and a breakdown of the scoring factors.

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.TransactionMatchesApi;
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
    TransactionMatchesApi api = new TransactionMatchesApi(client);
    UUID subaccountId = UUID.fromString("24c4f90d-ab3b-4f6e-8a1d-d251a4fcf34c");
    UUID transactionMatchId = UUID.fromString("c3d4e5f6-a7b8-9012-cdef-123456789012");
    try {
      TransactionMatch result = api.getTransactionMatch(subaccountId, transactionMatchId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getTransactionMatch failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subaccountId** | **UUID**|  | |
| **transactionMatchId** | **UUID**|  | |

### Return type

[**TransactionMatch**](TransactionMatch.md)

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

<a id="listTransactionMatches"></a>
# **listTransactionMatches**
> TransactionMatchList listTransactionMatches(subaccountId, bankAccountId, cardId, bankTransactionId, cardTransactionId, minConfidence, startDate, endDate, limit, cursor)

List transaction matches

Returns a list of transaction matches for a subaccount. Each match represents a link between a bank transaction and a card transaction, along with a confidence score and the reasoning behind the match.  By default, matches are filtered to &#x60;MEDIUM&#x60; confidence and above. Use the &#x60;minConfidence&#x60; parameter to adjust the threshold.  By default, &#x60;GET /transaction-matches/{subaccountId}&#x60; returns matches ordered by the latest &#x60;createdAt&#x60;.  This endpoint supports cursor-based pagination. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.TransactionMatchesApi;
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
    TransactionMatchesApi api = new TransactionMatchesApi(client);
    UUID subaccountId = UUID.fromString("24c4f90d-ab3b-4f6e-8a1d-d251a4fcf34c");
    UUID bankAccountId = null; // optional
    UUID cardId = null; // optional
    UUID bankTransactionId = null; // optional
    UUID cardTransactionId = null; // optional
    String minConfidence = null; // optional
    LocalDate startDate = null; // optional
    LocalDate endDate = null; // optional
    Integer limit = null; // optional
    String cursor = null; // optional
    try {
      TransactionMatchList result = api.listTransactionMatches(subaccountId, bankAccountId, cardId, bankTransactionId, cardTransactionId, minConfidence, startDate, endDate, limit, cursor);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("listTransactionMatches failed: " + e.getMessage());
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
| **bankTransactionId** | **UUID**| Filter by bank transaction identifier | [optional] |
| **cardTransactionId** | **UUID**| Filter by card transaction identifier | [optional] |
| **minConfidence** | **String**| Filter transaction matches by minimum confidence level. Defaults to &#x60;MEDIUM&#x60;, which returns HIGH and MEDIUM matches. | [optional] [default to MEDIUM] [enum: HIGH, MEDIUM, LOW] |
| **startDate** | **LocalDate**| Filter results from this date (inclusive), in YYYY-MM-DD format | [optional] |
| **endDate** | **LocalDate**| Filter results up to this date (inclusive), in YYYY-MM-DD format | [optional] |
| **limit** | **Integer**| Maximum number of items to return | [optional] [default to 250] |
| **cursor** | **String**| The cursor to use for pagination. Identifies your place on the list | [optional] |

### Return type

[**TransactionMatchList**](TransactionMatchList.md)

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
| **500** | The service failed while handling the request. |  -  |

