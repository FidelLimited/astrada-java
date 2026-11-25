

# Card


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**CardLinks**](CardLinks.md) |  |  |
|**embedded** | [**CardEmbedded**](CardEmbedded.md) |  |  [optional] |
|**id** | **UUID** | The unique identifier of this card entity. |  |
|**subaccountId** | **UUID** | The unique identifier of the subaccount related to this entity. |  |
|**expiryMonth** | **Integer** | The month that the card will expire in MM format. |  |
|**expiryYear** | **Integer** | The year that the card will expire in YYYY format. |  |
|**first6digits** | **String** | The first 6 digits of the card number (PAN). Nullable for bank-derived cards (created from a bank connection, where the full PAN is never seen); populated when known (e.g. the SDK captured the BIN before pivoting to the bank flow) and attached when the card is later PAN-enrolled (upgrade-in-place keeps the same card id).  |  [optional] |
|**last4digits** | **String** | The last 4 digits of the card number (PAN). |  |
|**network** | [**NetworkEnum**](#NetworkEnum) | The card network that processes payments for this card. &#x60;amex&#x60; and &#x60;null&#x60; occur only on bank-derived cards: Amex-institution connections mint &#x60;amex&#x60;; other institutions cannot reveal the network from bank data, so it stays &#x60;null&#x60; until the card is PAN-enrolled (upgrade-in-place).  |  [optional] |
|**country** | [**CountryEnum**](#CountryEnum) | The country of issuance for this card following ISO 3166 alpha-3. |  |
|**createdAt** | **OffsetDateTime** | Date and time when this resource was created, in UTC, following ISO 8601 format. |  |
|**updatedAt** | **OffsetDateTime** | Date and time when this resource was last updated, in UTC, following ISO 8601 format. |  |



## Enum: NetworkEnum

| Name | Value |
|---- | -----|
| VISA | &quot;visa&quot; |
| MASTERCARD | &quot;mastercard&quot; |
| AMEX | &quot;amex&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: CountryEnum

| Name | Value |
|---- | -----|
| USA | &quot;USA&quot; |
| CAN | &quot;CAN&quot; |
| GBR | &quot;GBR&quot; |
| SWE | &quot;SWE&quot; |
| ITA | &quot;ITA&quot; |
| AUS | &quot;AUS&quot; |
| NZL | &quot;NZL&quot; |
| PRT | &quot;PRT&quot; |
| BEL | &quot;BEL&quot; |
| SGP | &quot;SGP&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



