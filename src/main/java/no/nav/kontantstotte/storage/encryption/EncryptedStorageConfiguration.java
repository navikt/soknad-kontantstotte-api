package no.nav.kontantstotte.storage.encryption;

import no.nav.kontantstotte.storage.s3.S3Storage;
import no.nav.kontantstotte.storage.s3.S3StorageConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;


@Configuration
@Import({S3StorageConfiguration.class})
public class EncryptedStorageConfiguration {

    @Bean
    public Encryptor encryptor(SecretKeyProvider secretKeyProvider) {
        return new Encryptor(secretKeyProvider);
    }

    @Bean
    public SecretKeyProvider secretKeyProvider(
            @Value("${SOKNAD_KONTANTSTOTTE_STORAGE_ENCRYPTION_PASSWORD}") String passphrase) {
        return new SecretKeyProvider(passphrase);
    }

    @Profile("!gcp")
    @Bean
    public EncryptedStorage encryptedStorage(@Autowired S3Storage storage, Encryptor encryptor) {
        return new EncryptedStorage(storage, encryptor);
    }

}
