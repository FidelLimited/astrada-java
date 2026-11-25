

# Transaction


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**TransactionLinks**](TransactionLinks.md) |  |  |
|**embedded** | [**TransactionEmbedded**](TransactionEmbedded.md) |  |  [optional] |
|**acceptor** | [**TransactionAcceptor**](TransactionAcceptor.md) |  |  |
|**cardId** | **UUID** | The unique identifier of the card related to this entity. Null only for bank-sourced transactions on a bank account with no linked cards (the card-less unified feed); the &#x60;_links.card&#x60; link is omitted in that case. |  |
|**cardholderBillingCurrency** | **String** | The currency of the country of issuance following ISO 4217 alpha-3 format. |  |
|**cardholderBillingHoldAmount** | **Double** | This amount models the hold on the cardholder account, in the card&#39;s issued currency, that occurs after an authorization and before a financial message. This amount will change as both authorization and financial messages are added to the Transaction in order to reflect the remaining hold amount. A pending refund (an approved credit authorization) reflects its signed amount here as a negative hold while pending, released back to zero once the clearing financial message settles it. |  |
|**cardholderBillingSettledAmount** | **Double** | The settled amount models the bottom line impact to the customer&#39;s bank statement in the card&#39;s issued currency. This amount will change as financial messages are added to the Transaction. |  |
|**controlAccountNumber** | **String** | The American Express Basic Control Account number associated with the card, as provided by the Amex real-time authorization (RTA) and General Ledger (GL) feeds. Treat it as an opaque string and keep it exactly as returned: it is numeric today and may contain leading zeros, so never parse it as a number. Present only on American Express transactions where the feed supplies it; absent for all other networks and for transactions ingested before this field was introduced. It is never returned as null or an empty string. This is an account identifier, not a card number. |  [optional] |
|**descriptor** | **String** | Cleansed merchant name. |  |
|**id** | **UUID** | The unique identifier of this transaction entity. |  |
|**network** | **String** | The card network that processed this payment. |  |
|**networkBulkFeedId** | **UUID** | The network bulk feed that delivered this transaction&#39;s first feed-carrying message (SmartData for Mastercard, VBDS for Visa clearing). Present only on transactions sourced from a network bulk feed; absent otherwise. Transactions delivered before the producers began emitting this field, or never delivered via a bulk feed, omit it entirely. It is never returned as null or an empty string. |  [optional] |
|**subaccountId** | **UUID** | The unique identifier of the subaccount related to this entity. |  |
|**supersededMessageIds** | **List&lt;UUID&gt;** | Message ids from &#x60;_links.messages&#x60; whose financial contribution was superseded during aggregation; typically duplicate deliveries of the same clearing across network feeds, or a provisional bank-sourced settlement replaced by the network clearing. Superseded messages stay listed in &#x60;_links.messages&#x60; for audit but do not count toward the settled amounts, so exclude them when deriving totals from individual messages (or better, use &#x60;transactionSettledAmount&#x60; / &#x60;cardholderBillingSettledAmount&#x60; directly). Always a subset of &#x60;_links.messages&#x60;. Each payload carries the full current set (a snapshot, not a delta). Transactions last updated before this field was introduced report &#x60;[]&#x60; until their next update. |  [optional] |
|**status** | [**StatusEnum**](#StatusEnum) | A semantic understanding of the state of the transaction within the transaction lifecycle. &#x60;VOIDED&#x60; is not final: a clearing can still arrive after a full reversal, and the transaction then moves to &#x60;SETTLED&#x60; at the cleared amount. [Learn More](https://docs.astrada.co/docs/transaction) |  |
|**transactionCurrency** | **String** | The currency in which the transaction took place following ISO 4217 alpha-3 format. |  |
|**transactionHoldAmount** | **Double** | This amount models the hold on the cardholder account, in the currency the transaction was conducted in, that occurs after an authorization and before a financial message. This amount will change as both authorization and financial messages are added to the Transaction in order to reflect the remaining hold amount. A pending refund (an approved credit authorization) reflects its signed amount here as a negative hold while pending, released back to zero once the clearing financial message settles it. |  |
|**transactionOccurrenceDate** | **LocalDate** | The UTC date when the transaction took place, following ISO 8601 format (YYYY-MM-DD). This is a date-only field with no time component. |  |
|**transactionSettledAmount** | **Double** | The settled amount models the bottom line impact to the customer&#39;s bank statement in the currency the transaction was conducted in. This amount will change as financial messages are added to the Transaction. |  |
|**createdAt** | **OffsetDateTime** | Date and time when this resource was created, in UTC, following ISO 8601 format. |  |
|**updatedAt** | **OffsetDateTime** | Date and time when this resource was last updated, in UTC, following ISO 8601 format. |  |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| PENDING | &quot;PENDING&quot; |
| VOIDED | &quot;VOIDED&quot; |
| DECLINED | &quot;DECLINED&quot; |
| SETTLED | &quot;SETTLED&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



