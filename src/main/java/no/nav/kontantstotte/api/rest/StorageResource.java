package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.storage.Storage;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_VEDLEGG;
import static no.nav.kontantstotte.config.toggle.UnleashProvider.toggle;
import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

@Component
@Path("vedlegg")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class StorageResource {

    private static final Logger log = LoggerFactory.getLogger(StorageResource.class);

    private final Storage storage;

    @Inject
    StorageResource(Storage storage) {
        this.storage = storage;
    }

    @POST
    @Consumes(APPLICATION_OCTET_STREAM)
    @Produces(APPLICATION_JSON)
    @Path("{soknadsId}/{filnavn}")
    public Map<String, String> addAttachment(
            @PathParam("soknadsId") String soknadsId,
            byte[] data) {

        toggle(KONTANTSTOTTE_VEDLEGG).throwIfDisabled(() -> new WebApplicationException(Response.status(Response.Status.NOT_IMPLEMENTED).build()));

        String directory = soknadsId + hentFnrFraToken();

        String uuid = UUID.randomUUID().toString();

        storage.put(directory, uuid, new ByteArrayInputStream(data));

        return new HashMap<String, String>() {{
            put("vedleggsId", uuid);
        }};
    }

    @GET
    @Produces(APPLICATION_OCTET_STREAM)
    @Path("{soknadsId}/{vedleggsId}")
    public byte[] getAttachment(
            @PathParam("soknadsId") String soknadsId,
            @PathParam("vedleggsId") String vedleggsId) {

        toggle(KONTANTSTOTTE_VEDLEGG).throwIfDisabled(() -> new WebApplicationException(Response.status(Response.Status.NOT_IMPLEMENTED).build()));

        String directory = soknadsId + hentFnrFraToken();
        byte[] data = storage.get(directory, vedleggsId).orElse(null);
        log.debug("Loaded file with {}" + data);
        return data;
    }

}
