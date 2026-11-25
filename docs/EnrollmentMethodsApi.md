# EnrollmentMethodsApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getEnrollmentMethods**](EnrollmentMethodsApi.md#getEnrollmentMethods) | **POST** /enrollment-methods | List the available enrollment methods given a card BIN |


<a id="getEnrollmentMethods"></a>
# **getEnrollmentMethods**
> EnrollmentMethodsResult getEnrollmentMethods(enrollmentMethodsParams)

List the available enrollment methods given a card BIN

List the available enrollment methods given a card BIN.  The Unified Enrollment SDK v2 calls this endpoint for each card: when a person presses **Continue** on the card number screen, with the first 8 to 11 digits they typed (8 by default), and on an employee page that opens on the card form, once 8 digits are typed there. It sends the session&#39;s &#x60;subaccountId&#x60; with the digits, and asks again with the digits alone when that answers &#x60;400&#x60; or &#x60;403&#x60;. It routes the session from the answer together with &#x60;GET /bin-lookup&#x60;. The SDK token therefore needs &#x60;enrollment-methods:write&#x60;. If your own backend calls it, send up to 11 digits for the most precise answer.  Send &#x60;subaccountId&#x60; to ask whether that subaccount can enroll this card one at a time with &#x60;POST /card-subscriptions&#x60;. The response then adds &#x60;singleDecision&#x60;, from the single-enrollment controls that create runs for the subaccount, and &#x60;singleEligible&#x60; and &#x60;availableEnrollmentMethods&#x60; follow it. Create also checks the &#x60;country&#x60; sent with the card; this request has no country, so &#x60;singleDecision&#x60; does not cover that check. Without &#x60;subaccountId&#x60;, the response is about the card alone and no subaccount is read.  When Mastercard&#39;s live card data is temporarily unavailable, the response falls back to Astrada&#39;s stored card data instead of failing, and &#x60;binInfo&#x60; comes from that stored data. &#x60;bulkLikelihood&#x60; is then capped at &#x60;medium&#x60;, except for BINs with proven bulk history, which stay &#x60;proven&#x60;, so &#x60;network-bulk&#x60; is then offered only for those proven BINs. When nothing decides the grade, &#x60;bulkLikelihood&#x60; is omitted and &#x60;unavailable&#x60; lists it; &#x60;singleEligible&#x60; is then &#x60;true&#x60; and &#x60;availableEnrollmentMethods&#x60; is &#x60;[\&quot;cardholder-single\&quot;]&#x60;, unless &#x60;singleDecision&#x60; refuses the card. Retry later for a full answer.

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.EnrollmentMethodsApi;
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
    EnrollmentMethodsApi api = new EnrollmentMethodsApi(client);
    EnrollmentMethodsParams enrollmentMethodsParams = new EnrollmentMethodsParams();
    try {
      EnrollmentMethodsResult result = api.getEnrollmentMethods(enrollmentMethodsParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getEnrollmentMethods failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **enrollmentMethodsParams** | [**EnrollmentMethodsParams**](EnrollmentMethodsParams.md)| Enrollment methods payload | |

### Return type

[**EnrollmentMethodsResult**](EnrollmentMethodsResult.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successful operation |  -  |
| **400** | The request failed validation, for example a &#x60;bin&#x60; that is not 6 to 11 digits or a &#x60;subaccountId&#x60; that is not a UUID. A &#x60;subaccountId&#x60; that is not one of your account&#39;s subaccounts also gets &#x60;400&#x60;, with the detail &#x60;Given subaccount is not valid&#x60;. |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **413** | Content Too Large |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

