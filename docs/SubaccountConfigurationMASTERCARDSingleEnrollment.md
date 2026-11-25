

# SubaccountConfigurationMASTERCARDSingleEnrollment


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**countries** | [**Set&lt;CountriesEnum&gt;**](#Set&lt;CountriesEnum&gt;) | List of countries supported for Mastercard single enrollment |  |
|**blockedCardTypes** | [**Set&lt;BlockedCardTypesEnum&gt;**](#Set&lt;BlockedCardTypesEnum&gt;) | Card types blocked for Mastercard single enrollment. The account&#39;s defaults for this network and enrollment mode are always added, so the stored list can include values that were not sent. |  |
|**blockedFundingCardTypes** | [**Set&lt;BlockedFundingCardTypesEnum&gt;**](#Set&lt;BlockedFundingCardTypesEnum&gt;) | Funding card types blocked for Mastercard single enrollment. The account&#39;s defaults for this network and enrollment mode are always added, so the stored list can include values that were not sent. |  |



## Enum: Set&lt;CountriesEnum&gt;

| Name | Value |
|---- | -----|
| USA | &quot;USA&quot; |
| CAN | &quot;CAN&quot; |
| GBR | &quot;GBR&quot; |
| IRL | &quot;IRL&quot; |
| SWE | &quot;SWE&quot; |
| DEU | &quot;DEU&quot; |
| NLD | &quot;NLD&quot; |
| DNK | &quot;DNK&quot; |
| ITA | &quot;ITA&quot; |
| POL | &quot;POL&quot; |
| FIN | &quot;FIN&quot; |
| BEL | &quot;BEL&quot; |
| ESP | &quot;ESP&quot; |
| FRA | &quot;FRA&quot; |
| PRT | &quot;PRT&quot; |
| AUT | &quot;AUT&quot; |
| GRC | &quot;GRC&quot; |
| MLT | &quot;MLT&quot; |
| NOR | &quot;NOR&quot; |
| CHE | &quot;CHE&quot; |
| ISR | &quot;ISR&quot; |
| AUS | &quot;AUS&quot; |
| NZL | &quot;NZL&quot; |
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



