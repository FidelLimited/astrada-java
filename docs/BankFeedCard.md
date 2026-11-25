

# BankFeedCard

A card created from a bank account at bank-link completion (a `bank-feed` enrollment), with its subscription.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **UUID** | The minted card&#39;s identifier on the cards platform. |  |
|**subscriptionId** | **UUID** | The bank-feed card subscription created by the mint. |  |
|**last4digits** | **String** | The card number&#39;s last 4; equals this account&#39;s mask by construction. |  |
|**network** | **String** | The card network when the institution implies it (e.g. &#x60;amex&#x60;); null until a PAN enrollment upgrades the card in place. |  |
|**enrollmentType** | [**EnrollmentTypeEnum**](#EnrollmentTypeEnum) | Always &#x60;bank-feed&#x60;; the enrollment transport this card was materialized through. |  |



## Enum: EnrollmentTypeEnum

| Name | Value |
|---- | -----|
| BANK_FEED | &quot;bank-feed&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



