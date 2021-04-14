package no.nav.kontantstotte.dokument;

import no.nav.kontantstotte.storage.Storage;
import no.nav.kontantstotte.storage.attachment.AttachmentStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

@Component
public class DokumentService {

    private static final Logger log = LoggerFactory.getLogger(DokumentService.class);

    private final Storage storage;

    private final int maxFileSize;

    private FamilieDokumentClient familieDokumentClient;

    DokumentService(AttachmentStorage storage,
                    @Value("${attachment.max.size.mb}") int maxFileSizeMB,
                    FamilieDokumentClient familieDokumentClient) {
        this.storage = storage;
        this.maxFileSize = maxFileSizeMB * 1000 * 1000;
        this.familieDokumentClient = familieDokumentClient;
    }

    public String lagreDokument(MultipartFile multipartFile) throws IOException {
        byte[] bytes = multipartFile.getBytes();
        log.debug("Vedlegg med lastet opp med størrelse: " + bytes.length);

        if (bytes.length > this.maxFileSize) {
            throw new IllegalArgumentException(HttpStatus.PAYLOAD_TOO_LARGE.toString());
        }
        try {
            return familieDokumentClient.lagreVedlegg(multipartFile);
        }catch(Throwable e){
            log.warn("Feil med å lagre vedlegg til familie-dokument");
            Map<String, String> savedDokument = saveToStorage(multipartFile);
            return savedDokument.get("vedleggsId");
        }
    }

    private Map<String, String> saveToStorage(MultipartFile multipartFile) throws IOException {
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

    public byte[] hentDokument(String key) {
        try {
            byte[] dokument = familieDokumentClient.hentVedlegg(key);
            if (dokument != null) {
                return dokument;
            }
        } catch (Throwable e) {
            log.warn("Feil med hent av vedlegg fra familie-dokument");
        }
        String directory = hentFnrFraToken();
        byte[] data = storage.get(directory, key).orElse(null);
        return data;
    }
}
