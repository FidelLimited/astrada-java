package co.astrada.client.model;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Objects;
import java.util.Optional;

/**
 * A transaction message's {@code dateTime}, which keeps what the API sent. Hand-written; not
 * generated (scripts/typed-dates.mjs gives {@link TransactionMessage#getDateTime()} this type).
 *
 * <p>Visa and Amex send UTC ({@code ...Z}): an exact moment, so {@link #getOffset()} is present
 * and {@link #toOffsetDateTime()} and {@link #toInstant()} return it. Mastercard sends the
 * merchant's local date and time with no offset: the wall-clock time where the card was used.
 * Astrada does not receive that merchant's time zone, so it is not an exact moment: the offset is
 * empty, and so are {@code toOffsetDateTime()} and {@code toInstant()}. Use
 * {@link #getLocalDateTime()} to show it, or {@link #atOffset(ZoneOffset)} when you know the
 * merchant's offset. {@link #getRaw()} and JSON give back exactly what the API sent.
 *
 * <p>A value in a form this SDK cannot read does not fail the response: it is kept as
 * {@link #getRaw()}, {@link #isParsed()} is false, the offset and moment are empty, and
 * {@link #getLocalDateTime()} throws.
 */
@JsonAdapter(TransactionDateTime.GsonAdapter.class)
public final class TransactionDateTime {
    private final String raw;
    private final LocalDateTime localDateTime;
    private final ZoneOffset offset;

    private TransactionDateTime(String raw, LocalDateTime localDateTime, ZoneOffset offset) {
        this.raw = raw;
        this.localDateTime = localDateTime;
        this.offset = offset;
    }

    /**
     * Reads an ISO 8601 date-time, with or without an offset.
     *
     * @throws IllegalArgumentException for anything else
     */
    public static TransactionDateTime parse(String raw) {
        Objects.requireNonNull(raw, "raw");
        try {
            TemporalAccessor parsed = DateTimeFormatter.ISO_DATE_TIME.parse(raw);
            LocalDateTime local = LocalDateTime.from(parsed);
            ZoneOffset offset = parsed.isSupported(java.time.temporal.ChronoField.OFFSET_SECONDS)
                    ? ZoneOffset.from(parsed) : null;
            return new TransactionDateTime(raw, local, offset);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Not an ISO 8601 date-time: " + raw, e);
        }
    }

    /** The value as the API sent it, such as {@code 2026-01-01T10:00:00} or {@code 2026-01-01T10:00:00Z}. */
    public String getRaw() {
        return raw;
    }

    /** False when the API sent a form this SDK cannot read; only {@link #getRaw()} has it then. */
    public boolean isParsed() {
        return localDateTime != null;
    }

    /**
     * The date and time as written: UTC for Visa and Amex, the merchant's local time for Mastercard.
     *
     * @throws IllegalStateException when {@link #isParsed()} is false
     */
    public LocalDateTime getLocalDateTime() {
        if (localDateTime == null) {
            throw new IllegalStateException("Not an ISO 8601 date-time this SDK can read: " + raw);
        }
        return localDateTime;
    }

    /** The offset the API sent (UTC for Visa and Amex), or empty when it sent none (Mastercard). */
    public Optional<ZoneOffset> getOffset() {
        return Optional.ofNullable(offset);
    }

    /** True when the API sent an offset: the value is an exact moment. */
    public boolean hasOffset() {
        return offset != null;
    }

    /** The exact moment with its offset, or empty when the API sent no offset. */
    public Optional<OffsetDateTime> toOffsetDateTime() {
        return offset == null || localDateTime == null ? Optional.empty() : Optional.of(localDateTime.atOffset(offset));
    }

    /** The exact moment, or empty when the API sent no offset. */
    public Optional<Instant> toInstant() {
        return toOffsetDateTime().map(OffsetDateTime::toInstant);
    }

    /** This wall-clock time at the given offset, for a value without one when you know the merchant's. */
    public OffsetDateTime atOffset(ZoneOffset merchantOffset) {
        return getLocalDateTime().atOffset(Objects.requireNonNull(merchantOffset, "merchantOffset"));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TransactionDateTime && raw.equals(((TransactionDateTime) o).raw);
    }

    @Override
    public int hashCode() {
        return raw.hashCode();
    }

    /** The raw string. */
    @Override
    public String toString() {
        return raw;
    }

    /** Gson: reads the JSON string (keeping a form it cannot read as raw) and writes the raw string back. */
    public static final class GsonAdapter extends TypeAdapter<TransactionDateTime> {
        @Override
        public void write(JsonWriter out, TransactionDateTime value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.raw);
            }
        }

        @Override
        public TransactionDateTime read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String raw = in.nextString();
            try {
                return parse(raw);
            } catch (IllegalArgumentException e) {
                // A response must not fail on one value: keep it, and let the typed getters say so.
                return new TransactionDateTime(raw, null, null);
            }
        }
    }
}
