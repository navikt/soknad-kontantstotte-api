package no.nav.kontantstotte.innsending;

import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneId;

import no.nav.familie.ks.kontrakter.søknad.Søknad;
import org.eclipse.jetty.http.HttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class ArkivInnsendingService implements InnsendingService {

    private static final Logger LOG = LoggerFactory.getLogger(ArkivInnsendingService.class);
    private final Counter soknadSendtInnSendtProxy = Metrics.counter("soknad.kontantstotte", "innsending", "sendtproxy");
    private final HttpClient client;
    private final OppsummeringPdfGenerator oppsummeringPdfGenerator;
    private final VedleggProvider vedleggProvider;
    private final String kontantstotteProxyApiKeyUsername;
    private final String kontantstotteProxyApiKeyPassword;
    private URI proxyServiceUri;
    private OIDCRequestContextHolder contextHolder;
    private ObjectMapper mapper;

    public ArkivInnsendingService(@Value("${SOKNAD_KONTANTSTOTTE_PROXY_API_URL}") URI proxyServiceUri,
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
        this.client = HttpClientUtil.create();
        this.proxyServiceUri = proxyServiceUri;
        this.oppsummeringPdfGenerator = oppsummeringPdfGenerator;
        this.vedleggProvider = vedleggProvider;
    }

    @Override
    public Soknad sendInnSoknad(Soknad soknad) {
        SoknadArkivDto soknadArkivDto = new SoknadArkivDto(
                hentFnrFraToken(),
                oppsummeringPdfGenerator.generer(soknad, hentFnrFraToken()),
                soknad.innsendingsTidspunkt,
                vedleggProvider.hentVedleggFor(soknad));

        sendDtoTilArkiv(soknadArkivDto);
        return soknad;
    }

    private void sendDtoTilArkiv(SoknadArkivDto dto) {
        HttpResponse<String> response;
        try {
            String body = mapper.writeValueAsString(dto);
            HttpRequest request = HttpClientUtil.createRequest(TokenHelper.generateAuthorizationHeaderValueForLoggedInUser(contextHolder))
                    .header(kontantstotteProxyApiKeyUsername, kontantstotteProxyApiKeyPassword)
                    .header(HttpHeader.CONTENT_TYPE.asString(), MediaType.APPLICATION_JSON_VALUE)
                    .uri(URI.create(proxyServiceUri + "soknad"))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (!HttpStatus.Series.SUCCESSFUL.equals(HttpStatus.Series.resolve(response.statusCode()))) {
                throw new InnsendingException("Response fra proxy: " + response.statusCode() + ". Response.entity: " + response.body());
            }
            LOG.info("Søknad sendt til proxy for innsending til arkiv");

            soknadSendtInnSendtProxy.increment();
        } catch (JsonProcessingException e) {
            throw new InnsendingException("Feiler under konvertering av innsending til json.");
        } catch (InterruptedException e) {
            throw new InnsendingException("Timer ut under innsending.");
        } catch (IOException e) {
            throw new InnsendingException("Ukjent IO feil i " + getClass().getName() + "." + e.getMessage());
        }
    }
}
