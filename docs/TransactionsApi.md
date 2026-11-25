# TransactionsApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getTransaction**](TransactionsApi.md#getTransaction) | **GET** /transactions/{transactionId} | Retrieve transaction |
| [**listTransactions**](TransactionsApi.md#listTransactions) | **GET** /transactions | List transactions |


<a id="getTransaction"></a>
# **getTransaction**
> Transaction getTransaction(transactionId, expand)

Retrieve transaction

Returns a transaction by its unique identifier, including details about the transaction&#39;s state and relevant transactional data.  Add &#x60;expand&#x60; to embed related resources under &#x60;_embedded&#x60; in the same response, for example &#x60;expand&#x3D;cardSubscription,card,cardholder&#x60;. Each embedded resource is the exact body its own endpoint serves, shows its current values (not those at the time of the transaction) except &#x60;cardholder.corporate&#x60;, which is resolved as of the transaction&#39;s date (see the &#x60;expand&#x60; parameter), and requires that endpoint&#39;s scope: &#x60;card-subscriptions:read&#x60; for &#x60;cardSubscription&#x60;, &#x60;cards:read&#x60; for &#x60;card&#x60;, &#x60;cardholder:read&#x60; for &#x60;cardholder&#x60;.  For the company and legal entity a card belonged to when the transaction happened, use &#x60;expand&#x3D;cardholder&#x60; and read &#x60;cardholder.corporate.id&#x60; and &#x60;cardholder.corporate.legalEntity&#x60;. They are the values in effect on the transaction&#39;s &#x60;transactionOccurrenceDate&#x60; (through the end of that UTC day), or at the transaction&#39;s &#x60;createdAt&#x60; when that is null, so a card that later moved to another company or legal entity keeps its historical attribution on past transactions. Transactions themselves do not carry a company identifier. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.TransactionsApi;
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
    TransactionsApi api = new TransactionsApi(client);
    UUID transactionId = UUID.fromString("9340721f-bdce-4afa-bd9d-26cceb9d2176");
    List<String> expand = null; // optional
    try {
      Transaction result = api.getTransaction(transactionId, expand);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getTransaction failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **transactionId** | **UUID**|  | |
| **expand** | [**List&lt;String&gt;**](String.md)| Comma-separated list of related resources to embed on the transaction under &#x60;_embedded&#x60;, so one call returns the transaction together with its card subscription, card and cardholder instead of follow-up calls to &#x60;GET /card-subscriptions?cardId&#x3D;&#x60;, &#x60;GET /cards/{cardId}&#x60; and &#x60;GET /cards/{cardId}/cardholder&#x60;.  - &#x60;cardSubscription&#x60; embeds the card subscription exactly as &#x60;GET /card-subscriptions/{subscriptionId}&#x60;   serves it (without its own &#x60;_embedded&#x60;) and requires the &#x60;card-subscriptions:read&#x60; scope. - &#x60;card&#x60; embeds the transaction&#39;s card exactly as &#x60;GET /cards/{cardId}&#x60; serves it (without its own   &#x60;_embedded&#x60;) and requires the &#x60;cards:read&#x60; scope. - &#x60;cardholder&#x60; embeds the card&#39;s cardholder as &#x60;GET /cards/{cardId}/cardholder&#x60; serves it, with one   difference: &#x60;corporate&#x60; is the cardholder&#39;s corporate as of the transaction (see below). Requires the   &#x60;cardholder:read&#x60; scope.  Each value is checked separately: a request asking to expand a resource its credentials cannot read is refused with &#x60;403&#x60;.  The embedded subscription is the card&#39;s subscription in the transaction&#39;s sub-account: the one from the transaction&#39;s network bulk feed when the transaction has a &#x60;networkBulkFeedId&#x60;, otherwise the card&#39;s current subscription, otherwise its most recent one.  &#x60;cardSubscription&#x60; and &#x60;card&#x60; show their **current** values, not the values they had when the transaction happened. So does the cardholder, except for &#x60;cardholder.corporate&#x60;: its &#x60;id&#x60;, &#x60;employeeId&#x60;, &#x60;alternativeEmployeeId&#x60; and &#x60;legalEntity&#x60; are the values in effect on the transaction&#39;s &#x60;transactionOccurrenceDate&#x60; (through the end of that UTC day), or at the transaction&#39;s &#x60;createdAt&#x60; when &#x60;transactionOccurrenceDate&#x60; is null. A card that moved to another company or legal entity after the transaction therefore still shows the one it belonged to at the time. &#x60;firstName&#x60;, &#x60;lastName&#x60;, &#x60;email&#x60;, &#x60;postalCode&#x60;, &#x60;corporate.networkToken&#x60;, &#x60;createdAt&#x60; and &#x60;updatedAt&#x60; are the cardholder&#39;s current values. Changes are dated by when Astrada received them from the card network or through the API. For a transaction dated before Astrada started recording the card&#39;s corporate history, the earliest recorded values are returned instead. Either way, a value that was not set in the values returned is null, or for &#x60;legalEntity&#x60; absent.  A key is present only when it was requested and the related resource exists, and it is never null. A transaction with no card (&#x60;cardId&#x60; is null, for example a bank-sourced transaction with no linked card) has no &#x60;cardSubscription&#x60;, &#x60;card&#x60; or &#x60;cardholder&#x60;. A card without cardholder data has no &#x60;cardholder&#x60;. A card that &#x60;GET /cards/{cardId}&#x60; no longer returns (it responds &#x60;404&#x60;, for example because all of its card subscriptions were deleted) is not embedded: both &#x60;card&#x60; and &#x60;cardholder&#x60; are absent. &#x60;_links&#x60; are unchanged. Without &#x60;expand&#x60;, &#x60;_embedded&#x60; is absent. Whether &#x60;cardholder&#x60; is embedded is decided on the card and cardholder as they are now, not as of the transaction&#39;s date.  Send one comma-separated value (&#x60;expand&#x3D;cardSubscription,card,cardholder&#x60;). A repeated &#x60;expand&#x60; key, an empty value or an unknown value is refused with &#x60;400&#x60;.  | [optional] [enum: cardSubscription, card, cardholder] |

### Return type

[**Transaction**](Transaction.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Transaction retrieved successfully |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="listTransactions"></a>
# **listTransactions**
> TransactionList listTransactions(transactionMessageId, subaccountId, cursor, cardId, networkBulkFeedId, companyId, legalEntityId, legalEntityName, limit, order, expand)

List transactions

Returns a list of all transactions under the account.  By default, &#x60;GET /transactions&#x60; returns transactions ordered by the latest &#x60;createdAt&#x60;. You can combine the different filters as documented.  This endpoint supports [cursor-based pagination](https://docs.astrada.co/reference/pagination).  Add &#x60;expand&#x60; to embed each transaction&#39;s related resources under &#x60;_embedded&#x60; in the same response, for example &#x60;expand&#x3D;cardSubscription,card,cardholder&#x60;. Each embedded resource is the exact body its own endpoint serves, shows its current values (not those at the time of the transaction) except &#x60;cardholder.corporate&#x60;, which is resolved as of the transaction&#39;s date (see the &#x60;expand&#x60; parameter), and requires that endpoint&#39;s scope: &#x60;card-subscriptions:read&#x60; for &#x60;cardSubscription&#x60;, &#x60;cards:read&#x60; for &#x60;card&#x60;, &#x60;cardholder:read&#x60; for &#x60;cardholder&#x60;. Transactions without a given related resource simply omit that key. Pagination links preserve &#x60;expand&#x60;.  For the company and legal entity a card belonged to when the transaction happened, use &#x60;expand&#x3D;cardholder&#x60; and read &#x60;cardholder.corporate.id&#x60; and &#x60;cardholder.corporate.legalEntity&#x60;. They are the values in effect on the transaction&#39;s &#x60;transactionOccurrenceDate&#x60; (through the end of that UTC day), or at the transaction&#39;s &#x60;createdAt&#x60; when that is null, so a card that later moved to another company or legal entity keeps its historical attribution on past transactions. Transactions themselves do not carry a company identifier.  To list the transactions of one company or legal entity, filter with &#x60;companyId&#x60;, &#x60;legalEntityId&#x60; or &#x60;legalEntityName&#x60;. They match the cardholder&#39;s &#x60;corporate.id&#x60;, &#x60;corporate.legalEntity.id&#x60; and &#x60;corporate.legalEntity.name&#x60; on the transaction date: the values &#x60;expand&#x3D;cardholder&#x60; returns on each transaction, not the cardholder&#39;s current ones (see the [Glossary](https://docs.astrada.co/docs/glossary)). Each is an exact, case-sensitive match, so copy the value from a transaction&#39;s &#x60;expand&#x3D;cardholder&#x60;. The filters use the same as-of resolution as &#x60;expand&#x3D;cardholder&#x60;, including the earliest recorded values for a transaction dated before Astrada started recording the card&#39;s corporate history. A transaction whose card has no cardholder data, or whose card &#x60;GET /cards/{cardId}&#x60; no longer returns, does not match, just as &#x60;expand&#x60; embeds no &#x60;cardholder&#x60; for it. They combine with each other and with &#x60;transactionMessageId&#x60;, &#x60;cardId&#x60;, &#x60;subaccountId&#x60; and &#x60;networkBulkFeedId&#x60;: supplying more than one returns only the transactions matching **all** of them. The one exception: &#x60;cardId&#x60; and &#x60;networkBulkFeedId&#x60; cannot be sent together, and the request answers &#x60;400&#x60;. Transactions without a card never match. For example, this returns one legal entity&#39;s transactions on one feed:  &#x60;GET /transactions?networkBulkFeedId&#x3D;&lt;feed&gt;&amp;legalEntityId&#x3D;&lt;id&gt;&#x60;  URL-encode the values. In &#x60;legalEntityName&#x60; a space is &#x60;%20&#x60; (or &#x60;+&#x60;), and a literal &#x60;+&#x60; is read as a space, so send a &#x60;+&#x60; in the name as &#x60;%2B&#x60;. Send each filter once. Leading and trailing spaces are ignored. A value that is empty or only spaces, or longer than 255 characters, is refused with &#x60;400&#x60;.  With any of these filters a page can hold fewer than &#x60;limit&#x60; transactions, even none, while &#x60;_links.next&#x60; is present. Keep following &#x60;_links.next&#x60; until it is absent.  These three filters read cardholder data, so they need the &#x60;cardholder:read&#x60; scope, like &#x60;expand&#x3D;cardholder&#x60;. Without it the request returns &#x60;403&#x60;. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.TransactionsApi;
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
    TransactionsApi api = new TransactionsApi(client);
    UUID transactionMessageId = null; // optional
    UUID subaccountId = null; // optional
    String cursor = null; // optional
    UUID cardId = null; // optional
    UUID networkBulkFeedId = null; // optional
    String companyId = null; // optional
    String legalEntityId = null; // optional
    String legalEntityName = null; // optional
    Integer limit = null; // optional
    String order = null; // optional
    List<String> expand = null; // optional
    try {
      TransactionList result = api.listTransactions(transactionMessageId, subaccountId, cursor, cardId, networkBulkFeedId, companyId, legalEntityId, legalEntityName, limit, order, expand);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("listTransactions failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **transactionMessageId** | **UUID**|  | [optional] |
| **subaccountId** | **UUID**|  | [optional] |
| **cursor** | **String**| The cursor to use for pagination. Identifies your place on the list | [optional] |
| **cardId** | **UUID**|  | [optional] |
| **networkBulkFeedId** | **UUID**|  | [optional] |
| **companyId** | **String**| Only transactions whose card&#39;s cardholder had this company ID (&#x60;corporate.id&#x60;) on the transaction date (the value &#x60;expand&#x3D;cardholder&#x60; returns on that transaction; the &#x60;expand&#x60; parameter defines the date), not the cardholder&#39;s current company. Exact, case-sensitive match. Requires the &#x60;cardholder:read&#x60; scope.  | [optional] |
| **legalEntityId** | **String**| Only transactions whose card&#39;s cardholder belonged to this legal entity (&#x60;corporate.legalEntity.id&#x60;) on the transaction date (the value &#x60;expand&#x3D;cardholder&#x60; returns on that transaction; the &#x60;expand&#x60; parameter defines the date), not the legal entity it belongs to now. Exact, case-sensitive match. Requires the &#x60;cardholder:read&#x60; scope.  | [optional] |
| **legalEntityName** | **String**| Only transactions whose card&#39;s cardholder belonged to a legal entity with this name (&#x60;corporate.legalEntity.name&#x60;) on the transaction date (the value &#x60;expand&#x3D;cardholder&#x60; returns on that transaction; the &#x60;expand&#x60; parameter defines the date), not the legal entity it belongs to now. Exact, case-sensitive match. URL-encode the value: a space is &#x60;%20&#x60; (or &#x60;+&#x60;), for example &#x60;legalEntityName&#x3D;Astrada%20Life&#x60;. A literal &#x60;+&#x60; is read as a space, so send a &#x60;+&#x60; in the name as &#x60;%2B&#x60;. Requires the &#x60;cardholder:read&#x60; scope.  | [optional] |
| **limit** | **Integer**| Maximum number of items to return | [optional] [default to 25] |
| **order** | **String**|  | [optional] [default to desc] [enum: asc, desc] |
| **expand** | [**List&lt;String&gt;**](String.md)| Comma-separated list of related resources to embed on the transaction under &#x60;_embedded&#x60;, so one call returns the transaction together with its card subscription, card and cardholder instead of follow-up calls to &#x60;GET /card-subscriptions?cardId&#x3D;&#x60;, &#x60;GET /cards/{cardId}&#x60; and &#x60;GET /cards/{cardId}/cardholder&#x60;.  - &#x60;cardSubscription&#x60; embeds the card subscription exactly as &#x60;GET /card-subscriptions/{subscriptionId}&#x60;   serves it (without its own &#x60;_embedded&#x60;) and requires the &#x60;card-subscriptions:read&#x60; scope. - &#x60;card&#x60; embeds the transaction&#39;s card exactly as &#x60;GET /cards/{cardId}&#x60; serves it (without its own   &#x60;_embedded&#x60;) and requires the &#x60;cards:read&#x60; scope. - &#x60;cardholder&#x60; embeds the card&#39;s cardholder as &#x60;GET /cards/{cardId}/cardholder&#x60; serves it, with one   difference: &#x60;corporate&#x60; is the cardholder&#39;s corporate as of the transaction (see below). Requires the   &#x60;cardholder:read&#x60; scope.  Each value is checked separately: a request asking to expand a resource its credentials cannot read is refused with &#x60;403&#x60;.  The embedded subscription is the card&#39;s subscription in the transaction&#39;s sub-account: the one from the transaction&#39;s network bulk feed when the transaction has a &#x60;networkBulkFeedId&#x60;, otherwise the card&#39;s current subscription, otherwise its most recent one.  &#x60;cardSubscription&#x60; and &#x60;card&#x60; show their **current** values, not the values they had when the transaction happened. So does the cardholder, except for &#x60;cardholder.corporate&#x60;: its &#x60;id&#x60;, &#x60;employeeId&#x60;, &#x60;alternativeEmployeeId&#x60; and &#x60;legalEntity&#x60; are the values in effect on the transaction&#39;s &#x60;transactionOccurrenceDate&#x60; (through the end of that UTC day), or at the transaction&#39;s &#x60;createdAt&#x60; when &#x60;transactionOccurrenceDate&#x60; is null. A card that moved to another company or legal entity after the transaction therefore still shows the one it belonged to at the time. &#x60;firstName&#x60;, &#x60;lastName&#x60;, &#x60;email&#x60;, &#x60;postalCode&#x60;, &#x60;corporate.networkToken&#x60;, &#x60;createdAt&#x60; and &#x60;updatedAt&#x60; are the cardholder&#39;s current values. Changes are dated by when Astrada received them from the card network or through the API. For a transaction dated before Astrada started recording the card&#39;s corporate history, the earliest recorded values are returned instead. Either way, a value that was not set in the values returned is null, or for &#x60;legalEntity&#x60; absent.  A key is present only when it was requested and the related resource exists, and it is never null. A transaction with no card (&#x60;cardId&#x60; is null, for example a bank-sourced transaction with no linked card) has no &#x60;cardSubscription&#x60;, &#x60;card&#x60; or &#x60;cardholder&#x60;. A card without cardholder data has no &#x60;cardholder&#x60;. A card that &#x60;GET /cards/{cardId}&#x60; no longer returns (it responds &#x60;404&#x60;, for example because all of its card subscriptions were deleted) is not embedded: both &#x60;card&#x60; and &#x60;cardholder&#x60; are absent. &#x60;_links&#x60; are unchanged. Without &#x60;expand&#x60;, &#x60;_embedded&#x60; is absent. Whether &#x60;cardholder&#x60; is embedded is decided on the card and cardholder as they are now, not as of the transaction&#39;s date.  Send one comma-separated value (&#x60;expand&#x3D;cardSubscription,card,cardholder&#x60;). A repeated &#x60;expand&#x60; key, an empty value or an unknown value is refused with &#x60;400&#x60;.  | [optional] [enum: cardSubscription, card, cardholder] |

### Return type

[**TransactionList**](TransactionList.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Transactions retrieved successfully |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

