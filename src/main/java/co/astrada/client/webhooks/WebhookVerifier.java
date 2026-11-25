package co.astrada.client.webhooks;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Checks that a webhook came from Astrada. Hand-written; not generated.
 *
 * <p>Astrada delivers webhooks through Svix. Each delivery carries {@code webhook-id},
 * {@code webhook-timestamp} and {@code webhook-signature} headers (Svix also sends the same values as
 * {@code svix-id}, {@code svix-timestamp} and {@code svix-signature}). The signature is {@code v1,} +
 * base64(HMAC-SHA256(key, "{id}.{timestamp}.{body}")), where the key is the base64-decoded webhook
 * secret returned once by {@code POST /webhooks}.
 *
 * <pre>{@code
 * WebhookVerifier verifier = new WebhookVerifier(webhookSecret);
 * WebhookEvent event = verifier.verifyAndParse(rawBody, headers); // throws when it must be rejected
 * Transaction transaction = event.getTransaction(); // for transaction.created and .updated
 * }</pre>
 */
public class WebhookVerifier {
    private static final long DEFAULT_TOLERANCE_SECONDS = 300;

    private final byte[] key;
    private final long toleranceSeconds;

    public WebhookVerifier(String secret) {
        this(secret, DEFAULT_TOLERANCE_SECONDS);
    }

    /** @param toleranceSeconds largest accepted difference between the delivery time and now */
    public WebhookVerifier(String secret, long toleranceSeconds) {
        if (secret == null) {
            throw new IllegalArgumentException("secret is required");
        }
        String encoded = secret.startsWith("whsec_") ? secret.substring("whsec_".length()) : secret;
        try {
            this.key = Base64.getDecoder().decode(encoded);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The webhook secret is not base64", e);
        }
        if (key.length == 0) {
            throw new IllegalArgumentException("The webhook secret is empty");
        }
        if (toleranceSeconds < 0) {
            throw new IllegalArgumentException("toleranceSeconds must be 0 or more");
        }
        this.toleranceSeconds = toleranceSeconds;
    }

    /**
     * Checks the signature and age of a delivery against the current time.
     *
     * @param payload the raw request body, exactly as received (do not parse and re-serialise it)
     * @param headers the request headers; names are matched case-insensitively
     */
    public void verify(String payload, Map<String, String> headers) throws WebhookVerificationException {
        verify(payload, headers, System.currentTimeMillis() / 1000L);
    }

    /**
     * Checks the delivery like {@link #verify(String, Map)} and returns it as an event with its
     * type ({@code webhook-event-type}) and body.
     *
     * @param payload the raw request body, exactly as received
     * @param headers the request headers; names are matched case-insensitively
     */
    public WebhookEvent verifyAndParse(String payload, Map<String, String> headers)
            throws WebhookVerificationException {
        verify(payload, headers);
        return new WebhookEvent(header(headers, "webhook-id", "svix-id"),
                header(headers, "webhook-event-type", "webhook-event-type"), payload);
    }

    /** Same as {@link #verify(String, Map)}, with the current Unix time given; for tests. */
    public void verify(String payload, Map<String, String> headers, long nowSeconds)
            throws WebhookVerificationException {
        String id = header(headers, "webhook-id", "svix-id");
        String timestamp = header(headers, "webhook-timestamp", "svix-timestamp");
        String signatures = header(headers, "webhook-signature", "svix-signature");
        if (id == null || timestamp == null || signatures == null) {
            throw new WebhookVerificationException(
                    "Missing webhook-id, webhook-timestamp or webhook-signature header");
        }

        long sentAt;
        try {
            sentAt = Long.parseLong(timestamp.trim());
        } catch (NumberFormatException e) {
            throw new WebhookVerificationException("Invalid webhook-timestamp header");
        }
        // A range comparison with saturated bounds: no subtraction of the sent time, which an extreme
        // timestamp could overflow (Math.abs(Long.MIN_VALUE) is itself negative).
        long earliest = nowSeconds < Long.MIN_VALUE + toleranceSeconds ? Long.MIN_VALUE : nowSeconds - toleranceSeconds;
        long latest = nowSeconds > Long.MAX_VALUE - toleranceSeconds ? Long.MAX_VALUE : nowSeconds + toleranceSeconds;
        if (sentAt < earliest || sentAt > latest) {
            throw new WebhookVerificationException("The webhook timestamp is outside the accepted window");
        }

        byte[] expected = sign(id + "." + timestamp + "." + payload);
        // The header can hold several space-separated "v1,<signature>" entries (during secret rotation).
        for (String entry : signatures.split(" ")) {
            int comma = entry.indexOf(',');
            if (comma < 0 || !"v1".equals(entry.substring(0, comma))) {
                continue;
            }
            byte[] received;
            try {
                received = Base64.getDecoder().decode(entry.substring(comma + 1));
            } catch (IllegalArgumentException e) {
                continue;
            }
            if (MessageDigest.isEqual(received, expected)) {
                return;
            }
        }
        throw new WebhookVerificationException("No matching webhook signature");
    }

    private byte[] sign(String content) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("HmacSHA256 is not available", e);
        }
    }

    private static String header(Map<String, String> headers, String name, String fallback) {
        String value = null;
        String other = null;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            if (entry.getKey().equalsIgnoreCase(name)) {
                value = entry.getValue();
            } else if (entry.getKey().equalsIgnoreCase(fallback)) {
                other = entry.getValue();
            }
        }
        return value != null ? value : other;
    }

    /** The delivery must be rejected: bad or missing signature, or too old. */
    public static class WebhookVerificationException extends Exception {
        public WebhookVerificationException(String message) {
            super(message);
        }
    }
}
