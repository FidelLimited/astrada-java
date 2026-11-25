

# CardVerificationLinks


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**self** | [**Link**](Link.md) | The link to this verification resource. |  |
|**steps** | [**Link**](Link.md) | The link to this verification&#39;s steps (&#x60;/card-verifications/3ds/{verificationId}/steps&#x60;). |  |
|**currentStep** | [**Link**](Link.md) | The link to the step the client should execute next. Absent once the verification reaches a terminal &#x60;state&#x60; (&#x60;completed&#x60; or &#x60;failed&#x60;).  |  [optional] |



