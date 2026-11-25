

# BankTransactionWebhooksParams


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**ids** | **List&lt;UUID&gt;** | List of bank transaction identifiers to emit webhooks for. Must contain at least one ID. |  |
|**webhooks** | [**List&lt;WebhooksEnum&gt;**](#List&lt;WebhooksEnum&gt;) | The types of webhook events to emit. Leave it out and only &#x60;banktransaction.created&#x60; is sent. |  [optional] |
|**forceRematch** | **Boolean** | Set to &#x60;true&#x60; to re-run the matching algorithm for the named transactions before emitting webhooks. Transactions that already have a match are genuinely re-scored: the existing match id is preserved when the pairing survives, updated when a better pairing exists, and removed when the movement no longer matches. |  [optional] |
|**dispatchUnmatched** | **Boolean** | After the re-match, emit the standalone bank-only feed entry for each named transaction the matcher left unmatched (idempotent per transaction; already-emitted standalones are skipped). Requires &#x60;forceRematch: true&#x60; and &#x60;webhooks&#x60; to include &#x60;transaction.match.created&#x60;; the request is rejected with &#x60;400&#x60; otherwise, because only a same-run re-match can distinguish a genuinely unmatched movement from a duplicate of an already-settled purchase. |  [optional] |



## Enum: List&lt;WebhooksEnum&gt;

| Name | Value |
|---- | -----|
| BANKTRANSACTION_CREATED | &quot;banktransaction.created&quot; |
| TRANSACTION_MATCH_CREATED | &quot;transaction.match.created&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



