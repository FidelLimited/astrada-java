package co.astrada.client.webhooks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.astrada.client.model.CardSubscription;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.Test;

/** verifyAndParse: a signed delivery becomes a typed event. Hand-written. */
class WebhookEventTest {
    // A made-up secret, base64 of "test-secret-for-the-sdk-tests-0001".
    private static final String SECRET = Base64.getEncoder()
            .encodeToString("test-secret-for-the-sdk-tests-0001".getBytes(StandardCharsets.UTF_8));

    // The cardsubscription.created sample from the Event types reference, with a new field added.
    private static final String BODY = "{\"_links\":{\"card\":{\"href\":\"/cards/94b9b0ce-5775-470d-855e-0c0d0e72b0ea\"},"
            + "\"self\":{\"href\":\"/card-subscriptions/aadf6c89-0356-4d94-8fa8-b5465077a0fb\"}},"
            + "\"cardId\":\"94b9b0ce-5775-470d-855e-0c0d0e72b0ea\",\"createdAt\":\"2024-09-02T09:42:39.678Z\","
            + "\"customerReferenceId\":null,\"effectiveDate\":\"2024-09-02T09:42:39.677Z\","
            + "\"enrollmentType\":\"cardholder-single\",\"expirationDate\":\"2025-09-02T09:42:43.270Z\","
            + "\"id\":\"aadf6c89-0356-4d94-8fa8-b5465077a0fb\",\"networkBulkFeedId\":null,\"state\":\"reqSCA\","
            + "\"subaccountId\":\"6103874b-248d-4825-af88-a0d24dd9b123\",\"updatedAt\":\"2024-09-02T09:42:43.576Z\","
            + "\"somethingNew\":true}";

