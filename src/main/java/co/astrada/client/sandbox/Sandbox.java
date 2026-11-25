package co.astrada.client.sandbox;

import co.astrada.client.ApiClient;
import co.astrada.client.ApiException;
import co.astrada.client.api.SandboxApi;
import co.astrada.client.model.SimulateBulkFeedFileParams;
import java.util.Objects;

/**
 * The sandbox-only calls, from {@code client.sandbox()}: the generated {@link SandboxApi} plus
 * {@link #simulateBulkFeedFile}. Hand-written. Every call is checked by {@link SandboxGuard}
 * first, so none can reach production.
 */
public class Sandbox extends SandboxApi {
    public Sandbox(ApiClient client) {
        super(client);
    }

    /**
     * Drops a synthetic bulk feed file for the feed with {@code params.networkReference}, as the
     * network would deliver it; the cards in it are enrolled asynchronously, as for a real file.
     * Calls the {@code simulateBulkFeedFile<Type>} method for {@code type}.
     *
     * @param type the feed's type
     * @param params the feed's network reference and how many cards and transactions to make
     * @throws SandboxOnlyException if the client is not set up for the sandbox
     * @throws ApiException if the call fails
     */
    public void simulateBulkFeedFile(BulkFeedType type, SimulateBulkFeedFileParams params) throws ApiException {
        switch (Objects.requireNonNull(type, "type")) {
            case GL:
                simulateBulkFeedFileGl(params);
                break;
            case SMARTDATA:
                simulateBulkFeedFileSmartdata(params);
                break;
            case VBDS:
                simulateBulkFeedFileVbds(params);
                break;
            case VCF:
                simulateBulkFeedFileVcf(params);
                break;
            default:
                throw new IllegalArgumentException("unknown bulk feed type " + type);
        }
    }
}
