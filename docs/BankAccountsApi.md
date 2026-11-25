# BankAccountsApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**backfillBankAccount**](BankAccountsApi.md#backfillBankAccount) | **POST** /bank-accounts/{bankAccountId}/backfill | **Needs banking:admin.** Backfill bank account history |
| [**deleteBankAccount**](BankAccountsApi.md#deleteBankAccount) | **DELETE** /bank-accounts/{bankAccountId} | **Needs banking:admin.** Delete bank account |
| [**getBankAccount**](BankAccountsApi.md#getBankAccount) | **GET** /bank-accounts/{bankAccountId} | Retrieve bank account |
| [**linkEnrolledCards**](BankAccountsApi.md#linkEnrolledCards) | **POST** /bank-accounts/link-enrolled-cards | Link enrolled cards to bank accounts (admin backfill) |
| [**listBankAccounts**](BankAccountsApi.md#listBankAccounts) | **GET** /bank-accounts | List bank accounts |
| [**refreshBankAccount**](BankAccountsApi.md#refreshBankAccount) | **POST** /bank-accounts/{bankAccountId}/refresh | Refresh bank account |
| [**refreshBankAccountTransactions**](BankAccountsApi.md#refreshBankAccountTransactions) | **POST** /bank-accounts/{bankAccountId}/transactions/refresh | **Needs banking:admin.** Refresh transactions |
| [**runBankAccountMaintenance**](BankAccountsApi.md#runBankAccountMaintenance) | **POST** /bank-accounts/maintenance | **Needs banking:admin.** Bank account maintenance for a subaccount |
| [**updateBankAccount**](BankAccountsApi.md#updateBankAccount) | **PATCH** /bank-accounts/{bankAccountId} | Update bank account |


<a id="backfillBankAccount"></a>
# **backfillBankAccount** (needs banking:admin)
> BankAccountBackfillResult backfillBankAccount(bankAccountId, bankAccountBackfillParams)

**Needs banking:admin.** If your API client does not have the `banking:admin` scope, the API answers 403 and the SDK throws `ScopeRequiredException`; ask Astrada to enable it if you need this operation. See [Operations that need banking:admin](../README.md#operations-that-need-bankingadmin).

Backfill bank account history

Needs the &#x60;banking:admin&#x60; scope. If your API client does not have it, the API answers &#x60;403 Forbidden&#x60;; ask Astrada to enable it if you need this operation.  Imports historical bank transactions for this account from the provider over a date range, then runs matching against the account&#39;s full current card set. Unlike the &#x60;backfill&#x60; object on &#x60;PATCH /bank-accounts/{bankAccountId}&#x60; (which imports history for cards being added in that same request), this endpoint recovers a date window without touching card links. Each imported bank transaction sends &#x60;banktransaction.created&#x60; and each new match &#x60;transaction.match.created&#x60;.  Requires a backing connection. Starting a backfill while another sync already holds the account&#39;s connection returns &#x60;409 Conflict&#x60;; retry shortly.  For Mastercard accounts at US banks (&#x60;provider: mastercard&#x60;, &#x60;countryCode: USA&#x60;), &#x60;loadHistoric: true&#x60; additionally asks the provider to generate deep history (up to 2 years) for this account before the import runs. This is a billable provider operation and generation is asynchronous on the provider side: if the immediate import returns few rows, re-run the same window without the flag once generation completes (typically within minutes). Providers without a deep-history operation reject the flag with &#x60;400 Bad Request&#x60;. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankAccountsApi;
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
    BankAccountsApi api = new BankAccountsApi(client);
    UUID bankAccountId = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    BankAccountBackfillParams bankAccountBackfillParams = new BankAccountBackfillParams();
    try {
      BankAccountBackfillResult result = api.backfillBankAccount(bankAccountId, bankAccountBackfillParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("backfillBankAccount failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **bankAccountId** | **UUID**|  | |
| **bankAccountBackfillParams** | [**BankAccountBackfillParams**](BankAccountBackfillParams.md)| Date range to import. Both bounds are optional; send an empty object for the default window. | |

### Return type

[**BankAccountBackfillResult**](BankAccountBackfillResult.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Backfill completed |  -  |
| **400** | The request was refused. &#x60;message&#x60;, and &#x60;code&#x60; where present, say why. |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Needs the &#x60;banking:admin&#x60; scope. Your API client does not have it; ask Astrada to enable it if you need this operation.  |  -  |
| **404** | The resource was not found for this account. |  -  |
| **409** | The resource is not in a state that allows this request. |  -  |
| **500** | The service failed while handling the request. |  -  |

<a id="deleteBankAccount"></a>
# **deleteBankAccount** (needs banking:admin)
> deleteBankAccount(bankAccountId)

**Needs banking:admin.** If your API client does not have the `banking:admin` scope, the API answers 403 and the SDK throws `ScopeRequiredException`; ask Astrada to enable it if you need this operation. See [Operations that need banking:admin](../README.md#operations-that-need-bankingadmin).

Delete bank account

Needs the &#x60;banking:admin&#x60; scope. If your API client does not have it, the API answers &#x60;403 Forbidden&#x60;; ask Astrada to enable it if you need this operation.  Deletes a bank account and all associated bank transactions, transaction matches, and card links.  Cards are not deleted. A &#x60;bank-feed&#x60; subscription created from this account is deactivated; a card enrolled by its number only loses its bank account. The account sends &#x60;bankaccount.state_changed&#x60; with reason &#x60;deleted&#x60;.  **Note:** This action is irreversible. Deleting the last account of a bank connection also removes the connection at the bank. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankAccountsApi;
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
    BankAccountsApi api = new BankAccountsApi(client);
    UUID bankAccountId = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    try {
      api.deleteBankAccount(bankAccountId);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("deleteBankAccount failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **bankAccountId** | **UUID**|  | |

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
| **204** | Bank account deleted |  -  |
| **400** | The request was refused. &#x60;message&#x60;, and &#x60;code&#x60; where present, say why. |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Needs the &#x60;banking:admin&#x60; scope. Your API client does not have it; ask Astrada to enable it if you need this operation.  |  -  |
| **404** | The resource was not found for this account. |  -  |
| **500** | The service failed while handling the request. |  -  |

<a id="getBankAccount"></a>
# **getBankAccount**
> BankAccount getBankAccount(bankAccountId)

Retrieve bank account

Returns detailed information about a specific bank account by its identifier.

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankAccountsApi;
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
    BankAccountsApi api = new BankAccountsApi(client);
    UUID bankAccountId = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    try {
      BankAccount result = api.getBankAccount(bankAccountId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getBankAccount failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **bankAccountId** | **UUID**|  | |

### Return type

[**BankAccount**](BankAccount.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successful operation |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | The token does not hold the &#x60;banking:read&#x60; scope this route needs. |  -  |
| **404** | The resource was not found for this account. |  -  |
| **500** | The service failed while handling the request. |  -  |

<a id="linkEnrolledCards"></a>
# **linkEnrolledCards**
> BankAccountLinkCardsResult linkEnrolledCards(bankAccountLinkCardsParams)

Link enrolled cards to bank accounts (admin backfill)

Admin backfill/retry for immediate mask-linking: matches the subaccount&#39;s ENROLLED cards against its credit bank accounts by mask (an enrolled card&#39;s last 4 digits) and links each exact match. The same exactly-one-or-skip rules as completion-time linking apply: a mask matching multiple cards, or multiple accounts claiming a mask, links nothing (the scheduled mask sweep retries this every few hours). Idempotent: already-linked pairs are skipped. Intended for onboarding backfills (cards enrolled before the bank link) and recovery after a transient directory failure at completion. Emits no webhook; linked cardIds appear on subsequent bank-account reads.  The body takes &#x60;subaccountId&#x60; only; any other field is a &#x60;400&#x60;. The admin actions (minting cards for cardless accounts, the cardholder backfill) are on &#x60;POST /bank-accounts/maintenance&#x60; at &#x60;banking:admin&#x60;. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankAccountsApi;
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
    BankAccountsApi api = new BankAccountsApi(client);
    BankAccountLinkCardsParams bankAccountLinkCardsParams = new BankAccountLinkCardsParams();
    try {
      BankAccountLinkCardsResult result = api.linkEnrolledCards(bankAccountLinkCardsParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("linkEnrolledCards failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **bankAccountLinkCardsParams** | [**BankAccountLinkCardsParams**](BankAccountLinkCardsParams.md)|  | |

### Return type

[**BankAccountLinkCardsResult**](BankAccountLinkCardsResult.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Backfill executed. Maps each bank account that gained links to the card ids linked in this run (empty object when nothing matched). |  -  |
| **400** | The request was refused. &#x60;message&#x60;, and &#x60;code&#x60; where present, say why. |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | The token does not hold the &#x60;banking:write&#x60; scope this route needs. |  -  |
| **500** | The service failed while handling the request. |  -  |

<a id="listBankAccounts"></a>
# **listBankAccounts**
> BankAccountList listBankAccounts(subaccountId, state, cardId, limit, cursor)

List bank accounts

Returns a list of bank accounts under the account. Bank accounts are created automatically when a bank link enrollment is completed.  By default, &#x60;GET /bank-accounts&#x60; returns bank accounts ordered by the latest &#x60;createdAt&#x60;.  This endpoint supports cursor-based pagination. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankAccountsApi;
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
    BankAccountsApi api = new BankAccountsApi(client);
    UUID subaccountId = null; // optional
    String state = null; // optional
    UUID cardId = null; // optional
    Integer limit = null; // optional
    String cursor = null; // optional
    try {
      BankAccountList result = api.listBankAccounts(subaccountId, state, cardId, limit, cursor);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("listBankAccounts failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subaccountId** | **UUID**|  | [optional] |
| **state** | **String**| Filter bank accounts by state | [optional] [enum: active, auth_required, disconnected] |
| **cardId** | **UUID**|  | [optional] |
| **limit** | **Integer**| Maximum number of items to return | [optional] [default to 250] |
| **cursor** | **String**| The cursor to use for pagination. Identifies your place on the list | [optional] |

### Return type

[**BankAccountList**](BankAccountList.md)

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

<a id="refreshBankAccount"></a>
# **refreshBankAccount**
> BankAccountRefreshResult refreshBankAccount(bankAccountId)

Refresh bank account

Creates a new hosted link URL for the user to re-authenticate an account in &#x60;auth_required&#x60;. Any other state answers &#x60;400&#x60;. To ask the bank for new transactions, use &#x60;POST /bank-accounts/{bankAccountId}/transactions/refresh&#x60;. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankAccountsApi;
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
    BankAccountsApi api = new BankAccountsApi(client);
    UUID bankAccountId = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    try {
      BankAccountRefreshResult result = api.refreshBankAccount(bankAccountId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("refreshBankAccount failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **bankAccountId** | **UUID**|  | |

### Return type

[**BankAccountRefreshResult**](BankAccountRefreshResult.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Refresh triggered |  -  |
| **400** | The request was refused. &#x60;message&#x60;, and &#x60;code&#x60; where present, say why. |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | The token does not hold the &#x60;banking:write&#x60; scope this route needs. |  -  |
| **404** | The resource was not found for this account. |  -  |
| **500** | The service failed while handling the request. |  -  |

<a id="refreshBankAccountTransactions"></a>
# **refreshBankAccountTransactions** (needs banking:admin)
> BankAccountTransactionsRefreshResult refreshBankAccountTransactions(bankAccountId)

**Needs banking:admin.** If your API client does not have the `banking:admin` scope, the API answers 403 and the SDK throws `ScopeRequiredException`; ask Astrada to enable it if you need this operation. See [Operations that need banking:admin](../README.md#operations-that-need-bankingadmin).

Refresh transactions

Needs the &#x60;banking:admin&#x60; scope. If your API client does not have it, the API answers &#x60;403 Forbidden&#x60;; ask Astrada to enable it if you need this operation.  Asks the data provider to check this account&#39;s institution **now**, instead of waiting for its own schedule (typically one to four checks a day). The call returns once the check has run, usually within seconds but occasionally 30 seconds or more. It imports nothing itself: anything the check finds arrives through the normal update path within moments.  **Billable per successful call.** Use it at moments that matter, not on a timer. Returns &#x60;400 Bad Request&#x60; when the account has no backing connection, or when its provider or institution doesn&#39;t support an on-demand refresh (e.g. some card issuers), and &#x60;502 Bad Gateway&#x60; when the provider&#39;s check fails. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankAccountsApi;
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
    BankAccountsApi api = new BankAccountsApi(client);
    UUID bankAccountId = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    try {
      BankAccountTransactionsRefreshResult result = api.refreshBankAccountTransactions(bankAccountId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("refreshBankAccountTransactions failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **bankAccountId** | **UUID**|  | |

### Return type

[**BankAccountTransactionsRefreshResult**](BankAccountTransactionsRefreshResult.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | The provider has run its check of the institution. &#x60;durationMs&#x60; is how long that check took, not an import; any changes it found arrive through the normal update path. |  -  |
| **400** | The request was refused. &#x60;message&#x60;, and &#x60;code&#x60; where present, say why. |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Needs the &#x60;banking:admin&#x60; scope. Your API client does not have it; ask Astrada to enable it if you need this operation.  |  -  |
| **404** | The resource was not found for this account. |  -  |
| **500** | The service failed while handling the request. |  -  |
| **502** | The bank data provider failed to complete the request. Try again later. |  -  |

<a id="runBankAccountMaintenance"></a>
# **runBankAccountMaintenance** (needs banking:admin)
> BankAccountMaintenanceResult runBankAccountMaintenance(bankAccountMaintenanceParams)

**Needs banking:admin.** If your API client does not have the `banking:admin` scope, the API answers 403 and the SDK throws `ScopeRequiredException`; ask Astrada to enable it if you need this operation. See [Operations that need banking:admin](../README.md#operations-that-need-bankingadmin).

Bank account maintenance for a subaccount

Needs the &#x60;banking:admin&#x60; scope. If your API client does not have it, the API answers &#x60;403 Forbidden&#x60;; ask Astrada to enable it if you need this operation.  Admin maintenance over one subaccount. It first runs the same mask pass as &#x60;POST /bank-accounts/link-enrolled-cards&#x60;, then each action the request asks for. The actions create cards on the cards platform or rewrite stored rows, so this route needs &#x60;banking:admin&#x60;, while the mask pass alone stays on &#x60;link-enrolled-cards&#x60; at &#x60;banking:write&#x60;. The caller&#39;s account comes from its token: it can run maintenance on any subaccount of its own account, and a subaccount of another account simply matches nothing (an empty result, not a &#x60;403&#x60;).  Three optional actions, each off by default:  - &#x60;mint: true&#x60; runs the completion-time mint pass over the subaccount&#39;s credit bank accounts that a completed link left without a card (the same-last-4 American Express case), so each gains a bank-feed card. Idempotent: an account that already holds a minted card is skipped. - &#x60;backfillJunctionLast4: true&#x60; fills, for the subaccount&#39;s card links, the last 4 each link answers to where it is still unknown, from the cards directory. Idempotent. - &#x60;createCardholderCards: true&#x60; runs, now and for this subaccount only, what a scheduled job does every 30 minutes: it gives each cardholder behind a pooled American Express billing account (a supplementary card that is not its own Plaid account) a bank-feed card of their own, with the cardholder&#39;s name, so their transactions can be emitted on that card. It does nothing unless Astrada has switched the feature on for the subaccount; the switches are set by Astrada only, from inside the platform, and no request can set them. Idempotent: a cardholder who already has a card is never given another.  The actions run in the order above, after the mask pass. If the cards directory cannot be read, the mask pass links nothing and still returns &#x60;201&#x60;; each action then reads the directory again on its own and says so in its result (&#x60;directoryUnavailable&#x60;), or, for &#x60;mint&#x60; with accounts to screen, fails rather than mint unscreened. Any step that fails returns &#x60;400&#x60; and runs nothing after it (steps before it have done their work, and every step is idempotent, so the request can simply be repeated). Emits no webhook from banking; a card created here produces the cards platform&#39;s usual card-subscription events. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankAccountsApi;
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
    BankAccountsApi api = new BankAccountsApi(client);
    BankAccountMaintenanceParams bankAccountMaintenanceParams = new BankAccountMaintenanceParams();
    try {
      BankAccountMaintenanceResult result = api.runBankAccountMaintenance(bankAccountMaintenanceParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("runBankAccountMaintenance failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **bankAccountMaintenanceParams** | [**BankAccountMaintenanceParams**](BankAccountMaintenanceParams.md)|  | |

### Return type

[**BankAccountMaintenanceResult**](BankAccountMaintenanceResult.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Maintenance executed. Maps each bank account that gained links to the card ids linked in this run (empty object when nothing matched). The other fields appear only for the actions the request asked for. |  -  |
| **400** | The request was refused. &#x60;message&#x60;, and &#x60;code&#x60; where present, say why. |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Needs the &#x60;banking:admin&#x60; scope. Your API client does not have it; ask Astrada to enable it if you need this operation.  |  -  |
| **500** | The service failed while handling the request. |  -  |

<a id="updateBankAccount"></a>
# **updateBankAccount**
> BankAccountUpdateResult updateBankAccount(bankAccountId, bankAccountUpdateParams)

Update bank account

Updates a bank account. Card links are detected automatically: a scheduled sweep re-checks each cardless credit account every few hours and links an enrolled card whose last 4 digits match the account&#39;s mask, without any API call. This endpoint is the manual override: use it to pre-link a card immediately, correct an assignment, or import history.  Each card in &#x60;cardIds&#x60; must belong to the same subaccount as the bank account. A card can only be linked to one bank account at a time; attempting to link a card that is already associated with another bank account will return a &#x60;409 Conflict&#x60; error.  A card that cannot take a bank-account link is also rejected with &#x60;409 Conflict&#x60; before any change is applied: one whose only enrollment is a bulk feed, or whose subscriptions are all in a dead, non-attachable state. The &#x60;message&#x60; names the card and says whether it is bulk-enrolled or has no live subscription. This body, like the \&quot;already linked\&quot; &#x60;409&#x60;, has no &#x60;code&#x60;. If the cards directory cannot be consulted, this screen is skipped (fail-open) rather than blocking the update.  &#x60;cardIds&#x60; is a full replacement. Send &#x60;cardIds: []&#x60; to clear explicit links (the scheduled mask sweep can re-link a matching card on its next run).  Cards Astrada created for the individual cardholders of a pooled American Express billing account are managed by the platform: a &#x60;cardIds&#x60; list without them does not remove them (they leave when the card is deactivated or the account is disconnected or deleted). An enrolled card that was linked for one such cardholder can be removed here, and stays removed: it is not linked again automatically.  Optionally include a &#x60;backfill&#x60; object with &#x60;startDate&#x60; and/or &#x60;endDate&#x60; to trigger a backfill of historical bank transactions for the specified date range. An automatically detected link re-matches the account&#39;s already-synced unmatched transactions around the new card&#39;s activity on its own; IMPORTING bank history from before the link (pulling older transactions from the provider) still happens only through this endpoint&#39;s &#x60;backfill&#x60;.  The current card set is always readable at &#x60;GET /bank-accounts/{bankAccountId}&#x60; (&#x60;cardIds&#x60;), and &#x60;GET /bank-accounts?cardId&#x3D;&#x60; finds the account a card is linked to. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BankAccountsApi;
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
    BankAccountsApi api = new BankAccountsApi(client);
    UUID bankAccountId = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    BankAccountUpdateParams bankAccountUpdateParams = new BankAccountUpdateParams();
    try {
      BankAccountUpdateResult result = api.updateBankAccount(bankAccountId, bankAccountUpdateParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("updateBankAccount failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **bankAccountId** | **UUID**|  | |
| **bankAccountUpdateParams** | [**BankAccountUpdateParams**](BankAccountUpdateParams.md)| Bank account update request | |

### Return type

[**BankAccountUpdateResult**](BankAccountUpdateResult.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Bank account updated |  -  |
| **400** | The request was refused. &#x60;message&#x60;, and &#x60;code&#x60; where present, say why. |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | The token does not hold the &#x60;banking:write&#x60; scope this route needs. |  -  |
| **404** | The resource was not found for this account. |  -  |
| **409** | The resource is not in a state that allows this request. |  -  |
| **500** | The service failed while handling the request. |  -  |

