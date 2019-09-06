package no.nav.kontantstotte.api.rest;

import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import no.nav.kontantstotte.storage.attachment.AttachmentStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class StorageController {

    private static final Logger log = LoggerFactory.getLogger(StorageController.class);

    private final Storage storage;
    private final int maxFileSize;

    StorageController(@Autowired AttachmentStorage storage,
                      @Value("${attachment.max.size.mb}") int maxFileSizeMB) {
        this.storage = storage;
        this.maxFileSize = maxFileSizeMB * 1000 * 1000;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> addAttachment(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) {
            return Map.of();
        }

        byte[] bytes = multipartFile.getBytes();
        log.debug("Vedlegg med lastet opp med størrelse: " + bytes.length);

        if (bytes.length > this.maxFileSize) {
            throw new IllegalArgumentException(HttpStatus.PAYLOAD_TOO_LARGE.toString());
        }

        String directory = hentFnrFraToken();

        String uuid = UUID.randomUUID().toString();

        ByteArrayInputStream file = new ByteArrayInputStream(bytes);

        storage.put(directory, uuid, file);

        return Map.of("vedleggsId", uuid, "filnavn", multipartFile.getName());
    }

    @GetMapping(path = "{vedleggsId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getAttachment(@PathVariable("vedleggsId") String vedleggsId) {
        String directory = hentFnrFraToken();
        byte[] data = storage.get(directory, vedleggsId).orElse(null);
        log.debug("Loaded file with {}", data);
        return data;
    }

}
