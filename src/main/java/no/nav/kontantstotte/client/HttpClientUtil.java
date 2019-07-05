package no.nav.kontantstotte.client;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

import org.eclipse.jetty.http.HttpHeader;
import org.slf4j.MDC;

import no.nav.log.MDCConstants;

public final class HttpClientUtil {

    private HttpClientUtil() {
    }

    public static HttpClient create() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public static HttpRequest.Builder createRequest(String authorizationHeader) {
        return HttpRequest.newBuilder()
                .header(HttpHeader.AUTHORIZATION.asString(), authorizationHeader)
                .header("Nav-Call-Id", MDC.get(MDCConstants.MDC_CORRELATION_ID))
                .timeout(Duration.ofMinutes(2));
    }

    public static HttpRequest.Builder createRequest() {
        return HttpRequest.newBuilder()
                .header("Nav-Call-Id", MDC.get(MDCConstants.MDC_CORRELATION_ID))
                .timeout(Duration.ofMinutes(2));
    }
}
