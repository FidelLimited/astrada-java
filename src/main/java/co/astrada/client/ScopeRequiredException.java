package co.astrada.client;

import java.util.List;
import java.util.Map;

/**
 * 403 on an operation that needs a scope ({@link #getRequiredScope()}), for example
 * {@code banking:admin}. The likely cause is that this API client does not have the scope, but as
 * with any 403 the resource may also not be this account's. Hand-written; the generated API methods whose Javadoc
 * starts with "Needs banking:admin" throw it. It is an {@link ApiException}, with the
 * 403's status, headers and body.
 *
 * <pre>{@code
 * try {
 *     new BankLinksApi(client).syncBankLink(bankLinkId, null);
 * } catch (ScopeRequiredException e) {
 *     // e.getRequiredScope() is "banking:admin": most likely this API client does not have it
 * }
 * }</pre>
 */
public class ScopeRequiredException extends ApiException {
    private static final long serialVersionUID = 1L;

    private final String operationId;
    private final String requiredScope;

    /**
     * @param operationId the refused method, for example {@code syncBankLink}
     * @param requiredScope the scope it needs
     * @param cause the 403 the API answered with
     */
    public ScopeRequiredException(String operationId, String requiredScope, ApiException cause) {
        super(cause.getMessage(), cause, cause.getCode(), cause.getResponseHeaders(), cause.getResponseBody());
        this.operationId = operationId;
        this.requiredScope = requiredScope;
    }

    /** The refused method, for example {@code syncBankLink}. */
    public String getOperationId() {
        return operationId;
    }

    /** The scope the operation needs, for example {@code banking:admin}. */
    public String getRequiredScope() {
        return requiredScope;
    }

    @Override
    public String getMessage() {
        return "(403) " + (operationId == null ? "This operation" : operationId) + " needs the `" + requiredScope
                + "` scope. The likely cause is that your API client does not have it; if so, ask Astrada to "
                + "enable it. A 403 can also mean the resource is not this account's. ("
                + super.getMessage() + ")";
    }

    /** {@code e} as a ScopeRequiredException when it is a 403, else {@code e} itself. */
    public static ApiException from(ApiException e, String operationId, String requiredScope) {
        if (e instanceof ScopeRequiredException || e.getCode() != 403) {
            return e;
        }
        return new ScopeRequiredException(operationId, requiredScope, e);
    }

    /** A callback that passes a 403 on as a ScopeRequiredException; null stays null. */
    public static <T> ApiCallback<T> wrap(ApiCallback<T> callback, String operationId, String requiredScope) {
        if (callback == null) {
            return null;
        }
        return new ApiCallback<T>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                callback.onFailure(from(e, operationId, requiredScope), statusCode, responseHeaders);
            }

            @Override
            public void onSuccess(T result, int statusCode, Map<String, List<String>> responseHeaders) {
                callback.onSuccess(result, statusCode, responseHeaders);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
                callback.onUploadProgress(bytesWritten, contentLength, done);
            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
                callback.onDownloadProgress(bytesRead, contentLength, done);
            }
        };
    }
}
