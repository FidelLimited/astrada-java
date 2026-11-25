

# NetworkBlockedCardRule

A platform rule that blocks enrollment of matching cards on one network.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**cardType** | [**CardTypeEnum**](#CardTypeEnum) | The BIN &#x60;type&#x60; this rule blocks. |  |
|**fundingTypes** | [**Set&lt;FundingTypesEnum&gt;**](#Set&lt;FundingTypesEnum&gt;) | The BIN &#x60;fundingType&#x60; values this rule blocks. Omitted or empty means every funding type. |  [optional] |



## Enum: CardTypeEnum

| Name | Value |
|---- | -----|
| COMMERCIAL | &quot;COMMERCIAL&quot; |
| CORPORATE | &quot;CORPORATE&quot; |
| CONSUMER | &quot;CONSUMER&quot; |
| SMALL_BUSINESS | &quot;SMALL_BUSINESS&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: Set&lt;FundingTypesEnum&gt;

| Name | Value |
|---- | -----|
| CREDIT | &quot;CREDIT&quot; |
| DEBIT | &quot;DEBIT&quot; |
| PREPAID | &quot;PREPAID&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



