

# NetworkBulkEnrollmentCreateParams


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**requestId** | **UUID** | Optional. Your idempotency key for this enrollment, unique per account. Repeating the request with a &#x60;requestId&#x60; that was already used returns the stored enrollment, whole and unchanged, with &#x60;200&#x60; instead of creating a second one with &#x60;201&#x60;. A request without a &#x60;requestId&#x60; is never a replay: it creates a new enrollment unless its program already has an open kickoff, which answers &#x60;200&#x60; with that kickoff&#39;s summary.  |  [optional] |
|**subaccountId** | **UUID** | The subaccount the card program enrolls into. Must belong to the calling account. |  |
|**network** | [**NetworkEnum**](#NetworkEnum) | The network of the card program. |  |
|**bin** | **String** | The leading 6 to 11 digits of the card that opened the kickoff. |  |
|**binInfo** | [**NetworkBulkEnrollmentCreateParamsBinInfo**](NetworkBulkEnrollmentCreateParamsBinInfo.md) |  |  [optional] |
|**company** | [**NetworkBulkCompanyParams**](NetworkBulkCompanyParams.md) |  |  |
|**cardAdmin** | [**NetworkBulkEnrollmentCreateParamsCardAdmin**](NetworkBulkEnrollmentCreateParamsCardAdmin.md) |  |  |
|**bankContact** | [**NetworkBulkBankContactParams**](NetworkBulkBankContactParams.md) |  |  [optional] |
|**cardCountBand** | [**CardCountBandEnum**](#CardCountBandEnum) | Optional. The estimated number of cards in the program, as a band. An estimate helps the bank size the file. |  [optional] |
|**cardCount** | **Integer** | Optional. The estimated number of cards in the program. When sent, it decides the stored &#x60;cardCountBand&#x60;, even if &#x60;cardCountBand&#x60; is also sent. |  [optional] |
|**outreach** | [**OutreachEnum**](#OutreachEnum) | Who contacts the bank. &#x60;astrada&#x60;: Astrada reaches out to the bank, copying the company and the card admin. &#x60;self&#x60;: the company emails the bank itself and copies onboarding@astrada.co so Astrada can guide the setup.  |  |
|**consent** | [**NetworkBulkEnrollmentCreateParamsConsent**](NetworkBulkEnrollmentCreateParamsConsent.md) |  |  |
|**customerReferenceId** | **String** | Optional. Your own id for this enrollment, echoed back on the resource. |  [optional] |



## Enum: NetworkEnum

| Name | Value |
|---- | -----|
| AMEX | &quot;AMEX&quot; |
| MASTERCARD | &quot;MASTERCARD&quot; |
| VISA | &quot;VISA&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: CardCountBandEnum

| Name | Value |
|---- | -----|
| BAND_1_50 | &quot;1-50&quot; |
| BAND_51_250 | &quot;51-250&quot; |
| BAND_251_1000 | &quot;251-1000&quot; |
| BAND_1001_5000 | &quot;1001-5000&quot; |
| BAND_5001_25000 | &quot;5001-25000&quot; |
| BAND_25000_PLUS | &quot;25000+&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: OutreachEnum

| Name | Value |
|---- | -----|
| ASTRADA | &quot;astrada&quot; |
| SELF | &quot;self&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



