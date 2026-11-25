package co.astrada.client.webhooks;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.astrada.client.webhooks.WebhookVerifier.WebhookVerificationException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Checks WebhookVerifier against two known deliveries: SVIX, the test vector published with the
 * Svix and Standard Webhooks libraries; ASTRADA, a delivery signed by the svix library 1.36.0 (the
 * version service-webhooks-platform uses) with a secret made the way Astrada makes them, base64 of
 * a UUID with no whsec_ prefix.
 */
class WebhookVerifierTest {
    private static final String SVIX_SECRET = "whsec_MfKQ9r8GKYqrTwjUPD8ILPZIo2LaLaSw"; // gitleaks:allow (test vector)
    private static final String SVIX_ID = "msg_p5jXN8AQM9LWM0D4loKWxJek";
    private static final long SVIX_TIMESTAMP = 1614265330L;
    private static final String SVIX_BODY = "{\"test\": 2432232314}";
    private static final String SVIX_SIGNATURE = "v1,g0hM9SsE+OTPJTGt/tmIKtSyZlE3uFJELVlNIOLJ1OE=";

    private static final String SECRET = "OGYxNGU0NWYtY2VlYS00NjdhLTlkMmItMWEyYjNjNGQ1ZTZm"; // gitleaks:allow (test vector)
    private static final String ID = "msg_2Qh7Yx0Example";
    private static final long TIMESTAMP = 1764000000L;
    private static final String BODY = "{\"type\":\"transaction.created\",\"data\":{\"id\":\"txn-1\"}}";
    private static final String SIGNATURE = "v1,ugIbG2sIrsoxOkgpZ737QRM1NgtSDpGCDnpIVrDPAtE=";

    private static Map<String, String> headers(String prefix, String id, long timestamp, String signature) {
        Map<String, String> headers = new HashMap<>();
        headers.put(prefix + "-id", id);
        headers.put(prefix + "-timestamp", Long.toString(timestamp));
        headers.put(prefix + "-signature", signature);
        return headers;
    }

    @Test
    void acceptsThePublishedSvixVector() {
        assertDoesNotThrow(() -> new WebhookVerifier(SVIX_SECRET)
                .verify(SVIX_BODY, headers("webhook", SVIX_ID, SVIX_TIMESTAMP, SVIX_SIGNATURE), SVIX_TIMESTAMP));
    }

    @Test
    void acceptsAnAstradaSecretWithEitherHeaderSet() {
        WebhookVerifier verifier = new WebhookVerifier(SECRET);
        assertDoesNotThrow(() -> verifier.verify(BODY, headers("webhook", ID, TIMESTAMP, SIGNATURE), TIMESTAMP));
        assertDoesNotThrow(() -> verifier.verify(BODY, headers("svix", ID, TIMESTAMP, SIGNATURE), TIMESTAMP));
        assertDoesNotThrow(() -> verifier.verify(BODY, headers("Webhook", ID, TIMESTAMP, SIGNATURE), TIMESTAMP));
    }

    @Test
    void acceptsAnyMatchingEntryInAMultiSignatureHeader() {
        Map<String, String> headers = headers("webhook", ID, TIMESTAMP, "v1,bm90IHRoZSBzaWduYXR1cmU= " + SIGNATURE);
        assertDoesNotThrow(() -> new WebhookVerifier(SECRET).verify(BODY, headers, TIMESTAMP));
    }

    @Test
    void rejectsAChangedBodyAWrongSecretAndAMissingHeader() {
        Map<String, String> headers = headers("webhook", ID, TIMESTAMP, SIGNATURE);
        assertThrows(WebhookVerificationException.class,
                () -> new WebhookVerifier(SECRET).verify(BODY.replace("txn-1", "txn-2"), headers, TIMESTAMP));
        assertThrows(WebhookVerificationException.class,
                () -> new WebhookVerifier(SVIX_SECRET).verify(BODY, headers, TIMESTAMP));
        Map<String, String> missing = new HashMap<>(headers);
        missing.remove("webhook-signature");
        assertThrows(WebhookVerificationException.class,
                () -> new WebhookVerifier(SECRET).verify(BODY, missing, TIMESTAMP));
    }

    @Test
    void rejectsADeliveryMoreThanFiveMinutesOff() {
        WebhookVerifier verifier = new WebhookVerifier(SECRET);
        Map<String, String> headers = headers("webhook", ID, TIMESTAMP, SIGNATURE);
        assertDoesNotThrow(() -> verifier.verify(BODY, headers, TIMESTAMP + 300));
        assertThrows(WebhookVerificationException.class, () -> verifier.verify(BODY, headers, TIMESTAMP + 301));
        assertThrows(WebhookVerificationException.class, () -> verifier.verify(BODY, headers, TIMESTAMP - 301));
    }

    @Test
    void anExtremeTimestampIsOutsideTheWindowWithoutOverflow() {
        WebhookVerifier verifier = new WebhookVerifier(SECRET);
        for (long sentAt : new long[] {Long.MIN_VALUE, Long.MIN_VALUE + 1, Long.MAX_VALUE}) {
            assertThrows(WebhookVerifier.WebhookVerificationException.class,
                    () -> verifier.verify(BODY, headers("webhook", ID, sentAt, SIGNATURE), TIMESTAMP), Long.toString(sentAt));
        }
        WebhookVerifier wide = new WebhookVerifier(SECRET, Long.MAX_VALUE);
        assertDoesNotThrow(() -> wide.verify(BODY, headers("webhook", ID, TIMESTAMP, SIGNATURE), TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> new WebhookVerifier(SECRET, -1));
    }
}

