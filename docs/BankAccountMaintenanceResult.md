

# BankAccountMaintenanceResult


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**linked** | **Map&lt;String, List&lt;UUID&gt;&gt;** | bankAccountId -&gt; cardIds linked in THIS run. |  |
|**minted** | [**Map&lt;String, BankAccountMaintenanceResultMintedValue&gt;**](BankAccountMaintenanceResultMintedValue.md) | Present whenever &#x60;mint: true&#x60; was sent (&#x60;{}&#x60; when nothing was minted). bankAccountId -&gt; the card minted for it in THIS run. &#x60;linkedHere&#x60; is false when the card is held by a sibling account instead (a re-link of the same bank; the background re-home converges it).  |  [optional] |
|**skipped** | **Map&lt;String, String&gt;** | Present whenever &#x60;mint: true&#x60; was sent (&#x60;{}&#x60; when nothing was skipped). bankAccountId -&gt; why the mint pass did not mint for it: &#x60;already_materialized&#x60; (the account already holds a minted card), &#x60;existing_enrolled_card&#x60; (its last 4 belongs to a card the customer enrolled directly) or &#x60;no_institution_id&#x60; (the connection never recorded its institution).  |  [optional] |
|**failures** | **Map&lt;String, String&gt;** | Present whenever &#x60;mint: true&#x60; was sent (&#x60;{}&#x60; when no mint failed). bankAccountId -&gt; the failure. The pass is idempotent, so the request can simply be repeated.  |  [optional] |
|**junctionLast4** | [**BankAccountMaintenanceResultJunctionLast4**](BankAccountMaintenanceResultJunctionLast4.md) |  |  [optional] |
|**cardholderCards** | [**BankAccountMaintenanceResultCardholderCards**](BankAccountMaintenanceResultCardholderCards.md) |  |  [optional] |



