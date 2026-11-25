

# BankSubscription


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **UUID** | The unique identifier of the bank subscription. |  |
|**accountId** | **UUID** | The unique identifier of the account that owns this subscription. |  |
|**subaccountId** | **UUID** | The unique identifier of the subaccount associated with this subscription. |  |
|**connectionId** | **String** | The provider&#39;s unique connection identifier (e.g. Plaid item ID). |  |
|**provider** | [**ProviderEnum**](#ProviderEnum) | The banking data provider for this subscription. - &#x60;PLAID&#x60;: Connected through Plaid. - &#x60;MASTERCARD_US&#x60;: Connected through Mastercard Open Finance US. Sandbox only. - &#x60;MASTERCARD_EU&#x60;: Connected through Mastercard Open Finance Europe. Sandbox only.  |  |
|**institutionName** | **String** | The name of the connected financial institution. |  |
|**state** | [**StateEnum**](#StateEnum) | Current state of the subscription. - &#x60;active&#x60;: The connection is functioning normally. - &#x60;error&#x60;: The connection has encountered an error. - &#x60;auth_required&#x60;: The connection requires user re-authentication. - &#x60;disconnected&#x60;: The connection has been disconnected.  |  |
|**webhook** | [**BankSubscriptionWebhook**](BankSubscriptionWebhook.md) |  |  |
|**syncCursor** | **String** | The current transaction sync cursor for this subscription. |  |
|**lastSyncedAt** | **OffsetDateTime** | Last successful sync of this connection, in UTC, following ISO 8601 format. Null until the first sync. |  [optional] |
|**authErrorFirstSeenAt** | **OffsetDateTime** | When a sync first found that the bank needs the user to sign in again, in UTC, following ISO 8601 format. Null when no such error is open. |  [optional] |
|**institutionLastCheckedAt** | **OffsetDateTime** | When the bank data provider last reached the bank for this connection, in UTC, following ISO 8601 format. Null when the provider gives no such time. |  [optional] |
|**institutionLastFailedAt** | **OffsetDateTime** | When the bank data provider last failed to reach the bank for this connection, in UTC, following ISO 8601 format. Null when the provider gives no such time. |  [optional] |
|**institutionHealth** | [**BankSubscriptionInstitutionHealth**](BankSubscriptionInstitutionHealth.md) |  |  [optional] |
|**createdAt** | **OffsetDateTime** | Date and time when this resource was created, in UTC, following ISO 8601 format. |  |
|**updatedAt** | **OffsetDateTime** | Date and time when this resource was last updated, in UTC, following ISO 8601 format. |  |



## Enum: ProviderEnum

| Name | Value |
|---- | -----|
| PLAID | &quot;PLAID&quot; |
| MASTERCARD_US | &quot;MASTERCARD_US&quot; |
| MASTERCARD_EU | &quot;MASTERCARD_EU&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| ACTIVE | &quot;active&quot; |
| ERROR | &quot;error&quot; |
| AUTH_REQUIRED | &quot;auth_required&quot; |
| DISCONNECTED | &quot;disconnected&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



