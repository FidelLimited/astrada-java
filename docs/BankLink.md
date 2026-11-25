

# BankLink


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**BankLinkLinks**](BankLinkLinks.md) |  |  |
|**id** | **String** | The client-provided identifier of the bank link. Must be alphanumeric with hyphens and underscores only. |  |
|**accountId** | **UUID** | The unique identifier of the account that owns this bank link. |  |
|**subaccountId** | **UUID** | The unique identifier of the subaccount associated with this bank link. |  |
|**state** | [**StateEnum**](#StateEnum) | Current state of the bank link. - &#x60;pending&#x60;: The link has been created and is awaiting user enrollment. - &#x60;completed&#x60;: The user has successfully connected their bank account. - &#x60;expired&#x60;: The link has expired without being completed.  |  |
|**provider** | [**ProviderEnum**](#ProviderEnum) | Open-banking provider this link was created on. Fixed at creation: every later operation on the link uses this value. Links created before provider selection existed are &#x60;plaid&#x60;.  |  [optional] |
|**hostedLink** | **String** | The hosted URL where the user can complete the bank enrollment. This link expires after 4 hours and is automatically refreshed when retrieved via GET. |  |
|**linkExpiresAt** | **OffsetDateTime** | Date and time when the hosted link expires, in UTC, following ISO 8601 format. |  |
|**completedAt** | **OffsetDateTime** | Date and time when the bank link enrollment was completed, in UTC, following ISO 8601 format. Only present when state is &#x60;completed&#x60;. |  [optional] |
|**lastRejection** | [**BankLinkLastRejection**](BankLinkLastRejection.md) |  |  [optional] |
|**lastMintFailure** | [**BankLinkLastMintFailure**](BankLinkLastMintFailure.md) |  |  [optional] |
|**filters** | [**BankLinkFilters**](BankLinkFilters.md) |  |  [optional] |
|**bankAccounts** | [**List&lt;BankLinkBankAccount&gt;**](BankLinkBankAccount.md) | Summary of bank accounts created from this link. Only present when state is &#x60;completed&#x60; and accounts exist. |  [optional] |
|**createdAt** | **OffsetDateTime** | Date and time when this resource was created, in UTC, following ISO 8601 format. |  |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| PENDING | &quot;pending&quot; |
| COMPLETED | &quot;completed&quot; |
| EXPIRED | &quot;expired&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: ProviderEnum

| Name | Value |
|---- | -----|
| PLAID | &quot;plaid&quot; |
| MASTERCARD | &quot;mastercard&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



