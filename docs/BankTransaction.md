

# BankTransaction


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**BankTransactionLinks**](BankTransactionLinks.md) |  |  |
|**id** | **UUID** | The unique identifier of the bank transaction. |  |
|**accountId** | **UUID** | The unique identifier of the account that owns this transaction. |  |
|**subaccountId** | **UUID** | The unique identifier of the subaccount associated with this transaction. |  |
|**bankAccountId** | **UUID** | The unique identifier of the bank account this transaction belongs to. |  |
|**cardId** | **UUID** | The card whose card transaction this bank transaction is matched to. Stamped when a match is persisted and cleared if a later forced rematch supersedes that match, so it is only ever non-null alongside &#x60;matchId&#x60;. Null while unmatched, including on accounts with linked cards: linking alone never attributes individual bank transactions.  |  |
|**matchId** | **UUID** | The identifier of the transaction match, if this bank transaction has been matched to a card transaction. |  |
|**date** | **LocalDate** | The date the transaction was posted, in YYYY-MM-DD format. |  |
|**amount** | **BigDecimal** | The transaction amount. |  |
|**currency** | **String** | The currency of the transaction following ISO 4217 format. |  |
|**description** | **String** | The transaction description provided by the financial institution. |  |
|**merchantName** | **String** | The name of the merchant. |  |
|**category** | **List&lt;String&gt;** | The transaction categories assigned by the provider. |  |
|**transactionType** | [**TransactionTypeEnum**](#TransactionTypeEnum) | The type of transaction. - &#x60;debit&#x60;: Money sent (outflow). - &#x60;credit&#x60;: Money received (inflow).  |  |
|**source** | [**SourceEnum**](#SourceEnum) | The source of this transaction. - &#x60;PLAID&#x60;: Synced from a connected financial institution via Plaid. - &#x60;UPLOAD&#x60;: Imported from a manually uploaded bank statement. - &#x60;MASTERCARD_US&#x60;: Synced from a connected financial institution via Mastercard Open Finance US. Sandbox only. - &#x60;MASTERCARD_EU&#x60;: Synced from a connected financial institution via Mastercard Open Finance Europe. Sandbox only.  |  |
|**status** | [**StatusEnum**](#StatusEnum) | The lifecycle stage of the bank movement, derived from its match pointer and settlement state. - &#x60;posted&#x60;: Synced, not yet matched. - &#x60;matched&#x60;: Paired to a card transaction without an early settlement of its own (e.g. connector off, or a below-HIGH match): the movement is represented by the card transaction&#39;s network settlement, and any standalone it previously emitted is netted to zero. - &#x60;settled&#x60;: A bank-only movement settled standalone on the unified feed. - &#x60;early_cleared&#x60;: A matched movement settled its card transaction early. - &#x60;superseded&#x60;: A previously bank-only movement was retracted or corrected.  |  |
|**settlement** | [**BankTransactionSettlement**](BankTransactionSettlement.md) |  |  |
|**createdAt** | **OffsetDateTime** | Date and time when this resource was created, in UTC, following ISO 8601 format. |  |



## Enum: TransactionTypeEnum

| Name | Value |
|---- | -----|
| DEBIT | &quot;debit&quot; |
| CREDIT | &quot;credit&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: SourceEnum

| Name | Value |
|---- | -----|
| PLAID | &quot;PLAID&quot; |
| UPLOAD | &quot;UPLOAD&quot; |
| MASTERCARD_US | &quot;MASTERCARD_US&quot; |
| MASTERCARD_EU | &quot;MASTERCARD_EU&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| POSTED | &quot;posted&quot; |
| MATCHED | &quot;matched&quot; |
| SETTLED | &quot;settled&quot; |
| EARLY_CLEARED | &quot;early_cleared&quot; |
| SUPERSEDED | &quot;superseded&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



