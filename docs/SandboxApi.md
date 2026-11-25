# SandboxApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deleteCardSubscription**](SandboxApi.md#deleteCardSubscription) | **DELETE** /card-subscriptions/{subscriptionId} | Delete card subscription (sandbox only) |
| [**deleteNetworkBulkFeed**](SandboxApi.md#deleteNetworkBulkFeed) | **DELETE** /network-bulk-feeds/{networkBulkFeedId} | Delete network bulk feed (sandbox only) |
| [**deleteSubaccount**](SandboxApi.md#deleteSubaccount) | **DELETE** /subaccounts/{subaccountId} | Delete subaccount (sandbox only) |
| [**simulateBulkFeedFileGl**](SandboxApi.md#simulateBulkFeedFileGl) | **POST** /simulate/ingestion/amex-gl | Simulate an American Express bulk file (sandbox only) |
| [**simulateBulkFeedFileSmartdata**](SandboxApi.md#simulateBulkFeedFileSmartdata) | **POST** /simulate/ingestion/smartdata | Simulate a Mastercard bulk file (sandbox only) |
| [**simulateBulkFeedFileVbds**](SandboxApi.md#simulateBulkFeedFileVbds) | **POST** /simulate/ingestion/vbds | Simulate a pulled Visa bulk feed delivery (sandbox only) |
| [**simulateBulkFeedFileVcf**](SandboxApi.md#simulateBulkFeedFileVcf) | **POST** /simulate/ingestion/vcf | Simulate a Visa bulk file (sandbox only) |
| [**simulateTransaction**](SandboxApi.md#simulateTransaction) | **POST** /simulate/transactions | Simulate a transaction (sandbox only) |
| [**simulateTransactionMessage**](SandboxApi.md#simulateTransactionMessage) | **POST** /simulate/transactions/{transactionId} | Simulate a transaction message (sandbox only) |
| [**simulateTransactionScenario**](SandboxApi.md#simulateTransactionScenario) | **POST** /simulate/transaction/scenario | Simulate a transaction scenario (sandbox only) |


<a id="deleteCardSubscription"></a>
# **deleteCardSubscription**
> deleteCardSubscription(subscriptionId)

Delete card subscription (sandbox only)

Works in the sandbox only (&#x60;https://api.sandbox.astrada.co&#x60;), for clearing test data between runs. Production has no delete call: there you deactivate card subscriptions with &#x60;PATCH&#x60;.  Deletes a card subscription and the data it owns, and cannot be undone. When it is the last subscription on its card, the card, cardholder, stored token and transactions are removed as well.  The call returns when the delete has finished: &#x60;204&#x60; when it is done, &#x60;502&#x60; when a step failed. Retrying a failed delete is always safe: completed steps are skipped on replay. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.SandboxApi;
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
    SandboxApi api = new SandboxApi(client);
    UUID subscriptionId = UUID.fromString("eb2ded7e-11fa-46fc-bd3e-f5b1d56fd3b3");
    try {
      api.deleteCardSubscription(subscriptionId);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("deleteCardSubscription failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subscriptionId** | **UUID**|  | |

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
| **204** | The request has been successfully processed and there is no content to return |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |
| **502** | The cascade delete did not complete; retrying the request resumes it |  -  |

<a id="deleteNetworkBulkFeed"></a>
# **deleteNetworkBulkFeed**
> deleteNetworkBulkFeed(networkBulkFeedId)

Delete network bulk feed (sandbox only)

Works in the sandbox only (&#x60;https://api.sandbox.astrada.co&#x60;), for clearing test data between runs. Production has no delete call.  Deletes a network bulk feed and the data it owns, and cannot be undone. The feed stops first, then every card subscription enrolled through it is removed, along with the cards, cardholders, stored tokens and transactions that only those subscriptions used.  The call returns when the delete has finished: &#x60;204&#x60; when it is done, &#x60;502&#x60; when a step failed. Retrying a failed delete is always safe: completed steps are skipped on replay. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.SandboxApi;
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
    SandboxApi api = new SandboxApi(client);
    UUID networkBulkFeedId = UUID.fromString("eb2ded7e-11fa-46fc-bd3e-f5b1d56fd3b3");
    try {
      api.deleteNetworkBulkFeed(networkBulkFeedId);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("deleteNetworkBulkFeed failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **networkBulkFeedId** | **UUID**|  | |

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
| **204** | The request has been successfully processed and there is no content to return |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |
| **502** | The cascade delete did not complete; retrying the request resumes it |  -  |

<a id="deleteSubaccount"></a>
# **deleteSubaccount**
> deleteSubaccount(subaccountId)

Delete subaccount (sandbox only)

Works in the sandbox only (&#x60;https://api.sandbox.astrada.co&#x60;), for clearing test data between runs. Production has no delete call: there you deactivate card subscriptions with &#x60;PATCH&#x60;.  Deletes a subaccount and everything under it, and cannot be undone. Its bank links (with their bank accounts, bank transactions and matches) go first, then every card subscription, card, cardholder, stored token, transaction, network bulk feed and webhook of the subaccount.  The call returns when the delete has finished: &#x60;204&#x60; when it is done, &#x60;502&#x60; when a step failed. Retrying a failed delete is always safe: completed steps are skipped on replay. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.SandboxApi;
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
    SandboxApi api = new SandboxApi(client);
    UUID subaccountId = UUID.fromString("24c4f90d-ab3b-4f6e-8a1d-d251a4fcf34c");
    try {
      api.deleteSubaccount(subaccountId);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("deleteSubaccount failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subaccountId** | **UUID**|  | |

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
| **204** | The request has been successfully processed and there is no content to return |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |
| **502** | The cascade delete did not complete; retrying the request resumes it |  -  |

<a id="simulateBulkFeedFileGl"></a>
# **simulateBulkFeedFileGl**
> simulateBulkFeedFileGl(simulateBulkFeedFileParams)

Simulate an American Express bulk file (sandbox only)

Works in the sandbox only (&#x60;https://api.sandbox.astrada.co&#x60;). Production has no such call.  Creates a simulated American Express bulk-enrollment file, plus a companion settlement file when &#x60;transactionsPerCard&#x60; is above &#x60;0&#x60;, and processes it as if American Express had delivered it. The call returns &#x60;202 Accepted&#x60; at once; enrolled cards appear as card subscriptions when processing finishes, and their settlements follow shortly after.  A bulk feed must already exist for the &#x60;networkReference&#x60;, created with &#x60;POST /network-bulk-feeds&#x60;.  &#x60;cardCount&#x60; replays the fixed head of the sandbox account pool (encrypted feeds; for tokenized feeds see below): the same accounts every call, so once enrolled in a subaccount they deduplicate (no new &#x60;cardsubscription.created&#x60;), exactly like a file that is delivered twice. &#x60;newCardCount&#x60; draws accounts the subaccount has never seen from a reserved pool, so every call enrolls that many new cards. All included cards get &#x60;transactionsPerCard&#x60; transactions with fresh ids and recent dates, so repeat calls always produce &#x60;transaction.*&#x60; webhooks.  For a feed registered &#x60;tokenized&#x60; (see &#x60;panFormat&#x60; on &#x60;POST /network-bulk-feeds&#x60;), &#x60;cardCount&#x60; draws American Express network tokens instead of raw card numbers, and &#x60;newCardCount&#x60; is not supported (there is no tokenized new-card pool). 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.SandboxApi;
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
    SandboxApi api = new SandboxApi(client);
    SimulateBulkFeedFileParams simulateBulkFeedFileParams = new SimulateBulkFeedFileParams();
    try {
      api.simulateBulkFeedFileGl(simulateBulkFeedFileParams);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("simulateBulkFeedFileGl failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **simulateBulkFeedFileParams** | [**SimulateBulkFeedFileParams**](SimulateBulkFeedFileParams.md)|  | |

### Return type

null (empty response body)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | Accepted. The simulated file was created and is processed asynchronously. The response has no body.  |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **413** | Content Too Large |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="simulateBulkFeedFileSmartdata"></a>
# **simulateBulkFeedFileSmartdata**
> simulateBulkFeedFileSmartdata(simulateBulkFeedFileParams)

Simulate a Mastercard bulk file (sandbox only)

Works in the sandbox only (&#x60;https://api.sandbox.astrada.co&#x60;). Production has no such call.  Creates a simulated Mastercard bulk-enrollment file and processes it as if Mastercard had delivered it. The call returns &#x60;202 Accepted&#x60; at once; enrolled cards appear as card subscriptions when processing finishes.  A bulk feed must already exist for the &#x60;networkReference&#x60;, created with &#x60;POST /network-bulk-feeds&#x60;.  &#x60;cardCount&#x60; replays the fixed head of the sandbox test-card pool: the same cards every call, so once enrolled in a subaccount they deduplicate (no new &#x60;cardsubscription.created&#x60;), exactly like a file that is delivered twice. &#x60;newCardCount&#x60; draws cards the subaccount has never seen from a reserved pool, so every call enrolls that many new cards. All included cards get &#x60;transactionsPerCard&#x60; transactions with fresh references and recent dates, so repeat calls always produce &#x60;transaction.*&#x60; webhooks. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.SandboxApi;
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
    SandboxApi api = new SandboxApi(client);
    SimulateBulkFeedFileParams simulateBulkFeedFileParams = new SimulateBulkFeedFileParams();
    try {
      api.simulateBulkFeedFileSmartdata(simulateBulkFeedFileParams);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("simulateBulkFeedFileSmartdata failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **simulateBulkFeedFileParams** | [**SimulateBulkFeedFileParams**](SimulateBulkFeedFileParams.md)|  | |

### Return type

null (empty response body)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | Accepted. The simulated file was created and is processed asynchronously. The response has no body.  |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **413** | Content Too Large |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="simulateBulkFeedFileVbds"></a>
# **simulateBulkFeedFileVbds**
> simulateBulkFeedFileVbds(simulateBulkFeedFileParams)

Simulate a pulled Visa bulk feed delivery (sandbox only)

Works in the sandbox only (&#x60;https://api.sandbox.astrada.co&#x60;). Production has no such call.  Simulates one delivery on a Visa bulk feed of &#x60;type: VBDS&#x60;, the kind Astrada pulls from Visa instead of receiving a file. The delivery enrolls cards and, when &#x60;transactionsPerCard&#x60; is above &#x60;0&#x60;, adds settlements for them. The call returns &#x60;202 Accepted&#x60; at once; enrolled cards appear as card subscriptions when processing finishes.  A bulk feed must already exist for the &#x60;networkReference&#x60;, created with &#x60;POST /network-bulk-feeds&#x60;; otherwise the call returns &#x60;400&#x60;.  &#x60;cardCount&#x60; replays the fixed head of the sandbox card pool: the same cards every call, so once enrolled in a subaccount they deduplicate (no new &#x60;cardsubscription.created&#x60;), exactly like a delivery that arrives twice. &#x60;newCardCount&#x60; draws cards the subaccount has never seen from a reserved pool, so every call enrolls that many new cards. All included cards get &#x60;transactionsPerCard&#x60; transactions with fresh ids and recent dates, so repeat calls always produce &#x60;transaction.*&#x60; webhooks. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.SandboxApi;
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
    SandboxApi api = new SandboxApi(client);
    SimulateBulkFeedFileParams simulateBulkFeedFileParams = new SimulateBulkFeedFileParams();
    try {
      api.simulateBulkFeedFileVbds(simulateBulkFeedFileParams);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("simulateBulkFeedFileVbds failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **simulateBulkFeedFileParams** | [**SimulateBulkFeedFileParams**](SimulateBulkFeedFileParams.md)|  | |

### Return type

null (empty response body)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | Accepted. The simulated delivery was queued and is processed asynchronously. The response has no body.  |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **413** | Content Too Large |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="simulateBulkFeedFileVcf"></a>
# **simulateBulkFeedFileVcf**
> simulateBulkFeedFileVcf(simulateBulkFeedFileParams)

Simulate a Visa bulk file (sandbox only)

Works in the sandbox only (&#x60;https://api.sandbox.astrada.co&#x60;). Production has no such call.  Creates a simulated Visa bulk-enrollment file (a feed of &#x60;type: VCF&#x60;) and processes it as if Visa had delivered it. The call returns &#x60;202 Accepted&#x60; at once; enrolled cards appear as card subscriptions when processing finishes.  A bulk feed must already exist for the &#x60;networkReference&#x60;, created with &#x60;POST /network-bulk-feeds&#x60;.  &#x60;cardCount&#x60; replays the fixed head of the sandbox test-card pool: the same cards every call, so once enrolled in a subaccount they deduplicate (no new &#x60;cardsubscription.created&#x60;), exactly like a file that is delivered twice. &#x60;newCardCount&#x60; draws cards the subaccount has never seen from a reserved pool, so every call enrolls that many new cards. All included cards get &#x60;transactionsPerCard&#x60; transactions with fresh references and recent dates, so repeat calls always produce &#x60;transaction.*&#x60; webhooks. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.SandboxApi;
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
    SandboxApi api = new SandboxApi(client);
    SimulateBulkFeedFileParams simulateBulkFeedFileParams = new SimulateBulkFeedFileParams();
    try {
      api.simulateBulkFeedFileVcf(simulateBulkFeedFileParams);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("simulateBulkFeedFileVcf failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **simulateBulkFeedFileParams** | [**SimulateBulkFeedFileParams**](SimulateBulkFeedFileParams.md)|  | |

### Return type

null (empty response body)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | Accepted. The simulated file was created and is processed asynchronously. The response has no body.  |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **413** | Content Too Large |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="simulateTransaction"></a>
# **simulateTransaction**
> SimulateTransactionAccepted simulateTransaction(simulateTransactionRequest)

Simulate a transaction (sandbox only)

Works in the sandbox only (&#x60;https://api.sandbox.astrada.co&#x60;). Production has no such call.  Starts a new simulated transaction on an enrolled card by publishing one transaction message, an authorization or a settlement, as if a card network had sent it. Follow it with &#x60;POST /simulate/transactions/{transactionId}&#x60; to add further messages to the transaction.  Besides &#x60;simulation:write&#x60;, the access token needs &#x60;cards:read&#x60;: the card is read from the public API to resolve its subaccount and network. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.SandboxApi;
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
    SandboxApi api = new SandboxApi(client);
    SimulateTransactionRequest simulateTransactionRequest = new SimulateTransactionRequest();
    try {
      SimulateTransactionAccepted result = api.simulateTransaction(simulateTransactionRequest);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("simulateTransaction failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **simulateTransactionRequest** | [**SimulateTransactionRequest**](SimulateTransactionRequest.md)|  | |

### Return type

[**SimulateTransactionAccepted**](SimulateTransactionAccepted.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | Accepted. The transaction message was published and is processed asynchronously. The transaction it creates is delivered through the &#x60;transaction.created&#x60; webhook and can be found with &#x60;GET /transactions?transactionMessageId&#x3D;&#x60;.  |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |
| **502** | The card could not be read from the sandbox API. Retry the request. |  -  |

<a id="simulateTransactionMessage"></a>
# **simulateTransactionMessage**
> SimulateTransactionAccepted simulateTransactionMessage(transactionId, simulateTransactionMessageRequest)

Simulate a transaction message (sandbox only)

Works in the sandbox only (&#x60;https://api.sandbox.astrada.co&#x60;). Production has no such call.  Publishes one more transaction message (authorization, adjustment, reversal, settlement or refund) on an existing sandbox transaction. The transaction can have been created by &#x60;POST /simulate/transactions&#x60; or by the scenario endpoint; take its id from the &#x60;transaction.created&#x60; webhook or from &#x60;GET /transactions?transactionMessageId&#x3D;&#x60;.  Besides &#x60;simulation:write&#x60;, the access token needs &#x60;transactions:read&#x60; and &#x60;transaction-messages:read&#x60;: the transaction and its messages are read from the public API to carry over the network reference and the current hold and settled amounts.  &#x60;adjustment&#x60; and &#x60;reversal&#x60; are checked against the hold Astrada has processed so far and rejected with &#x60;409&#x60; when there is nothing to adjust or reverse. The check is best effort: a step published immediately after another may see the earlier state. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.SandboxApi;
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
    SandboxApi api = new SandboxApi(client);
    UUID transactionId = UUID.fromString("9340721f-bdce-4afa-bd9d-26cceb9d2176");
    SimulateTransactionMessageRequest simulateTransactionMessageRequest = new SimulateTransactionMessageRequest();
    try {
      SimulateTransactionAccepted result = api.simulateTransactionMessage(transactionId, simulateTransactionMessageRequest);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("simulateTransactionMessage failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **transactionId** | **UUID**|  | |
| **simulateTransactionMessageRequest** | [**SimulateTransactionMessageRequest**](SimulateTransactionMessageRequest.md)|  | |

### Return type

[**SimulateTransactionAccepted**](SimulateTransactionAccepted.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | Accepted. The transaction message was published and is processed asynchronously. The updated transaction is delivered through the &#x60;transaction.updated&#x60; webhook.  |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **409** | Request conflict with the current state of the target resource |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |
| **502** | The transaction could not be read from the sandbox API. Retry the request. |  -  |

<a id="simulateTransactionScenario"></a>
# **simulateTransactionScenario**
> SimulateTransactionScenarioAccepted simulateTransactionScenario(simulateTransactionScenarioRequest)

Simulate a transaction scenario (sandbox only)

Works in the sandbox only (&#x60;https://api.sandbox.astrada.co&#x60;). Production has no such call.  Runs a predefined transaction scenario, such as a purchase followed by a refund, on an enrolled card. The call publishes every transaction message of the scenario as if a card network had sent them and returns &#x60;202 Accepted&#x60; with their ids. The transactions arrive asynchronously through the &#x60;transaction.created&#x60; and &#x60;transaction.updated&#x60; webhooks.  The call does not look the card up first: well-formed ids return &#x60;202&#x60; even when they do not name an enrolled card of your account. If no transaction appears, check that &#x60;cardId&#x60; is enrolled in &#x60;subaccountId&#x60;. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.SandboxApi;
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
    SandboxApi api = new SandboxApi(client);
    SimulateTransactionScenarioRequest simulateTransactionScenarioRequest = new SimulateTransactionScenarioRequest();
    try {
      SimulateTransactionScenarioAccepted result = api.simulateTransactionScenario(simulateTransactionScenarioRequest);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("simulateTransactionScenario failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **simulateTransactionScenarioRequest** | [**SimulateTransactionScenarioRequest**](SimulateTransactionScenarioRequest.md)|  | |

### Return type

[**SimulateTransactionScenarioAccepted**](SimulateTransactionScenarioAccepted.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | Accepted. The transaction messages were published and are processed asynchronously.  |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

