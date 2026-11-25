

# BankLinkFilters

The account filtering actually applied to this link, after resolution against the account's bank-linking policy. Echoes what the hosted session will offer; absent on links created before filtering existed. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**accountTypes** | [**List&lt;AccountTypesEnum&gt;**](#List&lt;AccountTypesEnum&gt;) | Account types selectable in the hosted session. |  [optional] |
|**linkCustomizationName** | **String** | The Plaid Link customization this link asked for: the request&#39;s &#x60;filters.linkCustomizationName&#x60;, or else the bank-linking policy&#39;s &#x60;plaidLinkCustomizationName&#x60;. Absent when neither applies. When Plaid refused &#x60;amex_only&#x60; and the create was retried without it, this still reads &#x60;amex_only&#x60;, and the link&#39;s completion is still held to American Express institutions.  |  [optional] |



## Enum: List&lt;AccountTypesEnum&gt;

| Name | Value |
|---- | -----|
| CHECKING | &quot;checking&quot; |
| SAVINGS | &quot;savings&quot; |
| CREDIT | &quot;credit&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



