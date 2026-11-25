package co.astrada.client.pagination;

import co.astrada.client.ApiException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

/**
 * Walks every page of a list call. Hand-written; not generated.
 *
 * <pre>{@code
 * SubaccountsApi subaccounts = new SubaccountsApi(client);
 * for (Subaccount s : Pages.iterate(cursor -> subaccounts.listSubaccounts(cursor, 100),
 *         page -> page.getEmbedded().getSubaccounts())) {
 *     System.out.println(s.getId());
 * }
 * }</pre>
 *
 * <p>Pages are fetched as the loop reaches them, one request each. A failed request throws
 * {@link ApiException} from the loop. Cursor lists use {@link #iterate}; the card, card
 * subscription and network bulk feed lists page by offset and use {@link #iterateByOffset}.
 */
public final class Pages {
    private Pages() {}

    /** Fetches one page; {@code position} is null for the first page. */
    @FunctionalInterface
    public interface Fetcher<K, P> {
        P fetch(K position) throws ApiException;
    }

    /** Every item of a cursor-paginated list, first page first. */
    public static <P extends Page, T> Iterable<T> iterate(Fetcher<String, P> fetch,
                                                         Function<? super P, ? extends List<T>> items) {
        return items(pages(fetch), items);
    }

    /** Every page of a cursor-paginated list. */
    public static <P extends Page> Iterable<P> pages(Fetcher<String, P> fetch) {
        Objects.requireNonNull(fetch, "fetch");
        return () -> new PageIterator<>(fetch, Page::nextCursor, Page::nextOffset, "Pages.iterateByOffset");
    }

    /** Every item of an offset-paginated list (cards, card subscriptions, network bulk feeds). */
    public static <P extends Page, T> Iterable<T> iterateByOffset(Fetcher<Integer, P> fetch,
                                                                 Function<? super P, ? extends List<T>> items) {
        return items(pagesByOffset(fetch), items);
    }

    /** Every page of an offset-paginated list. */
    public static <P extends Page> Iterable<P> pagesByOffset(Fetcher<Integer, P> fetch) {
        Objects.requireNonNull(fetch, "fetch");
        return () -> new PageIterator<>(fetch, Page::nextOffset, Page::nextCursor, "Pages.iterate");
    }

    private static <P, T> Iterable<T> items(Iterable<P> pages, Function<? super P, ? extends List<T>> items) {
        Objects.requireNonNull(items, "items");
        return () -> new Iterator<T>() {
            private final Iterator<P> pageIterator = pages.iterator();
            private Iterator<T> current = Collections.emptyIterator();

            @Override
            public boolean hasNext() {
                while (!current.hasNext() && pageIterator.hasNext()) {
                    List<T> list = items.apply(pageIterator.next());
                    current = list == null ? Collections.<T>emptyIterator() : list.iterator();
                }
                return current.hasNext();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return current.next();
            }
        };
    }

    private static final class PageIterator<K, P extends Page> implements Iterator<P> {
        private final Fetcher<K, P> fetch;
        private final Function<Page, K> nextPosition;
        private final Function<Page, ?> otherPosition;
        private final String otherMethod;
        private K position;
        private boolean started;
        private boolean done;

        PageIterator(Fetcher<K, P> fetch, Function<Page, K> nextPosition, Function<Page, ?> otherPosition,
                     String otherMethod) {
            this.fetch = fetch;
            this.nextPosition = nextPosition;
            this.otherPosition = otherPosition;
            this.otherMethod = otherMethod;
        }

        @Override
        public boolean hasNext() {
            return !done;
        }

        @Override
        public P next() {
            if (done) {
                throw new NoSuchElementException();
            }
            P page = fetch.fetch(started ? position : null);
            started = true;
            K next = page == null ? null : nextPosition.apply(page);
            if (next == null && page != null && otherPosition.apply(page) != null) {
                // Stopping here would silently drop every later page.
                throw new IllegalStateException("This list pages the other way: use " + otherMethod);
            }
            // Stop at the last page, and if the API ever handed back the same position again.
            if (next == null || next.equals(position)) {
                done = true;
            }
            position = next;
            return page;
        }
    }
}
