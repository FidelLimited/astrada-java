

# VerificationPolicy

Admin-managed overrides controlling how card verification is performed for an account or subaccount. Fields are optional and nullable; absent fields leave the current value unchanged, explicit `null` clears an override. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**stripeValidationLevel** | [**StripeValidationLevelEnum**](#StripeValidationLevelEnum) | Routes card verifications through Stripe at the specified risk tier when set. &#x60;null&#x60; (or absent) routes through Astrada&#39;s default card check for the network.  Tiers (increasing strictness):   * &#x60;LOW&#x60;: internal-only; 3DS engaged only when risk/regulation requires it; issuer-unsupported     soft declines are tolerated. Cannot be self-assigned via this endpoint.   * &#x60;MEDIUM&#x60;: 3DS when the card provider&#39;s checks or regulation call for it; the standard tier     to opt into.   * &#x60;HIGH&#x60;: 3DS plus a transient authorization hold (no settled charge) and added fraud signals.   * &#x60;HIGHEST&#x60;: 3DS challenge; if the issuer approves frictionlessly, a two-hold confirmation     becomes a mandatory second factor. See the \&quot;HIGHEST Verification\&quot; guide.  Network note: Visa uses Stripe at every set tier; Mastercard uses its network 3DS at all tiers and engages Stripe only at &#x60;HIGHEST&#x60; (for the two-hold second factor).  Resolution: the subaccount value is operative at request time; the account-level value is the inheritance source applied when a subaccount is created, not a live fallback.  |  [optional] |
|**sandbox** | **Boolean** | Test flag, settable by the customer via &#x60;PATCH /subaccounts&#x60; (and internally via the verification-policy endpoints). When &#x60;true&#x60;, card verifications on this subaccount route KNOWN test PANs through the card providers&#39; test modes (real cards still go live), and new bank links are created against Plaid Sandbox (Plaid test fixtures only; real bank credentials require the flag off), so an integrator can deterministically test against a provisioned subaccount. On the production API, absent / &#x60;null&#x60; / &#x60;false&#x60; → live processing only.  |  [optional] |
|**failedAttemptLockout** | **Boolean** | When &#x60;true&#x60;, ENFORCE the cross-network attempt lockout for this tenant: a card with too many recent hard verification failures is refused at create (&#x60;400&#x60; &#x60;verification.attempts_locked&#x60;) before any provider is called. Account-level fallback (set once for all partner subaccounts), overridable per subaccount. A new subaccount starts with a copy of the account&#39;s value; send &#x60;null&#x60; on the subaccount to make it follow the account again. When the value that applies is absent, &#x60;null&#x60; or &#x60;false&#x60;, the lockout is not enforced (failures are still recorded; only enforcement is gated).  |  [optional] |



## Enum: StripeValidationLevelEnum

| Name | Value |
|---- | -----|
| HIGHEST | &quot;HIGHEST&quot; |
| HIGH | &quot;HIGH&quot; |
| MEDIUM | &quot;MEDIUM&quot; |
| LOW | &quot;LOW&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



