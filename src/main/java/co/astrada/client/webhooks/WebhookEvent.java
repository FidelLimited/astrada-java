package co.astrada.client.webhooks;

import co.astrada.client.JSON;
import co.astrada.client.model.CardSubscription;
import co.astrada.client.model.Transaction;
import co.astrada.client.model.TransactionMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A verified webhook delivery: its event type and its body. Hand-written; not generated.
 * {@link WebhookVerifier#verifyAndParse} makes one.
 *
 * <p>The body is the resource itself, as the matching GET returns it. The typed getters read the
 * events whose body the API definition describes:
 * <ul>
 *   <li>{@code transaction.created}, {@code transaction.updated}: {@link #getTransaction()}, and
 *       {@link #getTransactionMessages()} for the messages in its {@code _embedded.messages}</li>
 *   <li>{@code transactionmessage.created}: {@link #getTransactionMessage()}</li>
 *   <li>{@code cardsubscription.created}, {@code cardsubscription.updated}:
 *       {@link #getCardSubscription()}</li>
 * </ul>
 * For any other event read {@link #getData()}, or {@link #getData(Class)} with a model.
 */
public final class WebhookEvent {
    public static final String TRANSACTION_CREATED = "transaction.created";
    public static final String TRANSACTION_UPDATED = "transaction.updated";
    public static final String TRANSACTION_MESSAGE_CREATED = "transactionmessage.created";
    public static final String CARD_SUBSCRIPTION_CREATED = "cardsubscription.created";
    public static final String CARD_SUBSCRIPTION_UPDATED = "cardsubscription.updated";

    private final String id;
    private final String type;
    private final String payload;
    private JsonObject data;

    /**
     * A delivery your server verified earlier and stored, read again: for replaying an event this SDK
     * could not read when it arrived ({@link WebhookPayloadException}), after you update the SDK. It
     * checks no signature (and {@link WebhookVerifier}'s age limit would refuse a stored delivery
     * anyway), so pass only what you stored after a successful check, never a body from a request,
     * and keep that record of the check with it. Skip ids you already handled; for a transaction
     * apply it only if its {@code updatedAt} is newer than your copy's, and for other resources
     * fetch the current state first ({@link #getId()} and the send time are no guide to order).
     *
     * @throws IllegalArgumentException an empty id, a blank type, or no body
     *
     * @param id the delivery's {@code webhook-id} ({@link #getId()})
     * @param type the delivery's {@code webhook-event-type} ({@link #getType()}); null when the
     *     delivery had none, as {@link WebhookVerifier#verifyAndParse} allows
     * @param payload the raw body as received ({@link #getPayload()})
     */
    public static WebhookEvent parseStored(String id, String type, String payload) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("parseStored needs the stored webhook-id (id is empty)");
        }
        if (type != null && type.trim().isEmpty()) {
            // Null is allowed, as verifyAndParse allows a delivery without the header.
            throw new IllegalArgumentException("parseStored needs the stored webhook-event-type, or null when the "
                    + "delivery had none (type is blank)");
        }
        if (payload == null) {
            throw new IllegalArgumentException("parseStored needs the stored body");
        }
        return new WebhookEvent(id, type, payload);
    }

    WebhookEvent(String id, String type, String payload) {
        this.id = id;
        this.type = type;
        this.payload = payload;
    }

    /** The delivery's {@code webhook-id}; the same on every retry of one event, so use it to skip duplicates. */
    public String getId() {
        return id;
    }

    /**
     * The {@code webhook-event-type} header, for example {@code transaction.created}; null if
     * absent. The signature covers the id, the timestamp and the body, not this header, so the
     * typed getters trust it as far as the transport does (HTTPS to your endpoint).
     */
    public String getType() {
        return type;
    }

    /** The raw body, as received. */
    public String getPayload() {
        return payload;
    }

    /**
     * The body as a JSON object.
     *
     * @throws WebhookPayloadException the body is not a JSON object: store the delivery, answer 5xx,
     *     then 2xx once stored (see {@link #getData(Class)}), and replay it with {@link #parseStored}
     */
    public synchronized JsonObject getData() {
        if (data == null) {
            JsonElement element;
            try {
                element = JsonParser.parseString(payload);
            } catch (RuntimeException e) {
                throw new WebhookPayloadException("The webhook body (" + type + ") is not JSON: " + e.getMessage(), e);
            }
            if (!element.isJsonObject()) {
                throw new WebhookPayloadException("The webhook body (" + type + ") is not a JSON object", null);
            }
            data = element.getAsJsonObject();
        }
        return data;
    }

    /**
     * The body read as a generated model, for example {@code BankTransaction.class}.
     *
     * @throws WebhookPayloadException the body does not fit the model (often an SDK older than the
     *     event). Store the delivery first ({@link #getId()}, {@link #getType()},
     *     {@link #getPayload()} and the time of the check), then answer 5xx so Astrada delivers it
     *     again; answer 2xx once it is stored and has failed a few times. After updating the SDK,
     *     read the stored copy with {@link #parseStored}. Answer 400 only for a failed verification.
     */
    public <T> T getData(Class<T> type) {
        JsonObject json = getData();
        try {
            return JSON.getGson().fromJson(json, type);
        } catch (RuntimeException e) {
            throw new WebhookPayloadException("The webhook body (" + this.type + ") does not fit "
                    + type.getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * A delivery whose body this SDK cannot read as its model: one that passed the signature check,
     * or a stored one read with {@link #parseStored}. For a delivery just received, store it first
     * (id, type, raw body and the time of the check), then answer 5xx so Astrada delivers it again,
     * and answer 2xx once it is stored and has failed a few times. After updating the SDK, read the
     * stored copy with {@link #parseStored}. Answer 400 only for a failed verification.
     */
    public static final class WebhookPayloadException extends RuntimeException {
        WebhookPayloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /** The transaction, for {@code transaction.created} and {@code transaction.updated}; else null. */
    public Transaction getTransaction() {
        return isType(TRANSACTION_CREATED, TRANSACTION_UPDATED) ? getData(Transaction.class) : null;
    }

    /**
     * The short-form messages in a {@code transaction.created} or {@code transaction.updated} body's
     * {@code _embedded.messages}, with {@code dateTime} typed; an empty list for other events or
     * when the body has none.
     */
    public List<TransactionWebhookMessage> getTransactionMessages() {
        if (!isType(TRANSACTION_CREATED, TRANSACTION_UPDATED)) {
            return Collections.emptyList();
        }
        JsonElement embedded = getData().get("_embedded");
        JsonElement messages = embedded != null && embedded.isJsonObject()
                ? embedded.getAsJsonObject().get("messages") : null;
        if (messages == null || !messages.isJsonArray()) {
            return Collections.emptyList();
        }
        List<TransactionWebhookMessage> out = new ArrayList<>();
        for (JsonElement m : (JsonArray) messages) {
            if (m.isJsonObject()) {
                out.add(new TransactionWebhookMessage(m.getAsJsonObject()));
            }
        }
        return Collections.unmodifiableList(out);
    }

    /** The transaction message, for {@code transactionmessage.created}; else null. */
    public TransactionMessage getTransactionMessage() {
        return isType(TRANSACTION_MESSAGE_CREATED) ? getData(TransactionMessage.class) : null;
    }

    /** The card subscription, for {@code cardsubscription.created} and {@code .updated}; else null. */
    public CardSubscription getCardSubscription() {
        return isType(CARD_SUBSCRIPTION_CREATED, CARD_SUBSCRIPTION_UPDATED) ? getData(CardSubscription.class) : null;
    }

    private boolean isType(String... types) {
        for (String t : types) {
            if (t.equals(type)) {
                return true;
            }
        }
        return false;
    }
}
