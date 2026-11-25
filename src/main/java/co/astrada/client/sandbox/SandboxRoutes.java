package co.astrada.client.sandbox;

/** Written by scripts/sandbox-routes.mjs from the sandbox-only API definition; do not edit. */
public final class SandboxRoutes {
    private SandboxRoutes() {}

    /** The sandbox-only operations: method and a pattern for the decoded, lower-case path. */
    public static final String[][] ROUTES = {
        {"DELETE", "^/card-subscriptions/[^/]+$"}, // /card-subscriptions/{subscriptionId}
        {"DELETE", "^/network-bulk-feeds/[^/]+$"}, // /network-bulk-feeds/{networkBulkFeedId}
        {"POST", "^/simulate/ingestion/amex-gl$"}, // /simulate/ingestion/amex-gl
        {"POST", "^/simulate/ingestion/smartdata$"}, // /simulate/ingestion/smartdata
        {"POST", "^/simulate/ingestion/vbds$"}, // /simulate/ingestion/vbds
        {"POST", "^/simulate/ingestion/vcf$"}, // /simulate/ingestion/vcf
        {"POST", "^/simulate/transaction/scenario$"}, // /simulate/transaction/scenario
        {"POST", "^/simulate/transactions/[^/]+$"}, // /simulate/transactions/{transactionId}
        {"POST", "^/simulate/transactions$"}, // /simulate/transactions
        {"DELETE", "^/subaccounts/[^/]+$"}, // /subaccounts/{subaccountId}
    };
}
