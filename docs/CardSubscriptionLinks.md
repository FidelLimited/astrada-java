

# CardSubscriptionLinks


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**self** | [**Link**](Link.md) |  |  |
|**card** | [**Link**](Link.md) |  |  |
|**verifications** | [**Link**](Link.md) | The link to this card&#39;s 3-D Secure verifications (&#x60;/card-verifications/3ds?cardId&#x3D;&#x60;). Useful when a subscription is stuck at &#x60;reqSCA&#x60; to inspect why verification failed.  |  [optional] |
|**networkBulkFeed** | [**Link**](Link.md) | The link to the network bulk feed associated with this card subscription, if applicable. This link may be present when the &#x60;enrollmentType&#x60; is &#x60;network-bulk&#x60;. It will not be present for subscriptions created through the &#x60;cardholder-single&#x60; flow.  |  [optional] |
|**bankAccount** | [**Link**](Link.md) | The link to the bank account this subscription&#39;s card was materialized from (&#x60;/bank-accounts/{bankAccountId}&#x60;). Present only when the &#x60;enrollmentType&#x60; is &#x60;bank-feed&#x60;; the bank-connected analog of &#x60;networkBulkFeed&#x60;. Not present for &#x60;cardholder-single&#x60; or &#x60;network-bulk&#x60; subscriptions.  |  [optional] |



