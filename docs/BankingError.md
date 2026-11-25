

# BankingError

The error body of the Banking API: plain JSON with `statusCode` and `message`. The card APIs use a different body (`title` and `detail`). Branch on `code` where a refusal carries one; `message` is text for people.  One exception: a `401` for a missing or invalid token comes from the gateway, before the request reaches the Banking API, and carries `title` and `detail` like the card APIs. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**statusCode** | **Integer** | The HTTP status code of the answer, repeated in the body. |  |
|**message** | **String** | What went wrong, in words. Some messages carry ids or states, so do not parse it. |  |
|**code** | **String** | A stable name for the refusal, such as &#x60;SUBACCOUNT_NOT_FOUND&#x60;. Present on some refusals only. |  [optional] |
|**error** | **String** | The name of the HTTP status, such as &#x60;Not Found&#x60;. Present on some refusals only. |  [optional] |
|**reason** | **String** | Why the refusal happened, on the refusals that name one. For example, &#x60;BANK_ENROLLMENT_DISABLED&#x60; carries &#x60;network-not-allowed&#x60; when the account&#39;s setting does not allow a bank link for that card network. |  [optional] |
|**errors** | **List&lt;Object&gt;** | The validation problems, on a &#x60;400&#x60; for a request that did not pass validation. Each item has a &#x60;path&#x60;, the place of the field in the request; its other fields come from the validation library and can change. |  [optional] |



