

# EnrollmentMethodsResultBinInfo

Best-effort card metadata for the looked-up BIN, resolved server-side. Present only when the issuer name is known and omitted otherwise; the other fields are best-effort and may each be omitted even when `binInfo` is present. Lets integrators read this data without the `cards:read` scope that `GET /bin-lookup` requires (it rides on this endpoint's `enrollment-methods:write` scope); the SDK surfaces it on the bulk-enrollment success payload.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**issuerName** | **String** | The issuing bank&#39;s name for this BIN. |  |
|**issuerCountry** | **String** | The country of the issuing bank following ISO 3166-1 alpha-3, for example &#x60;USA&#x60;. Omitted when the reference data does not record it. |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | The card type (e.g., COMMERCIAL, CORPORATE, CONSUMER, SMALL_BUSINESS). Omitted when the reference data does not classify the BIN. |  [optional] |
|**fundingType** | [**FundingTypeEnum**](#FundingTypeEnum) | The card funding type (e.g., prepaid, debit, credit). Omitted when the reference data does not classify the BIN. |  [optional] |
|**cardCategory** | [**CardCategoryEnum**](#CardCategoryEnum) | The card&#39;s classification, with the values of &#x60;cardCategory&#x60; on &#x60;GET /bin-lookup&#x60;. More specific than &#x60;type&#x60;, and set for many BINs where &#x60;type&#x60; is omitted. &#x60;UNKNOWN&#x60; means the card data does not classify the BIN. Omitted when there is no value. For Mastercard it is present only when the response falls back to stored card data. |  [optional] |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| COMMERCIAL | &quot;COMMERCIAL&quot; |
| CORPORATE | &quot;CORPORATE&quot; |
| CONSUMER | &quot;CONSUMER&quot; |
| SMALL_BUSINESS | &quot;SMALL_BUSINESS&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: FundingTypeEnum

| Name | Value |
|---- | -----|
| CREDIT | &quot;CREDIT&quot; |
| DEBIT | &quot;DEBIT&quot; |
| PREPAID | &quot;PREPAID&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: CardCategoryEnum

| Name | Value |
|---- | -----|
| CONSUMER | &quot;CONSUMER&quot; |
| BUSINESS | &quot;BUSINESS&quot; |
| CORPORATE | &quot;CORPORATE&quot; |
| FLEET | &quot;FLEET&quot; |
| PURCHASING | &quot;PURCHASING&quot; |
| GIFT | &quot;GIFT&quot; |
| PREPAID | &quot;PREPAID&quot; |
| SPECIALTY | &quot;SPECIALTY&quot; |
| UNKNOWN | &quot;UNKNOWN&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



