package no.nav.kontantstotte.dokument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class DokumentService {

    private static final Logger log = LoggerFactory.getLogger(DokumentService.class);

    private final int maxFileSize;

    private FamilieDokumentClient familieDokumentClient;

    DokumentService(@Value("${attachment.max.size.mb}") int maxFileSizeMB,
                    FamilieDokumentClient familieDokumentClient) {
        this.maxFileSize = maxFileSizeMB * 1000 * 1000;
        this.familieDokumentClient = familieDokumentClient;
    }

    public String lagreDokument(MultipartFile multipartFile) {
        byte[] bytes;
        try {
            bytes = multipartFile.getBytes();
        } catch (IOException e) {
            log.error("Ugyldige vedleggsdata");
            throw new IllegalArgumentException("Ugyldige vedleggsdata");
        }

        log.debug("Vedlegg med lastet opp med størrelse: " + bytes.length);
        if (bytes.length > this.maxFileSize) {
            throw new IllegalArgumentException(HttpStatus.PAYLOAD_TOO_LARGE.toString());
        }
        return familieDokumentClient.lagreVedlegg(bytes,
                                                  multipartFile.getOriginalFilename());
    }

    public byte[] hentDokument(String key) {
        byte[] dokument = familieDokumentClient.hentVedlegg(key);
        if (dokument != null) {
            return dokument;
        } else {
            log.info("Feil med hent av vedlegg fra familie-dokument");
            return null;
        }
    }
}
