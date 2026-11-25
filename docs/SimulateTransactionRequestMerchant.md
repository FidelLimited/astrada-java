

# SimulateTransactionRequestMerchant

The merchant (card acceptor) on the message. Defaults to a fixed sandbox coffee-shop merchant in the United States. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**name** | **String** | Merchant name, emitted as the transaction descriptor. |  |
|**mcc** | **String** | Four-digit merchant category code. |  |
|**city** | **String** | Merchant city. |  [optional] |
|**country** | **String** | Merchant country as an ISO 3166-1 alpha-3 code (for example &#x60;USA&#x60;, &#x60;GBR&#x60;, &#x60;FRA&#x60;), case insensitive; any other value is rejected with &#x60;400&#x60;. Defaults to &#x60;USA&#x60;.  |  [optional] |



