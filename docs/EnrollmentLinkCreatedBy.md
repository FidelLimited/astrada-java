

# EnrollmentLinkCreatedBy


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**via** | [**ViaEnum**](#ViaEnum) | - &#x60;api&#x60;: &#x60;POST /enrollment-links&#x60; with &#x60;subaccounts:write&#x60;. - &#x60;staff&#x60;: &#x60;POST /enrollment-links&#x60; with Astrada&#39;s &#x60;accounts:write&#x60;. - &#x60;sdk&#x60;: &#x60;POST /subaccounts/{subaccountId}/team-link&#x60;, the route for the SDK&#39;s   \&quot;Invite your team\&quot; ending.  |  |
|**clientId** | **String** | The client ID of the credential that created the link, when known. |  [optional] |



## Enum: ViaEnum

| Name | Value |
|---- | -----|
| API | &quot;api&quot; |
| SDK | &quot;sdk&quot; |
| STAFF | &quot;staff&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



