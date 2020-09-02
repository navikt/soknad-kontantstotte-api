package no.nav.kontantstotte.client;

import no.nav.familie.log.NavHttpHeaders;
import no.nav.familie.log.mdc.MDCConstants;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

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
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .header(NavHttpHeaders.NAV_CALL_ID.asString(), MDC.get(MDCConstants.MDC_CALL_ID))
                .timeout(Duration.ofMinutes(2));
    }

    public static HttpRequest.Builder createRequest() {
        return HttpRequest.newBuilder()
                .header(NavHttpHeaders.NAV_CALL_ID.asString(), MDC.get(MDCConstants.MDC_CALL_ID))
                .timeout(Duration.ofMinutes(2));
    }
}
