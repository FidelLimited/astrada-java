

# NetworkBulkConfirmation


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **UUID** | The unique identifier of this network bulk confirmation. |  |
|**requestId** | **UUID** | The idempotency key sent on the create request, unique per account. |  |
|**subaccountId** | **UUID** | The unique identifier of the subaccount the card program belongs to. |  |
|**network** | [**NetworkEnum**](#NetworkEnum) | The network of the card program. |  |
|**bin** | **String** | The first 8 digits of the card, exactly as submitted. |  |
|**grade** | [**GradeEnum**](#GradeEnum) | The bulk likelihood grade the client saw for this BIN, as sent on the create request. &#x60;unchecked&#x60; when the request had no grade because it could not be determined. |  |
|**binInfo** | [**NetworkBulkConfirmationBinInfo**](NetworkBulkConfirmationBinInfo.md) |  |  |
|**company** | [**NetworkBulkConfirmationCompany**](NetworkBulkConfirmationCompany.md) |  |  |
|**cardAdmin** | [**NetworkBulkConfirmationCardAdmin**](NetworkBulkConfirmationCardAdmin.md) |  |  |
|**bankContact** | [**NetworkBulkBankContact**](NetworkBulkBankContact.md) |  |  |
|**state** | [**StateEnum**](#StateEnum) | The state of the confirmation. &#x60;requested&#x60; is the only state for now. |  |
|**customerReferenceId** | **String** | An optional id passed by the customer to associate a context with the confirmation. &#x60;null&#x60; when not provided. |  |
|**createdAt** | **OffsetDateTime** | Date and time when this resource was created, in UTC, following ISO 8601 format. |  |



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



