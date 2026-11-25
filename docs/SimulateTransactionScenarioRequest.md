

# SimulateTransactionScenarioRequest


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**scenario** | [**ScenarioEnum**](#ScenarioEnum) | The predefined scenario to simulate. |  |
|**cardId** | **UUID** | The identifier of the enrolled card to transact against. |  |
|**subaccountId** | **UUID** | The subaccount identifier the card belongs to. |  |
|**amount** | **Integer** | Optional transaction amount in minor units (cents). When omitted, the scenario&#39;s default amount is used.  |  [optional] |
|**currency** | **String** | Optional ISO 4217 three-letter currency code, case insensitive. A code that is not a currency transactions can be made in is rejected with &#x60;400&#x60;. When omitted, the scenario&#39;s default currency is used.  |  [optional] |
|**network** | [**NetworkEnum**](#NetworkEnum) | Optional card network override, so one scenario can be run against any network. When omitted, the scenario&#39;s own default network is used.  |  [optional] |
|**source** | [**SourceEnum**](#SourceEnum) | Optional ingestion-source tag applied to every message the scenario emits, so simulated activity can be attributed to a given network feed. When omitted, defaults to the scenario&#39;s own source if it sets one (the &#x60;amex_auth_*&#x60; scenarios default to &#x60;amexrta&#x60;), else &#x60;tns&#x60;, or &#x60;amexgl&#x60; when the resolved network is AMEX.  |  [optional] |



## Enum: ScenarioEnum

| Name | Value |
|---- | -----|
| SIMPLE_PURCHASE | &quot;simple_purchase&quot; |
| SPLIT_SHIPMENT | &quot;split_shipment&quot; |
| TIP_ADJUSTMENT | &quot;tip_adjustment&quot; |
| EXPIRED_AUTH | &quot;expired_auth&quot; |
| REFUND | &quot;refund&quot; |
| HOTEL_PREAUTH | &quot;hotel_preauth&quot; |
| CAR_RENTAL | &quot;car_rental&quot; |
| GAS_PUMP | &quot;gas_pump&quot; |
| PARTIAL_CLEARING | &quot;partial_clearing&quot; |
| RECURRING_SUBSCRIPTION | &quot;recurring_subscription&quot; |
| FX_CONVERSION | &quot;fx_conversion&quot; |
| AMEX_AUTH_BASELINE | &quot;amex_auth_baseline&quot; |
| AMEX_AUTH_ADJUSTMENT | &quot;amex_auth_adjustment&quot; |
| AMEX_AUTH_REVERSAL | &quot;amex_auth_reversal&quot; |
| UBER_CANADA_REVERSAL | &quot;uber_canada_reversal&quot; |
| UBER_NETHERLANDS_POSITIVE_ADVICE | &quot;uber_netherlands_positive_advice&quot; |
| UBER_TURKEY_DUAL_AUTH | &quot;uber_turkey_dual_auth&quot; |
| UBER_BRAZIL_CANCELLED | &quot;uber_brazil_cancelled&quot; |
| UBER_KOREA_SETTLE_AT_AUTH | &quot;uber_korea_settle_at_auth&quot; |
| UBER_TURKEY_FX_REVERSAL | &quot;uber_turkey_fx_reversal&quot; |
| UBER_INDIA_FX_CLEARING | &quot;uber_india_fx_clearing&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: NetworkEnum

| Name | Value |
|---- | -----|
| VISA | &quot;VISA&quot; |
| MASTERCARD | &quot;MASTERCARD&quot; |
| AMEX | &quot;AMEX&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: SourceEnum

| Name | Value |
|---- | -----|
| AMEXGL | &quot;amexgl&quot; |
| AMEXRTA | &quot;amexrta&quot; |
| SMARTDATA | &quot;smartdata&quot; |
| TNS | &quot;tns&quot; |
| VBDS | &quot;vbds&quot; |
| VCF | &quot;vcf&quot; |
| VOP | &quot;vop&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



