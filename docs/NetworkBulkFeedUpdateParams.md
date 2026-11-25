

# NetworkBulkFeedUpdateParams


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**transactionStartDate** | **LocalDate** | Transactions posted on or after this calendar date (&#x60;YYYY-MM-DD&#x60;, a valid date, not in the future) are taken in for this feed&#39;s deliveries, replacing the default window, which keeps only transactions posted on or after the date 15 days before each file&#39;s delivery date (in UTC). On a file that serves several subaccounts, the earliest window among them applies to the whole file. Currently supported for &#x60;SMARTDATA&#x60; feeds only. The comparison is calendar-day based in UTC: the network&#39;s posting date is a date-only field, so a value of e.g. &#x60;2026-06-30&#x60; includes every transaction whose posting date is 2026-06-30 or later, with no local-timezone adjustment.  |  [optional] |
|**expectedCardCount** | **Integer** | The expected number of cards that will be enrolled under this bulk feed. |  [optional] |



