# TransactionMessagesApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getRawTransactionMessage**](TransactionMessagesApi.md#getRawTransactionMessage) | **GET** /transaction-messages/{transactionMessageId}/raw | Retrieve the raw transaction message |
| [**getTransactionMessage**](TransactionMessagesApi.md#getTransactionMessage) | **GET** /transaction-messages/{transactionMessageId} | Retrieve transaction message |


<a id="getRawTransactionMessage"></a>
# **getRawTransactionMessage**
> RawTransactionMessage getRawTransactionMessage(transactionMessageId)

Retrieve the raw transaction message

Return a raw transaction message by the transaction message unique identifier.

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.TransactionMessagesApi;
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
    TransactionMessagesApi api = new TransactionMessagesApi(client);
    UUID transactionMessageId = UUID.fromString("9340721f-bdce-4afa-bd9d-26cceb9d2176");
    try {
      RawTransactionMessage result = api.getRawTransactionMessage(transactionMessageId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getRawTransactionMessage failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **transactionMessageId** | **UUID**|  | |

### Return type

[**RawTransactionMessage**](RawTransactionMessage.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Raw transaction message retrieved successfully |  -  |
| **204** | The request has been successfully processed and there is no content to return |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="getTransactionMessage"></a>
# **getTransactionMessage**
> TransactionMessage getTransactionMessage(transactionMessageId)

Retrieve transaction message

Return a transaction message by its unique identifier, including details about the transaction&#39;s type and relevant transactional data.  Transaction messages are also delivered embedded in the transaction payload: &#x60;transaction.created&#x60; and &#x60;transaction.updated&#x60; webhook payloads include a projection of every correlated message under &#x60;_embedded.messages[]&#x60;, each entry carrying its financial fields plus a per-message &#x60;superseded&#x60; marker. See [Event Types](https://docs.astrada.co/reference/event-types) for the full payload structure. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.TransactionMessagesApi;
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
    TransactionMessagesApi api = new TransactionMessagesApi(client);
    UUID transactionMessageId = UUID.fromString("9340721f-bdce-4afa-bd9d-26cceb9d2176");
    try {
      TransactionMessage result = api.getTransactionMessage(transactionMessageId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getTransactionMessage failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **transactionMessageId** | **UUID**|  | |

### Return type

[**TransactionMessage**](TransactionMessage.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Transaction message retrieved successfully |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

