package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.config.toggle.UnleashProvider;
import no.nav.kontantstotte.storage.Storage;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_VEDLEGG;
import static no.nav.kontantstotte.config.toggle.UnleashProvider.toggle;
import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

@Component
@Path("vedlegg")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class StorageResource {

    private final Storage storage;

    @Inject
    StorageResource(Storage storage) {
        this.storage = storage;
    }

    @POST
    @Consumes(APPLICATION_OCTET_STREAM)
    @Path("{soknadsId}/{filnavn}")
    public void addAttachment(
            @PathParam("soknadsId") String soknadsId,
            @PathParam("filnavn") String filnavn,
            byte[] data) {

        toggle(KONTANTSTOTTE_VEDLEGG).throwIfDisabled(() -> new WebApplicationException(Response.status(Response.Status.NOT_IMPLEMENTED).build()));

        if(!UnleashProvider.get().isEnabled(KONTANTSTOTTE_VEDLEGG)) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_IMPLEMENTED).build());
        }

        String directory = soknadsId + hentFnrFraToken();

        storage.put(directory, filnavn, data.toString());
    }

}
