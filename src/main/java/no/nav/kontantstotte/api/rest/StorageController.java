package no.nav.kontantstotte.api.rest;

import no.nav.kontantstotte.dokument.DokumentService;
import no.nav.security.oidc.api.ProtectedWithClaims;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("api/vedlegg")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = {"acr=Level4"})
public class StorageController {

    private DokumentService dokumentService;

    StorageController(DokumentService dokumentService) {
        this.dokumentService = dokumentService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> addAttachment(@RequestParam("file") MultipartFile multipartFile){
        if (multipartFile.isEmpty()) {
            return Map.of();
        }

        String uuid = dokumentService.lagreDokument(multipartFile);
        if(uuid == null){
            throw new RuntimeException("Feil med å lagre vedlegg til familie-dokument");
        }
        return Map.of("vedleggsId", uuid, "filnavn", multipartFile.getName());
    }

    @GetMapping(path = "{vedleggsId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getAttachment(@PathVariable("vedleggsId") String vedleggsId) {
        byte[] vedlegg =  dokumentService.hentDokument(vedleggsId);
        return vedlegg;
    }
}
