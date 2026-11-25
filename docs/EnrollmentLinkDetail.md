

# EnrollmentLinkDetail


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**SelfLinks**](SelfLinks.md) |  |  |
|**id** | **UUID** | The link&#39;s identifier. |  |
|**kind** | [**KindEnum**](#KindEnum) | - &#x60;cardholder&#x60;: a personal link for one employee. It allows up to 5 session starts and   is used up by the first session that completes something. - &#x60;team&#x60;: a link to share with a team. Every session start counts as a use, up to   &#x60;maxUses&#x60;.  |  |
|**subaccountId** | **UUID** | The subaccount that cards enrolled through the link join. |  |
|**startingPoint** | [**StartingPointEnum**](#StartingPointEnum) | Where the hosted page starts. Only &#x60;card&#x60; is available now; links made before the field existed read as &#x60;card&#x60;. |  |
|**state** | [**StateEnum**](#StateEnum) | - &#x60;active&#x60;: sessions can start. - &#x60;used&#x60;: a personal link whose session completed something. - &#x60;revoked&#x60;: revoked with &#x60;POST /enrollment-links/{linkId}/revoke&#x60; or by deleting its   subaccount. Team links revoked by the former five-active-link limit on   &#x60;POST /subaccounts/{subaccountId}/team-link&#x60; stay revoked. - &#x60;expired&#x60;: an active link past &#x60;expiresAt&#x60;.  |  |
|**expiresAt** | **OffsetDateTime** | After this time no session starts from the link. |  |
|**maxUses** | **Integer** | 1 on a personal link. On a team link, the session starts it allows. |  |
|**uses** | **Integer** | On a team link, the sessions started so far. On a personal link, 1 once it is used, otherwise 0. |  |
|**starts** | **Integer** | The sessions started from the link so far. |  |
|**customerReferenceId** | **UUID** | The reference given when the link was created. Absent when none was given. |  [optional] |
|**platformName** | **String** | The platform name given when the link was created. Absent when none was given. The hosted page ignores it once the account has a brand profile.  |  [optional] |
|**organizationName** | **String** | The organization name given when the link was created. Absent when none was given. |  [optional] |
|**createdBy** | [**EnrollmentLinkCreatedBy**](EnrollmentLinkCreatedBy.md) |  |  |
|**createdAt** | **OffsetDateTime** |  |  |
|**updatedAt** | **OffsetDateTime** |  |  |
|**usedAt** | **OffsetDateTime** | When a personal link was used up. |  [optional] |
|**revokedAt** | **OffsetDateTime** | When the link was revoked. |  [optional] |
|**sessions** | [**List&lt;EnrollmentLinkSession&gt;**](EnrollmentLinkSession.md) | The sessions started from the link, oldest first. |  |



## Enum: KindEnum

| Name | Value |
|---- | -----|
| CARDHOLDER | &quot;cardholder&quot; |
| TEAM | &quot;team&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: StartingPointEnum

| Name | Value |
|---- | -----|
| CARD | &quot;card&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| ACTIVE | &quot;active&quot; |
| USED | &quot;used&quot; |
| REVOKED | &quot;revoked&quot; |
| EXPIRED | &quot;expired&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



