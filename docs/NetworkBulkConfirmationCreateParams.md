

# NetworkBulkConfirmationCreateParams


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**requestId** | **UUID** | Your idempotency key for this confirmation, unique per account. Repeating the request with a &#x60;requestId&#x60; that was already used returns the stored confirmation, whole and unchanged, with &#x60;200&#x60; instead of creating a second one with &#x60;201&#x60;. A new &#x60;requestId&#x60; for a program that already has an open request answers &#x60;200&#x60; with that request&#39;s summary.  |  |
|**subaccountId** | **UUID** | The subaccount the card program belongs to. Must belong to the calling account; an unknown subaccount answers &#x60;400&#x60;. |  |
|**network** | [**NetworkEnum**](#NetworkEnum) | The network of the card program. |  |
|**bin** | **String** | Exactly the first 8 digits of the card number. Never send the full card number. |  |
|**grade** | [**GradeEnum**](#GradeEnum) | The bulk likelihood grade your client saw for this BIN, as returned in &#x60;bulkLikelihood&#x60; by &#x60;POST /enrollment-methods&#x60;. Send it whenever that response had one. Leave it out (or send &#x60;unchecked&#x60;) only when the response had no &#x60;bulkLikelihood&#x60; because it could not be determined; the request is then stored as &#x60;unchecked&#x60;. |  [optional] |
|**binInfo** | [**NetworkBulkConfirmationCreateParamsBinInfo**](NetworkBulkConfirmationCreateParamsBinInfo.md) |  |  [optional] |
|**company** | [**NetworkBulkCompanyParams**](NetworkBulkCompanyParams.md) |  |  |
|**cardAdmin** | [**NetworkBulkConfirmationCreateParamsCardAdmin**](NetworkBulkConfirmationCreateParamsCardAdmin.md) |  |  |
|**bankContact** | [**NetworkBulkBankContactParams**](NetworkBulkBankContactParams.md) |  |  [optional] |
|**customerReferenceId** | **String** | Optional. Your own id for this confirmation, echoed back on the resource. |  [optional] |



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



