package co.astrada.client.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.astrada.client.JSON;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

/**
 * dateTime is a TransactionDateTime: UTC with Z for Visa and Amex (an exact moment), and the
 * merchant's local time with no offset for Mastercard (no exact moment). No method reads the host
 * time zone, so these results do not depend on it.
 */
class TransactionMessageDateTimeTest {
    private static JsonObject example() throws Exception {
        String stub = new String(Files.readAllBytes(Paths.get("mock/mappings/GetTransactionMessage.json")), StandardCharsets.UTF_8);
        return JsonParser.parseString(stub).getAsJsonObject().getAsJsonObject("response").getAsJsonObject("jsonBody");
    }

    @Test
    void aMastercardLocalTimeHasNoOffsetAndNoInstant() throws Exception {
        JsonObject json = example();
        json.addProperty("dateTime", "2026-01-01T10:00:00");
        TransactionDateTime value = JSON.getGson().fromJson(json, TransactionMessage.class).getDateTime();
        assertEquals(LocalDateTime.of(2026, 1, 1, 10, 0), value.getLocalDateTime());
        assertFalse(value.hasOffset());
        assertFalse(value.getOffset().isPresent());
        assertFalse(value.toInstant().isPresent());
        assertEquals(Instant.parse("2026-01-01T15:00:00Z"), value.atOffset(ZoneOffset.ofHours(-5)).toInstant());
    }

    @Test
    void aVisaOrAmexTimeIsAnExactMoment() throws Exception {
        JsonObject json = example();
        json.addProperty("dateTime", "2026-01-01T10:00:00.250Z");
        TransactionDateTime value = JSON.getGson().fromJson(json, TransactionMessage.class).getDateTime();
        assertTrue(value.hasOffset());
        assertEquals(ZoneOffset.UTC, value.getOffset().get());
        assertEquals(Instant.parse("2026-01-01T10:00:00.250Z"), value.toInstant().get());
        assertEquals(Instant.parse("2026-01-01T04:30:00Z"), TransactionDateTime.parse("2026-01-01T10:00:00+05:30").toInstant().get());
    }

    @Test
    void bothFormsAreWrittenBackAsSent() throws Exception {
        for (String raw : new String[] {"2026-01-01T10:00:00Z", "2026-01-01T10:00:00", "2026-01-01T10:00:00.250+05:30"}) {
            JsonObject json = example();
            json.addProperty("dateTime", raw);
            TransactionMessage message = JSON.getGson().fromJson(json, TransactionMessage.class);
            assertEquals(raw, message.getDateTime().getRaw());
            assertEquals(raw, JSON.getGson().toJsonTree(message).getAsJsonObject().get("dateTime").getAsString());
            // The JSON it writes reads back (a required field that is null, acceptor.acquirerId, is kept).
            assertEquals(raw, TransactionMessage.fromJson(message.toJson()).getDateTime().getRaw());
        }
        assertThrows(IllegalArgumentException.class, () -> TransactionDateTime.parse("yesterday"));
    }

    @Test
    void aFormItCannotReadKeepsTheMessageAndOnlyTheTypedGetterFails() throws Exception {
        for (String raw : new String[] {"2026-01-01 10:00:00", "2026-01-01T10:00:00+0000", "2026-02-30T10:00:00Z"}) {
            JsonObject json = example();
            json.addProperty("dateTime", raw);
            TransactionMessage message = JSON.getGson().fromJson(json, TransactionMessage.class);
            TransactionDateTime value = message.getDateTime();
            assertEquals(raw, value.getRaw());
            assertFalse(value.isParsed());
            assertFalse(value.toInstant().isPresent());
            assertThrows(IllegalStateException.class, value::getLocalDateTime);
            assertEquals(raw, JSON.getGson().toJsonTree(message).getAsJsonObject().get("dateTime").getAsString());
            assertThrows(IllegalArgumentException.class, () -> TransactionDateTime.parse(raw));
        }
    }

    @Test
    void nullsWrittenAreTheRequiredOnesAndUnknownFieldsThatCameNull() throws Exception {
        JsonObject json = example();
        json.add("somethingNew", com.google.gson.JsonNull.INSTANCE);
        json.getAsJsonObject("acceptor").add("acquirerId", com.google.gson.JsonNull.INSTANCE);
        json.getAsJsonObject("acceptor").add("anotherNew", com.google.gson.JsonNull.INSTANCE);
        TransactionMessage message = JSON.getGson().fromJson(json, TransactionMessage.class);
        message.setApprovalCode(null); // optional: left out
        JsonObject written = JsonParser.parseString(message.toJson()).getAsJsonObject();
        assertTrue(written.get("somethingNew").isJsonNull());
        assertFalse(written.has("approvalCode"));
        assertTrue(written.getAsJsonObject("acceptor").get("acquirerId").isJsonNull()); // required, nullable
        assertTrue(written.getAsJsonObject("acceptor").get("anotherNew").isJsonNull());
        java.util.Map<String, Object> free = new java.util.HashMap<>();
        free.put("gone", null);
        message.putAdditionalProperty("freeForm", free);
        assertEquals("{}", JsonParser.parseString(message.toJson()).getAsJsonObject().get("freeForm").toString());
    }
}
