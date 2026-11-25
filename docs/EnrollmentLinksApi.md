# EnrollmentLinksApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createEnrollmentLink**](EnrollmentLinksApi.md#createEnrollmentLink) | **POST** /enrollment-links | Create enrollment link |
| [**createTeamLink**](EnrollmentLinksApi.md#createTeamLink) | **POST** /subaccounts/{subaccountId}/team-link | Create team link |
| [**getEnrollmentLink**](EnrollmentLinksApi.md#getEnrollmentLink) | **GET** /enrollment-links/{linkId} | Retrieve enrollment link |
| [**listEnrollmentLinks**](EnrollmentLinksApi.md#listEnrollmentLinks) | **GET** /enrollment-links | List enrollment links |
| [**revokeEnrollmentLink**](EnrollmentLinksApi.md#revokeEnrollmentLink) | **POST** /enrollment-links/{linkId}/revoke | Revoke enrollment link |


<a id="createEnrollmentLink"></a>
# **createEnrollmentLink**
> EnrollmentLinkCreateResult createEnrollmentLink(enrollmentLinkCreateParams)

Create enrollment link

Creates an enrollment link for one of your subaccounts. The link opens a page on Astrada&#39;s SDK host where the person who has it enrolls their own cards into that subaccount.  - A personal link (&#x60;kind: cardholder&#x60;) is for one employee. It allows up to 5 session   starts and is used up by the first session that completes something. - A team link (&#x60;kind: team&#x60;) is for sharing. Each session start counts as a use, up to   &#x60;maxUses&#x60;.  The response&#39;s &#x60;url&#x60; carries the link&#39;s secret after &#x60;#&#x60;. This response is the only one that returns it, and it is sent with &#x60;Cache-Control: no-store&#x60;. Astrada stores only a hash of the secret. In sandbox the &#x60;url&#x60; starts with &#x60;https://sdk.sandbox.astrada.co/unified/v2/enroll.html#&#x60;.  Requires &#x60;subaccounts:write&#x60;.  Refusals carry a &#x60;code&#x60;:  - &#x60;404&#x60; &#x60;SUBACCOUNT_NOT_FOUND&#x60;: &#x60;subaccountId&#x60; is not one of your subaccounts. - &#x60;409&#x60; &#x60;ENROLLMENT_LINKS_NOT_ENABLED&#x60;: enrollment links are not enabled for your   account. - &#x60;409&#x60; &#x60;CARD_ENROLLMENT_OFF&#x60;: card enrollment is off for the subaccount (its   &#x60;enrollmentPolicy.cardEnrollment&#x60; is &#x60;false&#x60;). The hosted page offers card enrollment   only, so a link would have nothing to enroll. This check runs before the daily limit   is counted, so a refused request does not use it up. - &#x60;400&#x60; &#x60;LINK_NAME_NOT_ALLOWED&#x60;: &#x60;organizationName&#x60; or &#x60;platformName&#x60; is a   well-known brand name, such as a card network or a large bank, matched ignoring case,   accents and punctuation. Astrada and Plaid are also refused inside a longer name. - &#x60;429&#x60; &#x60;RATE_LIMITED&#x60;: your account has created 2,000 links today (UTC), team links   made with &#x60;POST /subaccounts/{subaccountId}/team-link&#x60; included.  A link made before card enrollment was turned off starts no session while card enrollment stays off: &#x60;POST /enrollment-link-sessions/preview&#x60; and &#x60;POST /enrollment-link-sessions&#x60; answer &#x60;409&#x60; &#x60;CARD_ENROLLMENT_OFF&#x60;, and no start is counted. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.EnrollmentLinksApi;
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
    EnrollmentLinksApi api = new EnrollmentLinksApi(client);
    EnrollmentLinkCreateParams enrollmentLinkCreateParams = new EnrollmentLinkCreateParams();
    try {
      EnrollmentLinkCreateResult result = api.createEnrollmentLink(enrollmentLinkCreateParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("createEnrollmentLink failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **enrollmentLinkCreateParams** | [**EnrollmentLinkCreateParams**](EnrollmentLinkCreateParams.md)|  | |

### Return type

[**EnrollmentLinkCreateResult**](EnrollmentLinkCreateResult.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | The link, with its &#x60;url&#x60;. |  -  |
| **400** | The body failed validation, and &#x60;errors&#x60; names the field. Or &#x60;organizationName&#x60; or &#x60;platformName&#x60; is a well-known brand name, with &#x60;code&#x60; &#x60;LINK_NAME_NOT_ALLOWED&#x60;.  |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | The token holds none of the scopes the route accepts, or it is an enrollment link session&#39;s token, which no scope lets manage links. A missing scope answers without a &#x60;code&#x60;. A link session token that holds an accepted scope answers with &#x60;code&#x60; &#x60;LINK_SESSION_FORBIDDEN&#x60;.  |  -  |
| **404** | The subaccount is not one of your account&#39;s subaccounts. Nothing is created.  |  -  |
| **409** | The link was not created, and nothing is stored. &#x60;code&#x60; says why:  - &#x60;ENROLLMENT_LINKS_NOT_ENABLED&#x60;: enrollment links are not enabled for the account.   Astrada enables them per account. - &#x60;CARD_ENROLLMENT_OFF&#x60;: card enrollment is off for the subaccount (its   &#x60;enrollmentPolicy.cardEnrollment&#x60; is &#x60;false&#x60;). The hosted page offers card enrollment   only, so a link would have nothing to enroll.  |  -  |
| **429** | A rate limit was reached. The operation&#39;s description names its limits. Retry later.  |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="createTeamLink"></a>
# **createTeamLink**
> TeamLink createTeamLink(subaccountId)

Create team link

Creates a team link for the subaccount: the route for the SDK&#39;s \&quot;Invite your team\&quot; ending, which calls it with the SDK token&#39;s &#x60;card-subscriptions:write&#x60;. An API client may call it with &#x60;subaccounts:write&#x60;. No body.  &#x60;_links.team-link&#x60; on &#x60;GET /subaccounts/{subaccountId}&#x60; points here when the route is available to the caller.  The link has the team defaults: it expires after 14 days and allows 50 session starts. The response holds only what the SDK screen shows, and is sent with &#x60;Cache-Control: no-store&#x60;. Every call makes a new link. A new link does not revoke an older one: each link ends at its own expiry, when its session starts are used up, or when it is revoked. Revoke a link you no longer want shared with &#x60;POST /enrollment-links/{linkId}/revoke&#x60;.  Refusals carry a &#x60;code&#x60;:  - &#x60;403&#x60; &#x60;LINK_SESSION_FORBIDDEN&#x60;: the token is an enrollment link session&#39;s. It holds   &#x60;card-subscriptions:write&#x60; too, but cannot make links. - &#x60;404&#x60; &#x60;SUBACCOUNT_NOT_FOUND&#x60;: the subaccount is not one of your account&#39;s. - &#x60;409&#x60; &#x60;ENROLLMENT_LINKS_NOT_ENABLED&#x60;: enrollment links are not enabled for your   account. - &#x60;409&#x60; &#x60;CARD_ENROLLMENT_OFF&#x60;: card enrollment is off for the subaccount (its   &#x60;enrollmentPolicy.cardEnrollment&#x60; is &#x60;false&#x60;). The hosted page offers card enrollment   only, so a link would have nothing to enroll. This check runs before the daily limits   are counted, so a refused request does not use them up. - &#x60;429&#x60; &#x60;RATE_LIMITED&#x60;: 20 links were made here for the subaccount today (UTC), or your   account has created 2,000 links today.  A link made before card enrollment was turned off starts no session while card enrollment stays off: &#x60;POST /enrollment-link-sessions/preview&#x60; and &#x60;POST /enrollment-link-sessions&#x60; answer &#x60;409&#x60; &#x60;CARD_ENROLLMENT_OFF&#x60;, and no start is counted. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.EnrollmentLinksApi;
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
    EnrollmentLinksApi api = new EnrollmentLinksApi(client);
    UUID subaccountId = UUID.fromString("24c4f90d-ab3b-4f6e-8a1d-d251a4fcf34c");
    try {
      TeamLink result = api.createTeamLink(subaccountId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("createTeamLink failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subaccountId** | **UUID**|  | |

### Return type

[**TeamLink**](TeamLink.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | The team link. |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | The token holds none of the scopes the route accepts, or it is an enrollment link session&#39;s token, which no scope lets manage links. A missing scope answers without a &#x60;code&#x60;. A link session token that holds an accepted scope answers with &#x60;code&#x60; &#x60;LINK_SESSION_FORBIDDEN&#x60;.  |  -  |
| **404** | The subaccount is not one of your account&#39;s subaccounts. Nothing is created.  |  -  |
| **409** | The link was not created, and nothing is stored. &#x60;code&#x60; says why:  - &#x60;ENROLLMENT_LINKS_NOT_ENABLED&#x60;: enrollment links are not enabled for the account.   Astrada enables them per account. - &#x60;CARD_ENROLLMENT_OFF&#x60;: card enrollment is off for the subaccount (its   &#x60;enrollmentPolicy.cardEnrollment&#x60; is &#x60;false&#x60;). The hosted page offers card enrollment   only, so a link would have nothing to enroll.  |  -  |
| **429** | A rate limit was reached. The operation&#39;s description names its limits. Retry later.  |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="getEnrollmentLink"></a>
# **getEnrollmentLink**
> EnrollmentLinkDetail getEnrollmentLink(linkId)

Retrieve enrollment link

Returns one of your enrollment links with its sessions, oldest first, without its &#x60;url&#x60;. With the &#x60;cardsubscription&#x60; webhooks, this is how you see what happened on the hosted page.  Each session&#39;s &#x60;outcome&#x60; holds the ids the hosted page reported. Nothing checks them against the card or bank APIs: the &#x60;cardsubscription&#x60; webhooks are the source of truth for what was enrolled.  Requires &#x60;subaccounts:write&#x60;. A link that is not one of yours answers &#x60;404&#x60; with &#x60;code&#x60; &#x60;ENROLLMENT_LINK_NOT_FOUND&#x60;. A link and its sessions are kept for 90 days after &#x60;expiresAt&#x60;, then answer &#x60;404&#x60;. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.EnrollmentLinksApi;
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
    EnrollmentLinksApi api = new EnrollmentLinksApi(client);
    UUID linkId = UUID.fromString("1d0a6c52-8f3e-4b7a-9c21-5e4d3b2a1f09");
    try {
      EnrollmentLinkDetail result = api.getEnrollmentLink(linkId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getEnrollmentLink failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **linkId** | **UUID**| The enrollment link&#39;s identifier, the &#x60;id&#x60; returned when the link was created. | |

### Return type

[**EnrollmentLinkDetail**](EnrollmentLinkDetail.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The link and its sessions. |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | The token holds none of the scopes the route accepts, or it is an enrollment link session&#39;s token, which no scope lets manage links. A missing scope answers without a &#x60;code&#x60;. A link session token that holds an accepted scope answers with &#x60;code&#x60; &#x60;LINK_SESSION_FORBIDDEN&#x60;.  |  -  |
| **404** | No link with this id belongs to your account. Another account&#39;s link answers the same.  |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="listEnrollmentLinks"></a>
# **listEnrollmentLinks**
> EnrollmentLinkList listEnrollmentLinks(subaccountId, state, limit, cursor)

List enrollment links

Lists the enrollment links of one of your subaccounts, newest first, without their &#x60;url&#x60;.  A page holds at most &#x60;limit&#x60; links and can hold fewer, even none, while more remain. Keep following &#x60;_links.next&#x60; until a response has no &#x60;_links.next&#x60;. Use its &#x60;href&#x60; as it is: it keeps your &#x60;subaccountId&#x60;, &#x60;state&#x60; and &#x60;limit&#x60;.  A subaccount that has no links, or that is not one of yours, gives an empty list.  Requires &#x60;subaccounts:write&#x60;, so link details such as references and counts never reach a browser SDK token. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.EnrollmentLinksApi;
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
    EnrollmentLinksApi api = new EnrollmentLinksApi(client);
    UUID subaccountId = UUID.fromString("6f1c2a3b-4d5e-4f60-8a7b-9c0d1e2f3a4b");
    String state = null; // optional
    Integer limit = null; // optional
    String cursor = null; // optional
    try {
      EnrollmentLinkList result = api.listEnrollmentLinks(subaccountId, state, limit, cursor);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("listEnrollmentLinks failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subaccountId** | **UUID**| The subaccount whose links to list. | |
| **state** | **String**| Only links in this state. &#x60;active&#x60; and &#x60;expired&#x60; are told apart by &#x60;expiresAt&#x60;. | [optional] [enum: active, used, revoked, expired] |
| **limit** | **Integer**| The most links a page holds. | [optional] [default to 50] |
| **cursor** | **String**| Where the page starts, as set in &#x60;_links.next&#x60;. A cursor from another subaccount&#39;s listing, or one that does not decode, answers &#x60;400&#x60; &#x60;INVALID_CURSOR&#x60;.  | [optional] |

### Return type

[**EnrollmentLinkList**](EnrollmentLinkList.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A page of links. |  -  |
| **400** | A query parameter is not valid, and &#x60;errors&#x60; names it. Or the cursor is not one this listing gave out, with &#x60;code&#x60; &#x60;INVALID_CURSOR&#x60;.  |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | The token holds none of the scopes the route accepts, or it is an enrollment link session&#39;s token, which no scope lets manage links. A missing scope answers without a &#x60;code&#x60;. A link session token that holds an accepted scope answers with &#x60;code&#x60; &#x60;LINK_SESSION_FORBIDDEN&#x60;.  |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="revokeEnrollmentLink"></a>
# **revokeEnrollmentLink**
> EnrollmentLink revokeEnrollmentLink(linkId)

Revoke enrollment link

Revokes one of your enrollment links. No body. An active link, an expired one included, becomes &#x60;revoked&#x60;: no session starts from it again, and its open sessions fail their next token refresh, so their access ends within one access token lifetime (at most 300 seconds). A link that is already used or revoked is returned as it is.  Requires &#x60;subaccounts:write&#x60;. A link that is not one of yours answers &#x60;404&#x60; with &#x60;code&#x60; &#x60;ENROLLMENT_LINK_NOT_FOUND&#x60;. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.EnrollmentLinksApi;
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
    EnrollmentLinksApi api = new EnrollmentLinksApi(client);
    UUID linkId = UUID.fromString("1d0a6c52-8f3e-4b7a-9c21-5e4d3b2a1f09");
    try {
      EnrollmentLink result = api.revokeEnrollmentLink(linkId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("revokeEnrollmentLink failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **linkId** | **UUID**| The enrollment link&#39;s identifier, the &#x60;id&#x60; returned when the link was created. | |

### Return type

[**EnrollmentLink**](EnrollmentLink.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The link as it is now. |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | The token holds none of the scopes the route accepts, or it is an enrollment link session&#39;s token, which no scope lets manage links. A missing scope answers without a &#x60;code&#x60;. A link session token that holds an accepted scope answers with &#x60;code&#x60; &#x60;LINK_SESSION_FORBIDDEN&#x60;.  |  -  |
| **404** | No link with this id belongs to your account. Another account&#39;s link answers the same.  |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

