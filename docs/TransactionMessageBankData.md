

# TransactionMessageBankData

Bank-side provenance, present only on bank-sourced messages (null otherwise). Deliberately slim; the bank's merchant string, dates and amounts map into the canonical fields above; `bankTransactionId` is the stable join key back to the bank feed. It equals `banktransaction.created.id` and `transaction.match.created.matches[].id`, and resolves via `GET /bank-transactions/{id}` for the full bank record.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**provider** | [**ProviderEnum**](#ProviderEnum) | Open banking provider that observed the bank transaction. &#x60;mastercard-open-finance&#x60; appears once the Mastercard rail is enabled for the subaccount; the payload shape is identical across providers. |  |
|**bankTransactionId** | **UUID** | The bank transaction entity id; the canonical, lifecycle-stable join key across every bank-data surface. |  |
|**personalFinanceCategory** | [**TransactionMessageBankDataPersonalFinanceCategory**](TransactionMessageBankDataPersonalFinanceCategory.md) |  |  [optional] |



## Enum: ProviderEnum

| Name | Value |
|---- | -----|
| PLAID | &quot;plaid&quot; |
| MASTERCARD_OPEN_FINANCE | &quot;mastercard-open-finance&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



