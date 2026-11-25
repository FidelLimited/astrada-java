

# BankAccountBackfillResult


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **UUID** | The bank account identifier. |  |
|**transactionsProcessed** | **Integer** | Unmatched bank transactions in the window that matching looked at, whether imported by this call or already stored. |  |
|**matchesFound** | **Integer** | Matches found against the account&#39;s linked cards during the backfill. |  |



