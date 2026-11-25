

# BankLinkSyncParams


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**webhooks** | [**List&lt;WebhooksEnum&gt;**](#List&lt;WebhooksEnum&gt;) | The events this sync sends. Default (field left out) is both. An empty array sends neither; transactions are still stored and matched. Names outside this list are ignored, not refused. |  [optional] |
|**force** | **Boolean** | Set to &#x60;true&#x60; to force a full sync even if no new transactions are detected. With &#x60;true&#x60;:  - Stored transactions: every transaction already stored for the link is gone over again, and &#x60;banktransaction.created&#x60; is sent again for each when &#x60;webhooks&#x60; allows it. - Existing matches: scored again, and &#x60;transaction.match.created&#x60; sent again, only when &#x60;webhooks&#x60; lists &#x60;transaction.match.created&#x60;. With &#x60;webhooks&#x60; left out they are kept and not sent again. - Unmatched transactions: whatever &#x60;webhooks&#x60; lists, a stored transaction with no match yet goes through matching, and a new match is sent when &#x60;webhooks&#x60; allows it.  |  [optional] |



## Enum: List&lt;WebhooksEnum&gt;

| Name | Value |
|---- | -----|
| BANKTRANSACTION_CREATED | &quot;banktransaction.created&quot; |
| TRANSACTION_MATCH_CREATED | &quot;transaction.match.created&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



