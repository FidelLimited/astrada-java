

# BankAccountMaintenanceResultJunctionLast4

Present only when `backfillJunctionLast4: true` was sent.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**rowsUpdated** | **Integer** | Links that gained a last 4 in this run. |  |
|**rowsUnresolved** | **Integer** | Links still without one (the directory did not list the card). |  |
|**directoryUnavailable** | **Boolean** | True when this action&#39;s own read of the cards directory failed (the mask pass does not fail on it); call again later. |  |



