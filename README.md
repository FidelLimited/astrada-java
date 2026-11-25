# Astrada Java SDK

[![Maven Central](https://img.shields.io/maven-central/v/co.astrada/astrada-java)](https://central.sonatype.com/artifact/co.astrada/astrada-java)
[![Javadoc](https://javadoc.io/badge2/co.astrada/astrada-java/javadoc.svg)](https://javadoc.io/doc/co.astrada/astrada-java)

The Java client for the [Astrada API](https://docs.astrada.co): subaccounts, cards, card
subscriptions and verifications, transactions, webhooks, and the Banking API (bank links, bank
accounts, bank transactions and transaction matches), all from one client. Requires Java 11 or
later; it is built and tested on Java 11, 17, 21 and 25.
The SDKs have no card-enrollment methods. Card enrollment happens in the browser with the Unified Enrollment SDK v2 or an enrollment link. If you are an approved direct-API partner and send card data through the generic request method, it passes through your server and your PCI scope applies.

**Start here:** [sign up for a sandbox](https://astrada.co/sandbox) ·
[Getting Started](https://docs.astrada.co/docs/getting-started) ·
[Authentication](https://docs.astrada.co/docs/authentication) ·
[API reference](https://docs.astrada.co/reference)

Questions: developer@astrada.co. Bugs and feature requests: [GitHub issues](https://github.com/FidelLimited/astrada-java/issues).
Security problems: report them privately as [SECURITY.md](https://github.com/FidelLimited/astrada-java/blob/main/SECURITY.md) explains.

The reference for every class and method is on [javadoc.io](https://javadoc.io/doc/co.astrada/astrada-java),
with one page per API class in [docs/](docs/) (for example [SubaccountsApi](docs/SubaccountsApi.md)).
Every code sample in this README and in docs/ is compiled against the SDK on each change.

## Install

Maven (use the version on the badge above):

```xml
<dependency>
  <groupId>co.astrada</groupId>
  <artifactId>astrada-java</artifactId>
  <version>VERSION</version>
</dependency>
```

Gradle:

```groovy
implementation "co.astrada:astrada-java:VERSION"
```

Runtime dependencies: OkHttp (with Okio and the Kotlin standard library it needs), Gson, gson-fire
and the jsr305 annotations.

## Upgrading from 1.x

- The artifact moved: 1.x was `co.astrada:java-astrada-sdk` on GitHub Packages; 2.0.0 is `co.astrada:astrada-java`
  on Maven Central, published from [FidelLimited/astrada-java](https://github.com/FidelLimited/astrada-java).
  GitHub Packages gets no new versions, so change the dependency and remove that repository from your build. Java
  11 or later is required.
- Build the client with `Astrada.builder().environment(...).credentials(...)`; the `co.astrada.client.auth` OAuth
  classes are gone.
- Methods: `CardApi.getCard` takes an `expand` list as a second argument, and `listCards` has a new argument list
  (see `docs/CardApi.md`) and returns `CardList`. `cardVerificationList` is `listCardVerifications` and
  `cardVerificationAttemptLockoutUnlock` is `unlockCardVerificationAttempts`, whose result has `getCleared()` in
  place of `getVaultCardFingerprint()`.
- Models: `EnrollmentPolicy.getSpinwheelEnabled()` and `getProgramBins()` are gone, and models are named for what
  they hold, not for the status code: `GetSubaccounts200Response` is `SubaccountList`, `ListCards200Response` is
  `CardList`, `CreateSubaccountRequest` is `SubaccountCreateParams` and `CreateWebhookRequest` is
  `WebhookCreateParams`.
- `ApiException` is unchecked and reads the problem body, and `TransactionMessage.getDateTime()` returns a
  `TransactionDateTime`, not a `String` (see "Reading transaction times").

Every change is listed in the [releases](https://github.com/FidelLimited/astrada-java/releases) and
`CHANGELOG.md`.

## First call

With the sandbox credentials from your sign-up in the environment, list your subaccounts:

```java
import co.astrada.client.ApiClient;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;
import co.astrada.client.api.SubaccountsApi;

public class FirstCall {
    public static void main(String[] args) throws Exception {
        ApiClient client = Astrada.builder().environment(Environment.SANDBOX)
            .credentials(System.getenv("ASTRADA_ACCOUNT_ID"), System.getenv("ASTRADA_CLIENT_ID"),
                System.getenv("ASTRADA_CLIENT_SECRET"))
            .build();
        new SubaccountsApi(client).listSubaccounts(null, 5).getEmbedded().getSubaccounts()
            .forEach(subaccount -> System.out.println(subaccount.getName()));
    }
}
```

## Log in

Create API credentials in the sandbox first (see [Authentication](https://docs.astrada.co/docs/authentication)):
an account id, a client id and a client secret. Sandbox and production have separate credentials.

```java
import co.astrada.client.ApiClient;
import co.astrada.client.Astrada;
import co.astrada.client.Environment;

ApiClient client = Astrada.builder()
    .environment(Environment.SANDBOX) // Environment.PRODUCTION for live cards
    .credentials(System.getenv("ASTRADA_ACCOUNT_ID"), System.getenv("ASTRADA_CLIENT_ID"),
        System.getenv("ASTRADA_CLIENT_SECRET"))
    .build();
```

The client requests an OAuth2 token on the first call, reuses it until shortly before it expires,
and logs in again after a 401. Build one `ApiClient` and share it: it is safe to use from many
threads, and every API class, the Banking API ones included, takes the same client.

## Mint a token for the browser enrollment SDK

The Unified Enrollment SDK v2 in your page asks your backend for a token (its `getAccessToken` option). Mint it
with your **SDK client**: the separate client id and secret Astrada gives you for the browser. Never hand the
browser a token of your backend client: it would carry every scope the backend holds, writes and `banking:admin`
included, so `mintSdkAccessToken` refuses the backend client's own id unless that client calls the sandbox API.

The token carries every scope the SDK client holds; the login does not narrow a token on request. List in
`allowedScopes` only the scopes the browser calls need, from the [SDK credentials
table](https://docs.astrada.co/docs/authentication#sdk-credentials): the eight in the sample, plus `cards:read`
if you want the card lookup and `network-bulk-enrollments:write` or `network-bulk-feeds:write` for bulk
enrollment requests on admin pages. Never list `banking:admin` or `webhooks:write`, and if your SDK client holds
any scope the browser does not need, ask Astrada to remove it. If you do not offer bank sign-in, leave out the
two banking scopes and have them removed too. With `allowedScopes`, the call throws `UnexpectedScopesException`
instead of returning a token that carries any scope outside the list (the check covers the scopes the login
states in its answer); a token missing a listed scope is still returned.

The token is not tied to one subaccount: for its five minutes it works on every subaccount of your account, for
card enrollment and card lookups, with the banking scopes for bank links and bank data, and with the bulk
enrollment scopes for bulk enrollment requests. Your server decides who gets a token, not what the token reaches,
so check that the signed-in user may use the subaccount the page opens, and remember that a user who copies the
token can call the API with it until it expires.

Sandbox self sign-up only, never in production: that account has one client for both uses, so its token carries
every backend scope. There, leave `allowedScopes` out (pass null).

```java
String token = client.mintSdkAccessToken(
    System.getenv("ASTRADA_SDK_CLIENT_ID"),
    System.getenv("ASTRADA_SDK_CLIENT_SECRET"),
    java.util.Arrays.asList("subaccounts:read", "card-subscriptions:read", "card-subscriptions:write",
        "card-verifications:read", "card-verifications:write", "enrollment-methods:write",
        "banking:read", "banking:write")); // Example: replace with the scopes the browser calls need
// return token from your backend's token route
```

## List subaccounts

A subaccount groups cards (for example one per end customer). List calls return one page;
`Pages.iterate` walks all of them, requesting each page as the loop reaches it.

```java
import co.astrada.client.api.SubaccountsApi;
import co.astrada.client.model.Subaccount;
import co.astrada.client.model.SubaccountList;
import co.astrada.client.pagination.Pages;
import java.util.UUID;

SubaccountsApi subaccounts = new SubaccountsApi(client);

SubaccountList firstPage = subaccounts.listSubaccounts(null, 10); // cursor, limit
for (Subaccount s : firstPage.getEmbedded().getSubaccounts()) {
  System.out.println(s.getId() + " " + s.getName());
}

for (Subaccount s : Pages.iterate(cursor -> subaccounts.listSubaccounts(cursor, 100),
    page -> page.getEmbedded().getSubaccounts())) {
  System.out.println(s.getId());
}
UUID subaccountId = firstPage.getEmbedded().getSubaccounts().get(0).getId();
```

### Pagination

- Cursor lists (subaccounts, transactions, webhooks, enrollment links and every Banking API list):
  `Pages.iterate(cursor -> api.listX(..., cursor), page -> items)`. Without the helper, pass
  `page.nextCursor()` as the next call's `cursor`; it is null on the last page.
- Offset lists (cards, card subscriptions, network bulk feeds):
  `Pages.iterateByOffset(offset -> api.listX(..., offset, ...), page -> items)`, or
  `page.nextOffset()`.
- `Pages.pages(...)` and `Pages.pagesByOffset(...)` give the pages instead of the items.

## Enroll a card

Card numbers never pass through this SDK or your servers. Your backend creates an enrollment link
for a subaccount and sends the cardholder its `url`; the hosted page (or the browser
[Unified Enrollment SDK](https://docs.astrada.co/docs/unified-enrollment-sdk-v2)) collects the card
and runs the issuer's verification. See [Create enrollment link](https://docs.astrada.co/reference/createenrollmentlink).

```java
import co.astrada.client.api.EnrollmentLinksApi;
import co.astrada.client.model.EnrollmentLinkCreateParams;
import co.astrada.client.model.EnrollmentLinkCreateResult;

EnrollmentLinkCreateResult link = new EnrollmentLinksApi(client).createEnrollmentLink(
    new EnrollmentLinkCreateParams()
        .subaccountId(subaccountId)
        .kind(EnrollmentLinkCreateParams.KindEnum.CARDHOLDER));
System.out.println("Send the cardholder to " + link.getUrl());
```

When the card is enrolled you get a `cardsubscription.created` webhook (see below).

## Why a card failed

A card whose subscription stays in `reqSCA` has not passed the issuer's verification (3DS). The
card's verifications say why: `failureReason.category` is stable to branch on, `description` is
safe to show the cardholder, and `retryable` says whether trying again can work.

```java
import co.astrada.client.api.CardVerificationApi;
import co.astrada.client.model.CardVerification;
import co.astrada.client.model.FailureReason;

UUID cardId = UUID.fromString(System.getenv("ASTRADA_CARD_ID"));
for (CardVerification v : new CardVerificationApi(client).listCardVerifications(cardId, "failed")
    .getEmbedded().getCardVerifications()) {
  FailureReason reason = v.getFailureReason();
  System.out.println(reason.getCategory() + ": " + reason.getDescription()
      + (Boolean.TRUE.equals(reason.getRetryable()) ? " (can retry)" : ""));
}
```

The categories are listed in [Error states and remediation](https://docs.astrada.co/docs/error-states-and-remediation).

## Sandbox simulation

In the sandbox you enroll the published test cards (see [Sandbox testing](https://docs.astrada.co/docs/sandbox-environment))
and then make transactions happen with the simulation calls of the
[Sandbox API](https://docs.astrada.co/docs/sandbox-api), under `client.sandbox()`. The
credentials need the `simulation:write` scope.

```java
import co.astrada.client.model.SimulateTransactionScenarioRequest;

client.sandbox().simulateTransactionScenario(new SimulateTransactionScenarioRequest()
    .scenario(SimulateTransactionScenarioRequest.ScenarioEnum.SIMPLE_PURCHASE)
    .cardId(cardId) // an enrolled sandbox test card
    .subaccountId(subaccountId)
    .amount(1500) // minor units: 15.00
    .currency("USD")); // accepted; the transactions arrive as webhooks
```

A sandbox call never reaches production: each one throws `SandboxOnlyException` (an
`ApiException`) before sending anything unless the client was built with
`Environment.SANDBOX` and points at `https://api.sandbox.astrada.co`. A proxy or local mock in
front of the sandbox must be named as one with `Astrada.builder().sandboxBaseUrl(url)`.

### Bulk enrollment in the sandbox

A network bulk feed enrolls a company's cards from a file the card network delivers. In the
sandbox, `simulateBulkFeedFile` delivers such a file for one of your feeds, made from the sandbox
test cards; its cards are then enrolled as for a real file, and `cardsubscription.created`
webhooks follow.

```java
import co.astrada.client.api.CardApi;
import co.astrada.client.api.NetworkBulkFeedsApi;
import co.astrada.client.model.Card;
import co.astrada.client.model.NetworkBulkFeeds;
import co.astrada.client.model.SimulateBulkFeedFileParams;
import co.astrada.client.sandbox.BulkFeedType;

UUID feedId = UUID.fromString(System.getenv("ASTRADA_BULK_FEED_ID")); // a feed you created
NetworkBulkFeeds feed = new NetworkBulkFeedsApi(client).getNetworkBulkFeed(feedId);
client.sandbox().simulateBulkFeedFile(BulkFeedType.valueOf(feed.getType().getValue()),
    new SimulateBulkFeedFileParams()
        .networkReference(feed.getNetworkReference())
        .cardCount(5)              // cards from the start of the sandbox test card pool
        .transactionsPerCard(2));  // simulated transactions for each card

// Later (the file is processed asynchronously): the feed's cards.
CardApi cards = new CardApi(client);
for (Card card : Pages.iterateByOffset(
    offset -> cards.listCards(null, null, null, null, feedId, null, 100, offset),
    page -> page.getEmbedded().getCards())) {
  System.out.println(card.getId() + " ends in " + card.getLast4digits());
}
```

`BulkFeedType` is the feed's `type`: `GL` (American Express), `SMARTDATA` (Mastercard), `VBDS` or
`VCF` (Visa). See [Sandbox bulk enrollment](https://docs.astrada.co/docs/sandbox-bulk-enrollment).

## Clear test data in the sandbox

`client.sandbox()` also has the sandbox-only deletes, for clearing test data between runs:
`deleteSubaccount` (the subaccount and everything under it: bank links, card subscriptions, cards,
transactions, bulk feeds and webhooks), `deleteCardSubscription` and `deleteNetworkBulkFeed`. They
cannot be undone. Each returns when the delete has finished, or throws an `ApiException` with code
502 when a step failed; calling it again after a failure is safe, because finished steps are
skipped. The SDK never retries them itself, since a second call while the first is still running is
not safe. The exceptions are answers that show the delete never ran: after a 401 it is sent once more
with a new token, and after an HTTP/2 refused stream or a connection shut down before the stream was
accepted it is sent again, three sends at most. The sandbox API's gateway answers 504 after 29 seconds
while a longer delete keeps running: after a 504, read the resource (`getSubaccount`, `getCardSubscription`,
`getNetworkBulkFeed`) every few seconds; an `ApiException` with code 404 means the delete finished. A delete runs
for at most about 5 minutes, so if the resource still answers after 6 minutes, the first delete stopped part-way
and calling it again is safe (finished steps are skipped). It waits up to 5 minutes for the answer (`SandboxGuard.DELETE_TIMEOUT`, or your read
timeout when it is longer or off). Production has no delete calls, and these throw
`SandboxOnlyException` outside the sandbox.

```java
client.sandbox().deleteSubaccount(subaccountId);
```

## Reading transaction times

A transaction message's `getDateTime()` is a `TransactionDateTime`, because what it holds depends on the card
network:

- Visa and Amex send UTC (`...Z`): an exact moment. `getOffset()` is `UTC`, and `toInstant()` and
  `toOffsetDateTime()` return it.
- Mastercard sends the merchant's local date and time with no offset: the wall-clock time where the card was
  used. Astrada does not receive that merchant's time zone, so it is not an exact moment: `getOffset()`,
  `toInstant()` and `toOffsetDateTime()` are empty. Show `getLocalDateTime()` as the merchant's local time, or
  use `atOffset(offset)` if you know the merchant's offset.
- Some messages carry an end-of-day value rather than the moment they happened: Visa clearings (`FINL_ADVC`,
  `FINL_REQU`) the settlement date at `23:59:59Z`, Visa reversals (`AUTH_ADVC` with a negative amount)
  `23:59:59Z` on the day Astrada received them, Amex clearings the business date at `23:59:59Z`. Use the
  transaction's `getTransactionOccurrenceDate()` for the purchase date.
- For other networks (such as `OPEN_BANKING`) the definition does not say which zone applies: a value with an
  offset is exact, one without is a wall time of unknown zone.

To order messages across networks, use `getCreatedDateTime()` (when Astrada recorded the message, UTC). A
`transaction.created` or `transaction.updated` webhook carries a short form of each message in
`_embedded.messages`; the webhook event's `getTransactionMessages()` gives them, with `getDateTime()` read the
same way.

`getRaw()` and the JSON give back exactly what the API sent.

```java
import co.astrada.client.model.TransactionDateTime;
import co.astrada.client.model.TransactionMessage;
import co.astrada.client.api.TransactionMessagesApi;

TransactionMessage message = new TransactionMessagesApi(client)
    .getTransactionMessage(java.util.UUID.fromString("3f2b8c1e-6a4d-4e2f-9b7a-1c5d8e9f0a2b"));
TransactionDateTime when = message.getDateTime();
System.out.println(when.toInstant()
    .map(instant -> "UTC " + instant)
    .orElse(when.getLocalDateTime() + " (merchant local time)"));
System.out.println("recorded " + message.getCreatedDateTime());
```

## Verify a webhook

Pass the raw request body (exactly as received, before any JSON parsing), the request headers and
the webhook's `secret` (returned once when you create the webhook). `verifyAndParse` throws
`WebhookVerificationException` when the signature does not match or the delivery is more than
five minutes old; answer 400 then. Answer 2xx within 5 seconds, and use `getId()` to skip a
delivery you have already handled (a failed delivery is sent again).

```java
import co.astrada.client.webhooks.WebhookEvent;
import co.astrada.client.webhooks.WebhookVerifier;
import co.astrada.client.model.CardSubscription;
import co.astrada.client.model.Transaction;
import java.util.Map;

public class AstradaWebhooks {
  private final WebhookVerifier verifier = new WebhookVerifier(System.getenv("ASTRADA_WEBHOOK_SECRET"));
  // After this many unreadable deliveries of one event, answer 2xx and rely on the stored copy.
  static final int MAX_UNREADABLE_DELIVERIES = 3;

  /**
   * Replace with your own store (a table keyed by the delivery id). Save the id, type, raw body and
   * the time of the check, and return how many times this id has been saved, counting this one.
   * Until you replace it, it throws, so the handler answers 500 and Astrada delivers the event again.
   */
  int storeUnreadable(WebhookEvent event) {
    throw new IllegalStateException("storeUnreadable is not set up (delivery " + event.getId() + ")");
  }

  /** Returns the HTTP status to answer with. */
  public int handle(String rawBody, Map<String, String> headers) {
    WebhookEvent event;
    try {
      event = verifier.verifyAndParse(rawBody, headers);
    } catch (WebhookVerifier.WebhookVerificationException e) {
      return 400; // not from Astrada, or too old: refuse it
    }
    try {
      return handle(event);
    } catch (WebhookEvent.WebhookPayloadException e) {
      // Signed by Astrada (the check passed), but not the shape this SDK knows for its type, often
      // because the SDK is older than the event. Store it first; answer 2xx only once it is stored.
      int failures;
      try {
        failures = storeUnreadable(event);
      } catch (RuntimeException storeError) {
        System.err.println("unreadable webhook not stored " + event.getId() + ": " + storeError.getMessage());
        return 500; // not stored: Astrada must deliver it again
      }
      System.err.println("unreadable webhook stored " + event.getId() + " " + failures);
      return failures >= MAX_UNREADABLE_DELIVERIES ? 204 : 500;
    }
  }

  private int handle(WebhookEvent event) {
    switch (String.valueOf(event.getType())) {
      case WebhookEvent.TRANSACTION_CREATED:
      case WebhookEvent.TRANSACTION_UPDATED:
        Transaction transaction = event.getTransaction();
        System.out.println(transaction.getId() + " " + transaction.getStatus());
        break;
      case WebhookEvent.CARD_SUBSCRIPTION_CREATED:
      case WebhookEvent.CARD_SUBSCRIPTION_UPDATED:
        CardSubscription subscription = event.getCardSubscription();
        System.out.println(subscription.getCardId() + " " + subscription.getState());
        break;
      default:
        System.out.println(event.getType() + " " + event.getData()); // a JsonObject
    }
    // Record event.getId() as handled in your store here, so a replay or a repeated delivery can skip it.
    return 200;
  }
}
```

Answer 400 only when the check fails (`WebhookVerificationException`). A `WebhookPayloadException` means the
delivery is signed but this SDK cannot read it: store it, then answer 500 so Astrada delivers it again, and answer
2xx once it is stored and has failed `MAX_UNREADABLE_DELIVERIES` times (3 here; choose your own). The stored copy
is how you recover the event: after updating the SDK, read each stored delivery with
`WebhookEvent.parseStored(id, type, body)`. It checks no signature (the five-minute limit would refuse a stored
delivery anyway), so give it only deliveries you stored after the check passed, and keep the time of that check
with them.
Replay with care: skip a stored delivery whose id you already handled, because a retry can succeed once the SDK
is updated. The stored body is a snapshot: for a transaction, apply it only if its
`getTransaction().getUpdatedAt()` is newer than your copy's; for other resources, fetch the current state first.
(`event.timestamp` is when the delivery was sent, so a late retry carries a newer one; do not compare it.) Mark
the stored row as handled, or delete it, once it is done.

`getTransaction()` (with `getTransactionMessages()` for its `_embedded.messages`), `getTransactionMessage()`
and `getCardSubscription()` read the events whose body the API definition describes; for the others, `getData()` gives the JSON and
`getData(SomeModel.class)` reads it as a model. The signature covers the delivery id, timestamp
and body; the event type comes from the `webhook-event-type` header, which it does not cover. The events and their bodies are listed in
[Event types](https://docs.astrada.co/reference/event-types).

## Calling an endpoint the SDK doesn't wrap

If the API has an endpoint this version of the SDK has no method for, call it with `client.request(method,
path, query, body)`. It uses the client's login, environment and timeouts, and throws the same `ApiException`
(with `getCode()` and the problem body) for any status outside 200 to 299. It retries a GET, PUT or DELETE on
the same rules as the rest of the SDK and never sends a POST or PATCH twice (except once more after a 401, with
a new token, as every call does). Every sandbox-only operation (listed from the sandbox API definition when the SDK
is built, such as `DELETE /subaccounts/{id}`) and any `/simulate` path get the same sandbox check as
`client.sandbox()`, and the sandbox-only deletes are never retried. The path is normalised once (`.` and `..`
resolved, repeated slashes dropped; a raw `?`, `#`, `\`, space or control character, or an encoded `/`, `\`, `.`, `?`, `#`
or control character, refused, see
`ApiClient.normaliseRequestPath`), and the checks run on the path that is sent. It returns the parsed JSON body as
a Gson `JsonElement`, or null for an empty one.

```java
com.google.gson.JsonElement page = client.request("GET", "/cards", java.util.Map.of("limit", 5), null);
System.out.println(page.getAsJsonObject().getAsJsonObject("_embedded").getAsJsonArray("cards").size());
```

## Testing your code

Two ways to run your tests without the network.

**WireMock**, with the `astrada-java-testing` artifact (test scope; it brings WireMock):

```xml
<dependency>
  <groupId>co.astrada</groupId>
  <artifactId>astrada-java-testing</artifactId>
  <version>VERSION</version>
  <scope>test</scope>
</dependency>
```

It holds one WireMock stub per operation, answering the example from the API reference, and a
login that accepts any credentials. Override an operation by its name (the method name with a
capital first letter):

```java
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import co.astrada.client.testing.AstradaMock;
import com.github.tomakehurst.wiremock.WireMockServer;

WireMockServer mock = new WireMockServer(options().dynamicPort());
mock.start();
AstradaMock.register(mock);
ApiClient mockClient = AstradaMock.client(mock.baseUrl()); // marked as a sandbox; no retries

new SubaccountsApi(mockClient).listSubaccounts(null, 10); // the reference example

AstradaMock.override(mock, "GetSubaccount", aResponse().withStatus(404)
    .withHeader("Content-Type", "application/problem+json")
    .withBody("{\"title\":\"Not Found\",\"detail\":\"Subaccount not found\"}"));
mock.stop();
```

A stub you add yourself with WireMock's default priority also wins over the shipped ones.

**Prism**, a mock server made from the API definition (Node.js needed), which also checks your
requests against it:

```shell
npx @stoplight/prism-cli@5.16.0 mock -p 4010 \
  https://raw.githubusercontent.com/FidelLimited/astrada-java/main/mock/astrada-api.json
```

Then build the client with `Astrada.builder().sandboxBaseUrl("http://127.0.0.1:4010")` and any
credentials. The same definition is in the testing jar as `co/astrada/client/testing/astrada-api.json`.

## Errors

Every failure is an `ApiException` (unchecked): an error status from the API, a refused login, a
network failure, or a response that cannot be read. `getCode()` is the HTTP status (0 when no
response arrived), and the error body is already parsed:

```java
import co.astrada.client.ApiException;
import co.astrada.client.model.ValidationProblem;
import co.astrada.client.model.ValidationProblemItem;

try {
  subaccounts.getSubaccount(UUID.randomUUID());
} catch (ApiException e) {
  System.out.println(e.getCode());       // 404
  System.out.println(e.getTitle());      // Not Found
  System.out.println(e.getDetail());     // the text for people
  System.out.println(e.getErrorCode());  // a code to branch on, where the error has one
  System.out.println(e.getMessage());    // all of the above on one line
  ValidationProblem problem = e.getCode() == 400 ? e.getErrorBody(ValidationProblem.class) : null;
  if (problem != null && problem.getErrors() != null) {
    for (ValidationProblemItem item : problem.getErrors()) {
      System.out.println(item.getTitle() + ": " + item.getDetail());
    }
  }
}
```

## Operations that need banking:admin

Some Banking API methods need the `banking:admin` scope. Their entries in [docs/](docs/) and their
Javadoc start with "Needs banking:admin". If your API client does not have the scope, the API
answers 403 and the SDK throws a `ScopeRequiredException`, an `ApiException` with code 403 that
names the scope. Ask Astrada to enable it if you need one of these operations. As with any 403, the
resource may also not be your account's, so the message names the scope as the likely cause.

```java
import co.astrada.client.ScopeRequiredException;
import co.astrada.client.api.BankLinksApi;

try {
  new BankLinksApi(client).syncBankLink("bank-link-id", null);
} catch (ScopeRequiredException e) {
  System.out.println(e.getOperationId() + " needs " + e.getRequiredScope()); // banking:admin
}
```

Catch `ApiException` to handle it with every other error.

## Retries and timeouts

GET, HEAD, PUT, DELETE and OPTIONS requests that get a 429 or a 5xx response, or cannot connect,
are retried twice with exponential backoff and jitter. A refused login (wrong secret or account)
and a read timeout are not retried. A `Retry-After` header sets the wait instead
(up to 60 seconds; a longer one is returned to you as the error). POST and PATCH are never retried,
so a create is never sent twice (with a client from `Astrada.builder()`: OkHttp itself does not resend a POST or
PATCH either, and does not follow a 307 or 308 for one), and neither are the sandbox deletes (above). Timeouts default to
10 seconds to connect and 30 seconds to read or write.

```java
import java.time.Duration;

ApiClient patientClient = Astrada.builder()
    .environment(Environment.SANDBOX)
    .credentials(System.getenv("ASTRADA_ACCOUNT_ID"), System.getenv("ASTRADA_CLIENT_ID"),
        System.getenv("ASTRADA_CLIENT_SECRET"))
    .maxRetries(4)                         // 0 turns retries off
    .connectTimeout(Duration.ofSeconds(5))
    .readTimeout(Duration.ofSeconds(60))
    .build();
```

`httpClient(okHttpClient)` starts from your own OkHttp client (proxy, TLS, interceptors). The login
uses the same client and timeouts, without OkHttp logging interceptors.

## Logging

`client.setDebugging(true)` logs each request and response line with its headers through OkHttp's
logging interceptor. The `Authorization` header (the bearer token) is always replaced by `██`, and
the login request is never logged. `client.setDebugging(HttpLoggingInterceptor.Level.BODY)` adds
bodies, which can hold cardholder data: use it in the sandbox only.

## New fields and values

The SDK reads responses leniently, so an API addition does not break a deployed client. A field the
SDK does not know yet is kept in `getAdditionalProperties()`. An enum value it does not know yet
reads as `UNKNOWN_DEFAULT_OPEN_API` (never null, never an exception); for a single-value field,
`getAdditionalProperty("<field>")` returns the value as sent, and `toJson()` writes it back until
you set a known value. In a list of enum values an unknown one reads as `UNKNOWN_DEFAULT_OPEN_API`
and its text is not kept.

## API endpoints

All paths are relative to `https://api.astrada.co` (production) or `https://api.sandbox.astrada.co`
(sandbox).

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*BankAccountsApi* | [**backfillBankAccount**](docs/BankAccountsApi.md#backfillBankAccount) | **POST** /bank-accounts/{bankAccountId}/backfill | **Needs banking:admin.** Backfill bank account history
*BankAccountsApi* | [**deleteBankAccount**](docs/BankAccountsApi.md#deleteBankAccount) | **DELETE** /bank-accounts/{bankAccountId} | **Needs banking:admin.** Delete bank account
*BankAccountsApi* | [**getBankAccount**](docs/BankAccountsApi.md#getBankAccount) | **GET** /bank-accounts/{bankAccountId} | Retrieve bank account
*BankAccountsApi* | [**linkEnrolledCards**](docs/BankAccountsApi.md#linkEnrolledCards) | **POST** /bank-accounts/link-enrolled-cards | Link enrolled cards to bank accounts (admin backfill)
*BankAccountsApi* | [**listBankAccounts**](docs/BankAccountsApi.md#listBankAccounts) | **GET** /bank-accounts | List bank accounts
*BankAccountsApi* | [**refreshBankAccount**](docs/BankAccountsApi.md#refreshBankAccount) | **POST** /bank-accounts/{bankAccountId}/refresh | Refresh bank account
*BankAccountsApi* | [**refreshBankAccountTransactions**](docs/BankAccountsApi.md#refreshBankAccountTransactions) | **POST** /bank-accounts/{bankAccountId}/transactions/refresh | **Needs banking:admin.** Refresh transactions
*BankAccountsApi* | [**runBankAccountMaintenance**](docs/BankAccountsApi.md#runBankAccountMaintenance) | **POST** /bank-accounts/maintenance | **Needs banking:admin.** Bank account maintenance for a subaccount
*BankAccountsApi* | [**updateBankAccount**](docs/BankAccountsApi.md#updateBankAccount) | **PATCH** /bank-accounts/{bankAccountId} | Update bank account
*BankLinksApi* | [**createBankLink**](docs/BankLinksApi.md#createBankLink) | **POST** /bank-links | Create bank link
*BankLinksApi* | [**deleteBankLink**](docs/BankLinksApi.md#deleteBankLink) | **DELETE** /bank-links/{bankLinkId} | **Needs banking:admin.** Delete bank link
*BankLinksApi* | [**getBankLink**](docs/BankLinksApi.md#getBankLink) | **GET** /bank-links/{bankLinkId} | Retrieve bank link
*BankLinksApi* | [**listBankLinks**](docs/BankLinksApi.md#listBankLinks) | **GET** /bank-links | List bank links
*BankLinksApi* | [**syncBankLink**](docs/BankLinksApi.md#syncBankLink) | **POST** /bank-links/{bankLinkId}/sync | **Needs banking:admin.** Sync bank link
*BankLinksApi* | [**updateBankLink**](docs/BankLinksApi.md#updateBankLink) | **PATCH** /bank-links/{bankLinkId} | Complete bank link
*BankSubscriptionsApi* | [**getBankSubscription**](docs/BankSubscriptionsApi.md#getBankSubscription) | **GET** /bank-subscriptions/{bankSubscriptionId} | **Needs banking:admin.** Retrieve bank subscription
*BankSubscriptionsApi* | [**getBankSubscriptionByConnection**](docs/BankSubscriptionsApi.md#getBankSubscriptionByConnection) | **GET** /bank-subscriptions/by-connection/{connectionId} | **Needs banking:admin.** Retrieve bank subscription by connection
*BankSubscriptionsApi* | [**listBankSubscriptions**](docs/BankSubscriptionsApi.md#listBankSubscriptions) | **GET** /bank-subscriptions | **Needs banking:admin.** List bank subscriptions
*BankTransactionsApi* | [**getBankTransaction**](docs/BankTransactionsApi.md#getBankTransaction) | **GET** /bank-transactions/{subaccountId}/{transactionId} | Retrieve bank transaction
*BankTransactionsApi* | [**listBankTransactions**](docs/BankTransactionsApi.md#listBankTransactions) | **GET** /bank-transactions/{subaccountId} | List bank transactions
*BankTransactionsApi* | [**triggerBankTransactionWebhooks**](docs/BankTransactionsApi.md#triggerBankTransactionWebhooks) | **POST** /bank-transactions/{subaccountId}/webhooks | **Needs banking:admin.** Trigger bank transaction webhooks
*BinLookupApi* | [**lookupBin**](docs/BinLookupApi.md#lookupBin) | **GET** /bin-lookup | Lookup card information by BIN
*CardApi* | [**getCard**](docs/CardApi.md#getCard) | **GET** /cards/{cardId} | Retrieve card
*CardApi* | [**getCardholder**](docs/CardApi.md#getCardholder) | **GET** /cards/{cardId}/cardholder | Retrieve cardholder information
*CardApi* | [**listCards**](docs/CardApi.md#listCards) | **GET** /cards | List cards
*CardApi* | [**updateCardholder**](docs/CardApi.md#updateCardholder) | **PATCH** /cards/{cardId}/cardholder | Update cardholder information
*CardSubscriptionApi* | [**getCardSubscription**](docs/CardSubscriptionApi.md#getCardSubscription) | **GET** /card-subscriptions/{subscriptionId} | Retrieve card subscription
*CardSubscriptionApi* | [**listCardSubscriptions**](docs/CardSubscriptionApi.md#listCardSubscriptions) | **GET** /card-subscriptions | List card subscriptions
*CardSubscriptionApi* | [**updateCardSubscription**](docs/CardSubscriptionApi.md#updateCardSubscription) | **PATCH** /card-subscriptions/{subscriptionId} | Update card subscription
*CardVerificationApi* | [**getCardVerification**](docs/CardVerificationApi.md#getCardVerification) | **GET** /card-verifications/3ds/{verificationId} | Retrieve 3DS verification
*CardVerificationApi* | [**listCardVerifications**](docs/CardVerificationApi.md#listCardVerifications) | **GET** /card-verifications/3ds | List a card&#39;s 3DS verifications
*CardVerificationApi* | [**unlockCardVerificationAttempts**](docs/CardVerificationApi.md#unlockCardVerificationAttempts) | **POST** /card-verifications/unlock | Unlock a card&#39;s verification attempt lockout
*EnrollmentLinksApi* | [**createEnrollmentLink**](docs/EnrollmentLinksApi.md#createEnrollmentLink) | **POST** /enrollment-links | Create enrollment link
*EnrollmentLinksApi* | [**createTeamLink**](docs/EnrollmentLinksApi.md#createTeamLink) | **POST** /subaccounts/{subaccountId}/team-link | Create team link
*EnrollmentLinksApi* | [**getEnrollmentLink**](docs/EnrollmentLinksApi.md#getEnrollmentLink) | **GET** /enrollment-links/{linkId} | Retrieve enrollment link
*EnrollmentLinksApi* | [**listEnrollmentLinks**](docs/EnrollmentLinksApi.md#listEnrollmentLinks) | **GET** /enrollment-links | List enrollment links
*EnrollmentLinksApi* | [**revokeEnrollmentLink**](docs/EnrollmentLinksApi.md#revokeEnrollmentLink) | **POST** /enrollment-links/{linkId}/revoke | Revoke enrollment link
*EnrollmentMethodsApi* | [**getEnrollmentMethods**](docs/EnrollmentMethodsApi.md#getEnrollmentMethods) | **POST** /enrollment-methods | List the available enrollment methods given a card BIN
*NetworkBulkEnrollmentsApi* | [**createNetworkBulkConfirmation**](docs/NetworkBulkEnrollmentsApi.md#createNetworkBulkConfirmation) | **POST** /network-bulk-confirmations | Create network bulk confirmation
*NetworkBulkEnrollmentsApi* | [**createNetworkBulkEnrollment**](docs/NetworkBulkEnrollmentsApi.md#createNetworkBulkEnrollment) | **POST** /network-bulk-enrollments | Create network bulk enrollment
*NetworkBulkEnrollmentsApi* | [**getNetworkBulkEnrollment**](docs/NetworkBulkEnrollmentsApi.md#getNetworkBulkEnrollment) | **GET** /network-bulk-enrollments/{networkBulkEnrollmentId} | Retrieve a network bulk enrollment
*NetworkBulkEnrollmentsApi* | [**getOpenNetworkBulkEnrollment**](docs/NetworkBulkEnrollmentsApi.md#getOpenNetworkBulkEnrollment) | **GET** /network-bulk-enrollments/open | Check for an open network bulk enrollment
*NetworkBulkFeedsApi* | [**createNetworkBulkFeed**](docs/NetworkBulkFeedsApi.md#createNetworkBulkFeed) | **POST** /network-bulk-feeds | Create network bulk feed
*NetworkBulkFeedsApi* | [**getNetworkBulkFeed**](docs/NetworkBulkFeedsApi.md#getNetworkBulkFeed) | **GET** /network-bulk-feeds/{networkBulkFeedId} | Retrieve a network bulk feed
*NetworkBulkFeedsApi* | [**listNetworkBulkFeeds**](docs/NetworkBulkFeedsApi.md#listNetworkBulkFeeds) | **GET** /network-bulk-feeds | List network bulk feed
*NetworkBulkFeedsApi* | [**updateNetworkBulkFeed**](docs/NetworkBulkFeedsApi.md#updateNetworkBulkFeed) | **PATCH** /network-bulk-feeds/{networkBulkFeedId} | Update a network bulk feed
*SandboxApi* | [**deleteCardSubscription**](docs/SandboxApi.md#deleteCardSubscription) | **DELETE** /card-subscriptions/{subscriptionId} | Delete card subscription (sandbox only)
*SandboxApi* | [**deleteNetworkBulkFeed**](docs/SandboxApi.md#deleteNetworkBulkFeed) | **DELETE** /network-bulk-feeds/{networkBulkFeedId} | Delete network bulk feed (sandbox only)
*SandboxApi* | [**deleteSubaccount**](docs/SandboxApi.md#deleteSubaccount) | **DELETE** /subaccounts/{subaccountId} | Delete subaccount (sandbox only)
*SandboxApi* | [**simulateBulkFeedFileGl**](docs/SandboxApi.md#simulateBulkFeedFileGl) | **POST** /simulate/ingestion/amex-gl | Simulate an American Express bulk file (sandbox only)
*SandboxApi* | [**simulateBulkFeedFileSmartdata**](docs/SandboxApi.md#simulateBulkFeedFileSmartdata) | **POST** /simulate/ingestion/smartdata | Simulate a Mastercard bulk file (sandbox only)
*SandboxApi* | [**simulateBulkFeedFileVbds**](docs/SandboxApi.md#simulateBulkFeedFileVbds) | **POST** /simulate/ingestion/vbds | Simulate a pulled Visa bulk feed delivery (sandbox only)
*SandboxApi* | [**simulateBulkFeedFileVcf**](docs/SandboxApi.md#simulateBulkFeedFileVcf) | **POST** /simulate/ingestion/vcf | Simulate a Visa bulk file (sandbox only)
*SandboxApi* | [**simulateTransaction**](docs/SandboxApi.md#simulateTransaction) | **POST** /simulate/transactions | Simulate a transaction (sandbox only)
*SandboxApi* | [**simulateTransactionMessage**](docs/SandboxApi.md#simulateTransactionMessage) | **POST** /simulate/transactions/{transactionId} | Simulate a transaction message (sandbox only)
*SandboxApi* | [**simulateTransactionScenario**](docs/SandboxApi.md#simulateTransactionScenario) | **POST** /simulate/transaction/scenario | Simulate a transaction scenario (sandbox only)
*SubaccountsApi* | [**createSubaccount**](docs/SubaccountsApi.md#createSubaccount) | **POST** /subaccounts | Create subaccount
*SubaccountsApi* | [**getSubaccount**](docs/SubaccountsApi.md#getSubaccount) | **GET** /subaccounts/{subaccountId} | Retrieve subaccount
*SubaccountsApi* | [**listSubaccounts**](docs/SubaccountsApi.md#listSubaccounts) | **GET** /subaccounts | List subaccounts
*SubaccountsApi* | [**updateSubaccount**](docs/SubaccountsApi.md#updateSubaccount) | **PATCH** /subaccounts/{subaccountId} | Update subaccount configurations
*TransactionMatchesApi* | [**getTransactionMatch**](docs/TransactionMatchesApi.md#getTransactionMatch) | **GET** /transaction-matches/{subaccountId}/{transactionMatchId} | Retrieve transaction match
*TransactionMatchesApi* | [**listTransactionMatches**](docs/TransactionMatchesApi.md#listTransactionMatches) | **GET** /transaction-matches/{subaccountId} | List transaction matches
*TransactionMessagesApi* | [**getRawTransactionMessage**](docs/TransactionMessagesApi.md#getRawTransactionMessage) | **GET** /transaction-messages/{transactionMessageId}/raw | Retrieve the raw transaction message
*TransactionMessagesApi* | [**getTransactionMessage**](docs/TransactionMessagesApi.md#getTransactionMessage) | **GET** /transaction-messages/{transactionMessageId} | Retrieve transaction message
*TransactionsApi* | [**getTransaction**](docs/TransactionsApi.md#getTransaction) | **GET** /transactions/{transactionId} | Retrieve transaction
*TransactionsApi* | [**listTransactions**](docs/TransactionsApi.md#listTransactions) | **GET** /transactions | List transactions
*WebhooksApi* | [**createWebhook**](docs/WebhooksApi.md#createWebhook) | **POST** /webhooks | Create webhook
*WebhooksApi* | [**deleteWebhook**](docs/WebhooksApi.md#deleteWebhook) | **DELETE** /webhooks/{webhookId} | Delete webhook
*WebhooksApi* | [**getWebhook**](docs/WebhooksApi.md#getWebhook) | **GET** /webhooks/{webhookId} | Retrieve webhook
*WebhooksApi* | [**listWebhooks**](docs/WebhooksApi.md#listWebhooks) | **GET** /webhooks | List webhooks
*WebhooksApi* | [**updateWebhook**](docs/WebhooksApi.md#updateWebhook) | **PATCH** /webhooks/{webhookId} | Update webhook


## Models

 - [BankAccount](docs/BankAccount.md)
 - [BankAccountBackfillParams](docs/BankAccountBackfillParams.md)
 - [BankAccountBackfillResult](docs/BankAccountBackfillResult.md)
 - [BankAccountCardsUpdate](docs/BankAccountCardsUpdate.md)
 - [BankAccountCardsUpdateBackfill](docs/BankAccountCardsUpdateBackfill.md)
 - [BankAccountDisconnect](docs/BankAccountDisconnect.md)
 - [BankAccountLinkCardsParams](docs/BankAccountLinkCardsParams.md)
 - [BankAccountLinkCardsResult](docs/BankAccountLinkCardsResult.md)
 - [BankAccountLinks](docs/BankAccountLinks.md)
 - [BankAccountList](docs/BankAccountList.md)
 - [BankAccountMaintenanceParams](docs/BankAccountMaintenanceParams.md)
 - [BankAccountMaintenanceResult](docs/BankAccountMaintenanceResult.md)
 - [BankAccountMaintenanceResultCardholderCards](docs/BankAccountMaintenanceResultCardholderCards.md)
 - [BankAccountMaintenanceResultJunctionLast4](docs/BankAccountMaintenanceResultJunctionLast4.md)
 - [BankAccountMaintenanceResultMintedValue](docs/BankAccountMaintenanceResultMintedValue.md)
 - [BankAccountRefreshResult](docs/BankAccountRefreshResult.md)
 - [BankAccountTransactionsRefreshResult](docs/BankAccountTransactionsRefreshResult.md)
 - [BankAccountUpdateParams](docs/BankAccountUpdateParams.md)
 - [BankAccountUpdateResult](docs/BankAccountUpdateResult.md)
 - [BankAccountUpdateResultBackfill](docs/BankAccountUpdateResultBackfill.md)
 - [BankFeedCard](docs/BankFeedCard.md)
 - [BankInstitutionIncident](docs/BankInstitutionIncident.md)
 - [BankLink](docs/BankLink.md)
 - [BankLinkBankAccount](docs/BankLinkBankAccount.md)
 - [BankLinkConsent](docs/BankLinkConsent.md)
 - [BankLinkCreateParams](docs/BankLinkCreateParams.md)
 - [BankLinkCreateParamsFilters](docs/BankLinkCreateParamsFilters.md)
 - [BankLinkFilters](docs/BankLinkFilters.md)
 - [BankLinkLastMintFailure](docs/BankLinkLastMintFailure.md)
 - [BankLinkLastRejection](docs/BankLinkLastRejection.md)
 - [BankLinkLinks](docs/BankLinkLinks.md)
 - [BankLinkList](docs/BankLinkList.md)
 - [BankLinkSyncParams](docs/BankLinkSyncParams.md)
 - [BankLinkSyncResult](docs/BankLinkSyncResult.md)
 - [BankLinkUpdateParams](docs/BankLinkUpdateParams.md)
 - [BankLinkingPolicy](docs/BankLinkingPolicy.md)
 - [BankSubscription](docs/BankSubscription.md)
 - [BankSubscriptionInstitutionHealth](docs/BankSubscriptionInstitutionHealth.md)
 - [BankSubscriptionList](docs/BankSubscriptionList.md)
 - [BankSubscriptionWebhook](docs/BankSubscriptionWebhook.md)
 - [BankTransaction](docs/BankTransaction.md)
 - [BankTransactionLinks](docs/BankTransactionLinks.md)
 - [BankTransactionList](docs/BankTransactionList.md)
 - [BankTransactionSettlement](docs/BankTransactionSettlement.md)
 - [BankTransactionWebhookFailure](docs/BankTransactionWebhookFailure.md)
 - [BankTransactionWebhooksParams](docs/BankTransactionWebhooksParams.md)
 - [BankTransactionWebhooksResult](docs/BankTransactionWebhooksResult.md)
 - [BankingError](docs/BankingError.md)
 - [BinLookup](docs/BinLookup.md)
 - [BinLookupValidationProblem](docs/BinLookupValidationProblem.md)
 - [BinLookupValidationProblemItem](docs/BinLookupValidationProblemItem.md)
 - [BinLookupWarning](docs/BinLookupWarning.md)
 - [Card](docs/Card.md)
 - [CardEmbedded](docs/CardEmbedded.md)
 - [CardLinks](docs/CardLinks.md)
 - [CardList](docs/CardList.md)
 - [CardListEmbedded](docs/CardListEmbedded.md)
 - [CardSubscription](docs/CardSubscription.md)
 - [CardSubscriptionDeactivationReason](docs/CardSubscriptionDeactivationReason.md)
 - [CardSubscriptionEmbedded](docs/CardSubscriptionEmbedded.md)
 - [CardSubscriptionLinks](docs/CardSubscriptionLinks.md)
 - [CardSubscriptionList](docs/CardSubscriptionList.md)
 - [CardSubscriptionListEmbedded](docs/CardSubscriptionListEmbedded.md)
 - [CardSubscriptionUpdateParams](docs/CardSubscriptionUpdateParams.md)
 - [CardVerification](docs/CardVerification.md)
 - [CardVerificationLinks](docs/CardVerificationLinks.md)
 - [CardVerificationList](docs/CardVerificationList.md)
 - [CardVerificationListEmbedded](docs/CardVerificationListEmbedded.md)
 - [CardVerificationUnlockParams](docs/CardVerificationUnlockParams.md)
 - [CardVerificationUnlockResult](docs/CardVerificationUnlockResult.md)
 - [Cardholder](docs/Cardholder.md)
 - [CardholderCorporate](docs/CardholderCorporate.md)
 - [CardholderCorporateLegalEntity](docs/CardholderCorporateLegalEntity.md)
 - [CardholderUpdateParams](docs/CardholderUpdateParams.md)
 - [CardholderUpdateParamsCorporate](docs/CardholderUpdateParamsCorporate.md)
 - [Conflict](docs/Conflict.md)
 - [ConsentPolicy](docs/ConsentPolicy.md)
 - [CreateSubaccountConfigurations](docs/CreateSubaccountConfigurations.md)
 - [CreateSubaccountConfigurationsAMEX](docs/CreateSubaccountConfigurationsAMEX.md)
 - [CreateSubaccountConfigurationsAMEXBulkEnrollment](docs/CreateSubaccountConfigurationsAMEXBulkEnrollment.md)
 - [CreateSubaccountConfigurationsAMEXSingleEnrollment](docs/CreateSubaccountConfigurationsAMEXSingleEnrollment.md)
 - [CreateSubaccountConfigurationsMASTERCARD](docs/CreateSubaccountConfigurationsMASTERCARD.md)
 - [CreateSubaccountConfigurationsMASTERCARDBulkEnrollment](docs/CreateSubaccountConfigurationsMASTERCARDBulkEnrollment.md)
 - [CreateSubaccountConfigurationsMASTERCARDSingleEnrollment](docs/CreateSubaccountConfigurationsMASTERCARDSingleEnrollment.md)
 - [CreateSubaccountConfigurationsVISA](docs/CreateSubaccountConfigurationsVISA.md)
 - [CreateSubaccountConfigurationsVISABulkEnrollment](docs/CreateSubaccountConfigurationsVISABulkEnrollment.md)
 - [CreateSubaccountConfigurationsVISASingleEnrollment](docs/CreateSubaccountConfigurationsVISASingleEnrollment.md)
 - [CursorPagination](docs/CursorPagination.md)
 - [CustomerEnrollmentPolicy](docs/CustomerEnrollmentPolicy.md)
 - [EnrollmentLink](docs/EnrollmentLink.md)
 - [EnrollmentLinkConflictProblem](docs/EnrollmentLinkConflictProblem.md)
 - [EnrollmentLinkCreateParams](docs/EnrollmentLinkCreateParams.md)
 - [EnrollmentLinkCreateResult](docs/EnrollmentLinkCreateResult.md)
 - [EnrollmentLinkCreateValidationProblem](docs/EnrollmentLinkCreateValidationProblem.md)
 - [EnrollmentLinkCreatedBy](docs/EnrollmentLinkCreatedBy.md)
 - [EnrollmentLinkDetail](docs/EnrollmentLinkDetail.md)
 - [EnrollmentLinkForbiddenProblem](docs/EnrollmentLinkForbiddenProblem.md)
 - [EnrollmentLinkList](docs/EnrollmentLinkList.md)
 - [EnrollmentLinkListEmbedded](docs/EnrollmentLinkListEmbedded.md)
 - [EnrollmentLinkListValidationProblem](docs/EnrollmentLinkListValidationProblem.md)
 - [EnrollmentLinkNotFoundProblem](docs/EnrollmentLinkNotFoundProblem.md)
 - [EnrollmentLinkRateLimitedProblem](docs/EnrollmentLinkRateLimitedProblem.md)
 - [EnrollmentLinkSession](docs/EnrollmentLinkSession.md)
 - [EnrollmentLinkSessionOutcome](docs/EnrollmentLinkSessionOutcome.md)
 - [EnrollmentLinkSubaccountNotFoundProblem](docs/EnrollmentLinkSubaccountNotFoundProblem.md)
 - [EnrollmentMethodsParams](docs/EnrollmentMethodsParams.md)
 - [EnrollmentMethodsResult](docs/EnrollmentMethodsResult.md)
 - [EnrollmentMethodsResultBinInfo](docs/EnrollmentMethodsResultBinInfo.md)
 - [EnrollmentMethodsResultSingleDecision](docs/EnrollmentMethodsResultSingleDecision.md)
 - [EnrollmentPolicy](docs/EnrollmentPolicy.md)
 - [FailureReason](docs/FailureReason.md)
 - [FailureReasonAcsReferences](docs/FailureReasonAcsReferences.md)
 - [Link](docs/Link.md)
 - [NetworkBlockedCardRule](docs/NetworkBlockedCardRule.md)
 - [NetworkBulkBankContact](docs/NetworkBulkBankContact.md)
 - [NetworkBulkBankContactParams](docs/NetworkBulkBankContactParams.md)
 - [NetworkBulkCompanyParams](docs/NetworkBulkCompanyParams.md)
 - [NetworkBulkConfirmation](docs/NetworkBulkConfirmation.md)
 - [NetworkBulkConfirmationBinInfo](docs/NetworkBulkConfirmationBinInfo.md)
 - [NetworkBulkConfirmationCardAdmin](docs/NetworkBulkConfirmationCardAdmin.md)
 - [NetworkBulkConfirmationCompany](docs/NetworkBulkConfirmationCompany.md)
 - [NetworkBulkConfirmationCreateParams](docs/NetworkBulkConfirmationCreateParams.md)
 - [NetworkBulkConfirmationCreateParamsBinInfo](docs/NetworkBulkConfirmationCreateParamsBinInfo.md)
 - [NetworkBulkConfirmationCreateParamsCardAdmin](docs/NetworkBulkConfirmationCreateParamsCardAdmin.md)
 - [NetworkBulkConfirmationCreateResult](docs/NetworkBulkConfirmationCreateResult.md)
 - [NetworkBulkConfirmationSummary](docs/NetworkBulkConfirmationSummary.md)
 - [NetworkBulkEnrollment](docs/NetworkBulkEnrollment.md)
 - [NetworkBulkEnrollmentBinInfo](docs/NetworkBulkEnrollmentBinInfo.md)
 - [NetworkBulkEnrollmentCardAdmin](docs/NetworkBulkEnrollmentCardAdmin.md)
 - [NetworkBulkEnrollmentCompany](docs/NetworkBulkEnrollmentCompany.md)
 - [NetworkBulkEnrollmentConsent](docs/NetworkBulkEnrollmentConsent.md)
 - [NetworkBulkEnrollmentCreateParams](docs/NetworkBulkEnrollmentCreateParams.md)
 - [NetworkBulkEnrollmentCreateParamsBinInfo](docs/NetworkBulkEnrollmentCreateParamsBinInfo.md)
 - [NetworkBulkEnrollmentCreateParamsCardAdmin](docs/NetworkBulkEnrollmentCreateParamsCardAdmin.md)
 - [NetworkBulkEnrollmentCreateParamsConsent](docs/NetworkBulkEnrollmentCreateParamsConsent.md)
 - [NetworkBulkEnrollmentCreateResult](docs/NetworkBulkEnrollmentCreateResult.md)
 - [NetworkBulkEnrollmentLinks](docs/NetworkBulkEnrollmentLinks.md)
 - [NetworkBulkEnrollmentOpen](docs/NetworkBulkEnrollmentOpen.md)
 - [NetworkBulkEnrollmentSummary](docs/NetworkBulkEnrollmentSummary.md)
 - [NetworkBulkEnrollmentSummaryLinks](docs/NetworkBulkEnrollmentSummaryLinks.md)
 - [NetworkBulkFeedCreateAmex](docs/NetworkBulkFeedCreateAmex.md)
 - [NetworkBulkFeedCreateCommon](docs/NetworkBulkFeedCreateCommon.md)
 - [NetworkBulkFeedCreateMastercard](docs/NetworkBulkFeedCreateMastercard.md)
 - [NetworkBulkFeedCreateParams](docs/NetworkBulkFeedCreateParams.md)
 - [NetworkBulkFeedCreateVisa](docs/NetworkBulkFeedCreateVisa.md)
 - [NetworkBulkFeedList](docs/NetworkBulkFeedList.md)
 - [NetworkBulkFeedListEmbedded](docs/NetworkBulkFeedListEmbedded.md)
 - [NetworkBulkFeedUpdateParams](docs/NetworkBulkFeedUpdateParams.md)
 - [NetworkBulkFeeds](docs/NetworkBulkFeeds.md)
 - [OffsetPageLinks](docs/OffsetPageLinks.md)
 - [PageLinks](docs/PageLinks.md)
 - [PlaidEndCustomer](docs/PlaidEndCustomer.md)
 - [Problem](docs/Problem.md)
 - [RawTransactionMessage](docs/RawTransactionMessage.md)
 - [RawTransactionMessageLinks](docs/RawTransactionMessageLinks.md)
 - [SelfLinks](docs/SelfLinks.md)
 - [SimulateBulkFeedFileParams](docs/SimulateBulkFeedFileParams.md)
 - [SimulateTransactionAccepted](docs/SimulateTransactionAccepted.md)
 - [SimulateTransactionMessageRequest](docs/SimulateTransactionMessageRequest.md)
 - [SimulateTransactionRequest](docs/SimulateTransactionRequest.md)
 - [SimulateTransactionRequestMerchant](docs/SimulateTransactionRequestMerchant.md)
 - [SimulateTransactionScenarioAccepted](docs/SimulateTransactionScenarioAccepted.md)
 - [SimulateTransactionScenarioRequest](docs/SimulateTransactionScenarioRequest.md)
 - [SimulateTransactionScenarioTransaction](docs/SimulateTransactionScenarioTransaction.md)
 - [Subaccount](docs/Subaccount.md)
 - [SubaccountAmexProduct](docs/SubaccountAmexProduct.md)
 - [SubaccountAmexProductClearing](docs/SubaccountAmexProductClearing.md)
 - [SubaccountAmexProductRealTimeAuth](docs/SubaccountAmexProductRealTimeAuth.md)
 - [SubaccountConfiguration](docs/SubaccountConfiguration.md)
 - [SubaccountConfigurationAMEX](docs/SubaccountConfigurationAMEX.md)
 - [SubaccountConfigurationAMEXBulkEnrollment](docs/SubaccountConfigurationAMEXBulkEnrollment.md)
 - [SubaccountConfigurationAMEXSingleEnrollment](docs/SubaccountConfigurationAMEXSingleEnrollment.md)
 - [SubaccountConfigurationMASTERCARD](docs/SubaccountConfigurationMASTERCARD.md)
 - [SubaccountConfigurationMASTERCARDBulkEnrollment](docs/SubaccountConfigurationMASTERCARDBulkEnrollment.md)
 - [SubaccountConfigurationMASTERCARDSingleEnrollment](docs/SubaccountConfigurationMASTERCARDSingleEnrollment.md)
 - [SubaccountConfigurationVISA](docs/SubaccountConfigurationVISA.md)
 - [SubaccountConfigurationVISABulkEnrollment](docs/SubaccountConfigurationVISABulkEnrollment.md)
 - [SubaccountConfigurationVISASingleEnrollment](docs/SubaccountConfigurationVISASingleEnrollment.md)
 - [SubaccountCreateParams](docs/SubaccountCreateParams.md)
 - [SubaccountEnrollmentPolicy](docs/SubaccountEnrollmentPolicy.md)
 - [SubaccountLinks](docs/SubaccountLinks.md)
 - [SubaccountList](docs/SubaccountList.md)
 - [SubaccountListEmbedded](docs/SubaccountListEmbedded.md)
 - [SubaccountNetworkBlockedCardsValue](docs/SubaccountNetworkBlockedCardsValue.md)
 - [SubaccountUpdateParams](docs/SubaccountUpdateParams.md)
 - [SubaccountUpdateResult](docs/SubaccountUpdateResult.md)
 - [TeamLink](docs/TeamLink.md)
 - [Transaction](docs/Transaction.md)
 - [TransactionAcceptor](docs/TransactionAcceptor.md)
 - [TransactionEmbedded](docs/TransactionEmbedded.md)
 - [TransactionLinks](docs/TransactionLinks.md)
 - [TransactionList](docs/TransactionList.md)
 - [TransactionListEmbedded](docs/TransactionListEmbedded.md)
 - [TransactionMatch](docs/TransactionMatch.md)
 - [TransactionMatchBankTransaction](docs/TransactionMatchBankTransaction.md)
 - [TransactionMatchCardTransaction](docs/TransactionMatchCardTransaction.md)
 - [TransactionMatchLinks](docs/TransactionMatchLinks.md)
 - [TransactionMatchList](docs/TransactionMatchList.md)
 - [TransactionMatchReason](docs/TransactionMatchReason.md)
 - [TransactionMessage](docs/TransactionMessage.md)
 - [TransactionMessageAcceptor](docs/TransactionMessageAcceptor.md)
 - [TransactionMessageBankData](docs/TransactionMessageBankData.md)
 - [TransactionMessageBankDataPersonalFinanceCategory](docs/TransactionMessageBankDataPersonalFinanceCategory.md)
 - [TransactionMessageLinks](docs/TransactionMessageLinks.md)
 - [UnprocessableEntity](docs/UnprocessableEntity.md)
 - [ValidationProblem](docs/ValidationProblem.md)
 - [ValidationProblemItem](docs/ValidationProblemItem.md)
 - [VerificationPolicy](docs/VerificationPolicy.md)
 - [Webhook](docs/Webhook.md)
 - [WebhookCreateParams](docs/WebhookCreateParams.md)
 - [WebhookCreateResult](docs/WebhookCreateResult.md)
 - [WebhookList](docs/WebhookList.md)
 - [WebhookListEmbedded](docs/WebhookListEmbedded.md)
 - [WebhookListLinks](docs/WebhookListLinks.md)
 - [WebhookUpdateParams](docs/WebhookUpdateParams.md)


## Licence

MIT; see [LICENSE](LICENSE).
