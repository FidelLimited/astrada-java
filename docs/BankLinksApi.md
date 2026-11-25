# BankLinksApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createBankLink**](BankLinksApi.md#createBankLink) | **POST** /bank-links | Create bank link |
| [**deleteBankLink**](BankLinksApi.md#deleteBankLink) | **DELETE** /bank-links/{bankLinkId} | **Needs banking:admin.** Delete bank link |
| [**getBankLink**](BankLinksApi.md#getBankLink) | **GET** /bank-links/{bankLinkId} | Retrieve bank link |
| [**listBankLinks**](BankLinksApi.md#listBankLinks) | **GET** /bank-links | List bank links |
| [**syncBankLink**](BankLinksApi.md#syncBankLink) | **POST** /bank-links/{bankLinkId}/sync | **Needs banking:admin.** Sync bank link |
| [**updateBankLink**](BankLinksApi.md#updateBankLink) | **PATCH** /bank-links/{bankLinkId} | Complete bank link |


<a id="createBankLink"></a>
# **createBankLink**
> BankLink createBankLink(bankLinkCreateParams)

Create bank link

Creates a new bank link to initiate a bank enrollment. The response includes a &#x60;hostedLink&#x60; URL where the user can connect their bank account via Plaid.  The hosted link expires after 4 hours. If the link expires before the user completes enrollment, retrieve the bank link via &#x60;GET /bank-links/{bankLinkId}&#x60; to get a refreshed URL.  Creating a bank link with an ID that already exists for the account will return a &#x60;409 Conflict&#x60; error, unless the existing link has expired.  A refused create returns a JSON body with &#x60;statusCode&#x60; and &#x60;message&#x60;. These errors also carry a &#x60;code&#x60;, a stable name to branch on so that you never match on &#x60;message&#x60; text (the &#x60;409&#x60; above has no &#x60;code&#x60;):  - &#x60;404&#x60; &#x60;SUBACCOUNT_NOT_FOUND&#x60;: &#x60;subaccountId&#x60; is not one of the account&#39;s subaccounts. Nothing is created. The message is &#x60;Subaccount not found for this account&#x60;. - &#x60;403&#x60; &#x60;BANK_ENROLLMENT_DISABLED&#x60;: a create is refused when the account has locked bank enrollment (&#x60;enrollmentPolicy.bankEnrollmentLocked&#x60; is &#x60;true&#x60; on the subaccount) and the subaccount&#39;s &#x60;enrollmentPolicy.bankEnrollment&#x60; value does not allow a bank link for this card. Otherwise the check does not refuse. Nothing is created. The message is &#x60;Bank linking is not enabled for this card on this account&#x60;. The body also has a &#x60;reason&#x60;: &#x60;bank-enrollment-off&#x60; when the setting is &#x60;off&#x60;, &#x60;network-not-allowed&#x60; when it does not allow the card&#39;s network, and &#x60;network-unknown&#x60; when it limits the networks and the request does not show the card&#39;s network. The card&#39;s network comes from the request: American Express when the link uses the &#x60;amex_only&#x60; customization, otherwise &#x60;bin6&#x60; (a first digit of &#x60;4&#x60; is Visa, &#x60;2&#x60; or &#x60;5&#x60; is Mastercard, and any other digit, a &#x60;3&#x60; included, is unknown). Without &#x60;bin6&#x60; it is unknown. - &#x60;403&#x60; &#x60;MASTERCARD_CLIENT_NOT_PROVISIONED&#x60; and &#x60;PLAID_EC_NOT_PROVISIONED&#x60;: the account is not set up for bank linking on that provider yet. The messages are &#x60;This account is not set up for Mastercard bank linking yet&#x60; and &#x60;This account is not set up for Plaid bank linking yet&#x60;. - &#x60;403&#x60; &#x60;PLAID_CUSTOMIZATION_NOT_PROVISIONED&#x60;: Plaid refused the Link customization this link asked for, because the account&#39;s Plaid client cannot use it, and no fallback applied. Nothing is created. The message is &#x60;This account is not fully set up for Plaid bank linking yet&#x60;. A customization other than &#x60;amex_only&#x60; (one set by the bank-linking policy&#39;s &#x60;plaidLinkCustomizationName&#x60;) answers this at once. When Plaid refuses &#x60;amex_only&#x60;, from &#x60;filters.linkCustomizationName&#x60; or from the policy, the create is retried once with the environment&#39;s pinned customization in its place, or with no customization when the environment pins none. If Plaid accepts the retry, the link is created as usual: its hosted session opens without the &#x60;amex_only&#x60; customization, and its completion is still held to American Express institutions. For &#x60;amex_only&#x60;, only a refused retry answers this error. Astrada fixes it in the account&#39;s Plaid setup, so sending the same request again does not help. - &#x60;400&#x60; &#x60;INVALID_LINK_FILTERS&#x60;: &#x60;filters&#x60; asks for more than the account&#39;s bank-linking policy allows, or for a customization that is not allowed. - &#x60;400&#x60; &#x60;PROVIDER_NOT_ENABLED_FOR_ACCOUNT&#x60;: &#x60;provider&#x60; is not enabled for the account. - &#x60;400&#x60; &#x60;PROVIDER_NOT_SUPPORTED_FOR_COUNTRY&#x60;: no provider supports &#x60;countryCode&#x60; for the chosen &#x60;provider&#x60;. - &#x60;503&#x60; &#x60;BANK_FEED_OWNER_NOT_CONFIGURED&#x60;: &#x60;persona: cardholder&#x60; in an environment where cardholder links are not set up yet. Nothing is created; a link without &#x60;persona: cardholder&#x60; still works. The message is &#x60;Cardholder bank linking is not available yet&#x60;.  A body that breaks the field formats (for example an empty &#x60;accountTypes&#x60; or a lower-case &#x60;countryCode&#x60;) answers &#x60;400&#x60; with message &#x60;Bad Request&#x60; and an &#x60;errors&#x60; list naming each field, and no &#x60;code&#x60;. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankLinksApi;
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
    BankLinksApi api = new BankLinksApi(client);
    BankLinkCreateParams bankLinkCreateParams = new BankLinkCreateParams();
    try {
      BankLink result = api.createBankLink(bankLinkCreateParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("createBankLink failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **bankLinkCreateParams** | [**BankLinkCreateParams**](BankLinkCreateParams.md)| Bank link creation request | |

### Return type

[**BankLink**](BankLink.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Bank link created |  -  |
| **400** | The request was refused. &#x60;message&#x60;, and &#x60;code&#x60; where present, say why. |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | The request was refused for one of two reasons: - The token does not hold the &#x60;banking:write&#x60; scope. The message names the scope, and there is no &#x60;code&#x60;. - The account is not set up for this bank link. The answer carries one of the &#x60;403&#x60; codes in this operation&#39;s description, and its message names no scope.  |  -  |
| **404** | The resource was not found for this account. |  -  |
| **409** | The resource is not in a state that allows this request. |  -  |
| **500** | The service failed while handling the request. |  -  |
| **503** | Cardholder bank links are not set up in this environment yet (&#x60;BANK_FEED_OWNER_NOT_CONFIGURED&#x60;). Nothing was created. |  -  |

<a id="deleteBankLink"></a>
# **deleteBankLink** (needs banking:admin)
> deleteBankLink(bankLinkId)

**Needs banking:admin.** If your API client does not have the `banking:admin` scope, the API answers 403 and the SDK throws `ScopeRequiredException`; ask Astrada to enable it if you need this operation. See [Operations that need banking:admin](../README.md#operations-that-need-bankingadmin).

Delete bank link

Needs the &#x60;banking:admin&#x60; scope. If your API client does not have it, the API answers &#x60;403 Forbidden&#x60;; ask Astrada to enable it if you need this operation.  Deletes a bank link and all associated resources, including the bank subscription, bank accounts, and bank transactions.  Cards are not deleted. A &#x60;bank-feed&#x60; subscription created at completion is deactivated; a card enrolled by its number only loses its bank account. Each still-active account sends &#x60;bankaccount.state_changed&#x60;, and each changed subscription sends &#x60;cardsubscription.updated&#x60;.  **Note:** This action is irreversible and will revoke access to the connected financial institution. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankLinksApi;
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
    BankLinksApi api = new BankLinksApi(client);
    String bankLinkId = "enrollment_792dcb7d";
    try {
      api.deleteBankLink(bankLinkId);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("deleteBankLink failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **bankLinkId** | **String**| The client-provided identifier of the bank link. | |

### Return type

null (empty response body)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | Bank link deleted |  -  |
| **400** | The request was refused. &#x60;message&#x60;, and &#x60;code&#x60; where present, say why. |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Needs the &#x60;banking:admin&#x60; scope. Your API client does not have it; ask Astrada to enable it if you need this operation.  |  -  |
| **404** | The resource was not found for this account. |  -  |
| **409** | The resource is not in a state that allows this request. |  -  |
| **500** | The service failed while handling the request. |  -  |

<a id="getBankLink"></a>
# **getBankLink**
> BankLink getBankLink(bankLinkId)

Retrieve bank link

Returns detailed information about a specific bank link by its identifier.  If the bank link is in &#x60;pending&#x60; state and the hosted link URL has expired, this endpoint will automatically refresh the URL and return the updated link.  Once the user has finished the hosted flow, a read of the link answers &#x60;completed&#x60;: the link completes without a &#x60;PATCH&#x60; from you. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankLinksApi;
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
    BankLinksApi api = new BankLinksApi(client);
    String bankLinkId = "enrollment_792dcb7d";
    try {
      BankLink result = api.getBankLink(bankLinkId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getBankLink failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **bankLinkId** | **String**| The client-provided identifier of the bank link. | |

### Return type

[**BankLink**](BankLink.md)

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
| **403** | The token does not hold the &#x60;banking:read&#x60; scope this route needs. |  -  |
| **404** | The resource was not found for this account. |  -  |
| **500** | The service failed while handling the request. |  -  |

<a id="listBankLinks"></a>
# **listBankLinks**
> BankLinkList listBankLinks(subaccountId, state, limit, cursor)

List bank links

Returns a list of bank links under the account.  By default, &#x60;GET /bank-links&#x60; returns bank links ordered by the latest &#x60;createdAt&#x60;.  This endpoint supports cursor-based pagination. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankLinksApi;
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
    BankLinksApi api = new BankLinksApi(client);
    UUID subaccountId = null; // optional
    String state = null; // optional
    Integer limit = null; // optional
    String cursor = null; // optional
    try {
      BankLinkList result = api.listBankLinks(subaccountId, state, limit, cursor);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("listBankLinks failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subaccountId** | **UUID**|  | [optional] |
| **state** | **String**| Filter bank links by state | [optional] [enum: pending, completed, expired] |
| **limit** | **Integer**| Maximum number of items to return | [optional] [default to 250] |
| **cursor** | **String**| The cursor to use for pagination. Identifies your place on the list | [optional] |

### Return type

[**BankLinkList**](BankLinkList.md)

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
| **403** | The token does not hold the &#x60;banking:read&#x60; scope this route needs. |  -  |
| **500** | The service failed while handling the request. |  -  |

<a id="syncBankLink"></a>
# **syncBankLink** (needs banking:admin)
> BankLinkSyncResult syncBankLink(bankLinkId, bankLinkSyncParams)

**Needs banking:admin.** If your API client does not have the `banking:admin` scope, the API answers 403 and the SDK throws `ScopeRequiredException`; ask Astrada to enable it if you need this operation. See [Operations that need banking:admin](../README.md#operations-that-need-bankingadmin).

Sync bank link

Needs the &#x60;banking:admin&#x60; scope. If your API client does not have it, the API answers &#x60;403 Forbidden&#x60;; ask Astrada to enable it if you need this operation.  Triggers a manual sync of transactions for the bank link&#39;s associated subscription. This fetches the latest transactions from the financial institution.  Transaction syncs also occur automatically via provider webhooks and scheduled jobs. Use this endpoint to force an immediate sync when needed.  While another sync of the same connection is running, this call answers &#x60;200&#x60; and syncs nothing; send it again shortly. When the bank is temporarily unavailable it answers &#x60;503&#x60;. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankLinksApi;
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
    BankLinksApi api = new BankLinksApi(client);
    String bankLinkId = "enrollment_792dcb7d";
    BankLinkSyncParams bankLinkSyncParams = null; // optional
    try {
      BankLinkSyncResult result = api.syncBankLink(bankLinkId, bankLinkSyncParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("syncBankLink failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **bankLinkId** | **String**| The client-provided identifier of the bank link. | |
| **bankLinkSyncParams** | [**BankLinkSyncParams**](BankLinkSyncParams.md)| Optional sync configuration | [optional] |

### Return type

[**BankLinkSyncResult**](BankLinkSyncResult.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Sync triggered |  -  |
| **400** | The request was refused. &#x60;message&#x60;, and &#x60;code&#x60; where present, say why. |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Needs the &#x60;banking:admin&#x60; scope. Your API client does not have it; ask Astrada to enable it if you need this operation.  |  -  |
| **404** | The resource was not found for this account. |  -  |
| **409** | The resource is not in a state that allows this request. |  -  |
| **500** | The service failed while handling the request. |  -  |
| **503** | The bank is temporarily unavailable. Nothing was synced; try again later. The message comes from the bank data provider and can change, so do not parse it.  |  -  |

<a id="updateBankLink"></a>
# **updateBankLink**
> BankLink updateBankLink(bankLinkId, bankLinkUpdateParams)

Complete bank link

Completes a bank link enrollment, transitioning it from &#x60;pending&#x60; to &#x60;completed&#x60; state. The request body must contain &#x60;state: \&quot;completed\&quot;&#x60;.  When a bank link is completed, the system exchanges the enrollment credentials with the provider, creates a bank subscription (connection), and discovers the associated bank accounts.  A link that is no longer &#x60;pending&#x60; answers &#x60;409&#x60;; read it with &#x60;GET /bank-links/{bankLinkId}&#x60;.  The PATCH response echoes the bank link record only. The discovered &#x60;bankAccounts&#x60; (with each account&#39;s &#x60;cardIds&#x60; and any minted &#x60;cards&#x60;) are returned by &#x60;GET /bank-links/{bankLinkId}&#x60;, not by this PATCH. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankLinksApi;
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
    BankLinksApi api = new BankLinksApi(client);
    String bankLinkId = "enrollment_792dcb7d";
    BankLinkUpdateParams bankLinkUpdateParams = new BankLinkUpdateParams();
    try {
      BankLink result = api.updateBankLink(bankLinkId, bankLinkUpdateParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("updateBankLink failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **bankLinkId** | **String**| The client-provided identifier of the bank link. | |
| **bankLinkUpdateParams** | [**BankLinkUpdateParams**](BankLinkUpdateParams.md)| Bank link completion request | |

### Return type

[**BankLink**](BankLink.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Bank link completed |  -  |
| **400** | The request was refused. &#x60;message&#x60;, and &#x60;code&#x60; where present, say why. |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | The token does not hold the &#x60;banking:write&#x60; scope this route needs. |  -  |
| **404** | The resource was not found for this account. |  -  |
| **409** | The resource is not in a state that allows this request. |  -  |
| **500** | The service failed while handling the request. |  -  |

