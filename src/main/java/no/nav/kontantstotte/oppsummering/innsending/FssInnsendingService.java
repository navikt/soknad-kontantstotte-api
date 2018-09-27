package no.nav.kontantstotte.oppsummering.innsending;

import no.nav.kontantstotte.oppsummering.InnsendingService;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.oidc.jaxrs.OidcRequestContext;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

class FssInnsendingService implements InnsendingService {

    private static final String SELVBETJENING = "selvbetjening";

    private URI proxyServiceUri;

    private final Client client;

    private final PdfService pdfService;

    FssInnsendingService(Client client, URI proxyServiceUri, PdfService pdfService) {
        this.client = client;
        this.proxyServiceUri = proxyServiceUri;
        this.pdfService = pdfService;
    }

    public Response sendInnSoknad(Soknad soknad) {

        SoknadDto soknadDto = new SoknadDto(hentFnrFraToken(), pdfService.genererPdf(soknad), soknad.innsendingTimestamp);

        return client.target(proxyServiceUri)
                .path("soknad")
                .request()
                .buildPost(Entity.entity(soknadDto, MediaType.APPLICATION_JSON))
                .invoke();
    }

    private static String hentFnrFraToken() {
        OIDCValidationContext context = OidcRequestContext.getHolder().getOIDCValidationContext();
        return context.getClaims(SELVBETJENING).getClaimSet().getSubject();
    }

}
