

# FailureReason

Normalized, customer-facing reason a card verification failed. Present only when the verification `state` is `failed`. `category`, `description` and `retryable` are always present; `issuerMessage` and `acsReferences` appear only when the issuer returned them (typically on a frictionless decline; when the issuer renders its own challenge UI no per-attempt message is available). 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**category** | [**CategoryEnum**](#CategoryEnum) | Stable, provider-agnostic failure category an integrator can branch on without enumerating raw issuer codes.  |  |
|**description** | **String** | Human-readable explanation of the failure, safe to relay to the cardholder. |  |
|**retryable** | **Boolean** | Whether the cardholder can retry now and plausibly succeed. |  |
|**issuerMessage** | **String** | The issuer&#39;s own cardholder-facing message, when provided (frictionless declines only). |  [optional] |
|**code** | **String** | Raw provider reason code (e.g. a 3DS &#x60;transStatusReason&#x60; or a Stripe &#x60;decline_code&#x60;), surfaced for support escalation.  |  [optional] |
|**providerMessage** | **String** | The payment provider&#39;s cardholder-safe message for the specific failure code (e.g. \&quot;Your card does not support this type of purchase.\&quot;). Finer-grained than the category-level &#x60;description&#x60;; absent when the provider supplied none.  |  [optional] |
|**adviceCode** | **String** | The issuer&#39;s advice code on authorization declines, verbatim from the payment provider. Documented values: &#x60;try_again_later&#x60;, &#x60;do_not_try_again&#x60;, &#x60;confirm_card_data&#x60;; values the provider introduces later pass through unchanged. Absent when the issuer gave no advice (e.g. 3-D Secure authentication failures).  |  [optional] |
|**acsReferences** | [**FailureReasonAcsReferences**](FailureReasonAcsReferences.md) |  |  [optional] |



## Enum: CategoryEnum

| Name | Value |
|---- | -----|
| AUTHENTICATION_DECLINED | &quot;authentication_declined&quot; |
| AUTHENTICATION_CANCELED | &quot;authentication_canceled&quot; |
| CVC_CHECK_FAILED | &quot;cvc_check_failed&quot; |
| CARD_DECLINED | &quot;card_declined&quot; |
| CONTACT_ISSUER | &quot;contact_issuer&quot; |
| CARD_BLOCKED | &quot;card_blocked&quot; |
| TOO_MANY_ATTEMPTS | &quot;too_many_attempts&quot; |
| NOT_SUPPORTED | &quot;not_supported&quot; |
| TEMPORARY_ISSUE | &quot;temporary_issue&quot; |
| PROCESSING_ERROR | &quot;processing_error&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



