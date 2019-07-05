package no.nav.kontantstotte.innsending;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static javax.ws.rs.core.Response.Status.Family.familyOf;
import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.http.HttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.kontantstotte.client.HttpClientUtil;
import no.nav.kontantstotte.client.TokenHelper;
import no.nav.kontantstotte.innsending.oppsummering.OppsummeringPdfGenerator;
import no.nav.security.oidc.context.OIDCRequestContextHolder;

@Component
class ArkivInnsendingService implements InnsendingService {

    private static final Logger log = LoggerFactory.getLogger(ArkivInnsendingService.class);
    private final Counter soknadSendtInnSendtProxy = Metrics.counter("soknad.kontantstotte", "innsending", "sendtproxy");
    private final HttpClient client;
    private final OppsummeringPdfGenerator oppsummeringPdfGenerator;
    private final VedleggProvider vedleggProvider;
    private final String kontantstotteProxyApiKeyUsername;
    private final String kontantstotteProxyApiKeyPassword;
    private URI mottakServiceUri;
    private URI proxyServiceUri;
    private OIDCRequestContextHolder contextHolder;
    private ObjectMapper mapper;

    ArkivInnsendingService(@Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}") URI proxyServiceUri,
                           @Value("${SOKNAD_KONTANTSTOTTE_MOTTAK_API_URL}") URI mottakServiceUri,
                           @Value("${SOKNAD_KONTANTSTOTTE_API_SOKNAD_KONTANTSTOTTE_PROXY_API_APIKEY_USERNAME}") String kontantstotteProxyApiKeyUsername,
                           @Value("${SOKNAD_KONTANTSTOTTE_API_SOKNAD_KONTANTSTOTTE_PROXY_API_APIKEY_PASSWORD}") String kontantstotteProxyApiKeyPassword,
                           OppsummeringPdfGenerator oppsummeringPdfGenerator,
                           VedleggProvider vedleggProvider,
                           OIDCRequestContextHolder contextHolder,
                           ObjectMapper mapper) {
        this.mottakServiceUri = mottakServiceUri;
        this.kontantstotteProxyApiKeyUsername = kontantstotteProxyApiKeyUsername;
        this.kontantstotteProxyApiKeyPassword = kontantstotteProxyApiKeyPassword;
        this.contextHolder = contextHolder;
        this.mapper = mapper;
        this.client = HttpClientUtil.create();
        this.proxyServiceUri = proxyServiceUri;
        this.oppsummeringPdfGenerator = oppsummeringPdfGenerator;
        this.vedleggProvider = vedleggProvider;
    }

    @Override
    public Soknad sendInnSoknad(Soknad soknad) {
        SoknadDto soknadDto = new SoknadDto(
                hentFnrFraToken(),
                oppsummeringPdfGenerator.generer(soknad, hentFnrFraToken()),
                soknad.innsendingsTidspunkt,
                vedleggProvider.hentVedleggFor(soknad));

        HttpResponse<String> response;
        try {
            String body = mapper.writeValueAsString(soknadDto);
            HttpRequest request = HttpClientUtil.createRequest(TokenHelper.generatAuthorizationHeaderValueForLoggedInUser(contextHolder))
                    .header(kontantstotteProxyApiKeyUsername, kontantstotteProxyApiKeyPassword)
                    .header(HttpHeader.CONTENT_TYPE.asString(), MediaType.APPLICATION_JSON)
                    .uri(UriBuilder.fromUri(proxyServiceUri).path("soknad").build())
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (!SUCCESSFUL.equals(familyOf(response.statusCode()))) {
                throw new InnsendingException("Response fra proxy: " + Response.Status.fromStatusCode(response.statusCode()) + ". Response.entity: " + response.body());
            }

            try {
                log.info("Prøver å sende søknad til mottaket.");
                HttpRequest mottakRequest = HttpClientUtil.createRequest(TokenHelper.generatAuthorizationHeaderValueForLoggedInUser(contextHolder))
                        .header(kontantstotteProxyApiKeyUsername, kontantstotteProxyApiKeyPassword)
                        .header(HttpHeader.CONTENT_TYPE.asString(), MediaType.APPLICATION_JSON)
                        .uri(UriBuilder.fromUri(mottakServiceUri).path("soknad").build())
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();
                HttpResponse<String> mottakresponse = client.send(mottakRequest, HttpResponse.BodyHandlers.ofString());
                log.info("Søknad sendt til mottaket :: response={}, body={}", mottakresponse, mottakresponse.body());
            } catch (IOException | InterruptedException e) {
                log.info("Feilet under sending av søknad til mottak :: {}", e.getMessage());
            }
            log.info("Søknad sendt til proxy for innsending til arkiv");

            soknadSendtInnSendtProxy.increment();
            return soknad;
        } catch (JsonProcessingException e) {
            throw new InnsendingException("Feiler under konvertering av innsending til json.");
        } catch (InterruptedException e) {
            throw new InnsendingException("Timer ut under innsending.");
        } catch (IOException e) {
            throw new InnsendingException("Ukjent IO feil. " + e.getMessage());
        }
    }
}
