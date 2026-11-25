

# CardholderUpdateParamsCorporate

The corporate entity that this cardholder is associated with. `legalEntity` is set by the card network and cannot be changed here: it is ignored if sent, and the stored value is kept.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **String** | The company ID to store on the cardholder, the same value a network bulk feed supplies. See &#x60;cardholder.corporate.id&#x60;. |  [optional] |
|**employeeId** | **String** | The unique identifier of the employee |  [optional] |
|**alternativeEmployeeId** | **String** | An alternative employee identifier, typically configured for specific enterprise integrations. |  [optional] |



