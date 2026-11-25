

# NetworkBulkEnrollmentCreateResult


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**NetworkBulkEnrollmentSummaryLinks**](NetworkBulkEnrollmentSummaryLinks.md) |  |  |
|**id** | **UUID** | The unique identifier of the open kickoff. |  |
|**subaccountId** | **UUID** | The subaccount of the open kickoff, the same as the request&#39;s. |  |
|**network** | [**NetworkEnum**](#NetworkEnum) | The network of the open kickoff, the same as the request&#39;s. |  |
|**bin** | **String** | The leading 6 to 11 digits of the card that opened the kickoff, exactly as submitted. |  |
|**binInfo** | [**NetworkBulkEnrollmentBinInfo**](NetworkBulkEnrollmentBinInfo.md) |  |  |
|**company** | [**NetworkBulkEnrollmentCompany**](NetworkBulkEnrollmentCompany.md) |  |  |
|**cardAdmin** | [**NetworkBulkEnrollmentCardAdmin**](NetworkBulkEnrollmentCardAdmin.md) |  |  |
|**bankContact** | [**NetworkBulkBankContact**](NetworkBulkBankContact.md) |  |  |
|**cardCountBand** | [**CardCountBandEnum**](#CardCountBandEnum) | The estimated number of cards in the program, as a band. &#x60;null&#x60; when not provided. |  |
|**outreach** | [**OutreachEnum**](#OutreachEnum) | Who contacts the bank. &#x60;astrada&#x60;: Astrada reaches out to the bank, copying the company and the card admin. &#x60;self&#x60;: the company emails the bank itself and copies onboarding@astrada.co so Astrada can guide the setup.  |  |
|**consent** | [**NetworkBulkEnrollmentConsent**](NetworkBulkEnrollmentConsent.md) |  |  |
|**state** | [**StateEnum**](#StateEnum) | The open kickoff&#39;s state. &#x60;requested&#x60;: it was created less than 90 days ago and no feed has been registered against it yet. &#x60;feed-created&#x60;: a feed was registered against it and that feed is live.  |  |
|**networkBulkFeedId** | **UUID** | The live feed registered against the open kickoff. &#x60;null&#x60; while the kickoff is &#x60;requested&#x60;. |  |
|**customerReferenceId** | **String** | An optional id passed by the customer to associate a context with the enrollment. &#x60;null&#x60; when not provided. |  |
|**createdAt** | **OffsetDateTime** | When the open kickoff was created, in UTC, following ISO 8601 format. Its 90-day window counts from this time. |  |
|**updatedAt** | **OffsetDateTime** | Date and time when this resource was last updated, in UTC, following ISO 8601 format. |  |
|**alreadyOpen** | **Boolean** | Always &#x60;true&#x60;. Tells this summary apart from the full enrollment. |  |
|**requestId** | **UUID** | The &#x60;requestId&#x60; this request sent, or &#x60;null&#x60; when it sent none. It is never the &#x60;requestId&#x60; the open kickoff was stored under.  |  |



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
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



