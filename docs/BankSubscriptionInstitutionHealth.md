

# BankSubscriptionInstitutionHealth

The bank data provider's health figures for this connection's bank. Null when the provider publishes none.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**success** | **BigDecimal** | The share of the provider&#39;s recent transaction updates for this bank that succeeded, from 0 to 1. |  [optional] |
|**errorInstitution** | **BigDecimal** | The share that failed because of the bank, from 0 to 1. |  [optional] |
|**errorProvider** | **BigDecimal** | The share that failed because of the provider, from 0 to 1. |  [optional] |
|**refreshInterval** | [**RefreshIntervalEnum**](#RefreshIntervalEnum) | Whether the provider is refreshing this bank on schedule (&#x60;NORMAL&#x60;), late (&#x60;DELAYED&#x60;) or not at all (&#x60;STOPPED&#x60;). |  [optional] |
|**incidents** | [**List&lt;BankInstitutionIncident&gt;**](BankInstitutionIncident.md) | The provider&#39;s published outages and maintenance for this bank. |  [optional] |
|**observedAt** | **OffsetDateTime** | When Astrada read these figures, in UTC, following ISO 8601 format. |  [optional] |



## Enum: RefreshIntervalEnum

| Name | Value |
|---- | -----|
| NORMAL | &quot;NORMAL&quot; |
| DELAYED | &quot;DELAYED&quot; |
| STOPPED | &quot;STOPPED&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



