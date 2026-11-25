

# BankLinkLastRejection

Present while `state` is `pending` after a completion attempt was refused because the connected institution is not allowed for this link (for example, a non-Amex institution on an Amex-restricted link). The link can still be completed by reconnecting with an allowed institution; a successful completion clears this field. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**institutionName** | **String** | Display name of the refused institution. |  [optional] |
|**at** | **OffsetDateTime** | When the refusal happened, in UTC, following ISO 8601 format. |  [optional] |



