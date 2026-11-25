package co.astrada.client.sandbox;

import co.astrada.client.ApiException;

/**
 * A sandbox-only call was refused before any request was sent, because the client is not set up
 * for the sandbox. Hand-written; not generated. It is an {@link ApiException} with code 0.
 */
public class SandboxOnlyException extends ApiException {
    private static final long serialVersionUID = 1L;

    public SandboxOnlyException(String message) {
        super(message);
    }
}
