

# EnrollmentLinkCreateValidationProblem


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**title** | [**TitleEnum**](#TitleEnum) |  |  |
|**detail** | **String** |  |  |
|**code** | [**CodeEnum**](#CodeEnum) |  |  [optional] |
|**errors** | [**List&lt;ValidationProblemItem&gt;**](ValidationProblemItem.md) |  |  [optional] |



## Enum: TitleEnum

| Name | Value |
|---- | -----|
| BAD_REQUEST | &quot;Bad Request&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: CodeEnum

| Name | Value |
|---- | -----|
| LINK_NAME_NOT_ALLOWED | &quot;LINK_NAME_NOT_ALLOWED&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



