package co.astrada.client.http;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Keeps OkHttp from sending a POST or PATCH twice. Hand-written; {@code Astrada.builder()} adds it as
 * the innermost application interceptor (see the last paragraph). OkHttp itself resends a request after a connection drops mid-call or after
 * a 408 (retryOnConnectionFailure), which would create or change something twice. It never does so
 * for a request whose body is one-shot, so this interceptor marks the body of every POST and PATCH
 * one-shot. The SDK's own resends are unchanged: {@link RetryInterceptor} retries only idempotent
 * methods, and the login sends a request again only after a 401 (the request never ran). GET, PUT,
 * DELETE and the other idempotent methods keep OkHttp's recovery.
 *
 * <p>What a caller sees for a POST or PATCH: a connection that drops after the request was sent, a
 * 408, a 421 or a {@code 503 Retry-After: 0} is an {@code ApiException} (the IOException or the
 * status) instead of a silent resend. A 307 or 308 redirect is not followed (an {@code ApiException}
 * with that status), an HTTP/2 REFUSED_STREAM is not resent although that request never ran, and a
 * 407 from a plain-HTTP proxy or an OkHttp {@code authenticator} challenge cannot be answered. All
 * of these fail without sending the request twice; calling again is the caller's decision.
 *
 * <p>{@code Astrada.builder()} adds it as the innermost application interceptor, after the login,
 * so a body that an interceptor of {@code httpClient(...)} replaces is still marked. A client set up
 * by hand ({@code ClientCredentials.addTo(...)}) does not get it. An application interceptor added
 * after {@code build()}, or a network interceptor, that replaces the body can undo it.
 * {@code ApiClient.setDebugging} puts its logger just before it, so {@code BODY} still logs POST and
 * PATCH bodies.
 */
public final class SendOnce implements Interceptor {
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody body = request.body();
        if (body == null || body.isOneShot() || RetryInterceptor.isIdempotent(request.method())) {
            return chain.proceed(request);
        }
        return chain.proceed(request.newBuilder().method(request.method(), new OneShot(body)).build());
    }

    /** The same body, marked one-shot so OkHttp does not send it again by itself. */
    private static final class OneShot extends RequestBody {
        private final RequestBody delegate;

        OneShot(RequestBody delegate) {
            this.delegate = delegate;
        }

        @Override
        public MediaType contentType() {
            return delegate.contentType();
        }

        @Override
        public long contentLength() throws IOException {
            return delegate.contentLength();
        }

        @Override
        public boolean isOneShot() {
            return true;
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            delegate.writeTo(sink);
        }
    }
}
