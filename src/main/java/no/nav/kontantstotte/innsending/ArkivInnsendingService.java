package no.nav.kontantstotte.innsending;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static javax.ws.rs.core.Response.Status.Family.familyOf;
import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
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
    private URI proxyServiceUri;
    private OIDCRequestContextHolder contextHolder;
    private ObjectMapper mapper;

    ArkivInnsendingService(@Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}") URI proxyServiceUri,
                           @Value("${SOKNAD_KONTANTSTOTTE_API_SOKNAD_KONTANTSTOTTE_PROXY_API_APIKEY_USERNAME}") String kontantstotteProxyApiKeyUsername,
                           @Value("${SOKNAD_KONTANTSTOTTE_API_SOKNAD_KONTANTSTOTTE_PROXY_API_APIKEY_PASSWORD}") String kontantstotteProxyApiKeyPassword,
                           OppsummeringPdfGenerator oppsummeringPdfGenerator,
                           VedleggProvider vedleggProvider,
                           OIDCRequestContextHolder contextHolder,
                           ObjectMapper mapper) {
        this.kontantstotteProxyApiKeyUsername = kontantstotteProxyApiKeyUsername;
        this.kontantstotteProxyApiKeyPassword = kontantstotteProxyApiKeyPassword;
        this.contextHolder = contextHolder;
        this.mapper = mapper;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
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
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(proxyServiceUri.resolve("soknad"))
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(soknadDto)))
                    .header(HttpHeader.AUTHORIZATION.asString(), TokenHelper.generatAuthorizationHeaderValueForLoggedInUser(contextHolder))
                    .header(HttpHeader.CONTENT_TYPE.asString(), MediaType.APPLICATION_JSON)
                    .header(kontantstotteProxyApiKeyUsername, kontantstotteProxyApiKeyPassword)
                    .timeout(Duration.ofMinutes(2))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (JsonProcessingException e) {
            throw new InnsendingException("Feiler under konvertering av innsending til json.");
        } catch (InterruptedException e) {
            throw new InnsendingException("Timer ut under innsending.");
        } catch (IOException e) {
            throw new InnsendingException("Ukjent IO feil. " + e.getMessage());
        }

        if (!SUCCESSFUL.equals(familyOf(response.statusCode()))) {
            throw new InnsendingException("Response fra proxy: " + Response.Status.fromStatusCode(response.statusCode()) + ". Response.entity: " + response.body());
        }

        log.info("Søknad sendt til proxy for innsending til arkiv");

        soknadSendtInnSendtProxy.increment();
        return soknad;
    }


}
