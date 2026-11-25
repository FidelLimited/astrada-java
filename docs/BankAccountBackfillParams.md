

# BankAccountBackfillParams


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**startDate** | **LocalDate** | Start of the range in YYYY-MM-DD format. Defaults to 90 days before &#x60;endDate&#x60;. |  [optional] |
|**endDate** | **LocalDate** | End of the range in YYYY-MM-DD format. Defaults to today. |  [optional] |
|**loadHistoric** | **Boolean** | Mastercard accounts at US banks (&#x60;provider: mastercard&#x60;, &#x60;countryCode: USA&#x60;) only. Triggers the provider&#39;s billable deep-history generation (up to 2 years) for this account before the import. Generation is asynchronous; re-run the window without the flag if rows are missing. Providers without the capability return &#x60;400&#x60;. |  [optional] |



