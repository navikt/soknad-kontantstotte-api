package no.nav.kontantstotte.oppsummering.innsending;

import no.nav.kontantstotte.oppsummering.InnsendingService;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.oidc.jaxrs.OidcRequestContext;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

class ArkivInnsendingService implements InnsendingService {

    private static final String SELVBETJENING = "selvbetjening";

    private URI proxyServiceUri;

    private final Client client;

    private final OppsummeringGenerator oppsummeringGenerator;

    ArkivInnsendingService(Client client,
                           URI proxyServiceUri,
                           OppsummeringGenerator oppsummeringGenerator
    ) {
        this.client = client;
        this.proxyServiceUri = proxyServiceUri;
        this.oppsummeringGenerator = oppsummeringGenerator;
    }

    public Response sendInnSoknad(Soknad soknad) {
        SoknadDto soknadDto = new SoknadDto(hentFnrFraToken(), oppsummeringGenerator.genererOppsummering(soknad, hentFnrFraToken()), soknad.innsendingsTidspunkt);

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

    private void skrivTilFil(byte[] soknad) {
        try {
            new File(System.getProperty("user.dir") + "/TEST.pdf");
            OutputStream out = new FileOutputStream(System.getProperty("user.dir") + "/TEST.pdf");
            out.write(soknad);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
