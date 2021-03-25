package no.nav.kontantstotte.dokument;

import no.nav.kontantstotte.storage.Storage;
import no.nav.kontantstotte.storage.attachment.AttachmentStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

@Component
public class DokumentService {
    private static final Logger log = LoggerFactory.getLogger(DokumentService.class);

    private final Storage storage;

    private final int maxFileSize;

    private FamilieDokumentClient familieDokumentClient;

    DokumentService(@Autowired AttachmentStorage storage,
                    @Value("${attachment.max.size.mb}") int maxFileSizeMB,
                    @Autowired FamilieDokumentClient familieDokumentClient) {
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

        return familieDokumentClient.lagreVedlegg(multipartFile);
    }

    public byte[] hentDokument(String key){
        byte[] dokument = familieDokumentClient.hentVedlegg(key);
        if(dokument != null){
            return dokument;
        }
        String directory = hentFnrFraToken();
        byte[] data = storage.get(directory, key).orElse(null);
        return data;
    }
}
