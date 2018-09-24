package no.nav.kontantstotte.api.rest;

import no.finn.unleash.Unleash;
import no.nav.kontantstotte.oppsummering.OppsummeringTransformer;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.service.SoknadDto;
import no.nav.kontantstotte.service.InnsendingService;
import no.nav.kontantstotte.service.PdfService;
import no.nav.security.oidc.api.ProtectedWithClaims;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.oidc.jaxrs.OidcRequestContext;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final PdfService pdfService;

    private final InnsendingService innsendingService;

    private final OppsummeringTransformer oppsummeringTransformer;

    private static final String SELVBETJENING = "selvbetjening";

    private final Logger logger = LoggerFactory.getLogger(InnsendingResource.class);

    @Autowired
    private Unleash unleash;

    @Inject
    public InnsendingResource(PdfService pdfService, InnsendingService innsendingService, OppsummeringTransformer oppsummeringTransformer) {
        this.pdfService = pdfService;
        this.innsendingService = innsendingService;
        this.oppsummeringTransformer = oppsummeringTransformer;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response sendInnSoknad(@FormDataParam("soknad") Soknad soknad) {
        soknad.innsendingTimestamp = now();
        soknad.person.fnr = hentFnrFraToken();
        logger.warn("sendInnSoknad" + ", " + soknad.erGyldig());
        logger.warn(String.valueOf(unleash.getFeatureToggleNames()));
        logger.warn(String.valueOf(unleash));
        if(soknad.erGyldig()) {
            String oppsummeringHtml = oppsummeringTransformer.renderHTMLForPdf(soknad);
            logger.warn(oppsummeringHtml);
            byte[] soknadPdf = pdfService.genererPdf(oppsummeringHtml);
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
