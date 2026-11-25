

# BankAccountMaintenanceParams


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**subaccountId** | **UUID** | The subaccount to run maintenance over. |  |
|**mint** | **Boolean** | After the mask pass, also mint a bank-feed card for each of the subaccount&#39;s eligible credit bank accounts that holds no card. Accounts that already hold a minted card are skipped.  |  [optional] |
|**backfillJunctionLast4** | **Boolean** | After the mask pass, also fill each of the subaccount&#39;s card links with the last 4 it answers to, where still unknown, from the cards directory.  |  [optional] |
|**createCardholderCards** | **Boolean** | After the mask pass, also create and name the bank-feed cards of the cardholders behind the subaccount&#39;s pooled American Express billing accounts, as the scheduled job does. Does nothing unless Astrada has switched the feature on for the subaccount.  |  [optional] |



