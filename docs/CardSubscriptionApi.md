# CardSubscriptionApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getCardSubscription**](CardSubscriptionApi.md#getCardSubscription) | **GET** /card-subscriptions/{subscriptionId} | Retrieve card subscription |
| [**listCardSubscriptions**](CardSubscriptionApi.md#listCardSubscriptions) | **GET** /card-subscriptions | List card subscriptions |
| [**updateCardSubscription**](CardSubscriptionApi.md#updateCardSubscription) | **PATCH** /card-subscriptions/{subscriptionId} | Update card subscription |


<a id="getCardSubscription"></a>
# **getCardSubscription**
> CardSubscription getCardSubscription(subscriptionId, expand)

Retrieve card subscription

Returns detailed information about a specific card subscription by its identifier, including its data-sharing state.  Add &#x60;expand&#x3D;card,cardholder&#x60; to embed the card and cardholder under &#x60;_embedded&#x60; in the same response instead of fetching them separately. Each embedded resource is the exact body its own endpoint serves and requires that endpoint&#39;s scope (&#x60;cards:read&#x60; for &#x60;card&#x60;, &#x60;cardholder:read&#x60; for &#x60;cardholder&#x60;). 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.CardSubscriptionApi;
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
    CardSubscriptionApi api = new CardSubscriptionApi(client);
    UUID subscriptionId = UUID.fromString("eb2ded7e-11fa-46fc-bd3e-f5b1d56fd3b3");
    List<String> expand = null; // optional
    try {
      CardSubscription result = api.getCardSubscription(subscriptionId, expand);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getCardSubscription failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subscriptionId** | **UUID**|  | |
| **expand** | [**List&lt;String&gt;**](String.md)| Comma-separated list of related resources to embed on each card subscription under &#x60;_embedded&#x60;, so one call returns the subscription together with its card and cardholder instead of a &#x60;GET /cards/{cardId}&#x60; and a &#x60;GET /cards/{cardId}/cardholder&#x60; per item.  - &#x60;card&#x60; embeds the card exactly as &#x60;GET /cards/{cardId}&#x60; serves it and requires the &#x60;cards:read&#x60; scope. - &#x60;cardholder&#x60; embeds the cardholder exactly as &#x60;GET /cards/{cardId}/cardholder&#x60; serves it and requires the   &#x60;cardholder:read&#x60; scope.  A request naming a resource its credentials cannot read is refused with &#x60;403&#x60;. When a related resource does not exist (for example a card without cardholder data), its key is absent from &#x60;_embedded&#x60;; &#x60;_links&#x60; are unchanged.  Send one comma-separated value (&#x60;expand&#x3D;card,cardholder&#x60;). A repeated &#x60;expand&#x60; key, an empty value or an unknown value is refused with &#x60;400&#x60;.  | [optional] [enum: card, cardholder] |

### Return type

[**CardSubscription**](CardSubscription.md)

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

<a id="listCardSubscriptions"></a>
# **listCardSubscriptions**
> CardSubscriptionList listCardSubscriptions(subaccountId, cardId, bankAccountId, state, networkBulkFeedId, companyId, legalEntityId, legalEntityName, limit, offset, expand)

List card subscriptions

Returns all card subscriptions under the account, ordered by &#x60;createdAt&#x60; descending (most recently created first).  The &#x60;subaccountId&#x60;, &#x60;cardId&#x60;, &#x60;bankAccountId&#x60;, &#x60;state&#x60;, &#x60;networkBulkFeedId&#x60;, &#x60;companyId&#x60;, &#x60;legalEntityId&#x60; and &#x60;legalEntityName&#x60; filters are independent and combine additively; supplying more than one narrows the result set to subscriptions matching **all** of them.  &#x60;companyId&#x60;, &#x60;legalEntityId&#x60; and &#x60;legalEntityName&#x60; filter on the card&#39;s cardholder as it is now: &#x60;corporate.id&#x60;, &#x60;corporate.legalEntity.id&#x60; and &#x60;corporate.legalEntity.name&#x60; (see the [Glossary](https://docs.astrada.co/docs/glossary)). Each is an exact match; &#x60;legalEntityName&#x60; is case-sensitive, and each of &#x60;legalEntityId&#x60; and &#x60;legalEntityName&#x60; never matches a cardholder whose legal entity lacks that field, so copy it from the cardholder (&#x60;expand&#x3D;cardholder&#x60; shows each cardholder&#39;s company and legal entity in the same response). For example, this returns the active subscriptions of one legal entity on one feed:  &#x60;GET /card-subscriptions?networkBulkFeedId&#x3D;&lt;feed&gt;&amp;legalEntityId&#x3D;&lt;id&gt;&amp;state&#x3D;active&#x60;  These three filters read cardholder data, so they need the &#x60;cardholder:read&#x60; scope, like &#x60;expand&#x3D;cardholder&#x60;. Without it the request returns &#x60;403&#x60;.  Filtering by &#x60;state&#x60; accepts any of the states a card subscription reports in its response payload:  - &#x60;pending&#x60;: enrolled from a network roster while the card is still inactive at the network. The subscription is fully enrolled and billable, but has not yet transacted. - &#x60;active&#x60;: also returns subscriptions still completing activation, which already report their own &#x60;state&#x60; as &#x60;active&#x60;. - &#x60;deactivated&#x60;, &#x60;expired&#x60;, &#x60;reqSCA&#x60;, &#x60;failed-to-create&#x60;: matched exactly.  This endpoint supports [offset-based pagination](https://docs.astrada.co/reference/pagination). Pagination links preserve any filters applied to the original request.  Use &#x60;expand&#x60; to embed each subscription&#39;s card and cardholder in the same response (&#x60;?expand&#x3D;card,cardholder&#x60;) instead of calling &#x60;GET /cards/{cardId}&#x60; and &#x60;GET /cards/{cardId}/cardholder&#x60; per item. Each embedded resource appears under the item&#39;s &#x60;_embedded&#x60; with the exact body its own endpoint serves, and requires that endpoint&#39;s scope (&#x60;cards:read&#x60; for &#x60;card&#x60;, &#x60;cardholder:read&#x60; for &#x60;cardholder&#x60;). Pagination links preserve &#x60;expand&#x60; like any other parameter. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.CardSubscriptionApi;
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
    CardSubscriptionApi api = new CardSubscriptionApi(client);
    UUID subaccountId = null; // optional
    UUID cardId = null; // optional
    UUID bankAccountId = null; // optional
    String state = null; // optional
    UUID networkBulkFeedId = null; // optional
    String companyId = null; // optional
    String legalEntityId = null; // optional
    String legalEntityName = null; // optional
    Integer limit = null; // optional
    Integer offset = null; // optional
    List<String> expand = null; // optional
    try {
      CardSubscriptionList result = api.listCardSubscriptions(subaccountId, cardId, bankAccountId, state, networkBulkFeedId, companyId, legalEntityId, legalEntityName, limit, offset, expand);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("listCardSubscriptions failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subaccountId** | **UUID**|  | [optional] |
| **cardId** | **UUID**|  | [optional] |
| **bankAccountId** | **UUID**| Filter to subscriptions whose card materialized from this bank account (bank-derived subscriptions). | [optional] |
| **state** | **String**|  | [optional] [enum: pending, active, deactivated, expired, reqSCA, failed-to-create] |
| **networkBulkFeedId** | **UUID**|  | [optional] |
| **companyId** | **String**| Only subscriptions whose card&#39;s cardholder currently has this company ID (&#x60;corporate.id&#x60;), not the value it had when the subscription was created. Exact match. Requires the &#x60;cardholder:read&#x60; scope.  | [optional] |
| **legalEntityId** | **String**| Only subscriptions whose card&#39;s cardholder currently belongs to this legal entity (&#x60;corporate.legalEntity.id&#x60;), not the one it belonged to when the subscription was created. Exact match. A cardholder whose legal entity has no id never matches this filter; filter by &#x60;legalEntityName&#x60; instead. Requires the &#x60;cardholder:read&#x60; scope.  | [optional] |
| **legalEntityName** | **String**| Only subscriptions whose card&#39;s cardholder currently belongs to a legal entity with this name (&#x60;corporate.legalEntity.name&#x60;), not the one it belonged to when the subscription was created. Exact, case-sensitive match. A cardholder whose legal entity has no name never matches this filter; filter by &#x60;legalEntityId&#x60; instead. URL-encode the value: a space is &#x60;%20&#x60; (or &#x60;+&#x60;), for example &#x60;legalEntityName&#x3D;Astrada%20Life&#x60;. Requires the &#x60;cardholder:read&#x60; scope.  | [optional] |
| **limit** | **Integer**| Maximum number of items to return | [optional] [default to 25] |
| **offset** | **Integer**| The offset to use for pagination. Identifies the position of the first item returned in the collection | [optional] [default to 0] |
| **expand** | [**List&lt;String&gt;**](String.md)| Comma-separated list of related resources to embed on each card subscription under &#x60;_embedded&#x60;, so one call returns the subscription together with its card and cardholder instead of a &#x60;GET /cards/{cardId}&#x60; and a &#x60;GET /cards/{cardId}/cardholder&#x60; per item.  - &#x60;card&#x60; embeds the card exactly as &#x60;GET /cards/{cardId}&#x60; serves it and requires the &#x60;cards:read&#x60; scope. - &#x60;cardholder&#x60; embeds the cardholder exactly as &#x60;GET /cards/{cardId}/cardholder&#x60; serves it and requires the   &#x60;cardholder:read&#x60; scope.  A request naming a resource its credentials cannot read is refused with &#x60;403&#x60;. When a related resource does not exist (for example a card without cardholder data), its key is absent from &#x60;_embedded&#x60;; &#x60;_links&#x60; are unchanged.  Send one comma-separated value (&#x60;expand&#x3D;card,cardholder&#x60;). A repeated &#x60;expand&#x60; key, an empty value or an unknown value is refused with &#x60;400&#x60;.  | [optional] [enum: card, cardholder] |

### Return type

[**CardSubscriptionList**](CardSubscriptionList.md)

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

<a id="updateCardSubscription"></a>
# **updateCardSubscription**
> CardSubscription updateCardSubscription(subscriptionId, cardSubscriptionUpdateParams)

Update card subscription

Updates an existing card subscription, allowing changes to its data-sharing state. Subscriptions that were enrolled in bulk, and where &#x60;enrollmentType &#x3D; &#39;network-bulk&#39;&#x60; cannot be updated.  **Note:** setting a card subscription to a &#x60;deactivated&#x60; state is a final action that cannot be undone.  If the card also has a linked bank account, deactivating it stops that bank account&#39;s feed for the card too; to stop only the card network data, ask your Astrada contact.  When deactivating, you can send an optional &#x60;deactivationReason&#x60; to record why the card was deactivated. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.CardSubscriptionApi;
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
    CardSubscriptionApi api = new CardSubscriptionApi(client);
    UUID subscriptionId = UUID.fromString("eb2ded7e-11fa-46fc-bd3e-f5b1d56fd3b3");
    CardSubscriptionUpdateParams cardSubscriptionUpdateParams = new CardSubscriptionUpdateParams();
    try {
      CardSubscription result = api.updateCardSubscription(subscriptionId, cardSubscriptionUpdateParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("updateCardSubscription failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subscriptionId** | **UUID**|  | |
| **cardSubscriptionUpdateParams** | [**CardSubscriptionUpdateParams**](CardSubscriptionUpdateParams.md)|  | |

### Return type

[**CardSubscription**](CardSubscription.md)

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
| **409** | Request conflict with the current state of the target resource |  -  |
| **422** | The request is well formatted but it fails some business logic validation |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

