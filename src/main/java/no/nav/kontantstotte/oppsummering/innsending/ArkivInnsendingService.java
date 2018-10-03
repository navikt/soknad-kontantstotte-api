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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_NY_OPPSUMMERING;

class ArkivInnsendingService implements InnsendingService {

    private static final String SELVBETJENING = "selvbetjening";
    private final Unleash unleash;

    private URI proxyServiceUri;

    private final Client client;

    private final OppsummeringGenerator oppsummeringGeneratorV1;
    private final OppsummeringGenerator oppsummeringGeneratorV2;

    ArkivInnsendingService(Client client,
                           URI proxyServiceUri,
                           OppsummeringGenerator oppsummeringGeneratorV1,
                           OppsummeringGenerator oppsummeringGeneratorV2,
                           Unleash unleash
    ) {
        this.client = client;
        this.proxyServiceUri = proxyServiceUri;
        this.oppsummeringGeneratorV1 = oppsummeringGeneratorV1;
        this.oppsummeringGeneratorV2 = oppsummeringGeneratorV2;
        this.unleash = unleash;

    }

    public Response sendInnSoknad(Soknad soknad) {

        OppsummeringGenerator oppsummeringGenerator;
        if (unleash.isEnabled(KONTANTSTOTTE_NY_OPPSUMMERING)) {
            oppsummeringGenerator = this.oppsummeringGeneratorV2;
        } else {
            oppsummeringGenerator = this.oppsummeringGeneratorV1;
        }

        SoknadDto soknadDto = new SoknadDto(hentFnrFraToken(), oppsummeringGenerator.genererOppsummering(soknad), soknad.innsendingsTidspunkt);

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
