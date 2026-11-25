

# TransactionMatch


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**TransactionMatchLinks**](TransactionMatchLinks.md) |  |  |
|**id** | **UUID** | The unique identifier of the transaction match. |  |
|**accountId** | **UUID** | The unique identifier of the account that owns this match. |  |
|**subaccountId** | **UUID** | The unique identifier of the subaccount associated with this match. |  |
|**cardTransactionId** | **UUID** | The identifier of the matched card transaction message (the message the bank posting was matched against, for example the authorization). It is the &#x60;id&#x60; carried by &#x60;transactionmessage.created&#x60; and appears in &#x60;_embedded.messages[].id&#x60; on the unified transaction; it is not the unified transaction&#39;s own &#x60;id&#x60;. |  |
|**bankTransactionId** | **UUID** | The unique identifier of the matched bank transaction. |  |
|**confidence** | [**ConfidenceEnum**](#ConfidenceEnum) | The confidence level of the match. - &#x60;HIGH&#x60;: Score &gt;&#x3D; 0.70 - &#x60;MEDIUM&#x60;: Score &gt;&#x3D; 0.50 - &#x60;LOW&#x60;: Score &gt;&#x3D; 0.20  |  |
|**score** | **BigDecimal** | The numeric match score between 0 and 1, representing how closely the bank and card transactions match. |  |
|**cardTransaction** | [**TransactionMatchCardTransaction**](TransactionMatchCardTransaction.md) |  |  |
|**bankTransaction** | [**TransactionMatchBankTransaction**](TransactionMatchBankTransaction.md) |  |  |
|**reasons** | [**List&lt;TransactionMatchReason&gt;**](TransactionMatchReason.md) | Breakdown of the scoring factors that contributed to the match score. |  |
|**createdAt** | **OffsetDateTime** | Date and time when this match was created, in UTC, following ISO 8601 format. |  |



## Enum: ConfidenceEnum

| Name | Value |
|---- | -----|
| HIGH | &quot;HIGH&quot; |
| MEDIUM | &quot;MEDIUM&quot; |
| LOW | &quot;LOW&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



