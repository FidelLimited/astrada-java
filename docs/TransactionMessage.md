

# TransactionMessage


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**TransactionMessageLinks**](TransactionMessageLinks.md) |  |  |
|**id** | **UUID** | The unique identifier of this transaction message entity. |  |
|**acceptor** | [**TransactionMessageAcceptor**](TransactionMessageAcceptor.md) |  |  |
|**approvalCode** | **String** | Unique code that authorizes a transaction message, sent by the card issuer/issuer-processor when approving a transaction. Always absent on bank-only messages; no card authorization occurred (its absence, together with the &#x60;bank_&#x60;-prefixed transactionReference, is the marker of a bank-only message). |  [optional] |
|**traceId** | **String** | The American Express trace identifier for this transaction message. Populated only when &#x60;network&#x60; is &#x60;AMEX&#x60;, where it is the reconciliation handle used to match against Amex settlement data. Amex real-time authorizations are not observed to carry an &#x60;approvalCode&#x60;, so &#x60;traceId&#x60; is the identifier to reconcile on. The value is a numeric string and leading zeros are significant, so treat it as an opaque string and never parse it as a number; its width can vary by Amex feed, so do not assume a fixed length. Null on every other network; Visa and Mastercard authorizations correlate on &#x60;transactionReference&#x60; / &#x60;approvalCode&#x60; instead. Treat null as normal, not as an error. |  [optional] |
|**discountAmount** | **BigDecimal** | The issuer-reported discount that the merchant netted from the settled amount. Today this is populated for Corpay fleet fuel coupons on Mastercard SmartData clearings. It is a positive amount in the message&#39;s &#x60;cardholderBillingCurrency&#x60;. The parent transaction&#39;s &#x60;cardholderBillingSettledAmount&#x60; is grossed up to the pre-discount statement figure, so &#x60;discountAmount&#x60; is informational. Use it to reconcile the settled amount against the receipt or fuel-card statement, which shows the discounted figure. Null on every other message, including authorizations and clearings with no coupon. Treat null as normal, not as an error. |  [optional] |
|**bankData** | [**TransactionMessageBankData**](TransactionMessageBankData.md) |  |  [optional] |
|**cardId** | **UUID** | The unique identifier of the card related to this entity. Null only for bank-sourced messages from a bank account with no linked cards (the card-less unified feed). |  |
|**cardType** | **String** | The type of card used in the transaction. The only available value is &#39;virtual&#39; and the field is nullable. |  [optional] |
|**cardholderBillingAmount** | **BigDecimal** | The amount in the currency of the country of issuance. The transactionAmount and cardholderBillingAmount values may differ in case of international transactions. |  |
|**cardholderBillingCurrency** | **String** | The currency of the country of issuance following ISO 4217 alpha-3 format. |  |
|**createdDateTime** | **OffsetDateTime** | Date and time when this resource was created, in UTC, following ISO 8601 format. |  |
|**dateTime** | **TransactionDateTime** (getRaw, getLocalDateTime, getOffset, toOffsetDateTime, toInstant; no offset for Mastercard) | The timestamp of the transaction message (e.g. authorization, movement of funds, etc) following ISO 8601 format. The timezone depends on the card network: - **Visa/Amex**: UTC (suffixed with &#x60;Z&#x60;) - **Mastercard**: Local time zone of the merchant/acquirer (no &#x60;Z&#x60; suffix, timezone offset not included)  The presence or absence of the &#x60;Z&#x60; suffix indicates whether the value is UTC or local time.  Some messages carry an end-of-day value rather than the moment they happened: - **Visa clearings** (&#x60;FINL_ADVC&#x60;, &#x60;FINL_REQU&#x60;): the settlement date at &#x60;23:59:59Z&#x60;. - **Visa reversals** (&#x60;AUTH_ADVC&#x60; with a negative &#x60;transactionAmount&#x60;): &#x60;23:59:59Z&#x60; on the UTC day Astrada received the reversal. - **Amex clearings**: the business date at &#x60;23:59:59Z&#x60;.  For the date of the purchase itself, use the transaction&#39;s &#x60;transactionOccurrenceDate&#x60;.  |  |
|**messageType** | [**MessageTypeEnum**](#MessageTypeEnum) | The type of operation this transaction message represents. [Learn More](https://docs.astrada.co/docs/transaction-message) |  |
|**network** | [**NetworkEnum**](#NetworkEnum) | The rail that processed this payment. &#x60;OPEN_BANKING&#x60; marks a bank-sourced message with no card-network involvement (the concrete provider is in &#x60;bankData.provider&#x60;). |  |
|**subaccountId** | **UUID** | The unique identifier of the subaccount related to this entity. |  |
|**result** | [**ResultEnum**](#ResultEnum) | The issuer&#39;s decision on whether the transaction message is authorized. |  |
|**transactionAmount** | **BigDecimal** | The amount of the transaction message in the currency it was charged in. In the case of international transactions, the transactionAmount and cardholderBillingAmount may differ due to being in different currencies. |  |
|**transactionCurrency** | **String** | The currency in which the transaction took place following ISO 4217 alpha-3 format. |  |
|**transactionReference** | **String** | Identification of the Transaction that has to be unique for a time period. |  |
|**transactionType** | [**TransactionTypeEnum**](#TransactionTypeEnum) | Types provide additional information about the transaction message, describing the effect that it had on the customer account and the type of accounts affected. [Learn More](https://docs.astrada.co/docs/transaction-message) |  |
|**transmissionDateTime** | **OffsetDateTime** | The timestamp of when the transaction message was received by the data source (i.e. Mastercard or Visa), in UTC, following ISO 8601 format. |  |



## Enum: MessageTypeEnum

| Name | Value |
|---- | -----|
| AUTH_ADVC | &quot;AUTH_ADVC&quot; |
| AUTH_REQU | &quot;AUTH_REQU&quot; |
| FINL_ADVC | &quot;FINL_ADVC&quot; |
| CBCK_ADVC | &quot;CBCK_ADVC&quot; |
| FINL_REQU | &quot;FINL_REQU&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: NetworkEnum

| Name | Value |
|---- | -----|
| VISA | &quot;VISA&quot; |
| MASTERCARD | &quot;MASTERCARD&quot; |
| AMEX | &quot;AMEX&quot; |
| OPEN_BANKING | &quot;OPEN_BANKING&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: ResultEnum

| Name | Value |
|---- | -----|
| APPROVED | &quot;APPROVED&quot; |
| DECLINED | &quot;DECLINED&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



## Enum: TransactionTypeEnum

| Name | Value |
|---- | -----|
| DEBIT_01 | &quot;DEBIT_01&quot; |
| DEBIT_02 | &quot;DEBIT_02&quot; |
| DEBIT_19 | &quot;DEBIT_19&quot; |
| CREDIT_01 | &quot;CREDIT_01&quot; |
| CREDIT_02 | &quot;CREDIT_02&quot; |
| CREDIT_08 | &quot;CREDIT_08&quot; |
| INQUIRY_01 | &quot;INQUIRY_01&quot; |
| TRANSFER_01 | &quot;TRANSFER_01&quot; |
| ADMIN_01 | &quot;ADMIN_01&quot; |
| ADMIN_02 | &quot;ADMIN_02&quot; |
| UNKNOWN_DEFAULT_OPEN_API | &quot;unknown_default_open_api&quot; |



