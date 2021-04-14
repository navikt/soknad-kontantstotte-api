package no.nav.kontantstotte.api.rest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import no.nav.kontantstotte.dokument.DokumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping("api/vedlegg")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = {"acr=Level4"})
public class StorageController {
    private static Logger logger = LoggerFactory.getLogger(StorageController.class);

    private DokumentService dokumentService;

    StorageController(@Autowired DokumentService dokumentService) {
        this.dokumentService = dokumentService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> addAttachment(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        logger.info("Save attachment");

        if (multipartFile.isEmpty()) {
            logger.info("Attachment is empty");
            return Map.of();
        }

        String uuid = dokumentService.lagreDokument(multipartFile);

        verifySavedAttachment(uuid, multipartFile);

        return Map.of("vedleggsId", uuid, "filnavn", multipartFile.getName());
    }

    @GetMapping(path = "{vedleggsId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getAttachment(@PathVariable("vedleggsId") String vedleggsId) {
        logger.info("Get attachment {}", vedleggsId);
        return dokumentService.hentDokument(vedleggsId);
    }

    private void verifySavedAttachment(String vedleggId, MultipartFile vedlegg) throws IOException {
        byte[] fetched = getAttachment(vedleggId);
        byte[] vedleggData = vedlegg.getBytes();
        logger.info("Vedlegg consistency {}", Arrays.equals(fetched, vedleggData));
    }
}
