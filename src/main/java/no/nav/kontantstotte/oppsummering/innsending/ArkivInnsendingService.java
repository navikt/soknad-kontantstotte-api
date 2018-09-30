package no.nav.kontantstotte.oppsummering.innsending;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.oppsummering.InnsendingService;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.oidc.jaxrs.OidcRequestContext;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

class ArkivInnsendingService implements InnsendingService {

    private static final String SELVBETJENING = "selvbetjening";
    public static final String KONTANTSTOTTE_OPPSUMMERING_NY = "kontantstotte.oppsummering.ny";
    private final Unleash unleash;

    private URI proxyServiceUri;

    private final Client client;

    private final OppsummeringService oppsummeringServiceV1;
    private final OppsummeringService oppsummeringServiceV2;

    ArkivInnsendingService(Client client,
                           URI proxyServiceUri,
                           OppsummeringService oppsummeringServiceV1,
                           OppsummeringService oppsummeringServiceV2,
                           Unleash unleash
    ) {
        this.client = client;
        this.proxyServiceUri = proxyServiceUri;
        this.oppsummeringServiceV1 = oppsummeringServiceV1;
        this.oppsummeringServiceV2 = oppsummeringServiceV2;
        this.unleash = unleash;

    }

    public Response sendInnSoknad(Soknad soknad) {

        OppsummeringService oppsummeringService;
        if(unleash.isEnabled(KONTANTSTOTTE_OPPSUMMERING_NY)){
            oppsummeringService =this.oppsummeringServiceV2;
        }else{
            oppsummeringService =this.oppsummeringServiceV1;
        }

        SoknadDto soknadDto = new SoknadDto(hentFnrFraToken(), oppsummeringService.genererOppsummering(soknad), soknad.innsendingTimestamp);

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
