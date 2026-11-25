

# BankLinkBankAccount

A bank account created from a bank link, in summary.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **UUID** | The bank account identifier. |  [optional] |
|**name** | **String** | The display name of the bank account. |  [optional] |
|**institutionName** | **String** | Display name of the connected institution. Present on this API response; not included on the &#x60;banklink.completed&#x60; event payload.  |  [optional] |
|**mask** | **String** | Last 4 digits of the account number. For &#x60;credit&#x60; accounts this is the card number&#39;s last 4; the per-card identity for bank-connected (e.g. American Express) enrollments. Not guaranteed per-card when the institution combines multiple cards into one account.  |  [optional] |
|**type** | **String** | Normalized account type (e.g. &#x60;checking&#x60;, &#x60;savings&#x60;, &#x60;credit&#x60;). |  [optional] |
|**cardIds** | **List&lt;UUID&gt;** | Identifiers of already-enrolled cards linked to this account, matched when an enrolled card&#39;s last 4 equals a credit account&#39;s mask (applied at completion and by a scheduled sweep that re-checks each cardless credit account every few hours). A link is an association to an EXISTING card, not a new enrollment.  |  [optional] |
|**cards** | [**List&lt;BankFeedCard&gt;**](BankFeedCard.md) | Cards created from this account at completion (&#x60;bank-feed&#x60; enrollments), each paired with its subscription. Present only on credit accounts that created a card; a created card also appears in &#x60;cardIds&#x60;.  |  [optional] |
|**noLinkReason** | [**NoLinkReasonEnum**](#NoLinkReasonEnum) | Why this credit account has no card. Present on this API response; not included on the &#x60;banklink.completed&#x60; event payload. Present ONLY while the account is still cardless; once any card links, the field disappears.  A credit account can legitimately finish linking with no card, and this says which case it was rather than leaving an unexplained empty &#x60;cardIds&#x60;:   - &#x60;bulk_card_owns_mask&#x60;: a bulk-enrolled card is the live owner of this last 4. It correctly suppresses a duplicate card, and a bulk subscription cannot take an automatic bank-account link.   - &#x60;mask_ambiguous_accounts&#x60;: several accounts on this link share the mask, so no automatic link is safe.   - &#x60;mask_ambiguous_cards&#x60;: several enrolled cards share the mask, and card-number (BIN) evidence could not narrow them to one.   - &#x60;card_linked_elsewhere&#x60;: the matching card is already linked to another bank account.   - &#x60;card_not_attachable&#x60;: the matching card has no active subscription to attach to.   - &#x60;no_institution_id&#x60;: the provider reported no institution, so no stable card identity can be derived.   - &#x60;mask_matches_no_plausible_card&#x60;: enrolled cards share the mask, but BIN evidence ruled every one of them out for the connected institution (wrong issuing bank, or debit/prepaid funding), so nothing linked rather than linking a known-wrong card.  The scheduled mask sweep and a manual &#x60;PATCH cardIds&#x60; both remain available; the field describes the state at completion, not a permanent verdict.  |  [optional] |



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



