package co.astrada.client.pagination;

import co.astrada.client.model.Link;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * A list response whose next page is the {@code _links.next.href} URL (the card, subaccount,
 * transaction, webhook and enrollment link lists). Hand-written.
 */
public interface LinkedPage extends Page {
    NextLink getLinks();

    @Override
    default String nextCursor() {
        return queryParameter(nextHref(this), "cursor");
    }

    @Override
    default Integer nextOffset() {
        String offset = queryParameter(nextHref(this), "offset");
        return offset == null ? null : Integer.valueOf(offset);
    }

    private static String nextHref(LinkedPage page) {
        NextLink links = page.getLinks();
        Link next = links == null ? null : links.getNext();
        return next == null ? null : next.getHref();
    }

    /** The decoded value of a query parameter in a URL or path, or null. */
    static String queryParameter(String href, String name) {
        if (href == null) {
            return null;
        }
        int question = href.indexOf('?');
        if (question < 0) {
            return null;
        }
        for (String pair : href.substring(question + 1).split("&")) {
            int equals = pair.indexOf('=');
            String key = equals < 0 ? pair : pair.substring(0, equals);
            if (key.equals(name)) {
                String value = equals < 0 ? "" : pair.substring(equals + 1);
                return URLDecoder.decode(value, StandardCharsets.UTF_8);
            }
        }
        return null;
    }
}
