

# BankLinkLastMintFailure

Present once a completion attempt was aborted because card creation (the `bank-feed` mint) failed for one or more discovered credit accounts. The link stays pending and completion is retried; a later successful completion clears this field (an expired link keeps its last failure). `transient` failures resolve on retry; a `permanent` one keeps aborting until investigated. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**at** | **OffsetDateTime** | When the failed completion attempt happened, in UTC, following ISO 8601 format. |  [optional] |
|**kind** | [**KindEnum**](#KindEnum) | Whether the underlying failure is expected to clear on retry. |  [optional] |
|**failedAccountCount** | **Integer** | How many discovered credit accounts failed to mint on the attempt. |  [optional] |



## Enum: KindEnum

| Name | Value |
|---- | -----|
| PERMANENT | &quot;permanent&quot; |
| TRANSIENT | &quot;transient&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



