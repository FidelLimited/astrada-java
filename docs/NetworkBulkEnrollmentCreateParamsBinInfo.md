

# NetworkBulkEnrollmentCreateParamsBinInfo

Optional. The issuer details shown to the person submitting the kickoff, in the `GET /bin-lookup` vocabulary. Stored as a display snapshot only; every field is optional. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**issuerName** | **String** | The name of the issuing bank. |  [optional] |
|**issuerCountry** | **String** | The country of the issuing bank, ISO 3166 alpha-3. Stored as sent; its format is not checked. &#x60;GET /bin-lookup&#x60; gives it as ISO 3166 alpha-3. |  [optional] |
|**type** | **String** | The card type, for example &#x60;COMMERCIAL&#x60;. |  [optional] |
|**fundingType** | **String** | The card funding type, for example &#x60;CREDIT&#x60;. |  [optional] |



