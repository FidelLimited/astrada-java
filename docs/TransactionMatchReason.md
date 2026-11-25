

# TransactionMatchReason

One scoring factor of a match and what it added to the score.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**type** | [**TypeEnum**](#TypeEnum) | The matching factor type. |  [optional] |
|**score** | **BigDecimal** | The individual score for this factor. |  [optional] |
|**cardValue** | **String** | The value from the card transaction used for comparison. May be a string or numeric value. |  [optional] |
|**bankValue** | **String** | The value from the bank transaction used for comparison. May be a string or numeric value. |  [optional] |
|**diff** | **BigDecimal** | The numeric difference between the card and bank values. |  [optional] |
|**message** | **String** | Human-readable explanation of the score for this factor. |  [optional] |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| AMOUNT | &quot;Amount&quot; |
| DATE | &quot;Date&quot; |
| MERCHANT | &quot;Merchant&quot; |
| REFERENCE_ID | &quot;ReferenceId&quot; |
| PRE_AUTH | &quot;PreAuth&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



