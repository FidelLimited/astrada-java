

# ConsentPolicy

Which consent records `POST /card-subscriptions` accepts for this subaccount. Astrada sets it, on the account as a default and on the subaccount as an override; there is no route to change it. The value is the effective one: each field comes from the subaccount when it sets one, otherwise from the account, except that an account `mode` of `off` turns the mode off for every subaccount. Absent (never `null`) when neither level sets a field.  A subaccount with no `consentPolicy` behaves as `log` with every channel allowed. Which mode a new or existing account starts in: [When consent is required](https://docs.astrada.co/docs/direct-api-card-enrollment-integration#when-consent-is-required). 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**allowedChannels** | [**Set&lt;AllowedChannelsEnum&gt;**](#Set&lt;AllowedChannelsEnum&gt;) | The channels a consent may come from. A consent from any other channel fails with &#x60;consent_channel_not_allowed&#x60;. |  [optional] |
|**mode** | [**ModeEnum**](#ModeEnum) | &#x60;off&#x60; records nothing, &#x60;log&#x60; records without refusing, &#x60;enforce&#x60; refuses a missing or failing consent. What each mode answers: [When consent is required](https://docs.astrada.co/docs/direct-api-card-enrollment-integration#when-consent-is-required).  |  [optional] |



## Enum: Set&lt;AllowedChannelsEnum&gt;

| Name | Value |
|---- | -----|
| SDK_UNIFIED | &quot;sdk-unified&quot; |
| SDK_LEGACY | &quot;sdk-legacy&quot; |
| API | &quot;api&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: ModeEnum

| Name | Value |
|---- | -----|
| OFF | &quot;off&quot; |
| LOG | &quot;log&quot; |
| ENFORCE | &quot;enforce&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



