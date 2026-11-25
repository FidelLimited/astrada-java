# NetworkBulkEnrollmentsApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createNetworkBulkConfirmation**](NetworkBulkEnrollmentsApi.md#createNetworkBulkConfirmation) | **POST** /network-bulk-confirmations | Create network bulk confirmation |
| [**createNetworkBulkEnrollment**](NetworkBulkEnrollmentsApi.md#createNetworkBulkEnrollment) | **POST** /network-bulk-enrollments | Create network bulk enrollment |
| [**getNetworkBulkEnrollment**](NetworkBulkEnrollmentsApi.md#getNetworkBulkEnrollment) | **GET** /network-bulk-enrollments/{networkBulkEnrollmentId} | Retrieve a network bulk enrollment |
| [**getOpenNetworkBulkEnrollment**](NetworkBulkEnrollmentsApi.md#getOpenNetworkBulkEnrollment) | **GET** /network-bulk-enrollments/open | Check for an open network bulk enrollment |


<a id="createNetworkBulkConfirmation"></a>
# **createNetworkBulkConfirmation**
> NetworkBulkConfirmationCreateResult createNetworkBulkConfirmation(networkBulkConfirmationCreateParams)

Create network bulk confirmation

Asks Astrada to confirm that a card program can join a network bulk feed. Astrada checks with the issuing bank and the network by hand, then emails the card program admin with the answer. Unlike &#x60;POST /network-bulk-enrollments&#x60;, this is not a kickoff: it does not start outreach to the bank and can never be linked to a feed. A validation failure or an unknown subaccount answers &#x60;400&#x60;, as for the kickoff.  A program has one open confirmation request at a time. A program is the calling account, the &#x60;subaccountId&#x60;, the &#x60;network&#x60; and the &#x60;bin&#x60;. Every stored request is open for 30 days from its &#x60;createdAt&#x60;, as nothing closes a request yet. When the program already has an open request that another request wrote, this request stores nothing and answers &#x60;200&#x60; with that request&#39;s summary (&#x60;alreadyOpen: true&#x60;), whatever new &#x60;requestId&#x60; it sent. No second request reaches Astrada. Two requests for one program sent at the same time store one request between them.  Once a request is 30 days old it stops holding its program: nothing is written to it, and the next request for the program is stored with &#x60;201&#x60; and reaches Astrada again.  The route answers these errors as &#x60;application/problem+json&#x60;, with &#x60;title&#x60; and &#x60;detail&#x60;:  | Status | When | &#x60;detail&#x60; | | --- | --- | --- | | &#x60;400&#x60; | The body does not pass validation. &#x60;errors&#x60; lists each field that failed. | &#x60;Request parameters are invalid&#x60; | | &#x60;400&#x60; | &#x60;subaccountId&#x60; is not a subaccount of the calling account. | &#x60;The subaccount {subaccountId} was not found&#x60; | | &#x60;403&#x60; | The token holds neither &#x60;network-bulk-enrollments:write&#x60; nor &#x60;network-bulk-feeds:write&#x60;. | &#x60;You do not have permission to access the requested resource&#x60; | | &#x60;500&#x60; | The request could not be completed. | &#x60;An unexpected error occurred&#x60; or &#x60;The server encountered an unexpected condition that prevented it from fulfilling the request&#x60; | 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.NetworkBulkEnrollmentsApi;
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
    NetworkBulkEnrollmentsApi api = new NetworkBulkEnrollmentsApi(client);
    NetworkBulkConfirmationCreateParams networkBulkConfirmationCreateParams = new NetworkBulkConfirmationCreateParams();
    try {
      NetworkBulkConfirmationCreateResult result = api.createNetworkBulkConfirmation(networkBulkConfirmationCreateParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("createNetworkBulkConfirmation failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **networkBulkConfirmationCreateParams** | [**NetworkBulkConfirmationCreateParams**](NetworkBulkConfirmationCreateParams.md)|  | |

### Return type

[**NetworkBulkConfirmationCreateResult**](NetworkBulkConfirmationCreateResult.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A stored confirmation request, returned unchanged, in one of two shapes:  - A confirmation with this &#x60;requestId&#x60; already exists: the full confirmation, as your   request wrote it. - The program already has an open request that another request wrote: its summary,   marked &#x60;alreadyOpen: true&#x60;, with the &#x60;requestId&#x60; this request sent. Names, emails,   bank contact, &#x60;binInfo&#x60; and &#x60;grade&#x60; are left out.  Both are plain JSON with no &#x60;_links&#x60;.  |  -  |
| **201** | Resource created. The program had no open request, so this request wrote one. Served as plain JSON rather than HAL, since a confirmation has no GET and so carries no &#x60;_links&#x60;. |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="createNetworkBulkEnrollment"></a>
# **createNetworkBulkEnrollment**
> NetworkBulkEnrollmentCreateResult createNetworkBulkEnrollment(networkBulkEnrollmentCreateParams)

Create network bulk enrollment

Starts a bulk enrollment kickoff for a whole card program. The request records the company, the card program admin, an optional bank contact and program size, and the admin&#39;s consent, and Astrada&#39;s onboarding team takes it from there with the issuing bank. The Unified Enrollment SDK calls this endpoint when an admin sends the bulk kickoff form (in SDK v1, with &#x60;bulkFlow: \&quot;kickoff\&quot;&#x60;); you can also call it directly.  Nothing is enrolled yet at this point. Once the bank has issued the network reference, register the feed with &#x60;POST /network-bulk-feeds&#x60; and pass this enrollment&#39;s &#x60;id&#x60; as &#x60;networkBulkEnrollmentId&#x60;; the enrollment then moves to &#x60;feed-created&#x60; and links to the feed.  The subaccount must belong to the calling account. The BIN&#39;s bulk eligibility is not re-checked here. A validation failure or an unknown subaccount answers &#x60;400&#x60;.  A program has one open kickoff at a time. A program is the calling account, the &#x60;subaccountId&#x60;, the &#x60;network&#x60; and the first 8 digits of the &#x60;bin&#x60;; a &#x60;bin&#x60; shorter than 8 digits matches only a kickoff with exactly the same digits. A kickoff is open while it is &#x60;requested&#x60; and was created less than 90 days ago, or while it is &#x60;feed-created&#x60; and its feed is live (not &#x60;failed&#x60; or &#x60;deactivated&#x60;, and not deleted), at any age. When the program already has an open kickoff that another request wrote, this request stores no second kickoff and answers &#x60;200&#x60; with that kickoff&#39;s summary (&#x60;alreadyOpen: true&#x60;), whatever new &#x60;requestId&#x60; it sent, or none. Astrada keeps the card admin named in this request with the open kickoff, so its onboarding team knows who else is waiting, and the open kickoff stays the only one they work on. A repeat of the same &#x60;requestId&#x60; is kept once. &#x60;GET /network-bulk-enrollments/open&#x60; answers the same question before the form. Two kickoffs for one program sent at the same time store one kickoff between them.  The 90 days count from the kickoff&#39;s &#x60;createdAt&#x60;. Once a &#x60;requested&#x60; kickoff is 90 days old it stops holding its program: nothing is written to it, it keeps its state and can still be read, and the next kickoff for the program is created with &#x60;201&#x60;. A &#x60;cancelled&#x60; kickoff, or one whose feed failed, was deactivated or was deleted, does not hold its program either.  Requires the &#x60;network-bulk-enrollments:write&#x60; scope. Tokens that only hold &#x60;network-bulk-feeds:write&#x60; are still accepted, so existing integrations keep working. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.NetworkBulkEnrollmentsApi;
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
    NetworkBulkEnrollmentsApi api = new NetworkBulkEnrollmentsApi(client);
    NetworkBulkEnrollmentCreateParams networkBulkEnrollmentCreateParams = new NetworkBulkEnrollmentCreateParams();
    try {
      NetworkBulkEnrollmentCreateResult result = api.createNetworkBulkEnrollment(networkBulkEnrollmentCreateParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("createNetworkBulkEnrollment failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **networkBulkEnrollmentCreateParams** | [**NetworkBulkEnrollmentCreateParams**](NetworkBulkEnrollmentCreateParams.md)|  | |

### Return type

[**NetworkBulkEnrollmentCreateResult**](NetworkBulkEnrollmentCreateResult.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A stored kickoff, returned unchanged, in one of two shapes:  - An enrollment with this &#x60;requestId&#x60; already exists: the full enrollment, as your   request wrote it. - The program already has an open kickoff that another request wrote: its summary,   marked &#x60;alreadyOpen: true&#x60;, with the &#x60;requestId&#x60; this request sent (&#x60;null&#x60; when it   sent none) and only the &#x60;self&#x60; link. Names, emails, bank contact, &#x60;binInfo&#x60;, consent   and outreach are left out.  |  -  |
| **201** | Resource created. The program had no open kickoff, so this request wrote one. |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="getNetworkBulkEnrollment"></a>
# **getNetworkBulkEnrollment**
> NetworkBulkEnrollment getNetworkBulkEnrollment(networkBulkEnrollmentId)

Retrieve a network bulk enrollment

Returns a network bulk enrollment by its identifier. Once a feed has been registered with this enrollment&#39;s id on &#x60;POST /network-bulk-feeds&#x60;, the representation carries &#x60;networkBulkFeedId&#x60; and a &#x60;networkBulkFeed&#x60; link. An enrollment that belongs to another account answers &#x60;404&#x60;.  Requires the &#x60;network-bulk-enrollments:read&#x60; scope. Tokens that only hold &#x60;network-bulk-feeds:read&#x60; are still accepted, so existing integrations keep working. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.NetworkBulkEnrollmentsApi;
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
    NetworkBulkEnrollmentsApi api = new NetworkBulkEnrollmentsApi(client);
    UUID networkBulkEnrollmentId = UUID.fromString("9c1f0c4e-2f0e-4a9c-9a1b-0c1d2e3f4a5b");
    try {
      NetworkBulkEnrollment result = api.getNetworkBulkEnrollment(networkBulkEnrollmentId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getNetworkBulkEnrollment failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **networkBulkEnrollmentId** | **UUID**|  | |

### Return type

[**NetworkBulkEnrollment**](NetworkBulkEnrollment.md)

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

<a id="getOpenNetworkBulkEnrollment"></a>
# **getOpenNetworkBulkEnrollment**
> NetworkBulkEnrollmentOpen getOpenNetworkBulkEnrollment(subaccountId, network, bin)

Check for an open network bulk enrollment

Answers whether a card program already has an open bulk enrollment kickoff, before anyone fills in the kickoff form. The Unified Enrollment SDK calls it while it looks up a card, and shows \&quot;Already under way\&quot; instead of the program screen when the program has one; you can also call it directly.  A program is the calling account, the &#x60;subaccountId&#x60;, the &#x60;network&#x60; and the card&#39;s first 8 digits (&#x60;bin&#x60;). The rule is the one &#x60;POST /network-bulk-enrollments&#x60; uses: a kickoff is open while it is &#x60;requested&#x60; and was created less than 90 days ago, or while it is &#x60;feed-created&#x60; and its feed is live, at any age. The account comes from the token, never from the query.  The answer carries only the open kickoff&#39;s &#x60;enrollmentId&#x60;, &#x60;state&#x60; and &#x60;requestedAt&#x60;. A &#x60;bin&#x60; of more or fewer than 8 digits, a missing or unknown parameter, or an &#x60;accountId&#x60; in the query answers &#x60;400&#x60;.  Requires the &#x60;network-bulk-enrollments:write&#x60; scope, the one the kickoff itself takes. Tokens that only hold &#x60;network-bulk-feeds:write&#x60; are also accepted. A token with only a read scope answers &#x60;403&#x60;. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.NetworkBulkEnrollmentsApi;
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
    NetworkBulkEnrollmentsApi api = new NetworkBulkEnrollmentsApi(client);
    UUID subaccountId = UUID.randomUUID();
    String network = "AMEX";
    String bin = "41111111";
    try {
      NetworkBulkEnrollmentOpen result = api.getOpenNetworkBulkEnrollment(subaccountId, network, bin);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getOpenNetworkBulkEnrollment failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subaccountId** | **UUID**| The subaccount of the card program. Must belong to the calling account to find anything. | |
| **network** | **String**| The card&#39;s network. | [enum: AMEX, MASTERCARD, VISA] |
| **bin** | **String**| Exactly the card&#39;s first 8 digits. | |

### Return type

[**NetworkBulkEnrollmentOpen**](NetworkBulkEnrollmentOpen.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Whether the program has an open kickoff. |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

