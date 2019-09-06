package no.nav.kontantstotte.storage.attachment;

import no.nav.kontantstotte.storage.encryption.EncryptedStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import no.nav.kontantstotte.client.RestClientConfiguration;
import no.nav.kontantstotte.storage.encryption.EncryptedStorageConfiguration;

@Configuration
@Import({RestClientConfiguration.class, EncryptedStorageConfiguration.class})
public class AttachmentConfiguration {

    @Bean
    public AttachmentToStorableFormatConverter converter(ImageConversionService imageConversionService) {
        return new AttachmentToStorableFormatConverter(imageConversionService);
    }

    @Bean
    public AttachmentStorage attachmentStorage(
            @Autowired EncryptedStorage storage,
            AttachmentToStorableFormatConverter storableFormatConverter) {
        return new AttachmentStorage(storage, storableFormatConverter);
    }
}
