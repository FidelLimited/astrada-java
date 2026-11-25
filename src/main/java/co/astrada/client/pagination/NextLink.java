package co.astrada.client.pagination;

import co.astrada.client.model.Link;

/** The {@code _links} of a list response. Hand-written; the generated links models implement it. */
public interface NextLink {
    /** The link to the next page, or null on the last page. */
    Link getNext();
}
