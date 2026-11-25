

# NetworkBulkEnrollmentSummary

The `200` body of `POST /network-bulk-enrollments` when the request's program already has an open kickoff that another request wrote. A program is the calling account, the `subaccountId`, the `network` and the first 8 digits of the `bin`. The summary names the open kickoff and leaves out its contact details: no names, emails, bank contact, `binInfo`, consent or outreach. `alreadyOpen` is always `true` here, and the full enrollment never carries it, a replay of your own `requestId` included. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**NetworkBulkEnrollmentSummaryLinks**](NetworkBulkEnrollmentSummaryLinks.md) |  |  |
|**alreadyOpen** | **Boolean** | Always &#x60;true&#x60;. Tells this summary apart from the full enrollment. |  |
|**id** | **UUID** | The unique identifier of the open kickoff. |  |
|**subaccountId** | **UUID** | The subaccount of the open kickoff, the same as the request&#39;s. |  |
|**network** | [**NetworkEnum**](#NetworkEnum) | The network of the open kickoff, the same as the request&#39;s. |  |
|**state** | [**StateEnum**](#StateEnum) | The open kickoff&#39;s state. &#x60;requested&#x60;: it was created less than 90 days ago and no feed has been registered against it yet. &#x60;feed-created&#x60;: a feed was registered against it and that feed is live.  |  |
|**networkBulkFeedId** | **UUID** | The live feed registered against the open kickoff. &#x60;null&#x60; while the kickoff is &#x60;requested&#x60;. |  |
|**requestId** | **UUID** | The &#x60;requestId&#x60; this request sent, or &#x60;null&#x60; when it sent none. It is never the &#x60;requestId&#x60; the open kickoff was stored under.  |  |
|**createdAt** | **OffsetDateTime** | When the open kickoff was created, in UTC, following ISO 8601 format. Its 90-day window counts from this time. |  |



## Enum: NetworkEnum

| Name | Value |
|---- | -----|
| AMEX | &quot;AMEX&quot; |
| MASTERCARD | &quot;MASTERCARD&quot; |
| VISA | &quot;VISA&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| REQUESTED | &quot;requested&quot; |
| FEED_CREATED | &quot;feed-created&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



