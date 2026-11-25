

# EnrollmentLinkSession

A session started from an enrollment link, as the hosted page ran it.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **UUID** | The session&#39;s identifier. |  |
|**state** | [**StateEnum**](#StateEnum) | - &#x60;open&#x60;: started and not yet completed. - &#x60;completed&#x60;: the page reported the session&#39;s outcome. - &#x60;expired&#x60;: an open session past &#x60;expiresAt&#x60;. It can no longer complete.  |  |
|**startedAt** | **OffsetDateTime** |  |  |
|**expiresAt** | **OffsetDateTime** | 60 minutes after &#x60;startedAt&#x60;. |  |
|**completedAt** | **OffsetDateTime** |  |  [optional] |
|**outcome** | [**EnrollmentLinkSessionOutcome**](EnrollmentLinkSessionOutcome.md) |  |  [optional] |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| OPEN | &quot;open&quot; |
| COMPLETED | &quot;completed&quot; |
| EXPIRED | &quot;expired&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



