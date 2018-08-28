package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.oppsummering.OppsummeringTransformer;
import no.nav.kontantstotte.oppsummering.Soknad;
import no.nav.kontantstotte.oppsummering.SoknadDto;
import no.nav.kontantstotte.service.InnsendingService;
import no.nav.kontantstotte.service.PdfService;
import no.nav.security.oidc.api.ProtectedWithClaims;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.oidc.jaxrs.OidcRequestContext;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static java.time.LocalDateTime.now;

@Component
@Path("sendinn")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class InnsendingResource {

    @Inject
    private PdfService pdfService;

    @Inject
    private InnsendingService innsendingService;

    @Inject
    private OppsummeringTransformer oppsummeringTransformer;

    private static final String SELVBETJENING = "selvbetjening";

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response sendInnSoknad(@FormDataParam("soknad") Soknad soknad) {
        soknad.innsendingTimestamp = now();
        soknad.person.fnr = hentFnrFraToken();
        String oppsummeringHtml = oppsummeringTransformer.renderHTMLForPdf(soknad);

        byte[] soknadPdf = pdfService.genererPdf(oppsummeringHtml);
        SoknadDto soknadDto = new SoknadDto(soknad.person.fnr, soknadPdf);
        return innsendingService.sendInnSoknad(soknadDto);
    }

    private static String hentFnrFraToken() {
        OIDCValidationContext context = OidcRequestContext.getHolder().getOIDCValidationContext();
        return context.getClaims(SELVBETJENING).getClaimSet().getSubject();
    }
}
