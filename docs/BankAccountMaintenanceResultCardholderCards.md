

# BankAccountMaintenanceResultCardholderCards

Present only when `createCardholderCards: true` was sent. Counts only; no cardholder name is ever returned. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**enabled** | **Boolean** | False when card creation is not switched on for the subaccount; nothing was created. |  |
|**routeEnabled** | **Boolean** | True when each cardholder&#39;s transactions are emitted on their own card. |  |
|**accounts** | **Integer** | Pooled American Express billing accounts served. |  |
|**suffixesSeen** | **Integer** | Distinct cardholders (by the card&#39;s last 4) seen in those accounts&#39; transactions. |  |
|**created** | **Integer** | Cardholder cards created in this run. |  |
|**linkedEnrolled** | **Integer** | Cards the cardholder had already enrolled, linked instead of creating one. |  |
|**named** | **Integer** | Cards whose cardholder name was written in this run. |  |
|**relinked** | **Integer** | Cardholder cards found on another bank account of the subaccount after a re-link, left where they are and counted (the background re-home moves them). |  |
|**failed** | **Integer** | Cards that could not be created this run; the next run tries again. |  |
|**skipped** | **Map&lt;String, Integer&gt;** | Reason -&gt; count of cardholders given no card of their own (their transactions stay on the account&#39;s main card). Reasons: &#x60;no_primary&#x60;, &#x60;ambiguous_primary&#x60;, &#x60;bulk_owned&#x60;, &#x60;ambiguous_enrolled&#x60;, and the permanent &#x60;deactivated&#x60;, &#x60;not_active&#x60;, &#x60;unlinked&#x60; and &#x60;foreign_reuse&#x60;. Only reasons with a count appear.  |  |
|**directoryUnavailable** | **Boolean** | True when this action&#39;s own read of the cards directory failed; no card was created this run. |  |



