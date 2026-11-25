

# EnrollmentMethodsResultSingleDecision

Whether the subaccount named in `subaccountId` can enroll this card one at a time. Present only when the request names a `subaccountId` and that subaccount's controls could be run for these digits. When they could not (for example, the subaccount could not be read, or, in sandbox, the digits match test cards of more than one card type), `singleDecision` is omitted and the rest of the response is the one given without `subaccountId`.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**allowed** | **Boolean** | &#x60;true&#x60; when every single-enrollment control passes for the subaccount and the card&#39;s own &#x60;singleEligible&#x60;, as answered without &#x60;subaccountId&#x60;, is &#x60;true&#x60;. |  |
|**reason** | [**ReasonEnum**](#ReasonEnum) | Why single enrollment is refused. Present only when &#x60;allowed&#x60; is &#x60;false&#x60;. A control&#39;s refusal is named before &#x60;coverage&#x60;. Each control&#39;s reason stands for the refusal that control gives on &#x60;POST /card-subscriptions&#x60;, shown in parentheses. &#x60;bank-enrollment-required&#x60;: the subaccount&#39;s &#x60;enrollmentPolicy.bankEnrollment&#x60; is &#x60;amex-visa&#x60; and this Visa card is not classified as business (&#x60;card_subscription.subaccount_blocking_card_type&#x60; with &#x60;reasonCode&#x60; &#x60;card_subscription.bank_enrollment_required&#x60;). &#x60;card-type&#x60;: the subaccount&#39;s single-enrollment &#x60;blockedCardTypes&#x60; holds the card&#39;s type (&#x60;card_subscription.subaccount_blocking_card_type&#x60;). &#x60;funding-type&#x60;: its &#x60;blockedFundingCardTypes&#x60; holds the card&#39;s funding type (&#x60;card_subscription.subaccount_blocking_card_funding_type&#x60;). &#x60;issuer-country&#x60;: &#x60;enforceIssuerCountry&#x60; is on and the card&#39;s issuing country is unknown or not one of the subaccount&#39;s single-enrollment countries (&#x60;card_subscription.subaccount_blocking_card_country&#x60;). &#x60;network-rule&#x60;: a rule in the subaccount&#39;s &#x60;networkBlockedCards&#x60; matches the card (&#x60;card_subscription.network_blocking_card&#x60;). &#x60;coverage&#x60;: every control passes, but the card&#39;s own &#x60;singleEligible&#x60; is &#x60;false&#x60;, for example for any American Express card. |  [optional] |



## Enum: ReasonEnum

| Name | Value |
|---- | -----|
| BANK_ENROLLMENT_REQUIRED | &quot;bank-enrollment-required&quot; |
| CARD_TYPE | &quot;card-type&quot; |
| FUNDING_TYPE | &quot;funding-type&quot; |
| ISSUER_COUNTRY | &quot;issuer-country&quot; |
| NETWORK_RULE | &quot;network-rule&quot; |
| COVERAGE | &quot;coverage&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



