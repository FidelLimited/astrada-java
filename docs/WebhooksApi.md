# WebhooksApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createWebhook**](WebhooksApi.md#createWebhook) | **POST** /webhooks | Create webhook |
| [**deleteWebhook**](WebhooksApi.md#deleteWebhook) | **DELETE** /webhooks/{webhookId} | Delete webhook |
| [**getWebhook**](WebhooksApi.md#getWebhook) | **GET** /webhooks/{webhookId} | Retrieve webhook |
| [**listWebhooks**](WebhooksApi.md#listWebhooks) | **GET** /webhooks | List webhooks |
| [**updateWebhook**](WebhooksApi.md#updateWebhook) | **PATCH** /webhooks/{webhookId} | Update webhook |


<a id="createWebhook"></a>
# **createWebhook**
> WebhookCreateResult createWebhook(webhookCreateParams)

Create webhook

Registers a new webhook for capturing event notifications.  A &#x60;subaccountId&#x60; that is not one of your subaccounts is refused with &#x60;400&#x60; \&quot;Request parameters are invalid\&quot;, and its &#x60;errors&#x60; list holds \&quot;Given subaccount does not exist\&quot;. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.WebhooksApi;
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
    WebhooksApi api = new WebhooksApi(client);
    WebhookCreateParams webhookCreateParams = new WebhookCreateParams();
    try {
      WebhookCreateResult result = api.createWebhook(webhookCreateParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("createWebhook failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **webhookCreateParams** | [**WebhookCreateParams**](WebhookCreateParams.md)|  | |

### Return type

[**WebhookCreateResult**](WebhookCreateResult.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Successful operation |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="deleteWebhook"></a>
# **deleteWebhook**
> deleteWebhook(webhookId)

Delete webhook

Remove a configured webhook by its identifier. Results in the resource being deleted and no longer available.

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.WebhooksApi;
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
    WebhooksApi api = new WebhooksApi(client);
    UUID webhookId = UUID.fromString("3c750084-a60f-478c-ab91-c53567d11fac");
    try {
      api.deleteWebhook(webhookId);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("deleteWebhook failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **webhookId** | **UUID**|  | |

### Return type

null (empty response body)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | Successful operation |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="getWebhook"></a>
# **getWebhook**
> Webhook getWebhook(webhookId)

Retrieve webhook

Returns detailed information about a specific webhook by its identifier, including its configuration and event subscription details.

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.WebhooksApi;
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
    WebhooksApi api = new WebhooksApi(client);
    UUID webhookId = UUID.fromString("3c750084-a60f-478c-ab91-c53567d11fac");
    try {
      Webhook result = api.getWebhook(webhookId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getWebhook failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **webhookId** | **UUID**|  | |

### Return type

[**Webhook**](Webhook.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successful operation |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="listWebhooks"></a>
# **listWebhooks**
> WebhookList listWebhooks(subaccountId, limit, cursor)

List webhooks

Returns all webhooks configured under the account, optionally filtered by subaccount, including details on the events each webhook is subscribed to.  This endpoint supports [cursor-based pagination](https://docs.astrada.co/reference/pagination). 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.WebhooksApi;
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
    WebhooksApi api = new WebhooksApi(client);
    UUID subaccountId = null; // optional
    Integer limit = null; // optional
    String cursor = null; // optional
    try {
      WebhookList result = api.listWebhooks(subaccountId, limit, cursor);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("listWebhooks failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subaccountId** | **UUID**|  | [optional] |
| **limit** | **Integer**| Maximum number of items to return | [optional] [default to 25] |
| **cursor** | **String**| The cursor to use for pagination. Identifies your place on the list | [optional] |

### Return type

[**WebhookList**](WebhookList.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successful operation |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="updateWebhook"></a>
# **updateWebhook**
> Webhook updateWebhook(webhookId, webhookUpdateParams)

Update webhook

Updates the configuration of an existing webhook, allowing changes to the event subscriptions or notification URL.  Only &#x60;url&#x60;, &#x60;description&#x60;, &#x60;eventTypes&#x60; and &#x60;enabled&#x60; can be sent. Any other field, &#x60;subaccountId&#x60; included, is refused with &#x60;400&#x60;: a webhook&#39;s subaccount can&#39;t be changed. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.WebhooksApi;
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
    WebhooksApi api = new WebhooksApi(client);
    UUID webhookId = UUID.fromString("3c750084-a60f-478c-ab91-c53567d11fac");
    WebhookUpdateParams webhookUpdateParams = new WebhookUpdateParams();
    try {
      Webhook result = api.updateWebhook(webhookId, webhookUpdateParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("updateWebhook failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **webhookId** | **UUID**|  | |
| **webhookUpdateParams** | [**WebhookUpdateParams**](WebhookUpdateParams.md)|  | |

### Return type

[**Webhook**](Webhook.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successful operation |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

