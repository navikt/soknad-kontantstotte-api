package no.nav.kontantstotte.api.rest;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.oppsummering.v1.OppsummeringTransformer;
import no.nav.kontantstotte.oppsummering.v1.Soknad;
import no.nav.kontantstotte.oppsummering.v2.SoknadOppsummering;
import no.nav.kontantstotte.oppsummering.v2.SoknadTilOppsummering;
import no.nav.kontantstotte.service.OppsummeringService;
import no.nav.kontantstotte.service.SoknadDto;
import no.nav.kontantstotte.service.InnsendingService;
import no.nav.kontantstotte.service.PdfService;
import no.nav.kontantstotte.tekst.TekstProvider;
import no.nav.security.oidc.api.ProtectedWithClaims;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.oidc.jaxrs.OidcRequestContext;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static java.time.LocalDateTime.now;

@Component
@Path("sendinn")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = {"acr=Level4"})
public class InnsendingResource {

    public static final String KONTANTSTOTTE_NY_PDF= "kontantstotte.nypdf";

    private final PdfService pdfService;
    private final OppsummeringService oppsummeringService;

    private final InnsendingService innsendingService;

    private final OppsummeringTransformer oppsummeringTransformer;

    private static final String SELVBETJENING = "selvbetjening";

    private final Logger logger = LoggerFactory.getLogger(InnsendingResource.class);

    private final Unleash unleash;
    private final TeksterResource teksterResource;

    @Inject
    public InnsendingResource(PdfService pdfService,
                              OppsummeringService oppsummeringService,
                              InnsendingService innsendingService,
                              TeksterResource teksterResource,
                              OppsummeringTransformer oppsummeringTransformer,
                              Unleash unleash) {
        this.pdfService = pdfService;
        this.oppsummeringService = oppsummeringService;
        this.innsendingService = innsendingService;
        this.teksterResource = teksterResource;
        this.oppsummeringTransformer = oppsummeringTransformer;
        this.unleash = unleash;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response sendInnSoknad(@FormDataParam("soknad") Soknad soknad) {
        soknad.innsendingTimestamp = now();
        soknad.person.fnr = hentFnrFraToken();

        if(soknad.erGyldig()) {
            byte[] soknadPdf;
            if(unleash.isEnabled(KONTANTSTOTTE_NY_PDF)){
                SoknadOppsummering oppsummering = new SoknadTilOppsummering().map(soknad, teksterResource.tekster(soknad.sprak));
                byte[] bytes = oppsummeringService.genererHtml(oppsummering);
                soknadPdf = pdfService.genererNyPdf(bytes);
            } else {
                String oppsummeringHtml = oppsummeringTransformer.renderHTMLForPdf(soknad);
                soknadPdf = pdfService.genererGammelPdf(oppsummeringHtml);
            }
            SoknadDto soknadDto = new SoknadDto(soknad.person.fnr, soknadPdf);
            return innsendingService.sendInnSoknad(soknadDto);
        } else {
            logger.warn("Noen har forsøkt å sende inn en ugyldig søknad.");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

    private static String hentFnrFraToken() {
        OIDCValidationContext context = OidcRequestContext.getHolder().getOIDCValidationContext();
        return context.getClaims(SELVBETJENING).getClaimSet().getSubject();
    }
}
