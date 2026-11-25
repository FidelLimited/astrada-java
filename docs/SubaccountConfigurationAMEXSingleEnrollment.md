

# SubaccountConfigurationAMEXSingleEnrollment


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**countries** | [**Set&lt;CountriesEnum&gt;**](#Set&lt;CountriesEnum&gt;) | List of countries supported for AMEX single enrollment |  |
|**blockedCardTypes** | [**Set&lt;BlockedCardTypesEnum&gt;**](#Set&lt;BlockedCardTypesEnum&gt;) | Card types blocked for AMEX single enrollment. The account&#39;s defaults for this network and enrollment mode are always added, so the stored list can include values that were not sent. |  |
|**blockedFundingCardTypes** | [**Set&lt;BlockedFundingCardTypesEnum&gt;**](#Set&lt;BlockedFundingCardTypesEnum&gt;) | Funding card types blocked for AMEX single enrollment. The account&#39;s defaults for this network and enrollment mode are always added, so the stored list can include values that were not sent. |  |



## Enum: Set&lt;CountriesEnum&gt;

| Name | Value |
|---- | -----|
| USA | &quot;USA&quot; |
| CAN | &quot;CAN&quot; |
| GBR | &quot;GBR&quot; |
| ITA | &quot;ITA&quot; |
| SWE | &quot;SWE&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



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



