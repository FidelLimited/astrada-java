

# BankLinkCreateParamsFilters

Optional per-link narrowing of the account's bank-linking policy. Values may only NARROW what the policy allows; the current system default is `accountTypes: [credit]` (credit-card accounts only). Requests widening beyond the policy are rejected with `400`. Account-type enforcement is applied by the provider at link-token creation and also filters the institution-select list. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**accountTypes** | [**List&lt;AccountTypesEnum&gt;**](#List&lt;AccountTypesEnum&gt;) | Account types selectable during this hosted session. |  [optional] |
|**linkCustomizationName** | **String** | Name of an allowlisted provider (Plaid) Link customization to apply to this hosted session, such as &#x60;amex_only&#x60; to surface an Amex-first institution list with a tailored headline for card-enrollment connections. Only &#x60;amex_only&#x60; is accepted; any other name is rejected with &#x60;400&#x60; (&#x60;INVALID_LINK_FILTERS&#x60;). A requested customization takes the place of the bank-linking policy&#39;s. Institution steering is by account-type filtering plus the customization&#39;s Dashboard configuration; the provider does not support hard institution pre-selection for hosted sessions.  With &#x60;amex_only&#x60; the link completes only at American Express: a sign-in at another institution leaves the link &#x60;pending&#x60; with &#x60;lastRejection&#x60;. Send &#x60;bin6&#x60; with it only when it starts with &#x60;3&#x60;: a link with another &#x60;bin6&#x60; is refused at American Express and cannot complete.  |  [optional] |



## Enum: List&lt;AccountTypesEnum&gt;

| Name | Value |
|---- | -----|
| CHECKING | &quot;checking&quot; |
| SAVINGS | &quot;savings&quot; |
| CREDIT | &quot;credit&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



