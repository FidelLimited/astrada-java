

# EnrollmentMethodsResult


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**availableEnrollmentMethods** | [**List&lt;AvailableEnrollmentMethodsEnum&gt;**](#List&lt;AvailableEnrollmentMethodsEnum&gt;) | The enrollment methods this card can use: &#x60;network-bulk&#x60; when &#x60;bulkLikelihood&#x60; is present and not &#x60;none&#x60; (for Visa and Mastercard cards, only &#x60;high&#x60; or &#x60;proven&#x60;), and &#x60;cardholder-single&#x60; when &#x60;singleEligible&#x60; is &#x60;true&#x60;. Empty when neither applies, for example for a card from a country Astrada doesn&#39;t cover for its network. |  |
|**bulkLikelihood** | [**BulkLikelihoodEnum**](#BulkLikelihoodEnum) | Graded likelihood that network-bulk enrollment will succeed for this BIN. A country-coverage veto forces &#x60;none&#x60;. Omitted when it could not be determined for this request; &#x60;unavailable&#x60; then lists it. &#x60;none&#x60; is only returned when it was established, never because the data was unavailable. Whether &#x60;network-bulk&#x60; is offered is in &#x60;availableEnrollmentMethods&#x60;: for Visa and Mastercard cards it needs &#x60;high&#x60; or &#x60;proven&#x60;. |  [optional] |
|**singleEligible** | **Boolean** | Whether cardholder-single enrollment is supported for this BIN. When the response has &#x60;singleDecision&#x60;, it equals &#x60;singleDecision.allowed&#x60;. |  [optional] |
|**singleDecision** | [**EnrollmentMethodsResultSingleDecision**](EnrollmentMethodsResultSingleDecision.md) |  |  [optional] |
|**binInfo** | [**EnrollmentMethodsResultBinInfo**](EnrollmentMethodsResultBinInfo.md) |  |  [optional] |
|**unavailable** | [**List&lt;UnavailableEnum&gt;**](#List&lt;UnavailableEnum&gt;) | Response fields whose value could not be determined for this request, for example when live card data was temporarily unavailable and the stored card data doesn&#39;t decide the grade. Today it can only contain &#x60;bulkLikelihood&#x60;; more values may be added, so ignore any you don&#39;t recognize. Omitted when every field was determined. |  [optional] |



## Enum: List&lt;AvailableEnrollmentMethodsEnum&gt;

| Name | Value |
|---- | -----|
| NETWORK_BULK | &quot;network-bulk&quot; |
| CARDHOLDER_SINGLE | &quot;cardholder-single&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: BulkLikelihoodEnum

| Name | Value |
|---- | -----|
| PROVEN | &quot;proven&quot; |
| HIGH | &quot;high&quot; |
| MEDIUM | &quot;medium&quot; |
| LOW | &quot;low&quot; |
| NONE | &quot;none&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: List&lt;UnavailableEnum&gt;

| Name | Value |
|---- | -----|
| BULK_LIKELIHOOD | &quot;bulkLikelihood&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



