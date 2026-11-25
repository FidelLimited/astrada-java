

# BankTransactionSettlement

Settlement outcome on the unified transaction feed. Present when the movement has settled live; null otherwise (unmatched, connector off, or shadow-only).

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**state** | [**StateEnum**](#StateEnum) |  |  |
|**emissionType** | [**EmissionTypeEnum**](#EmissionTypeEnum) |  |  |
|**emittedAt** | **OffsetDateTime** | When the settlement was emitted to the feed. |  |
|**supersededAt** | **OffsetDateTime** | When the settlement was superseded or corrected, if applicable. |  |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| SETTLED | &quot;settled&quot; |
| EARLY_CLEARED | &quot;early_cleared&quot; |
| SUPERSEDED | &quot;superseded&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: EmissionTypeEnum

| Name | Value |
|---- | -----|
| EARLY_CLEARING | &quot;early_clearing&quot; |
| BANK_ONLY | &quot;bank_only&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



