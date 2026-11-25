

# NetworkBulkEnrollmentOpen

Whether the card program has an open kickoff, the answer of `GET /network-bulk-enrollments/open`. When it has none, the body is `{ \"open\": false }` and nothing else. When it has one, the body carries only that kickoff's `enrollmentId`, `state` and `requestedAt`: no names, emails, bank contact, `binInfo`, consent or outreach, as the kickoff may have been sent by someone else. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**open** | **Boolean** | Whether the program has an open kickoff, by the same rule &#x60;POST /network-bulk-enrollments&#x60; uses. |  |
|**enrollmentId** | **UUID** | The open kickoff&#39;s identifier, the &#x60;id&#x60; that &#x60;POST /network-bulk-enrollments&#x60; answers with when it finds the same kickoff. Present only when &#x60;open&#x60; is &#x60;true&#x60;.  |  [optional] |
|**state** | [**StateEnum**](#StateEnum) | The open kickoff&#39;s state. &#x60;requested&#x60;: it was created less than 90 days ago and no feed is registered yet. &#x60;feed-created&#x60;: its feed is registered and live. Present only when &#x60;open&#x60; is &#x60;true&#x60;.  |  [optional] |
|**requestedAt** | **OffsetDateTime** | When the open kickoff was created, in UTC, following ISO 8601 format. Present only when &#x60;open&#x60; is &#x60;true&#x60;.  |  [optional] |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| REQUESTED | &quot;requested&quot; |
| FEED_CREATED | &quot;feed-created&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



