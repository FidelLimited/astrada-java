

# SubaccountLinks


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**self** | [**Link**](Link.md) |  |  |
|**teamLink** | [**Link**](Link.md) | Only on &#x60;GET /subaccounts/{subaccountId}&#x60;: &#x60;POST /subaccounts/{subaccountId}/team-link&#x60;, which creates a team link for the subaccount, the \&quot;Invite your team\&quot; action. Present when enrollment links are enabled for the account, the token holds &#x60;card-subscriptions:write&#x60; or &#x60;subaccounts:write&#x60;, and the subaccount&#39;s card enrollment is on (its &#x60;enrollmentPolicy.cardEnrollment&#x60; is not &#x60;false&#x60;). Never offered to an enrollment link session&#39;s token. If the check cannot be made, the link is left out and the read still succeeds.  |  [optional] |



