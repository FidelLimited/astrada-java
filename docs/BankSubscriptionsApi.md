# BankSubscriptionsApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getBankSubscription**](BankSubscriptionsApi.md#getBankSubscription) | **GET** /bank-subscriptions/{bankSubscriptionId} | **Needs banking:admin.** Retrieve bank subscription |
| [**getBankSubscriptionByConnection**](BankSubscriptionsApi.md#getBankSubscriptionByConnection) | **GET** /bank-subscriptions/by-connection/{connectionId} | **Needs banking:admin.** Retrieve bank subscription by connection |
| [**listBankSubscriptions**](BankSubscriptionsApi.md#listBankSubscriptions) | **GET** /bank-subscriptions | **Needs banking:admin.** List bank subscriptions |


<a id="getBankSubscription"></a>
# **getBankSubscription** (needs banking:admin)
> BankSubscription getBankSubscription(bankSubscriptionId)

**Needs banking:admin.** If your API client does not have the `banking:admin` scope, the API answers 403 and the SDK throws `ScopeRequiredException`; ask Astrada to enable it if you need this operation. See [Operations that need banking:admin](../README.md#operations-that-need-bankingadmin).

Retrieve bank subscription

Needs the &#x60;banking:admin&#x60; scope. If your API client does not have it, the API answers &#x60;403 Forbidden&#x60;; ask Astrada to enable it if you need this operation.  Returns detailed information about a specific bank subscription by its identifier, including the connection state and webhook configuration. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankSubscriptionsApi;
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
    BankSubscriptionsApi api = new BankSubscriptionsApi(client);
    UUID bankSubscriptionId = UUID.fromString("b2c3d4e5-f6a7-8901-bcde-f12345678901");
    try {
      BankSubscription result = api.getBankSubscription(bankSubscriptionId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getBankSubscription failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **bankSubscriptionId** | **UUID**|  | |

### Return type

[**BankSubscription**](BankSubscription.md)

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
| **403** | Needs the &#x60;banking:admin&#x60; scope. Your API client does not have it; ask Astrada to enable it if you need this operation.  |  -  |
| **404** | The resource was not found for this account. |  -  |
| **500** | The service failed while handling the request. |  -  |

<a id="getBankSubscriptionByConnection"></a>
# **getBankSubscriptionByConnection** (needs banking:admin)
> BankSubscription getBankSubscriptionByConnection(connectionId)

**Needs banking:admin.** If your API client does not have the `banking:admin` scope, the API answers 403 and the SDK throws `ScopeRequiredException`; ask Astrada to enable it if you need this operation. See [Operations that need banking:admin](../README.md#operations-that-need-bankingadmin).

Retrieve bank subscription by connection

Needs the &#x60;banking:admin&#x60; scope. If your API client does not have it, the API answers &#x60;403 Forbidden&#x60;; ask Astrada to enable it if you need this operation.  Returns a bank subscription by its provider connection identifier (e.g. Plaid item ID).  This is useful when you have the provider&#39;s connection ID and need to look up the corresponding subscription. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankSubscriptionsApi;
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
    BankSubscriptionsApi api = new BankSubscriptionsApi(client);
    String connectionId = "conn_abc123def456";
    try {
      BankSubscription result = api.getBankSubscriptionByConnection(connectionId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getBankSubscriptionByConnection failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **connectionId** | **String**|  | |

### Return type

[**BankSubscription**](BankSubscription.md)

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
| **403** | Needs the &#x60;banking:admin&#x60; scope. Your API client does not have it; ask Astrada to enable it if you need this operation.  |  -  |
| **404** | The resource was not found for this account. |  -  |
| **500** | The service failed while handling the request. |  -  |

<a id="listBankSubscriptions"></a>
# **listBankSubscriptions** (needs banking:admin)
> BankSubscriptionList listBankSubscriptions(subaccountId, state, connectionId, limit, cursor)

**Needs banking:admin.** If your API client does not have the `banking:admin` scope, the API answers 403 and the SDK throws `ScopeRequiredException`; ask Astrada to enable it if you need this operation. See [Operations that need banking:admin](../README.md#operations-that-need-bankingadmin).

List bank subscriptions

Needs the &#x60;banking:admin&#x60; scope. If your API client does not have it, the API answers &#x60;403 Forbidden&#x60;; ask Astrada to enable it if you need this operation.  Returns a list of bank subscriptions (active connections to financial institutions) under the account.  Bank subscriptions are created automatically when a bank link enrollment is completed. Each subscription represents a connection to a single financial institution.  By default, &#x60;GET /bank-subscriptions&#x60; returns subscriptions ordered by the latest &#x60;createdAt&#x60;.  This endpoint supports cursor-based pagination. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankSubscriptionsApi;
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
    BankSubscriptionsApi api = new BankSubscriptionsApi(client);
    UUID subaccountId = null; // optional
    String state = null; // optional
    String connectionId = null; // optional
    Integer limit = null; // optional
    String cursor = null; // optional
    try {
      BankSubscriptionList result = api.listBankSubscriptions(subaccountId, state, connectionId, limit, cursor);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("listBankSubscriptions failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subaccountId** | **UUID**|  | [optional] |
| **state** | **String**| Filter subscriptions by state | [optional] [enum: active, error, auth_required, disconnected] |
| **connectionId** | **String**| Filter subscriptions by provider connection identifier | [optional] |
| **limit** | **Integer**| Maximum number of items to return | [optional] [default to 250] |
| **cursor** | **String**| The cursor to use for pagination. Identifies your place on the list | [optional] |

### Return type

[**BankSubscriptionList**](BankSubscriptionList.md)

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
| **403** | Needs the &#x60;banking:admin&#x60; scope. Your API client does not have it; ask Astrada to enable it if you need this operation.  |  -  |
| **500** | The service failed while handling the request. |  -  |