    private static Map<String, String> signed(String type, String body) throws Exception {
        String id = "msg_test_1";
        String timestamp = Long.toString(System.currentTimeMillis() / 1000L);
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(Base64.getDecoder().decode(SECRET), "HmacSHA256"));
        String signature = Base64.getEncoder().encodeToString(
                mac.doFinal((id + "." + timestamp + "." + body).getBytes(StandardCharsets.UTF_8)));
        Map<String, String> headers = new HashMap<>();
        headers.put("Webhook-Id", id);
        headers.put("Webhook-Timestamp", timestamp);
        headers.put("Webhook-Signature", "v1," + signature);
        headers.put("Webhook-Event-Type", type);
        return headers;
    }

    @Test
    void parsesACardSubscriptionEvent() throws Exception {
        WebhookEvent event = new WebhookVerifier(SECRET).verifyAndParse(BODY, signed("cardsubscription.created", BODY));

        assertEquals("msg_test_1", event.getId());
        assertEquals(WebhookEvent.CARD_SUBSCRIPTION_CREATED, event.getType());
        CardSubscription subscription = event.getCardSubscription();
        assertEquals(CardSubscription.StateEnum.REQ_SCA, subscription.getState());
        assertEquals(Boolean.TRUE, subscription.getAdditionalProperty("somethingNew"));
        assertNull(event.getTransaction());
        assertEquals("cardholder-single", event.getData().get("enrollmentType").getAsString());
    }

    @Test
    void refusesAChangedBody() throws Exception {
        Map<String, String> headers = signed("cardsubscription.created", BODY);
        assertThrows(WebhookVerifier.WebhookVerificationException.class,
                () -> new WebhookVerifier(SECRET).verifyAndParse(BODY.replace("reqSCA", "active"), headers));
    }

    // The docs' "Transaction with Projected TransactionMessage Data" sample on a full transaction.
    private static final String TRANSACTION = "{\"_links\":{\"self\":{\"href\":\"/transactions/0b4f3c5e-8a1d-4f2b-9c3e-6d7a8b9c0d1e\"},"
            + "\"subaccount\":{\"href\":\"/subaccounts/5a6b7c8d-9e0f-4a1b-8c2d-3e4f5a6b7c8d\"},"
            + "\"card\":{\"href\":\"/cards/1c2d3e4f-5a6b-4c7d-8e9f-0a1b2c3d4e5f\"},"
            + "\"messages\":[{\"href\":\"/transaction-messages/2d3e4f5a-6b7c-4d8e-9f0a-1b2c3d4e5f6a\"}]},"
            + "\"acceptor\":{\"city\":\"LONDON\",\"country\":\"GBR\",\"mcc\":\"5734\",\"state\":\"LND\"},"
            + "\"cardId\":\"1c2d3e4f-5a6b-4c7d-8e9f-0a1b2c3d4e5f\",\"cardholderBillingCurrency\":\"GBP\","
            + "\"cardholderBillingHoldAmount\":0,\"cardholderBillingSettledAmount\":0.81,"
            + "\"createdAt\":\"2024-05-01T06:41:43.810Z\",\"descriptor\":\"Uber\","
            + "\"id\":\"0b4f3c5e-8a1d-4f2b-9c3e-6d7a8b9c0d1e\",\"network\":\"MASTERCARD\",\"status\":\"SETTLED\","
            + "\"subaccountId\":\"5a6b7c8d-9e0f-4a1b-8c2d-3e4f5a6b7c8d\",\"transactionCurrency\":\"USD\","
            + "\"transactionHoldAmount\":0,\"transactionOccurrenceDate\":\"2024-05-01\",\"transactionSettledAmount\":1,"
            + "\"updatedAt\":\"2024-05-02T08:41:43.810Z\",\"_embedded\":{\"messages\":["
            + "{\"_links\":{\"self\":{\"href\":\"/transaction-messages/2d3e4f5a-6b7c-4d8e-9f0a-1b2c3d4e5f6a\"}},"
            + "\"approvalCode\":\"537254\",\"transactionReference\":\"IWVQ22_2024-04-24\",\"dateTime\":\"2024-04-24T13:39:45\","
            + "\"id\":\"2d3e4f5a-6b7c-4d8e-9f0a-1b2c3d4e5f6a\",\"messageType\":\"FINL_ADVC\",\"network\":\"MASTERCARD\","
            + "\"result\":\"APPROVED\"},"
            + "{\"id\":\"3e4f5a6b-7c8d-4e9f-8a1b-2c3d4e5f6a7b\",\"messageType\":\"AUTH_REQU\",\"network\":\"VISA\","
            + "\"dateTime\":\"2024-04-24T13:39:45Z\"}]}}";

    @Test
    void aTransactionWebhookKeepsItsMessagesWithTheirDateTimeTyped() throws Exception {
        for (String type : new String[] {WebhookEvent.TRANSACTION_CREATED, WebhookEvent.TRANSACTION_UPDATED}) {
            WebhookEvent event = new WebhookVerifier(SECRET).verifyAndParse(TRANSACTION, signed(type, TRANSACTION));
            assertEquals("0b4f3c5e-8a1d-4f2b-9c3e-6d7a8b9c0d1e", event.getTransaction().getId().toString());
            java.util.List<TransactionWebhookMessage> messages = event.getTransactionMessages();
            assertEquals(2, messages.size());
            assertEquals("537254", messages.get(0).getApprovalCode());
            assertEquals(false, messages.get(0).getDateTime().hasOffset()); // Mastercard local time
            assertEquals(java.time.Instant.parse("2024-04-24T13:39:45Z"), messages.get(1).getDateTime().toInstant().get());
            assertEquals("FINL_ADVC", messages.get(0).getMessageType());
        }
        assertEquals(0, new WebhookVerifier(SECRET).verifyAndParse(BODY, signed("cardsubscription.created", BODY))
                .getTransactionMessages().size());
    }

    @Test
    void oneUnreadableMessageKeepsTheOthersAndAnUnreadableBodyIsAPayloadError() throws Exception {
        String body = "{\"_embedded\":{\"messages\":[{\"id\":\"not-a-uuid\",\"dateTime\":\"yesterday\"},"
                + "{\"id\":\"2d3e4f5a-6b7c-4d8e-9f0a-1b2c3d4e5f6a\",\"dateTime\":\"2024-04-24T13:39:45Z\"}]}}";
        WebhookEvent event = new WebhookVerifier(SECRET).verifyAndParse(body, signed(WebhookEvent.TRANSACTION_CREATED, body));
        java.util.List<TransactionWebhookMessage> messages = event.getTransactionMessages();
        assertEquals(2, messages.size());
        assertNull(messages.get(0).getId());
        assertEquals(false, messages.get(0).getDateTime().isParsed());
        assertEquals("2d3e4f5a-6b7c-4d8e-9f0a-1b2c3d4e5f6a", messages.get(1).getId().toString());
        assertEquals(true, messages.get(1).getDateTime().hasOffset());
        // The body is not a Transaction (no required fields): a payload error, not a verification one.
        assertThrows(WebhookEvent.WebhookPayloadException.class, event::getTransaction);
    }

    @Test
    void aNonObjectBodyOrWrongTypedFieldsAreAPayloadErrorOrNull() throws Exception {
        for (String body : new String[] {"[1,2]", "\"text\""}) {
            WebhookEvent event = new WebhookVerifier(SECRET).verifyAndParse(body, signed(WebhookEvent.TRANSACTION_CREATED, body));
            assertThrows(WebhookEvent.WebhookPayloadException.class, event::getTransaction);
            assertThrows(WebhookEvent.WebhookPayloadException.class, event::getTransactionMessages);
        }
        String body = "{\"_embedded\":{\"messages\":[{\"id\":{},\"dateTime\":{},\"network\":[1]}]}}";
        TransactionWebhookMessage message = new WebhookVerifier(SECRET).verifyAndParse(body,
                signed(WebhookEvent.TRANSACTION_UPDATED, body)).getTransactionMessages().get(0);
        assertNull(message.getId());
        assertNull(message.getDateTime());
        assertNull(message.getNetwork());
    }

    @Test
    void aStoredDeliveryIsReadAgainWithoutASignature() {
        WebhookEvent event = WebhookEvent.parseStored("msg_stored", WebhookEvent.CARD_SUBSCRIPTION_CREATED, BODY);
        assertEquals("msg_stored", event.getId());
        assertEquals(CardSubscription.StateEnum.REQ_SCA, event.getCardSubscription().getState());
        WebhookEvent unreadable = WebhookEvent.parseStored("msg_2", WebhookEvent.TRANSACTION_CREATED, "[1]");
        assertThrows(WebhookEvent.WebhookPayloadException.class, unreadable::getTransaction);
    }

    @Test
    void parseStoredReplaysAModelledTransactionAndRefusesEmptyFields() {
        WebhookEvent event = WebhookEvent.parseStored("msg_t", WebhookEvent.TRANSACTION_UPDATED, TRANSACTION);
        assertEquals("0b4f3c5e-8a1d-4f2b-9c3e-6d7a8b9c0d1e", event.getTransaction().getId().toString());
        assertEquals(2, event.getTransactionMessages().size());
        assertThrows(IllegalArgumentException.class, () -> WebhookEvent.parseStored("", WebhookEvent.TRANSACTION_UPDATED, TRANSACTION));
        assertThrows(IllegalArgumentException.class, () -> WebhookEvent.parseStored(null, WebhookEvent.TRANSACTION_UPDATED, TRANSACTION));
        assertThrows(IllegalArgumentException.class, () -> WebhookEvent.parseStored("msg_t", " ", TRANSACTION));
        // No type header: replays as plain JSON, as verifyAndParse gives it.
        WebhookEvent untyped = WebhookEvent.parseStored("msg_n", null, TRANSACTION);
        assertNull(untyped.getType());
        assertNull(untyped.getTransaction());
        assertEquals("Uber", untyped.getData().get("descriptor").getAsString());
        assertThrows(IllegalArgumentException.class, () -> WebhookEvent.parseStored("msg_t", WebhookEvent.TRANSACTION_UPDATED, null));
    }
}
