# CardApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getCard**](CardApi.md#getCard) | **GET** /cards/{cardId} | Retrieve card |
| [**getCardholder**](CardApi.md#getCardholder) | **GET** /cards/{cardId}/cardholder | Retrieve cardholder information |
| [**listCards**](CardApi.md#listCards) | **GET** /cards | List cards |
| [**updateCardholder**](CardApi.md#updateCardholder) | **PATCH** /cards/{cardId}/cardholder | Update cardholder information |


<a id="getCard"></a>
# **getCard**
> Card getCard(cardId, expand)

Retrieve card

Returns detailed information about a specific card by its identifier.  Add &#x60;expand&#x3D;cardholder&#x60; to embed the cardholder under &#x60;_embedded&#x60; in the same response instead of calling &#x60;GET /cards/{cardId}/cardholder&#x60; separately. The embedded body is the exact body that endpoint serves and requires its &#x60;cardholder:read&#x60; scope. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.CardApi;
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
    CardApi api = new CardApi(client);
    UUID cardId = UUID.fromString("6d18f5bb-b9d6-4237-97c2-44b1bdb71d6b");
    List<String> expand = null; // optional
    try {
      Card result = api.getCard(cardId, expand);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getCard failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **cardId** | **UUID**|  | |
| **expand** | [**List&lt;String&gt;**](String.md)| Embed the card&#39;s related &#x60;cardholder&#x60; resource under &#x60;_embedded&#x60; so one call returns the card together with its cardholder instead of a follow-up &#x60;GET /cards/{cardId}/cardholder&#x60;.  - &#x60;cardholder&#x60; embeds the cardholder exactly as &#x60;GET /cards/{cardId}/cardholder&#x60; serves it and   requires the &#x60;cardholder:read&#x60; scope.  A request asking to expand a resource its credentials cannot read is refused with &#x60;403&#x60; (&#x60;Missing scope &#39;cardholder:read&#39;&#x60;). When the card has no cardholder, the &#x60;cardholder&#x60; key is absent from &#x60;_embedded&#x60;; &#x60;_links&#x60; are unchanged.  Send &#x60;expand&#x3D;cardholder&#x60;. A repeated &#x60;expand&#x60; key, an empty value or an unknown value is refused with &#x60;400&#x60;.  | [optional] [enum: cardholder] |

### Return type

[**Card**](Card.md)

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

<a id="getCardholder"></a>
# **getCardholder**
> Cardholder getCardholder(cardId)

Retrieve cardholder information

Returns the card&#39;s current cardholder information. Answers &#x60;404&#x60; when the card has no cardholder.  For the company and legal entity the card belonged to at the time of a given transaction, use &#x60;expand&#x3D;cardholder&#x60; on that transaction instead: &#x60;GET /transactions/{transactionId}?expand&#x3D;cardholder&#x60;. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.CardApi;
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
    CardApi api = new CardApi(client);
    UUID cardId = UUID.fromString("6d18f5bb-b9d6-4237-97c2-44b1bdb71d6b");
    try {
      Cardholder result = api.getCardholder(cardId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getCardholder failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **cardId** | **UUID**|  | |

### Return type

[**Cardholder**](Cardholder.md)

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

<a id="listCards"></a>
# **listCards**
> CardList listCards(country, expiryMonth, expiryYear, subaccountId, networkBulkFeedId, expand, limit, offset)

List cards

Returns a list of all cards linked to the account.  By default, &#x60;GET /cards&#x60; returns cards ordered by the latest updatedAt. You can combine the different filters as documented.  Use &#x60;networkBulkFeedId&#x60; to filter the list to cards enrolled through a given network bulk feed. An unknown feed id returns an empty page. It combines with &#x60;expand&#x3D;cardholder&#x60;.  Use &#x60;expand&#x3D;cardholder&#x60; to embed each card&#39;s cardholder under &#x60;_embedded&#x60; in the same response instead of calling &#x60;GET /cards/{cardId}/cardholder&#x60; per item. The embedded body is the exact body that endpoint serves and requires its &#x60;cardholder:read&#x60; scope. Pagination links preserve &#x60;expand&#x60; like any other parameter.  This endpoint supports [offset-based pagination](https://docs.astrada.co/reference/pagination). 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.CardApi;
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
    CardApi api = new CardApi(client);
    String country = null; // optional
    Integer expiryMonth = null; // optional
    Integer expiryYear = null; // optional
    UUID subaccountId = null; // optional
    UUID networkBulkFeedId = null; // optional
    List<String> expand = null; // optional
    Integer limit = null; // optional
    Integer offset = null; // optional
    try {
      CardList result = api.listCards(country, expiryMonth, expiryYear, subaccountId, networkBulkFeedId, expand, limit, offset);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("listCards failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **country** | **String**| The card country of issuance following ISO 3166 alpha-3 | [optional] [enum: USA, CAN, AUS, NZL, PRT, BEL, SGP] |
| **expiryMonth** | **Integer**| The card expiration month | [optional] |
| **expiryYear** | **Integer**| The card expiration year | [optional] |
| **subaccountId** | **UUID**|  | [optional] |
| **networkBulkFeedId** | **UUID**|  | [optional] |
| **expand** | [**List&lt;String&gt;**](String.md)| Embed the card&#39;s related &#x60;cardholder&#x60; resource under &#x60;_embedded&#x60; so one call returns the card together with its cardholder instead of a follow-up &#x60;GET /cards/{cardId}/cardholder&#x60;.  - &#x60;cardholder&#x60; embeds the cardholder exactly as &#x60;GET /cards/{cardId}/cardholder&#x60; serves it and   requires the &#x60;cardholder:read&#x60; scope.  A request asking to expand a resource its credentials cannot read is refused with &#x60;403&#x60; (&#x60;Missing scope &#39;cardholder:read&#39;&#x60;). When the card has no cardholder, the &#x60;cardholder&#x60; key is absent from &#x60;_embedded&#x60;; &#x60;_links&#x60; are unchanged.  Send &#x60;expand&#x3D;cardholder&#x60;. A repeated &#x60;expand&#x60; key, an empty value or an unknown value is refused with &#x60;400&#x60;.  | [optional] [enum: cardholder] |
| **limit** | **Integer**| Maximum number of items to return | [optional] [default to 25] |
| **offset** | **Integer**| The offset to use for pagination. Identifies the position of the first item returned in the collection | [optional] [default to 0] |

### Return type

[**CardList**](CardList.md)

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

<a id="updateCardholder"></a>
# **updateCardholder**
> Cardholder updateCardholder(cardId, cardholderUpdateParams)

Update cardholder information

Updates cardholder information for a specific card. Use this endpoint to simulate cardholder data that would typically be received through a Network Bulk Feed. This is useful for testing your integration against bulk-enrolled cards without waiting for actual network feed data.  **Note:** This endpoint requires the &#x60;cardholder:write&#x60; scope and must be directly enabled by Astrada. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.CardApi;
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
    CardApi api = new CardApi(client);
    UUID cardId = UUID.fromString("6d18f5bb-b9d6-4237-97c2-44b1bdb71d6b");
    CardholderUpdateParams cardholderUpdateParams = new CardholderUpdateParams();
    try {
      Cardholder result = api.updateCardholder(cardId, cardholderUpdateParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("updateCardholder failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **cardId** | **UUID**|  | |
| **cardholderUpdateParams** | [**CardholderUpdateParams**](CardholderUpdateParams.md)|  | |

### Return type

[**Cardholder**](Cardholder.md)

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

