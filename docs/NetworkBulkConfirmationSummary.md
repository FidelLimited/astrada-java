

# NetworkBulkConfirmationSummary

The `200` body of `POST /network-bulk-confirmations` when the request's program already has an open confirmation request that another request wrote. A program is the calling account, the `subaccountId`, the `network` and the `bin`. The summary names the open request and leaves out its contact details: no names, emails, bank contact, `binInfo` or `grade`. It is plain JSON with no `_links`, as a confirmation has no GET. `alreadyOpen` is always `true` here, and the full confirmation never carries it, a replay of your own `requestId` included. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**alreadyOpen** | **Boolean** | Always &#x60;true&#x60;. Tells this summary apart from the full confirmation. |  |
|**id** | **UUID** | The unique identifier of the open confirmation request. |  |
|**requestId** | **UUID** | The &#x60;requestId&#x60; this request sent. It is never the &#x60;requestId&#x60; the open request was stored under. |  |
|**subaccountId** | **UUID** | The subaccount of the open request, the same as the request&#39;s. |  |
|**network** | [**NetworkEnum**](#NetworkEnum) | The network of the open request, the same as the request&#39;s. |  |
|**state** | [**StateEnum**](#StateEnum) | The state of the open request. &#x60;requested&#x60; is the only state for now. |  |
|**createdAt** | **OffsetDateTime** | When the open request was created, in UTC, following ISO 8601 format. Its 30-day window counts from this time. |  |



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
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



