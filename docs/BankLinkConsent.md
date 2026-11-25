

# BankLinkConsent

The bank-link consent record. The Unified Enrollment SDK sets it on channel `sdk-unified`, naming the Astrada consent text of the bank-connect screen the person saw. Integrators do not send it. Astrada relays it to each card subscription the bank link creates, where it is recorded and never refused. A consent without `textVersionId` is accepted but not kept. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**channel** | [**ChannelEnum**](#ChannelEnum) | Always &#x60;sdk-unified&#x60;. |  |
|**textVersionId** | **String** | The id of the bank-connect consent text the SDK showed, such as &#x60;sdk-unified:bank-consent.plaid.bank.en@v1&#x60;. |  [optional] |
|**textHash** | **String** | &#x60;sha256:&#x60; and the lowercase hex SHA-256 of the text named by &#x60;textVersionId&#x60;, compared with the registered text when sent. |  [optional] |
|**sdkVersion** | **String** | The SDK release that collected the consent. |  [optional] |
|**variables** | **Map&lt;String, String&gt;** | &#x60;companyName&#x60; and &#x60;providerName&#x60;, as the screen showed them. For a Plaid link, Astrada adds &#x60;plaidLinkSessionId&#x60; when it relays the record.  |  [optional] |



## Enum: ChannelEnum

| Name | Value |
|---- | -----|
| SDK_UNIFIED | &quot;sdk-unified&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



