package co.astrada.client.oauth;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * {@code mintSdkAccessToken}'s token carries scopes outside {@code allowedScopes}, or the login did
 * not say which scopes it carries. The token is not returned. Either the SDK client holds scopes it
 * should not (Astrada changes an SDK client's scopes: ask your Astrada contact to remove them),
 * {@code allowedScopes} is missing a scope the SDK client should have, or it is a sandbox self
 * sign-up client, whose token carries every backend scope (pass null there, in the sandbox only).
 * Hand-written; not generated.
 */
public class UnexpectedScopesException extends IOException {
    private final List<String> extraScopes;

    public UnexpectedScopesException(List<String> extraScopes, String message) {
        super(message);
        this.extraScopes = Collections.unmodifiableList(extraScopes);
    }

    /** The extra scopes, sorted; empty when the login stated none. */
    public List<String> getExtraScopes() {
        return extraScopes;
    }
}
