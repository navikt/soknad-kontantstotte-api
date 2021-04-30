package no.nav.kontantstotte.storage.attachment;

import no.nav.kontantstotte.storage.encryption.EncryptedStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import no.nav.kontantstotte.client.RestClientConfiguration;
import no.nav.kontantstotte.storage.encryption.EncryptedStorageConfiguration;

@Configuration
@Import({RestClientConfiguration.class, EncryptedStorageConfiguration.class})
public class AttachmentConfiguration {

    @Bean
    public AttachmentToStorableFormatConverter converter(ImageConversionService imageConversionService) {
        return new AttachmentToStorableFormatConverter(imageConversionService);
    }

    @Profile("!gcp")
    @Bean
    public AttachmentStorage attachmentStorage(
            @Autowired EncryptedStorage storage,
            AttachmentToStorableFormatConverter storableFormatConverter) {
        return new AttachmentStorage(storage, storableFormatConverter);
    }

    @Profile("gcp")
    @Primary
    @Bean
    public AttachmentStorage nonAttachmentStorage() {
        return null;
    }
}
