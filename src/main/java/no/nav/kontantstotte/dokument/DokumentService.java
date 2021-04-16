package no.nav.kontantstotte.dokument;

import no.nav.kontantstotte.storage.Storage;
import no.nav.kontantstotte.storage.attachment.AttachmentStorage;
import no.nav.kontantstotte.storage.attachment.AttachmentToStorableFormatConverter;
import no.nav.kontantstotte.storage.encryption.Encryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static no.nav.kontantstotte.innlogging.InnloggingUtils.hentFnrFraToken;

@Component
public class DokumentService {

    private static final Logger log = LoggerFactory.getLogger(DokumentService.class);

    private final Storage storage;

    private final int maxFileSize;

    private final Encryptor encryptor;

    private final AttachmentToStorableFormatConverter formatConverter;

    private FamilieDokumentClient familieDokumentClient;

    DokumentService(AttachmentStorage storage,
                    @Value("${attachment.max.size.mb}") int maxFileSizeMB,
                    FamilieDokumentClient familieDokumentClient,
                    Encryptor encryptor,
                    AttachmentToStorableFormatConverter formatConverter) {
        this.storage = storage;
        this.maxFileSize = maxFileSizeMB * 1000 * 1000;
        this.familieDokumentClient = familieDokumentClient;
        this.encryptor = encryptor;
        this.formatConverter = formatConverter;
    }

    public String lagreDokument(MultipartFile multipartFile){
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
        try {
            return familieDokumentClient.lagreVedlegg(encrypt(konvertererTilLagretFormat(bytes)),
                                                      multipartFile.getOriginalFilename());
        }catch(IOException e){
            log.error("Feil med encrypt vedlegg");
            return null;
        }
    }

    public byte[] hentDokument(String key) {
        byte[] dokument = familieDokumentClient.hentVedlegg(key);
        if (dokument != null) {
            return decrypt(dokument);
        } else {
            log.info("Feil med hent av vedlegg fra familie-dokument");
        }
        //TODO: fjerner å hent fra s3 når alle dokument funnes in familie-dokument
        String directory = hentFnrFraToken();
        byte[] data = storage.get(directory, key).orElse(null);
        return data;
    }

    private byte[] encrypt(byte[] data) throws IOException {
        return encryptor.encryptedStream(hentFnrFraToken(), new ByteArrayInputStream(data)).readAllBytes();
    }

    private byte[] decrypt(byte[] data){
        return encryptor.decrypt(hentFnrFraToken(), data);
    }

    private byte[] konvertererTilLagretFormat(byte[] data){
        return formatConverter.toStorageFormat(data);
    }
}
