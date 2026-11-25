

# BankTransactionWebhooksResult


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**message** | **String** | Human-readable summary of the operation. |  |
|**emitted** | **Integer** | Number of webhook events emitted. |  |
|**resent** | **Integer** | Number of webhooks resent. |  |
|**matched** | **Integer** | Number of transactions that were matched. |  |
|**skipped** | **Integer** | Number of transactions that were skipped. |  |
|**failed** | [**List&lt;BankTransactionWebhookFailure&gt;**](BankTransactionWebhookFailure.md) | Transactions that failed to process. |  |
|**dispatched** | **Integer** | Number of standalone bank-only emissions dispatched for still-unmatched transactions. Only present when &#x60;dispatchUnmatched&#x60; was requested (and on service versions that support the flag). |  [optional] |
|**dispatchError** | **String** | Present when the standalone dispatch step failed after a successful re-match; the matching results above still stand. |  [optional] |



