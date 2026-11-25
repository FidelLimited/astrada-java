package co.astrada.client.sandbox;

/**
 * The kind of network bulk feed a simulated file is for; the same values as a feed's
 * {@code type}. Hand-written; not generated.
 */
public enum BulkFeedType {
    /** American Express. */
    GL,
    /** Mastercard. */
    SMARTDATA,
    /** Visa. */
    VBDS,
    /** Visa. */
    VCF
}
