package no.nav.kontantstotte.storage.attachment;

import javax.inject.Named;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import no.nav.kontantstotte.client.RestClientConfiguration;
import no.nav.kontantstotte.storage.Storage;
import no.nav.kontantstotte.storage.encryption.EncryptedStorageConfiguration;

@Configuration
@Import({RestClientConfiguration.class, EncryptedStorageConfiguration.class})
public class AttachmentConfiguration {

    @Bean
    public AttachmentToStorableFormatConverter converter(ImageConversionService imageConversionService) {
        return new AttachmentToStorableFormatConverter(imageConversionService);
    }

    @Bean("attachmentStorage")
    public Storage attachmentStorage(
            @Named("encryptedStorage") Storage storage,
            AttachmentToStorableFormatConverter storableFormatConverter) {
        return new AttachmentStorage(storage, storableFormatConverter);
    }
}
