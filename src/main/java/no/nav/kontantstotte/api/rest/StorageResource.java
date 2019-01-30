package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.storage.Storage;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.*;
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
    StorageResource(@Named("attachmentStorage") Storage storage) {
        this.storage = storage;
    }

    @POST
    @Consumes(MULTIPART_FORM_DATA)
    @Produces(APPLICATION_JSON)
    public Map<String, String> addAttachment(
            @FormDataParam("file") InputStream file,
            @FormDataParam("file") FormDataContentDisposition fileDisposition
    ) {

        toggle(KONTANTSTOTTE_VEDLEGG).throwIfDisabled(() -> new WebApplicationException(Response.status(Response.Status.NOT_IMPLEMENTED).build()));

        String directory = hentFnrFraToken();

        String uuid = UUID.randomUUID().toString();

        storage.put(directory, uuid, file);

        return new HashMap<String, String>() {{
            put("vedleggsId", uuid);
            put("filnavn", fileDisposition.getFileName());
        }};
    }

    @GET
    @Produces(APPLICATION_OCTET_STREAM)
    @Path("{vedleggsId}")
    public byte[] getAttachment(
            @PathParam("vedleggsId") String vedleggsId
    ) {

        toggle(KONTANTSTOTTE_VEDLEGG).throwIfDisabled(() -> new WebApplicationException(Response.status(Response.Status.NOT_IMPLEMENTED).build()));

        String directory = hentFnrFraToken();
        byte[] data = storage.get(directory, vedleggsId).orElse(null);
        log.debug("Loaded file with {}", data);
        return data;
    }

}
