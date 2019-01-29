package no.nav.kontantstotte.api.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.kontantstotte.api.rest.dto.InnsendingsResponsDto;
import no.nav.kontantstotte.innsending.InnsendingService;
import no.nav.kontantstotte.innsending.Soknad;
import no.nav.security.oidc.api.ProtectedWithClaims;
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

@Component
@Path("sendinn")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = {"acr=Level4"})
public class InnsendingResource {

    private final InnsendingService innsendingService;

    private final Logger logger = LoggerFactory.getLogger(InnsendingResource.class);

    private final Counter soknadSendtInn = Metrics.counter("soknad.kontantstotte", "innsending", "mottatt");
    private final Counter soknadSendtInnUgyldig = Metrics.counter("soknad.kontantstotte", "innsending", "ugyldig");

    @Inject
    public InnsendingResource(InnsendingService innsendingService) {
        this.innsendingService = innsendingService;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public InnsendingsResponsDto sendInnSoknad(@FormDataParam("soknad") Soknad soknad) {

        if (!soknad.harBekreftetOpplysninger() || !soknad.harBekreftetPlikter()) {
            logger.info("Noen har forsøkt å sende inn en ugyldig søknad.");
            soknadSendtInnUgyldig.increment();
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        soknad.markerInnsendingsTidspunkt();
        soknadSendtInn.increment();
        innsendingService.sendInnSoknad(soknad);

        return new InnsendingsResponsDto(soknad.innsendingsTidspunkt.toString());
    }
}
