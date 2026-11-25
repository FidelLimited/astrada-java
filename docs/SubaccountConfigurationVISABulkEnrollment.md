

# SubaccountConfigurationVISABulkEnrollment


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**countries** | [**Set&lt;CountriesEnum&gt;**](#Set&lt;CountriesEnum&gt;) | List of countries supported for Visa bulk enrollment |  |
|**blockedCardTypes** | [**Set&lt;BlockedCardTypesEnum&gt;**](#Set&lt;BlockedCardTypesEnum&gt;) | Card types blocked for Visa bulk enrollment. The account&#39;s defaults for this network and enrollment mode are always added, so the stored list can include values that were not sent. |  |
|**blockedFundingCardTypes** | [**Set&lt;BlockedFundingCardTypesEnum&gt;**](#Set&lt;BlockedFundingCardTypesEnum&gt;) | Funding card types blocked for Visa bulk enrollment. The account&#39;s defaults for this network and enrollment mode are always added, so the stored list can include values that were not sent. |  |



## Enum: Set&lt;CountriesEnum&gt;

| Name | Value |
|---- | -----|
| USA | &quot;USA&quot; |
| CAN | &quot;CAN&quot; |
| IRL | &quot;IRL&quot; |
| DEU | &quot;DEU&quot; |
| CZE | &quot;CZE&quot; |
| SWE | &quot;SWE&quot; |
| NLD | &quot;NLD&quot; |
| GBR | &quot;GBR&quot; |
| SVN | &quot;SVN&quot; |
| LUX | &quot;LUX&quot; |
| POL | &quot;POL&quot; |
| SVK | &quot;SVK&quot; |
| HUN | &quot;HUN&quot; |
| ITA | &quot;ITA&quot; |
| ROU | &quot;ROU&quot; |
| FIN | &quot;FIN&quot; |
| MLT | &quot;MLT&quot; |
| AUT | &quot;AUT&quot; |
| FRA | &quot;FRA&quot; |
| ESP | &quot;ESP&quot; |
| NOR | &quot;NOR&quot; |
| CHE | &quot;CHE&quot; |
| AUS | &quot;AUS&quot; |
| NZL | &quot;NZL&quot; |
| PRT | &quot;PRT&quot; |
| BEL | &quot;BEL&quot; |
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



