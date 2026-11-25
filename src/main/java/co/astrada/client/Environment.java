package co.astrada.client;

/**
 * Where the SDK sends requests. Hand-written; not generated.
 *
 * <p>Sandbox and production have separate credentials; a sandbox client cannot log in to
 * production. Start in {@link #SANDBOX}.
 */
public enum Environment {
    /** Live cards and transactions: {@code https://api.astrada.co}. */
    PRODUCTION("https://api.astrada.co"),
    /** Test cards and simulated transactions: {@code https://api.sandbox.astrada.co}. */
    SANDBOX("https://api.sandbox.astrada.co");

    private final String baseUrl;

    Environment(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /** The API base URL, without a trailing slash. */
    public String getBaseUrl() {
        return baseUrl;
    }
}
