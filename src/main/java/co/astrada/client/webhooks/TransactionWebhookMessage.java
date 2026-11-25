package co.astrada.client.webhooks;

import co.astrada.client.model.TransactionDateTime;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.UUID;

/**
 * One of the transaction messages a transaction webhook carries in {@code _embedded.messages}.
 * Hand-written; not generated. {@link WebhookEvent#getTransactionMessages()} makes them.
 *
 * <p>This is a short form of a transaction message ({@code _links}, {@code id}, {@code messageType},
 * {@code network}, {@code result}, {@code dateTime}, {@code approvalCode},
 * {@code transactionReference}), so every getter may return null. {@link #getJson()} has any other
 * key the webhook sends. Read the full message with {@code getTransactionMessage} for the rest.
 */
public final class TransactionWebhookMessage {
    private final JsonObject json;

    TransactionWebhookMessage(JsonObject json) {
        this.json = json;
    }

    /** The message as sent. */
    public JsonObject getJson() {
        return json;
    }

    /** The message id, or null when it is missing or not a UUID ({@link #getJson()} still has it). */
    public UUID getId() {
        String id = string("id");
        try {
            return id == null ? null : UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public String getMessageType() {
        return string("messageType");
    }

    public String getNetwork() {
        return string("network");
    }

    public String getResult() {
        return string("result");
    }

    public String getApprovalCode() {
        return string("approvalCode");
    }

    public String getTransactionReference() {
        return string("transactionReference");
    }

    /**
     * When the message happened, read the same way as {@code TransactionMessage.getDateTime()}: with
     * an offset for Visa and Amex (UTC), without one for Mastercard (the merchant's local time).
     */
    public TransactionDateTime getDateTime() {
        JsonElement raw = json.get("dateTime");
        // Read like the API models: a form the SDK cannot read is kept (isParsed() false).
        return raw == null || !raw.isJsonPrimitive() ? null
                : co.astrada.client.JSON.getGson().fromJson(raw, TransactionDateTime.class);
    }

    private String string(String key) {
        JsonElement value = json.get(key);
        // Anything but a string or number (an object, an array) reads as null; getJson() keeps it.
        return value == null || !value.isJsonPrimitive() ? null : value.getAsString();
    }

    @Override
    public String toString() {
        return json.toString();
    }
}
