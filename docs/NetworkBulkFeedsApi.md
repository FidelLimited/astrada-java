# NetworkBulkFeedsApi

All URIs are relative to *https://api.astrada.co*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createNetworkBulkFeed**](NetworkBulkFeedsApi.md#createNetworkBulkFeed) | **POST** /network-bulk-feeds | Create network bulk feed |
| [**getNetworkBulkFeed**](NetworkBulkFeedsApi.md#getNetworkBulkFeed) | **GET** /network-bulk-feeds/{networkBulkFeedId} | Retrieve a network bulk feed |
| [**listNetworkBulkFeeds**](NetworkBulkFeedsApi.md#listNetworkBulkFeeds) | **GET** /network-bulk-feeds | List network bulk feed |
| [**updateNetworkBulkFeed**](NetworkBulkFeedsApi.md#updateNetworkBulkFeed) | **PATCH** /network-bulk-feeds/{networkBulkFeedId} | Update a network bulk feed |


<a id="createNetworkBulkFeed"></a>
# **createNetworkBulkFeed**
> NetworkBulkFeeds createNetworkBulkFeed(networkBulkFeedCreateParams)

Create network bulk feed

Registers a new feed for network bulk. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.NetworkBulkFeedsApi;
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
    NetworkBulkFeedsApi api = new NetworkBulkFeedsApi(client);
    NetworkBulkFeedCreateParams networkBulkFeedCreateParams = new NetworkBulkFeedCreateParams();
    try {
      NetworkBulkFeeds result = api.createNetworkBulkFeed(networkBulkFeedCreateParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("createNetworkBulkFeed failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **networkBulkFeedCreateParams** | [**NetworkBulkFeedCreateParams**](NetworkBulkFeedCreateParams.md)|  | |

### Return type

[**NetworkBulkFeeds**](NetworkBulkFeeds.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Resource created |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **409** | The network bulk feed already exists. |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="getNetworkBulkFeed"></a>
# **getNetworkBulkFeed**
> NetworkBulkFeeds getNetworkBulkFeed(networkBulkFeedId)

Retrieve a network bulk feed

Returns detailed information about a specific network bulk feed by its identifier.

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.NetworkBulkFeedsApi;
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
    NetworkBulkFeedsApi api = new NetworkBulkFeedsApi(client);
    UUID networkBulkFeedId = UUID.fromString("eb2ded7e-11fa-46fc-bd3e-f5b1d56fd3b3");
    try {
      NetworkBulkFeeds result = api.getNetworkBulkFeed(networkBulkFeedId);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("getNetworkBulkFeed failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **networkBulkFeedId** | **UUID**|  | |

### Return type

[**NetworkBulkFeeds**](NetworkBulkFeeds.md)

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

<a id="listNetworkBulkFeeds"></a>
# **listNetworkBulkFeeds**
> NetworkBulkFeedList listNetworkBulkFeeds(networkReference, subaccountId, limit, offset)

List network bulk feed

Returns all network bulk feeds under the account.  By default, &#x60;GET /network-bulk-feeds&#x60; returns network bulk feeds ordered by the latest &#x60;createdAt&#x60;.  This endpoint supports [offset-based pagination](https://docs.astrada.co/reference/pagination). 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.NetworkBulkFeedsApi;
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
    NetworkBulkFeedsApi api = new NetworkBulkFeedsApi(client);
    String networkReference = null; // optional
    UUID subaccountId = null; // optional
    Integer limit = null; // optional
    Integer offset = null; // optional
    try {
      NetworkBulkFeedList result = api.listNetworkBulkFeeds(networkReference, subaccountId, limit, offset);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("listNetworkBulkFeeds failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **networkReference** | **String**| Filters the returned feeds to an exact match on the feed&#39;s network reference. The network reference format depends on the card network (see the schema below).  | [optional] |
| **subaccountId** | **UUID**|  | [optional] |
| **limit** | **Integer**| Maximum number of items to return | [optional] [default to 50] |
| **offset** | **Integer**| The offset to use for pagination. Identifies the position of the first item returned in the collection | [optional] [default to 0] |

### Return type

[**NetworkBulkFeedList**](NetworkBulkFeedList.md)

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
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

<a id="updateNetworkBulkFeed"></a>
# **updateNetworkBulkFeed**
> NetworkBulkFeeds updateNetworkBulkFeed(networkBulkFeedId, networkBulkFeedUpdateParams)

Update a network bulk feed

Updates a network bulk feed. Supports setting &#x60;transactionStartDate&#x60; (currently for &#x60;SMARTDATA&#x60; feeds only; other feed types are rejected with a 422) and &#x60;expectedCardCount&#x60;. At least one field must be provided; &#x60;transactionStartDate&#x60; cannot be unset (&#x60;null&#x60; is rejected).  Setting &#x60;transactionStartDate&#x60; widens the feed&#39;s ingestion window: transactions posted on or after the date are ingested for the feed&#39;s deliveries, replacing the default 15-day window. 

### Example
```java
import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.model.*;
import co.astrada.client.api.NetworkBulkFeedsApi;
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
    NetworkBulkFeedsApi api = new NetworkBulkFeedsApi(client);
    UUID networkBulkFeedId = UUID.fromString("eb2ded7e-11fa-46fc-bd3e-f5b1d56fd3b3");
    NetworkBulkFeedUpdateParams networkBulkFeedUpdateParams = new NetworkBulkFeedUpdateParams();
    try {
      NetworkBulkFeeds result = api.updateNetworkBulkFeed(networkBulkFeedId, networkBulkFeedUpdateParams);
      System.out.println(result);
    } catch (ApiException e) {
      // getCode() is the HTTP status (0 if no response arrived); getMessage() has the title,
      // detail and error code from the body.
      System.err.println("updateNetworkBulkFeed failed: " + e.getMessage());
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **networkBulkFeedId** | **UUID**|  | |
| **networkBulkFeedUpdateParams** | [**NetworkBulkFeedUpdateParams**](NetworkBulkFeedUpdateParams.md)|  | |

### Return type

[**NetworkBulkFeeds**](NetworkBulkFeeds.md)

### Authorization

OAuth2 client credentials; `Astrada.builder().credentials(...)` logs in (see the [README](../README.md#log-in)).

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/hal+json, application/problem+json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successful operation |  -  |
| **400** | The server cannot or will not process the request due to something that is perceived to be a client error |  -  |
| **401** | Authentication credentials were either missing or incorrect |  -  |
| **403** | Authentication credentials used do not have have permissions to perform the request |  -  |
| **404** | The requested resource was not found on the server |  -  |
| **422** | The request is well formatted but it fails some business logic validation |  -  |
| **500** | The server encountered an unexpected condition that prevented it from fulfilling the request |  -  |

