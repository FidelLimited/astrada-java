# CardVerificationApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getCardVerification**](CardVerificationApi.md#getCardVerification) | **GET** /card-verifications/3ds/{verificationId} | Retrieve 3DS verification |
| [**listCardVerifications**](CardVerificationApi.md#listCardVerifications) | **GET** /card-verifications/3ds | List a card&#39;s 3DS verifications |
| [**unlockCardVerificationAttempts**](CardVerificationApi.md#unlockCardVerificationAttempts) | **POST** /card-verifications/unlock | Unlock a card&#39;s verification attempt lockout |


<a id="getCardVerification"></a>
# **getCardVerification**
> CardVerification getCardVerification(verificationId)

Retrieve 3DS verification

Returns the current status and detailed information of a specific 3DS verification by its identifier.

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.CardVerificationApi;
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
    CardVerificationApi api = new CardVerificationApi(client);
    UUID verificationId = UUID.fromString("9cac38a2-6584-4c1f-92fd-85223774979b");
    try {
      CardVerification result = api.getCardVerification(verificationId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getCardVerification failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **verificationId** | **UUID**|  | |

### Return type

[**CardVerification**](CardVerification.md)

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

<a id="listCardVerifications"></a>
# **listCardVerifications**
> CardVerificationList listCardVerifications(cardId, state)

List a card&#39;s 3DS verifications

List the 3DS verifications for a card, most recent first. Use the optional &#x60;state&#x60; filter (e.g. &#x60;state&#x3D;failed&#x60;) to retrieve only failed verifications; each failed verification carries a &#x60;failureReason&#x60; explaining why it failed. This includes cards declined synchronously at creation (the &#x60;POST&#x60; returned &#x60;400&#x60;): those attempts are persisted as failed verifications with &#x60;currentStepId: null&#x60;. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.CardVerificationApi;
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
    CardVerificationApi api = new CardVerificationApi(client);
    UUID cardId = UUID.fromString("8164683a-8d23-4421-9694-1a81fc9b9c37");
    String state = null; // optional
    try {
      CardVerificationList result = api.listCardVerifications(cardId, state);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("listCardVerifications failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **cardId** | **UUID**|  | |
| **state** | **String**| Optional filter to retrieve only verifications in the given state. | [optional] [enum: completed, failed, in-progress] |

### Return type

[**CardVerificationList**](CardVerificationList.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successful operation |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="unlockCardVerificationAttempts"></a>
# **unlockCardVerificationAttempts**
> CardVerificationUnlockResult unlockCardVerificationAttempts(cardVerificationUnlockParams)

Unlock a card&#39;s verification attempt lockout

Clear the cross-network verification **attempt lockout** for a card, identified by its &#x60;cardId&#x60;. This re-enables verification attempts for the card after it has been throttled by too many recent hard failures; the account-scoped, cross-network throttle that protects the non-HIGHEST tiers (Visa/Stripe, Mastercard/TNS). Idempotent: unlocking a card that has no active lock succeeds and clears nothing. The HIGHEST per-fingerprint lockout is **not** affected by this endpoint. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.CardVerificationApi;
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
    CardVerificationApi api = new CardVerificationApi(client);
    CardVerificationUnlockParams cardVerificationUnlockParams = new CardVerificationUnlockParams();
    try {
      CardVerificationUnlockResult result = api.unlockCardVerificationAttempts(cardVerificationUnlockParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("unlockCardVerificationAttempts failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **cardVerificationUnlockParams** | [**CardVerificationUnlockParams**](CardVerificationUnlockParams.md)|  | |

### Return type

[**CardVerificationUnlockResult**](CardVerificationUnlockResult.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The card&#39;s attempt lockout was cleared, or the card had no active lock (idempotent).  |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

