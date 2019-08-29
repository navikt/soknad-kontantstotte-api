package no.nav.kontantstotte.client;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

import no.nav.familie.http.client.NavHttpHeaders;
import no.nav.familie.log.mdc.MDCConstants;
import org.slf4j.MDC;

public final class HttpClientUtil {

    private static final String AUTHORIZATION = "Authorization";

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
                .header(AUTHORIZATION, authorizationHeader)
                .header(NavHttpHeaders.NAV_CALLID.asString(), MDC.get(MDCConstants.MDC_CALL_ID))
                .timeout(Duration.ofMinutes(2));
    }

    public static HttpRequest.Builder createRequest() {
        return HttpRequest.newBuilder()
                .header(NavHttpHeaders.NAV_CALLID.asString(), MDC.get(MDCConstants.MDC_CALL_ID))
                .timeout(Duration.ofMinutes(2));
    }
}
