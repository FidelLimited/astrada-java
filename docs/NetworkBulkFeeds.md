

# NetworkBulkFeeds


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**SelfLinks**](SelfLinks.md) |  |  |
|**id** | **UUID** | The unique identifier of this network bulk feed entity. |  |
|**subaccountId** | **UUID** | The unique identifier of the subaccount related to this entity. |  |
|**network** | [**NetworkEnum**](#NetworkEnum) | The network where the feed belongs. |  |
|**networkReference** | **String** | The identifier associated with the card network&#39;s bulk enrollment feed. Its format depends on the network. For Amex, the Recipient ID in the format &#39;R{RecipientId}&#39;, such as &#39;R123456&#39;, or the 6-digit Amex corporate ID (CID), such as &#39;014640&#39;. For Mastercard, the Delivery ID, such as &#39;G1234567&#39;; its format is not checked. For Visa-VBDS, the RPIC: four numeric parts joined by underscores, &#39;RegionId_ProcessorId_IssuerId_CompanyId&#39;, such as &#39;1_123_45_6789&#39;. For Visa-VCF, the same RPIC &#39;RegionId_ProcessorId_IssuerId_CompanyId&#39;, such as &#39;1_123_45_6789&#39; (the region segment is ignored), or the 3-segment PIC form &#39;ProcessorId_IssuerId_CompanyId&#39;, such as &#39;123_45_6789&#39;. For Visa, a part that is not all digits is refused with &#x60;400&#x60;, naming the part. Spaces around each part and leading zeros are removed before the reference is stored. For more details on our bulk enrollment process, refer to [this guide](https://docs.astrada.co/docs/network-bulk-enrollment).  |  |
|**state** | [**StateEnum**](#StateEnum) | The state of the network bulk feed entity.  &gt; **Note**: The &#x60;failed&#x60; state is currently only applicable to VISA bulk feeds.  |  |
|**type** | [**TypeEnum**](#TypeEnum) | The feed&#39;s delivery rail. Always set for &#x60;AMEX&#x60; (&#x60;GL&#x60;) and &#x60;MASTERCARD&#x60; (&#x60;SMARTDATA&#x60;), and for a &#x60;VISA&#x60; feed created with an explicit type.  &#x60;null&#x60; while a &#x60;VISA&#x60; feed created without a type is still discovering its rail: it stays &#x60;submitted&#x60; and becomes &#x60;VBDS&#x60; or &#x60;VCF&#x60; (and &#x60;active&#x60;) as soon as a VBDS connection or a VCF file confirms the rail. The &#x60;networkbulkfeed.statechanged&#x60; webhook for that transition carries the resolved value.  |  |
|**panFormat** | [**PanFormatEnum**](#PanFormatEnum) | The PAN format of the feed&#39;s card data: &#x60;encrypted&#x60; (raw PAN) or &#x60;tokenized&#x60; (Amex network token / &#x60;tokenizedId&#x60;).  Outside the sandbox environment this is derived internally at feed creation (determined by Astrada from the network/file signals, never accepted on the create request) and defaults to &#x60;encrypted&#x60;. In the sandbox environment it may be set explicitly on the create request for &#x60;type: GL&#x60; (AMEX) feeds only, so the Simulate flow can register a tokenized feed (see &#x60;panFormat&#x60; on &#x60;POST /network-bulk-feeds&#x60;). It is always returned on the feed representation.  |  |
|**errorReason** | **String** | A human-readable reason explaining why the bulk feed failed. Present when &#x60;state&#x60; is &#x60;failed&#x60;. For a &#x60;VISA&#x60; feed still discovering its rail (&#x60;type&#x60; is &#x60;null&#x60;, &#x60;state&#x60; is &#x60;submitted&#x60;) it may also carry the latest error reported by one rail, prefixed with that rail (for example &#x60;VBDS: ...&#x60;); the feed keeps waiting for the other rail and the field is cleared when the feed becomes &#x60;active&#x60;.  &gt; **Note**: Currently only applicable to VISA bulk feeds.  |  [optional] |
|**expectedCardCount** | **Integer** | The expected number of cards that will be enrolled under this bulk feed, as provided at creation time. Only present when the feed was created with this field.  |  [optional] |
|**transactionStartDate** | **LocalDate** | Transactions posted on or after this calendar date are ingested for this feed&#39;s deliveries, replacing the default 15-day ingestion window. Currently supported for SMARTDATA feeds only. Only present when set; when absent, the default 15-day window applies. The comparison is calendar-day based in UTC: the network&#39;s posting date is a date-only field, so the cutoff is the start of the stated day with no local-timezone adjustment.  |  [optional] |
|**createdAt** | **OffsetDateTime** | Date and time when this resource was created, in UTC, following ISO 8601 format. |  |
|**updatedAt** | **OffsetDateTime** | Date and time when this resource was last updated, in UTC, following ISO 8601 format. |  |



## Enum: NetworkEnum

| Name | Value |
|---- | -----|
| AMEX | &quot;AMEX&quot; |
| MASTERCARD | &quot;MASTERCARD&quot; |
| VISA | &quot;VISA&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| SUBMITTED | &quot;submitted&quot; |
| ACTIVE | &quot;active&quot; |
| DEACTIVATED | &quot;deactivated&quot; |
| FAILED | &quot;failed&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| GL | &quot;GL&quot; |
| SMARTDATA | &quot;SMARTDATA&quot; |
| VBDS | &quot;VBDS&quot; |
| VCF | &quot;VCF&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: PanFormatEnum

| Name | Value |
|---- | -----|
| ENCRYPTED | &quot;encrypted&quot; |
| TOKENIZED | &quot;tokenized&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



