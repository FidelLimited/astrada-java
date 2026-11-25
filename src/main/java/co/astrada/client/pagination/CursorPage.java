package co.astrada.client.pagination;

import co.astrada.client.model.CursorPagination;

/**
 * A Banking API list response, whose next page is {@code pagination.cursor} while
 * {@code pagination.hasMore} is true. Hand-written.
 */
public interface CursorPage extends Page {
    CursorPagination getPagination();

    @Override
    default String nextCursor() {
        CursorPagination pagination = getPagination();
        if (pagination == null || !Boolean.TRUE.equals(pagination.getHasMore())) {
            return null;
        }
        return pagination.getCursor();
    }

    @Override
    default Integer nextOffset() {
        return null;
    }
}
