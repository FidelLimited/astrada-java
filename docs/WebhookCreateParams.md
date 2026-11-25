

# WebhookCreateParams


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**url** | **URI** |  |  |
|**description** | **String** |  |  [optional] |
|**eventTypes** | [**List&lt;EventTypesEnum&gt;**](#List&lt;EventTypesEnum&gt;) |  |  |
|**subaccountId** | **UUID** |  |  |
|**enabled** | **Boolean** |  |  [optional] |



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



