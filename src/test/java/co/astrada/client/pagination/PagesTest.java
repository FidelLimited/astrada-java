package co.astrada.client.pagination;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import co.astrada.client.model.Card;
import co.astrada.client.model.CardList;
import co.astrada.client.model.CardListEmbedded;
import co.astrada.client.model.Link;
import co.astrada.client.model.OffsetPageLinks;
import co.astrada.client.model.WebhookList;
import co.astrada.client.model.WebhookListLinks;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Offset and cursor positions read from the generated list models. Hand-written. */
class PagesTest {
    private static CardList cards(String nextHref, Card... items) {
        OffsetPageLinks links = new OffsetPageLinks().self(new Link().href("/cards"));
        if (nextHref != null) {
            links.next(new Link().href(nextHref));
        }
        return new CardList().links(links).embedded(new CardListEmbedded().cards(Arrays.asList(items)));
    }

    @Test
    void walksAnOffsetList() {
        Card a = new Card().last4digits("1111");
        Card b = new Card().last4digits("2222");
        List<Integer> offsets = new ArrayList<>();
        List<String> seen = new ArrayList<>();
        for (Card c : Pages.iterateByOffset(offset -> {
            offsets.add(offset);
            return offset == null ? cards("/cards?limit=1&offset=1", a) : cards(null, b);
        }, page -> page.getEmbedded().getCards())) {
            seen.add(c.getLast4digits());
        }
        assertEquals(Arrays.asList(null, 1), offsets);
        assertEquals(List.of("1111", "2222"), seen);
    }

    @Test
    void readsTheCursorOfAWebhookList() {
        WebhookList page = new WebhookList().links(new WebhookListLinks()
                .next(new Link().href("https://api.astrada.co/webhooks?limit=2&cursor=a%2Bb%3D")));
        assertEquals("a+b=", page.nextCursor());
        assertNull(page.nextOffset());
        assertNull(new WebhookList().links(new WebhookListLinks()).nextCursor());
    }

    @Test
    void stopsWhenTheCursorRepeats() {
        int[] calls = {0};
        WebhookList same = new WebhookList().links(new WebhookListLinks().next(new Link().href("/webhooks?cursor=x")));
        for (WebhookList p : Pages.pages(cursor -> {
            calls[0]++;
            return same;
        })) {
            assertEquals("x", p.nextCursor());
        }
        assertEquals(2, calls[0]);
    }

    @Test
    void refusesTheWrongWalkInsteadOfStoppingEarly() {
        CardList offsetPage = cards("/cards?limit=1&offset=1", new Card().last4digits("1111"));
        java.util.Iterator<Card> it = Pages.iterate(cursor -> offsetPage, page -> page.getEmbedded().getCards()).iterator();
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, it::hasNext);

        WebhookList cursorPage = new WebhookList().links(new WebhookListLinks().next(new Link().href("/webhooks?cursor=x")));
        java.util.Iterator<WebhookList> pages = Pages.pagesByOffset(offset -> cursorPage).iterator();
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, pages::next);
    }
}
