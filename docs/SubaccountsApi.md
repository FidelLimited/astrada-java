# SubaccountsApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createSubaccount**](SubaccountsApi.md#createSubaccount) | **POST** /subaccounts | Create subaccount |
| [**getSubaccount**](SubaccountsApi.md#getSubaccount) | **GET** /subaccounts/{subaccountId} | Retrieve subaccount |
| [**listSubaccounts**](SubaccountsApi.md#listSubaccounts) | **GET** /subaccounts | List subaccounts |
| [**updateSubaccount**](SubaccountsApi.md#updateSubaccount) | **PATCH** /subaccounts/{subaccountId} | Update subaccount configurations |


<a id="createSubaccount"></a>
# **createSubaccount**
> Subaccount createSubaccount(subaccountCreateParams)

Create subaccount

Creates a new subaccount, allowing you to set network and regional configurations.  Each network&#39;s &#x60;countries&#x60; can be set to either an array of specific ISO 3166 alpha-3 country codes, or the string &#x60;\&quot;*\&quot;&#x60; to inherit all countries from the parent account&#39;s capabilities for that network.  When using &#x60;\&quot;*\&quot;&#x60;, the subaccount will automatically reflect the parent account&#39;s current country list. If the parent account&#39;s capabilities change, subaccounts configured with &#x60;\&quot;*\&quot;&#x60; will inherit the updated countries.  The single and bulk enrollment configurations are optional. Countries are set per network: each one&#39;s country list is the network&#39;s &#x60;countries&#x60;, limited to the countries the account allows for that way of enrolling. The supported card types for single and bulk enrollment is CONSUMER, while the supported funding card types is DEBIT. If not provided, the block rules for both card types will default to the parent account&#39;s default block rule for that network and enrollment level. A list you send is combined with the account&#39;s defaults, so it can add blocks but never remove one of the account&#39;s defaults. Visa consumer debit and prepaid cards are blocked at the network level regardless of these settings; see &#x60;networkBlockedCards&#x60; on the subaccount.  To set the default rule on the parent account, please reach out to your account manager or support. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.SubaccountsApi;
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
    SubaccountsApi api = new SubaccountsApi(client);
    SubaccountCreateParams subaccountCreateParams = null; // optional
    try {
      Subaccount result = api.createSubaccount(subaccountCreateParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("createSubaccount failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subaccountCreateParams** | [**SubaccountCreateParams**](SubaccountCreateParams.md)|  | [optional] |

### Return type

[**Subaccount**](Subaccount.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | New subaccount created successfully. The body is the same effective read model &#x60;GET /subaccounts/{subaccountId}&#x60; returns (&#x60;enrollmentPolicy&#x60;, &#x60;bankLinkingPolicy&#x60;, &#x60;plaidEndCustomer&#x60; and &#x60;enforceIssuerCountry&#x60; resolved against the parent account, and &#x60;networkBlockedCards&#x60; resolved from the platform network configuration), not the raw create-time snapshot. Account-level and platform-level settings therefore appear here even though the new subaccount stores no value for them.  |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="getSubaccount"></a>
# **getSubaccount**
> Subaccount getSubaccount(subaccountId)

Retrieve subaccount

Returns detailed information about a specific subaccount by its identifier, including configuration details and associated data.  &#x60;_links.team-link&#x60; appears when enrollment links are enabled for the account, the token holds &#x60;card-subscriptions:write&#x60; or &#x60;subaccounts:write&#x60;, and the subaccount&#39;s card enrollment is on (its &#x60;enrollmentPolicy.cardEnrollment&#x60; is not &#x60;false&#x60;). It points to &#x60;POST /subaccounts/{subaccountId}/team-link&#x60;, which creates a team link for the subaccount. An enrollment link session&#39;s token never gets it. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.SubaccountsApi;
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
    SubaccountsApi api = new SubaccountsApi(client);
    UUID subaccountId = UUID.fromString("24c4f90d-ab3b-4f6e-8a1d-d251a4fcf34c");
    try {
      Subaccount result = api.getSubaccount(subaccountId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getSubaccount failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subaccountId** | **UUID**|  | |

### Return type

[**Subaccount**](Subaccount.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Details of the subaccount giving the accountId and subaccountId |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="listSubaccounts"></a>
# **listSubaccounts**
> SubaccountList listSubaccounts(cursor, limit)

List subaccounts

Returns a list of all subaccounts associated with the account.  This endpoint supports [cursor-based pagination](https://docs.astrada.co/reference/pagination). 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.SubaccountsApi;
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
    SubaccountsApi api = new SubaccountsApi(client);
    String cursor = null; // optional
    Integer limit = null; // optional
    try {
      SubaccountList result = api.listSubaccounts(cursor, limit);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("listSubaccounts failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **cursor** | **String**| The cursor to use for pagination. Identifies your place on the list | [optional] |
| **limit** | **Integer**| Maximum number of items to return | [optional] [default to 25] |

### Return type

[**SubaccountList**](SubaccountList.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Subaccounts list retrieved successfully |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="updateSubaccount"></a>
# **updateSubaccount**
> SubaccountUpdateResult updateSubaccount(subaccountId, subaccountUpdateParams)

Update subaccount configurations

Update a subaccount name and configurations. All updated configurations are validated against the capabilities of the main account to ensure compliance. It is possible to add new networks and/or regions but it is not possible to remove any configurations that were previously set. &#x60;configurations&#x60; replaces the whole object: send every network the subaccount has (a network left out is refused with &#x60;Delete a configuration is not supported.&#x60;), and every block list you want to keep (a block list left out goes back to the account&#39;s default).  Each network&#39;s &#x60;countries&#x60; can be set to either an array of specific ISO 3166 alpha-3 country codes, or the string &#x60;\&quot;*\&quot;&#x60; to inherit all countries from the parent account&#39;s capabilities for that network.  When using &#x60;\&quot;*\&quot;&#x60;, the subaccount will automatically reflect the parent account&#39;s current country list. If the parent account&#39;s capabilities change, subaccounts configured with &#x60;\&quot;*\&quot;&#x60; will inherit the updated countries.  The single and bulk enrollment configurations are optional. Countries are set per network: each one&#39;s country list is the network&#39;s &#x60;countries&#x60;, limited to the countries the account allows for that way of enrolling. The supported card types for single and bulk enrollment is CONSUMER, while the supported funding card types is DEBIT. If not provided, the block rules for both card types will default to the parent account&#39;s default block rule for that network and enrollment level. A list you send is combined with the account&#39;s defaults, so it can add blocks but never remove one of the account&#39;s defaults. Visa consumer debit and prepaid cards are blocked at the network level regardless of these settings; see &#x60;networkBlockedCards&#x60; on the subaccount.  To set the default rule on the parent account, please reach out to your account manager or support. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.SubaccountsApi;
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
    SubaccountsApi api = new SubaccountsApi(client);
    UUID subaccountId = UUID.fromString("24c4f90d-ab3b-4f6e-8a1d-d251a4fcf34c");
    SubaccountUpdateParams subaccountUpdateParams = new SubaccountUpdateParams();
    try {
      SubaccountUpdateResult result = api.updateSubaccount(subaccountId, subaccountUpdateParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("updateSubaccount failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subaccountId** | **UUID**|  | |
| **subaccountUpdateParams** | [**SubaccountUpdateParams**](SubaccountUpdateParams.md)|  | |

### Return type

[**SubaccountUpdateResult**](SubaccountUpdateResult.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Specified subaccount updated successfully. The body is the same effective read model &#x60;GET /subaccounts/{subaccountId}&#x60; returns (&#x60;enrollmentPolicy&#x60;, &#x60;bankLinkingPolicy&#x60;, &#x60;plaidEndCustomer&#x60; and &#x60;enforceIssuerCountry&#x60; resolved against the parent account, and &#x60;networkBlockedCards&#x60; resolved from the platform network configuration), so it may include account-level and platform-level settings that were not part of the request and that the subaccount stores no value for.  When the request&#39;s &#x60;enrollmentPolicy&#x60; holds keys this endpoint does not accept, the body also carries &#x60;ignoredFields&#x60;, which names each of them. Unknown keys outside &#x60;enrollmentPolicy&#x60; are ignored and not listed.  |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

