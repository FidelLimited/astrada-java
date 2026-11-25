

# BankAccountUpdateParams


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**cardIds** | **List&lt;UUID&gt;** | The identifiers of cards to link to this bank account for transaction matching. Send an empty array to unlink all cards. Each card must belong to the same subaccount. |  |
|**backfill** | [**BankAccountCardsUpdateBackfill**](BankAccountCardsUpdateBackfill.md) |  |  [optional] |
|**state** | [**StateEnum**](#StateEnum) | Set to &#x60;disconnected&#x60; to stop this account&#39;s feed and remove its card links while keeping the account, its transactions and its matches readable. With &#x60;cardId&#x60;, see that field: the account can keep feeding its other cards. This endpoint cannot set it back: a new bank link to the same bank creates a new bank account, and this one stays &#x60;disconnected&#x60;. When this is the connection&#39;s last active account, the connection at the bank is ended too. Disconnecting an account that is already disconnected, or one that was deleted, answers &#x60;200&#x60; and sends no webhook (an id that never existed still answers &#x60;404&#x60;). Use &#x60;DELETE /bank-accounts/{bankAccountId}&#x60; to remove the account and its transactions. |  |
|**reason** | **String** | Optional free-text reason recorded with the disconnect. Sent as &#x60;reason&#x60; on &#x60;bankaccount.state_changed&#x60;. Default &#x60;subscription_deactivated&#x60;. |  [optional] |
|**cardId** | **UUID** | Optional SCOPE for the disconnect. Omit for the historical account-scoped behaviour: the whole account is torn down and every card on it loses the feed. Supply the card that was deactivated to scope it to that card: its link is removed and the account keeps feeding any other cards attached to it. Disconnecting the last remaining card still tears the account down, so a single-card account behaves identically either way.  |  [optional] |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| DISCONNECTED | &quot;disconnected&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



