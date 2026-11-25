package co.astrada.client.testing;

import co.astrada.client.ApiClient;
import co.astrada.client.Astrada;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * A WireMock stand-in for the Astrada API, for testing code that uses astrada-java.
 *
 * <pre>{@code
 * WireMockServer server = new WireMockServer(options().dynamicPort());
 * server.start();
 * AstradaMock.register(server);                       // every operation answers its example
 * ApiClient client = AstradaMock.client(server.baseUrl());
 * new SubaccountsApi(client).listSubaccounts(null, 10);
 *
 * AstradaMock.override(server, "GetSubaccount",       // one operation answers something else
 *     aResponse().withStatus(404).withHeader("Content-Type", "application/json")
 *         .withBody("{\"title\":\"Not Found\",\"detail\":\"Subaccount not found\"}"));
 * }</pre>
 *
 * <p>The stubs are made from the API definition's examples, one per operation, named by its
 * operationId (the method name with a capital first letter: {@code listSubaccounts} is
 * {@code ListSubaccounts}). They match the method and path only. They have priority 10 or more,
 * so a stub your test adds with WireMock's default priority (5) wins, and {@link #override} uses
 * priority 1. Logging in works with any account, client id and secret.
 */
public final class AstradaMock {
    /** The classpath folder the stub mappings are in. */
    public static final String MAPPINGS = "co/astrada/client/testing/mappings/";
    /** The classpath location of the API definition for Prism, with the login route added. */
    public static final String DEFINITION = "co/astrada/client/testing/astrada-api.json";

    private static final String INDEX = "co/astrada/client/testing/mappings.txt";

    private AstradaMock() {}

    /** Every operationId that has a stub, and {@code _token} (the login) first. */
    public static List<String> names() {
        List<String> names = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(open(INDEX), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    names.add(line.trim());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return Collections.unmodifiableList(names);
    }

    /** Adds the login and every operation's stub to a WireMock server (or any WireMock {@link Admin}). */
    public static void register(Admin wireMock) {
        for (String name : names()) {
            wireMock.addStubMapping(stub(name));
        }
    }

    /** A new copy of one generated stub: an operationId such as {@code ListSubaccounts}, or {@code _token}. */
    public static StubMapping stub(String operationId) {
        String path = MAPPINGS + operationId + ".json";
        try (InputStream in = open(path)) {
            StubMapping mapping = StubMapping.buildFrom(new String(in.readAllBytes(), StandardCharsets.UTF_8));
            mapping.setId(UUID.randomUUID());
            return mapping;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Makes one operation answer {@code response} instead of its example: same method and path as
     * the generated stub, priority 1. Returns the added stub, so a test can remove it again.
     */
    public static StubMapping override(Admin wireMock, String operationId, ResponseDefinitionBuilder response) {
        StubMapping mapping = stub(operationId);
        mapping.setResponse(response.build());
        mapping.setPriority(1);
        mapping.setName(operationId + " (override)");
        wireMock.addStubMapping(mapping);
        return mapping;
    }

    /**
     * An ApiClient for the mock at {@code baseUrl} (for example {@code server.baseUrl()}): the
     * URL is marked as a sandbox, so {@code client.sandbox()} calls work too, and retries are
     * off so a stubbed 5xx comes straight back.
     */
    public static ApiClient client(String baseUrl) {
        return Astrada.builder()
                .sandboxBaseUrl(baseUrl)
                .credentials("mock-account", "mock-client", "mock-secret")
                .maxRetries(0)
                .build();
    }

    private static InputStream open(String path) throws IOException {
        InputStream in = AstradaMock.class.getClassLoader().getResourceAsStream(path);
        if (in == null) {
            throw new IOException("No mock resource " + path + " (unknown operationId?)");
        }
        return in;
    }
}
