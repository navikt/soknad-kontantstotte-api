package no.nav.kontantstotte.api.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.inject.Named;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import no.nav.kontantstotte.storage.Storage;
import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping("api/vedlegg")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = {"acr=Level4"})
public class StorageResource {

    private static final Logger log = LoggerFactory.getLogger(StorageResource.class);

    private final Storage storage;
    private final int maxFileSize;

    StorageResource(@Named("attachmentStorage") Storage storage,
                    @Value("${attachment.max.size.mb}") int maxFileSizeMB) {
        this.storage = storage;
        this.maxFileSize = maxFileSizeMB * 1000 * 1000;
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA, produces = APPLICATION_JSON)
    public Map<String, String> addAttachment(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) {
            return Map.of();
        }

        byte[] bytes = multipartFile.getBytes();
        log.debug("Vedlegg med lastet opp med størrelse: " + bytes.length);

        if (bytes.length > this.maxFileSize) {
            throw new WebApplicationException(Response.status(Response.Status.REQUEST_ENTITY_TOO_LARGE).build());
        }

        String directory = hentFnrFraToken();

        String uuid = UUID.randomUUID().toString();

        ByteArrayInputStream file = new ByteArrayInputStream(bytes);

        storage.put(directory, uuid, file);

        return Map.of("vedleggsId", uuid, "filnavn", multipartFile.getName());
    }

    @GetMapping(path = "{vedleggsId}", produces = APPLICATION_OCTET_STREAM)
    public byte[] getAttachment(@PathVariable("vedleggsId") String vedleggsId) {
        String directory = hentFnrFraToken();
        byte[] data = storage.get(directory, vedleggsId).orElse(null);
        log.debug("Loaded file with {}", data);
        return data;
    }

}
