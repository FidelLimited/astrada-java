# BinLookupApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**lookupBin**](BinLookupApi.md#lookupBin) | **GET** /bin-lookup | Lookup card information by BIN |


<a id="lookupBin"></a>
# **lookupBin**
> BinLookup lookupBin(bin)

Lookup card information by BIN

Retrieve card information such as network, type, and issuing bank by providing the card&#39;s BIN (Bank Identification Number): the first 6 to 11 digits of the card number. Never send the full card number; more than 11 digits is rejected with &#x60;400&#x60;.  The card details are found under the first 6 digits. One 6-digit BIN can be shared by issuers in several countries, so for a Mastercard card looked up with 6 to 11 digits, Astrada also runs a more precise issuer-country check on the digits sent. That check can return a different &#x60;issuerCountry&#x60;, &#x60;issuerRegion&#x60; and &#x60;issuerName&#x60; than the first 6 digits alone would. Send more digits (up to 11) for a more precise issuer country; never send the full card number.  When the digits sent match card ranges in more than one country, the response keeps the 6-digit values and adds &#x60;issuerCountryAmbiguous&#x60;, &#x60;issuerCountryCandidates&#x60;, and a &#x60;warnings&#x60; entry with code &#x60;ISSUER_COUNTRY_NOT_CONFIRMED&#x60;. In every other case none of these three fields is present.  Lookups for every other network are unaffected. If the precise check gives no answer, the request still succeeds with the 6-digit values. In sandbox the precise check is not run, so sandbox responses never include these fields. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.BinLookupApi;
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
    BinLookupApi api = new BinLookupApi(client);
    String bin = "55891900000";
    try {
      BinLookup result = api.lookupBin(bin);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("lookupBin failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **bin** | **String**| The first 6 to 11 digits of the card number. Send digits only, and never the full card number. Input that is shorter than 6 digits, longer than 11 digits, or contains anything other than digits is rejected with &#x60;400&#x60;; longer input is not truncated.  For a Mastercard card, send more digits (up to 11) for a more precise issuer country. 11 digits gives the most precise answer.  | |

### Return type

[**BinLookup**](BinLookup.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successful operation |  -  |
| **400** | The &#x60;bin&#x60; is not 6 to 11 digits (too short, too long, or not digits only).  |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

