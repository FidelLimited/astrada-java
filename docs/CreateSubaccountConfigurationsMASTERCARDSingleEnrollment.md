

# CreateSubaccountConfigurationsMASTERCARDSingleEnrollment


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**blockedCardTypes** | [**Set&lt;BlockedCardTypesEnum&gt;**](#Set&lt;BlockedCardTypesEnum&gt;) | Card types blocked for Mastercard single enrollment. The account&#39;s defaults for this network and enrollment mode are always added, so the stored list can include values that were not sent. |  [optional] |
|**blockedFundingCardTypes** | [**Set&lt;BlockedFundingCardTypesEnum&gt;**](#Set&lt;BlockedFundingCardTypesEnum&gt;) | Funding card types blocked for Mastercard single enrollment. The account&#39;s defaults for this network and enrollment mode are always added, so the stored list can include values that were not sent. |  [optional] |



## Enum: Set&lt;BlockedCardTypesEnum&gt;

| Name | Value |
|---- | -----|
| CONSUMER | &quot;CONSUMER&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: Set&lt;BlockedFundingCardTypesEnum&gt;

| Name | Value |
|---- | -----|
| DEBIT | &quot;DEBIT&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



