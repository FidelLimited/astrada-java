

# WebhookCreateResult


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**SelfLinks**](SelfLinks.md) |  |  |
|**id** | **UUID** |  |  |
|**url** | **URI** |  |  |
|**description** | **String** |  |  |
|**eventTypes** | [**List&lt;EventTypesEnum&gt;**](#List&lt;EventTypesEnum&gt;) |  |  |
|**subaccountId** | **UUID** |  |  |
|**createdAt** | **OffsetDateTime** | Date and time when this resource was created, in UTC, following ISO 8601 format. |  |
|**updatedAt** | **OffsetDateTime** | Date and time when this resource was last updated, in UTC, following ISO 8601 format. |  |
|**enabled** | **Boolean** |  |  [optional] |
|**secret** | **String** | Only returned on Webhook creation |  |



## Enum: List&lt;EventTypesEnum&gt;

| Name | Value |
|---- | -----|
| CARDSUBSCRIPTION_CREATED | &quot;cardsubscription.created&quot; |
| CARDSUBSCRIPTION_UPDATED | &quot;cardsubscription.updated&quot; |
| CARDHOLDER_UPDATED | &quot;cardholder.updated&quot; |
| TRANSACTION_CREATED | &quot;transaction.created&quot; |
| TRANSACTION_UPDATED | &quot;transaction.updated&quot; |
| TRANSACTIONMESSAGE_CREATED | &quot;transactionmessage.created&quot; |
| NETWORKBULKFEED_STATECHANGED | &quot;networkbulkfeed.statechanged&quot; |
| ENRICHMENT_MERCHANT_CREATED | &quot;enrichment.merchant.created&quot; |
| BANKLINK_COMPLETED | &quot;banklink.completed&quot; |
| BANKACCOUNT_STATE_CHANGED | &quot;bankaccount.state_changed&quot; |
| BANKTRANSACTION_CREATED | &quot;banktransaction.created&quot; |
| BANKTRANSACTION_DELETED | &quot;banktransaction.deleted&quot; |
| TRANSACTION_MATCH_CREATED | &quot;transaction.match.created&quot; |
| TRANSACTION_MATCH_DELETED | &quot;transaction.match.deleted&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



