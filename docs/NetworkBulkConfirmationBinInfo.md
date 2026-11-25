

# NetworkBulkConfirmationBinInfo

A snapshot of the issuer details shown to the person who asked for the confirmation, kept for display only. `null` when the request carried none. The fields use the `GET /bin-lookup` vocabulary. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**issuerName** | **String** | The name of the issuing bank. |  [optional] |
|**issuerCountry** | **String** | The country of the issuing bank, ISO 3166-1 alpha-3. |  [optional] |
|**type** | **String** | The card type, for example &#x60;COMMERCIAL&#x60;. |  [optional] |
|**fundingType** | **String** | The card funding type, for example &#x60;CREDIT&#x60;. |  [optional] |



