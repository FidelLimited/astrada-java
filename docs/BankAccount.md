

# BankAccount


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**BankAccountLinks**](BankAccountLinks.md) |  |  |
|**id** | **UUID** | The unique identifier of the bank account. |  |
|**accountId** | **UUID** | The unique identifier of the account that owns this bank account. |  |
|**subaccountId** | **UUID** | The unique identifier of the subaccount associated with this bank account. |  |
|**bankLinkId** | **String** | The identifier of the bank link that created this account. Null for accounts that predate persisted link attribution. |  [optional] |
|**externalId** | **String** | The provider account ID for the connected account. |  |
|**cardIds** | **List&lt;UUID&gt;** | The identifiers of cards linked to this bank account for transaction matching, always current. A card links when its last 4 digits match this account&#39;s mask (the same rule applied at bank-link completion and by a scheduled sweep that re-checks each cardless credit account every few hours), or when set manually via PATCH. Cards must belong to the same subaccount. |  |
|**noLinkReason** | [**NoLinkReasonEnum**](#NoLinkReasonEnum) | Why this credit account has no card. Present ONLY while the account is still cardless; once any card links, the field disappears, so it can never contradict &#x60;cardIds&#x60;. See the identically-named field on the bank link response for the full meaning of each value.  |  [optional] |
|**cards** | [**List&lt;BankFeedCard&gt;**](BankFeedCard.md) | Cards MATERIALIZED from this bank account (&#x60;bank-feed&#x60; enrollments minted at bank-link completion for credit accounts whose mask matched no already-enrolled card). Distinct from &#x60;cardIds&#x60;, which is the link surface: a mask-matched card appears in &#x60;cardIds&#x60; only, while a minted card appears in both. Empty for depository and mask-matched-only accounts.  |  |
|**institutionName** | **String** | The name of the connected financial institution. |  |
|**institutionId** | **String** | The provider identifier of the connected institution (Plaid &#x60;ins_*&#x60;). Null for connections made before this field was captured. |  [optional] |
|**name** | **String** | The display name of the bank account (e.g. \&quot;Checking\&quot;, \&quot;Savings\&quot;). |  |
|**mask** | **String** | The last 4 digits of the account number. For &#x60;credit&#x60; accounts this is the card number&#39;s last 4; the per-card identity for bank-connected (e.g. American Express) card enrollments. Not guaranteed to be per-card when the institution combines multiple cards into a single account. |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | The normalized type of bank account. (Correction: the service has always returned normalized values; the previous &#x60;depository&#x60;/&#x60;credit&#x60; enum understated live behavior. Codegen consumers should regenerate.) |  |
|**subtype** | **String** | The raw provider account subtype (e.g. Plaid &#x60;credit card&#x60;, &#x60;checking&#x60;). Null when the provider did not supply one or for accounts connected before this field was captured. |  [optional] |
|**currency** | **String** | The currency of the account following ISO 4217 format. |  |
|**state** | [**StateEnum**](#StateEnum) | Current state of the bank account. - &#x60;active&#x60;: The account is connected and functioning normally. - &#x60;auth_required&#x60;: The account requires re-authentication. - &#x60;disconnected&#x60;: The account has been disconnected from the provider.  |  |
|**subscriptionState** | [**SubscriptionStateEnum**](#SubscriptionStateEnum) | The health of the underlying aggregator connection backing this account. The account &#x60;state&#x60; above can read &#x60;active&#x60; while the connection has errored or needs re-auth, so this field can read \&quot;active but dead\&quot;. Null if no subscription is linked.  |  [optional] |
|**lastSyncedAt** | **OffsetDateTime** | Last successful sync of the backing connection, in UTC, following ISO 8601 format. Pairs with &#x60;subscriptionState&#x60; as the freshness signal. Null until the first sync. |  [optional] |
|**createdAt** | **OffsetDateTime** | Date and time when this resource was created, in UTC, following ISO 8601 format. |  |
|**updatedAt** | **OffsetDateTime** | Date and time when this resource was last updated, in UTC, following ISO 8601 format. |  |



## Enum: NoLinkReasonEnum

| Name | Value |
|---- | -----|
| BULK_CARD_OWNS_MASK | &quot;bulk_card_owns_mask&quot; |
| MASK_AMBIGUOUS_ACCOUNTS | &quot;mask_ambiguous_accounts&quot; |
| MASK_AMBIGUOUS_CARDS | &quot;mask_ambiguous_cards&quot; |
| CARD_LINKED_ELSEWHERE | &quot;card_linked_elsewhere&quot; |
| CARD_NOT_ATTACHABLE | &quot;card_not_attachable&quot; |
| NO_INSTITUTION_ID | &quot;no_institution_id&quot; |
| MASK_MATCHES_NO_PLAUSIBLE_CARD | &quot;mask_matches_no_plausible_card&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| CHECKING | &quot;checking&quot; |
| SAVINGS | &quot;savings&quot; |
| PREPAID | &quot;prepaid&quot; |
| CREDIT | &quot;credit&quot; |
| LOAN | &quot;loan&quot; |
| INVESTMENT | &quot;investment&quot; |
| OTHER | &quot;other&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| ACTIVE | &quot;active&quot; |
| AUTH_REQUIRED | &quot;auth_required&quot; |
| DISCONNECTED | &quot;disconnected&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: SubscriptionStateEnum

| Name | Value |
|---- | -----|
| ACTIVE | &quot;active&quot; |
| ERROR | &quot;error&quot; |
| AUTH_REQUIRED | &quot;auth_required&quot; |
| DISCONNECTED | &quot;disconnected&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



