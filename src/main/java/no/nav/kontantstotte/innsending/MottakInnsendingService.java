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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Objects;

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
    private RestOperations restClient;

    public MottakInnsendingService(@Value("${FAMILIE_KS_MOTTAK_API_URL}") URI mottakServiceUri,
                                   @Value("${SOKNAD_KONTANTSTOTTE_API_FAMILIE_KS_MOTTAK_APIKEY_USERNAME}")
                                           String kontantstotteMottakApiKeyUsername,
                                   @Value("${SOKNAD_KONTANTSTOTTE_API_FAMILIE_KS_MOTTAK_APIKEY_PASSWORD}")
                                           String kontantstotteMottakApiKeyPassword,
                                   OppsummeringPdfGenerator oppsummeringPdfGenerator,
                                   VedleggProvider vedleggProvider,
                                   ObjectMapper mapper,
                                   @Qualifier("tokenExchange") RestOperations restClient) {

        this.kontantstotteMottakApiKeyUsername = kontantstotteMottakApiKeyUsername;
        this.kontantstotteMottakApiKeyPassword = kontantstotteMottakApiKeyPassword;
        this.mottakServiceUri = mottakServiceUri;
        this.oppsummeringPdfGenerator = oppsummeringPdfGenerator;
        this.vedleggProvider = vedleggProvider;
        this.mapper = mapper;
        this.client = HttpClientUtil.create();
        this.restClient = restClient;
    }

    @Override
    public Søknad sendInnSøknad(Søknad søknad) {
        LOG.info("Prøver å sende søknad til mottaket");
        try {
            HttpEntity<SoknadDto> httpEntity = new HttpEntity<>(byggDtoMedKontrakt(søknad), httpHeaders());
            var respons = restClient.exchange(mottakServiceUri + "soknadmedvedlegg",
                                              HttpMethod.POST,
                                              httpEntity,
                                              Søknad.class);
            if (respons.getStatusCode() != HttpStatus.OK) {
                soknadSendtInnTilMottakFeilet.increment();
                throw new InnsendingException(
                        "Response fra mottak: " + respons.getStatusCode() + ". Response.entity: " + respons.getBody());
            }
            LOG.info("Søknad sendt til mottaket. Response status: {}, respons: {}",
                     respons.getStatusCode(),
                     respons.getBody());
            soknadSendtInnTilMottak.increment();
            return Objects.requireNonNull(respons.getBody(), "Fikk null respons fra mottak tjeneste");
        } catch (RestClientException e) {
            LOG.warn("Kan ikke sendes søknad, feiler med ", e);
            throw new InnsendingException(e.getMessage());
        }
    }

    @Override
    public String ping() {
        LOG.info("Ping familie-ks-mottak");
        HttpEntity<SoknadDto> httpEntity = new HttpEntity<>(httpHeaders());
        try {
            var respons = restClient.exchange(mottakServiceUri + "ping",
                                              HttpMethod.GET,
                                              httpEntity,
                                              String.class);
            LOG.info("Response status: {}, respons: {}", respons.getStatusCode(), respons.getBody());
            return respons.getBody();
        } catch (RestClientException e) {
            LOG.warn("Kan ikke oppnå familie-ks-mottak ", e);
            throw new InnsendingException(e.getMessage());
        }
    }

    public static HttpHeaders httpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        return httpHeaders;
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
