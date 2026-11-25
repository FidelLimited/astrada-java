

# BankSubscriptionWebhook

Webhook configuration for this subscription.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**url** | **String** | The URL where webhook events will be delivered. |  |
|**status** | [**StatusEnum**](#StatusEnum) | The current status of the webhook configuration. - &#x60;pending&#x60;: The webhook URL has been set but not yet confirmed by the provider. - &#x60;confirmed&#x60;: The provider has acknowledged the webhook URL. - &#x60;none&#x60;: No webhook URL has been configured.  |  |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| PENDING | &quot;pending&quot; |
| CONFIRMED | &quot;confirmed&quot; |
| NONE | &quot;none&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



