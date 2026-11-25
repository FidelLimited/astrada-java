

# SubaccountConfigurationMASTERCARDBulkEnrollment


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**countries** | [**Set&lt;CountriesEnum&gt;**](#Set&lt;CountriesEnum&gt;) | List of countries supported for Mastercard bulk enrollment |  |
|**blockedCardTypes** | [**Set&lt;BlockedCardTypesEnum&gt;**](#Set&lt;BlockedCardTypesEnum&gt;) | Card types blocked for Mastercard bulk enrollment. The account&#39;s defaults for this network and enrollment mode are always added, so the stored list can include values that were not sent. |  |
|**blockedFundingCardTypes** | [**Set&lt;BlockedFundingCardTypesEnum&gt;**](#Set&lt;BlockedFundingCardTypesEnum&gt;) | Funding card types blocked for Mastercard bulk enrollment. The account&#39;s defaults for this network and enrollment mode are always added, so the stored list can include values that were not sent. |  |



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
| AUT | &quot;AUT&quot; |
| NOR | &quot;NOR&quot; |
| CHE | &quot;CHE&quot; |
| ISR | &quot;ISR&quot; |
| AUS | &quot;AUS&quot; |
| NZL | &quot;NZL&quot; |
| SGP | &quot;SGP&quot; |
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



