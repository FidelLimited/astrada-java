

# NetworkBulkConfirmationCreateResult


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **UUID** | The unique identifier of the open confirmation request. |  |
|**requestId** | **UUID** | The &#x60;requestId&#x60; this request sent. It is never the &#x60;requestId&#x60; the open request was stored under. |  |
|**subaccountId** | **UUID** | The subaccount of the open request, the same as the request&#39;s. |  |
|**network** | [**NetworkEnum**](#NetworkEnum) | The network of the open request, the same as the request&#39;s. |  |
|**bin** | **String** | The first 8 digits of the card, exactly as submitted. |  |
|**grade** | [**GradeEnum**](#GradeEnum) | The bulk likelihood grade the client saw for this BIN, as sent on the create request. &#x60;unchecked&#x60; when the request had no grade because it could not be determined. |  |
|**binInfo** | [**NetworkBulkConfirmationBinInfo**](NetworkBulkConfirmationBinInfo.md) |  |  |
|**company** | [**NetworkBulkConfirmationCompany**](NetworkBulkConfirmationCompany.md) |  |  |
|**cardAdmin** | [**NetworkBulkConfirmationCardAdmin**](NetworkBulkConfirmationCardAdmin.md) |  |  |
|**bankContact** | [**NetworkBulkBankContact**](NetworkBulkBankContact.md) |  |  |
|**state** | [**StateEnum**](#StateEnum) | The state of the open request. &#x60;requested&#x60; is the only state for now. |  |
|**customerReferenceId** | **String** | An optional id passed by the customer to associate a context with the confirmation. &#x60;null&#x60; when not provided. |  |
|**createdAt** | **OffsetDateTime** | When the open request was created, in UTC, following ISO 8601 format. Its 30-day window counts from this time. |  |
|**alreadyOpen** | **Boolean** | Always &#x60;true&#x60;. Tells this summary apart from the full confirmation. |  |



## Enum: NetworkEnum

| Name | Value |
|---- | -----|
| AMEX | &quot;AMEX&quot; |
| MASTERCARD | &quot;MASTERCARD&quot; |
| VISA | &quot;VISA&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: GradeEnum

| Name | Value |
|---- | -----|
| PROVEN | &quot;proven&quot; |
| HIGH | &quot;high&quot; |
| MEDIUM | &quot;medium&quot; |
| LOW | &quot;low&quot; |
| NONE | &quot;none&quot; |
| UNCHECKED | &quot;unchecked&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| REQUESTED | &quot;requested&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



