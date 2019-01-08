package no.nav.kontantstotte.innsending;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.kontantstotte.innsending.oppsummering.OppsummeringPdfGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

class ArkivInnsendingService implements InnsendingService {

    private static final Logger log = LoggerFactory.getLogger(ArkivInnsendingService.class);

    private final Counter soknadSendtInnSendtProxy = Metrics.counter("soknad.kontantstotte", "innsending", "sendtproxy");

    private URI proxyServiceUri;

    private final Client client;

    private final OppsummeringPdfGenerator oppsummeringPdfGenerator;

    private final VedleggProvider vedleggProvider;

    ArkivInnsendingService(Client client,
                           URI proxyServiceUri,
                           OppsummeringPdfGenerator oppsummeringPdfGenerator,
                           VedleggProvider vedleggProvider) {
        this.client = client;
        this.proxyServiceUri = proxyServiceUri;
        this.oppsummeringPdfGenerator = oppsummeringPdfGenerator;
        this.vedleggProvider = vedleggProvider;
    }

    public Soknad sendInnSoknad(Soknad soknad) {
        SoknadDto soknadDto = new SoknadDto(
                hentFnrFraToken(),
                oppsummeringPdfGenerator.generer(soknad, hentFnrFraToken()),
                soknad.innsendingsTidspunkt,
                vedleggProvider.hentVedleggFor(soknad));

        Response response = client.target(proxyServiceUri)
                .path("soknad")
                .request()
                .buildPost(Entity.entity(soknadDto, MediaType.APPLICATION_JSON))
                .invoke();

        if (!SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
            throw new InnsendingException("Response fra proxy: "+ response.getStatus() + ". Response.entity: " + response.readEntity(String.class));
        }

        log.info("Søknad sendt til proxy for innsending til arkiv");

        soknadSendtInnSendtProxy.increment();
        return soknad;
    }


}
