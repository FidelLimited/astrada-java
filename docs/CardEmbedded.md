

# CardEmbedded

Related resources inlined on request with the `expand` query parameter (`?expand=cardholder`). The `cardholder` key is present only when it was requested and the cardholder exists; its body is identical to the one served by `GET /cards/{cardId}/cardholder`. Absent entirely when `expand` is not sent. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**cardholder** | [**Cardholder**](Cardholder.md) |  |  [optional] |



