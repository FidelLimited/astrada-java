

# CardSubscriptionUpdateParams


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**state** | [**StateEnum**](#StateEnum) |  |  [optional] |
|**deactivationReason** | **CardSubscriptionDeactivationReason** | Optional reason for the deactivation. Only accepted together with &#x60;state: deactivated&#x60;; sending it on its own returns &#x60;400&#x60;. Returned in the &#x60;deactivationReason&#x60; field of the card subscription.  |  [optional] |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| DEACTIVATED | &quot;deactivated&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



