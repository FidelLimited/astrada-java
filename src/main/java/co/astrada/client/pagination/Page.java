package co.astrada.client.pagination;

/**
 * One page of a list response. Hand-written; the generated list models implement it through
 * {@link LinkedPage} or {@link CursorPage} (scripts/merge-specs.mjs adds them).
 */
public interface Page {
    /** The {@code cursor} to pass for the next page, or null on the last page or an offset list. */
    String nextCursor();

    /** The {@code offset} to pass for the next page, or null on the last page or a cursor list. */
    Integer nextOffset();
}
