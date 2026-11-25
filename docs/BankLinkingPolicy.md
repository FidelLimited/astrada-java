

# BankLinkingPolicy

Admin-managed policy controlling which account types and institutions are offered when linking bank accounts via the hosted (Plaid) flow. Fields are optional and nullable; absent fields leave the current value unchanged, explicit `null` clears a field.  Resolution is per-field and LIVE: a subaccount field applies when set; when absent/`null` the parent account's value applies; when neither is set the field reads `null` and the system default is applied downstream at bank-link creation (`accountTypes: [credit]`: credit-card accounts only, the historical behavior). Subaccount GETs return these EFFECTIVE resolved values (nulls included; the default is never materialized in reads).  Bank-link creation uses a copy of the policy read at most 60 seconds earlier, so a change can take up to a minute to apply. When that read fails, the create uses the system default for that call. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**accountTypes** | [**List&lt;AccountTypesEnum&gt;**](#List&lt;AccountTypesEnum&gt;) | Account types selectable during bank linking. Enforced by the provider at link-token creation (Plaid account_filters: checking/savings → depository subtypes, credit → credit card); institutions offering none of the allowed types are hidden from institution search. Default: &#x60;[credit]&#x60;.  |  [optional] |
|**pinnedInstitutionId** | **String** | Provider institution id to pre-select at link creation (e.g. &#x60;ins_10&#x60; for American Express). NOTE: Plaid&#39;s &#x60;institution_id&#x60; pre-selection is currently rejected on production (&#x60;INVALID_INSTITUTION&#x60;); this field is stored and forwarded only behind a provider capability flag; until the provider supports it, institution limiting is achieved with &#x60;plaidLinkCustomizationName&#x60; plus account-type filtering.  |  [optional] |
|**plaidLinkCustomizationName** | **String** | Name of a Plaid Dashboard Link customization to pin on created link tokens (institution shortlist, Account Select mode). The customization must already exist for the account&#39;s Plaid client in the Plaid Dashboard. A &#x60;POST /bank-links&#x60; may name &#x60;amex_only&#x60; in &#x60;filters.linkCustomizationName&#x60;, which then takes this value&#39;s place, and a policy &#x60;amex_only&#x60; is not applied to a link whose &#x60;bin6&#x60; does not start with &#x60;3&#x60;. If Plaid refuses the name, &#x60;POST /bank-links&#x60; answers &#x60;403&#x60; &#x60;PLAID_CUSTOMIZATION_NOT_PROVISIONED&#x60;, except for &#x60;amex_only&#x60;, which is first retried once with the environment&#39;s pinned customization in its place, or with none.  |  [optional] |
|**providers** | [**List&lt;ProvidersEnum&gt;**](#List&lt;ProvidersEnum&gt;) | Open-banking provider allowlist for bank-link creation. Unset/&#x60;null&#x60; &#x3D; unrestricted: the client&#39;s requested &#x60;provider&#x60; governs, bounded only by which providers are enabled on the platform. Set: a &#x60;POST /bank-links&#x60; requesting a provider outside the list is rejected with &#x60;400&#x60;.  The &#x60;mastercard&#x60; provider is not yet available in every environment. In an environment without it, a &#x60;mastercard&#x60; link is refused with &#x60;400&#x60; &#x60;PROVIDER_NOT_SUPPORTED_FOR_COUNTRY&#x60;.  |  [optional] |
|**defaultProvider** | [**DefaultProviderEnum**](#DefaultProviderEnum) | Provider used when &#x60;POST /bank-links&#x60; doesn&#39;t name one. Unset/&#x60;null&#x60; &#x3D; the system default (&#x60;plaid&#x60;).  |  [optional] |
|**defaultCountryCode** | **String** | ISO 3166-1 alpha-3 country used when &#x60;POST /bank-links&#x60; doesn&#39;t name one. Unset/&#x60;null&#x60; &#x3D; the system default (&#x60;USA&#x60;).  |  [optional] |



## Enum: List&lt;AccountTypesEnum&gt;

| Name | Value |
|---- | -----|
| CHECKING | &quot;checking&quot; |
| SAVINGS | &quot;savings&quot; |
| CREDIT | &quot;credit&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: List&lt;ProvidersEnum&gt;

| Name | Value |
|---- | -----|
| PLAID | &quot;plaid&quot; |
| MASTERCARD | &quot;mastercard&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: DefaultProviderEnum

| Name | Value |
|---- | -----|
| PLAID | &quot;plaid&quot; |
| MASTERCARD | &quot;mastercard&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



