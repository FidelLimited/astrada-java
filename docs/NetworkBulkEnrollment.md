

# NetworkBulkEnrollment


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**NetworkBulkEnrollmentLinks**](NetworkBulkEnrollmentLinks.md) |  |  |
|**id** | **UUID** | The unique identifier of this network bulk enrollment. |  |
|**subaccountId** | **UUID** | The unique identifier of the subaccount the card program enrolls into. |  |
|**network** | [**NetworkEnum**](#NetworkEnum) | The network of the card program. |  |
|**bin** | **String** | The leading 6 to 11 digits of the card that opened the kickoff, exactly as submitted. |  |
|**binInfo** | [**NetworkBulkEnrollmentBinInfo**](NetworkBulkEnrollmentBinInfo.md) |  |  |
|**company** | [**NetworkBulkEnrollmentCompany**](NetworkBulkEnrollmentCompany.md) |  |  |
|**cardAdmin** | [**NetworkBulkEnrollmentCardAdmin**](NetworkBulkEnrollmentCardAdmin.md) |  |  |
|**bankContact** | [**NetworkBulkBankContact**](NetworkBulkBankContact.md) |  |  |
|**cardCountBand** | [**CardCountBandEnum**](#CardCountBandEnum) | The estimated number of cards in the program, as a band. &#x60;null&#x60; when not provided. |  |
|**outreach** | [**OutreachEnum**](#OutreachEnum) | Who contacts the bank. &#x60;astrada&#x60;: Astrada reaches out to the bank, copying the company and the card admin. &#x60;self&#x60;: the company emails the bank itself and copies onboarding@astrada.co so Astrada can guide the setup.  |  |
|**consent** | [**NetworkBulkEnrollmentConsent**](NetworkBulkEnrollmentConsent.md) |  |  |
|**state** | [**StateEnum**](#StateEnum) | &#x60;requested&#x60; until a network bulk feed is registered with this enrollment&#39;s id, then &#x60;feed-created&#x60;. Astrada does not set &#x60;cancelled&#x60; today. A kickoff that is never followed by a feed keeps &#x60;requested&#x60;, also after its 90 days (see &#x60;POST /network-bulk-enrollments&#x60;).  |  |
|**networkBulkFeedId** | **UUID** | The network bulk feed registered with this enrollment&#39;s id on &#x60;POST /network-bulk-feeds&#x60;. &#x60;null&#x60; until then. |  |
|**customerReferenceId** | **String** | An optional id passed by the customer to associate a context with the enrollment. &#x60;null&#x60; when not provided. |  |
|**createdAt** | **OffsetDateTime** | Date and time when this resource was created, in UTC, following ISO 8601 format. |  |
|**updatedAt** | **OffsetDateTime** | Date and time when this resource was last updated, in UTC, following ISO 8601 format. |  |



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



## Enum: StateEnum

| Name | Value |
|---- | -----|
| REQUESTED | &quot;requested&quot; |
| FEED_CREATED | &quot;feed-created&quot; |
| CANCELLED | &quot;cancelled&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



