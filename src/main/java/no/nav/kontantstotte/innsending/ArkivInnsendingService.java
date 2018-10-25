package no.nav.kontantstotte.innsending;

import no.nav.kontantstotte.innsending.oppsummering.OppsummeringPdfGenerator;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.oidc.jaxrs.OidcRequestContext;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

class ArkivInnsendingService implements InnsendingService {

    private static final String SELVBETJENING = "selvbetjening";

    private URI proxyServiceUri;

    private final Client client;

    private final OppsummeringPdfGenerator oppsummeringPdfGenerator;

    ArkivInnsendingService(Client client,
                           URI proxyServiceUri,
                           OppsummeringPdfGenerator oppsummeringPdfGenerator
    ) {
        this.client = client;
        this.proxyServiceUri = proxyServiceUri;
        this.oppsummeringPdfGenerator = oppsummeringPdfGenerator;
    }

    public Response sendInnSoknad(Soknad soknad) {
        SoknadDto soknadDto = new SoknadDto(hentFnrFraToken(), oppsummeringPdfGenerator.generer(soknad, hentFnrFraToken()), soknad.innsendingsTidspunkt);

        return client.target(proxyServiceUri)
                .path("soknad")
                .request()
                .buildPost(Entity.entity(soknadDto, MediaType.APPLICATION_JSON))
                .invoke();
    }

    public static String hentFnrFraToken() {
        OIDCValidationContext context = OidcRequestContext.getHolder().getOIDCValidationContext();
        return context.getClaims(SELVBETJENING).getClaimSet().getSubject();
    }

}
