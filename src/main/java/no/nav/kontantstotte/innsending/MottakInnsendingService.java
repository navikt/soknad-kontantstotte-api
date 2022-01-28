package no.nav.kontantstotte.innsending;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.familie.ks.kontrakter.søknad.Søknad;
import no.nav.familie.ks.kontrakter.søknad.SøknadKt;
import no.nav.kontantstotte.client.HttpClientUtil;
import no.nav.kontantstotte.innsending.oppsummering.OppsummeringPdfGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

@Component
public class MottakInnsendingService implements InnsendingService {

    private static final Logger LOG = LoggerFactory.getLogger(MottakInnsendingService.class);

    private final Counter soknadSendtInnTilMottak = Metrics.counter("soknad.kontantstotte", "innsending", "sendtmottak");
    private final Counter soknadSendtInnTilMottakFeilet =
            Metrics.counter("soknad.kontantstotte", "innsending", "sendtmottakFailure");
    private final Counter søknadMedVedlegg = Metrics.counter("soknad.kontantstotte.vedlegg");
    private final String kontantstotteMottakApiKeyUsername;
    private final String kontantstotteMottakApiKeyPassword;
    private final OppsummeringPdfGenerator oppsummeringPdfGenerator;
    private final VedleggProvider vedleggProvider;
    private URI mottakServiceUri;
    private ObjectMapper mapper;
    private HttpClient client;

    public MottakInnsendingService(@Value("${FAMILIE_KS_MOTTAK_API_URL}") URI mottakServiceUri,
                                   @Value("${SOKNAD_KONTANTSTOTTE_API_FAMILIE_KS_MOTTAK_APIKEY_USERNAME}")
                                           String kontantstotteMottakApiKeyUsername,
                                   @Value("${SOKNAD_KONTANTSTOTTE_API_FAMILIE_KS_MOTTAK_APIKEY_PASSWORD}")
                                           String kontantstotteMottakApiKeyPassword,
                                   OppsummeringPdfGenerator oppsummeringPdfGenerator,
                                   VedleggProvider vedleggProvider,
                                   ObjectMapper mapper) {

        this.kontantstotteMottakApiKeyUsername = kontantstotteMottakApiKeyUsername;
        this.kontantstotteMottakApiKeyPassword = kontantstotteMottakApiKeyPassword;
        this.mottakServiceUri = mottakServiceUri;
        this.oppsummeringPdfGenerator = oppsummeringPdfGenerator;
        this.vedleggProvider = vedleggProvider;
        this.mapper = mapper;
        this.client = HttpClientUtil.create();
    }

    @Override
    public Søknad sendInnSøknad(Søknad søknad) {
        LOG.info("Prøver å sende søknad til mottaket");
        /*try {
            HttpRequest mottakRequest =
                    HttpClientUtil.createRequest(TokenHelper.generateAuthorizationHeaderValueForLoggedInUser(contextHolder))
                                  .header(kontantstotteMottakApiKeyUsername, kontantstotteMottakApiKeyPassword)
                                  .header(HttpHeader.CONTENT_TYPE.asString(), MediaType.APPLICATION_JSON_VALUE)
                                  .uri(URI.create(mottakServiceUri + "soknadmedvedlegg"))
                                  .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(byggDtoMedKontrakt(søknad))))
                                  .build();

            sendRequest(mottakRequest);
        } catch (JsonProcessingException e) {
            throw new InnsendingException("Feiler under konvertering av innsending til json.");
        }*/
        return søknad;
    }

    private void sendRequest(HttpRequest mottakRequest) {
        try {
            HttpResponse<String> mottakresponse = client.send(mottakRequest, HttpResponse.BodyHandlers.ofString());

            if (!HttpStatus.Series.SUCCESSFUL.equals(HttpStatus.Series.resolve(mottakresponse.statusCode()))) {
                soknadSendtInnTilMottakFeilet.increment();
                throw new InnsendingException(
                        "Response fra mottak: " + mottakresponse.statusCode() + ". Response.entity: " + mottakresponse.body());
            }

            soknadSendtInnTilMottak.increment();
            LOG.info("Søknad sendt til mottaket. Response status: {}, respons: {}",
                     mottakresponse.statusCode(),
                     mottakresponse.body());
        } catch (IOException | InterruptedException e) {
            LOG.warn("Feilet under sending av søknad til mottak: {}", e.getMessage());
        }
    }

    private SoknadDto byggDtoMedKontrakt(Søknad søknad) {
        VedleggDto hovedskjema = new VedleggDto(oppsummeringPdfGenerator.genererNy(søknad, hentFnrFraToken()), "Hovedskjema");
        List<VedleggDto> vedlegg = vedleggProvider.hentVedleggForNy(søknad);
        if (!vedlegg.isEmpty()) {
            søknadMedVedlegg.increment();
        }
        vedlegg.add(hovedskjema);
        return new SoknadDto(hentFnrFraToken(), SøknadKt.toJson(søknad), vedlegg);
    }


}
