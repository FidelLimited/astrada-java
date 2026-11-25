

# PlaidEndCustomer

Admin-managed policy controlling whether an account's bank links are created under the account's own Plaid reseller End-Customer (EC) client instead of the shared legacy client. Fields are optional and nullable; absent fields leave the current value unchanged, explicit `null` clears a field. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**enabled** | **Boolean** | Master gate for routing this account&#39;s bank links through its own Plaid reseller End-Customer client. Absent / &#x60;null&#x60; / &#x60;false&#x60; → disabled (legacy shared client).  Admin-set, account-level. When &#x60;true&#x60;, the account&#39;s bank links are created under the account&#39;s own Plaid reseller End-Customer client; once that client is provisioned and Plaid&#39;s registration with the relevant institutions has completed, the hosted Link experience carries the account&#39;s own branding (display name and Link customization) rather than Astrada&#39;s. Enabling an account whose End-Customer client is not yet provisioned fails link creation rather than silently falling back to the shared client.  Resolution is passthrough from the account onto the effective subaccount read: this object is account-level only in v1 (there is no subaccount-level override) and subaccount GETs return the account&#39;s stored value as their **effective** value.  |  [optional] |



