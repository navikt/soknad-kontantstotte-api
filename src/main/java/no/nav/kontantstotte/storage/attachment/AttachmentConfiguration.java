package no.nav.kontantstotte.storage.attachment;

import no.nav.kontantstotte.client.RestClientConfigration;
import no.nav.kontantstotte.storage.Storage;
import no.nav.kontantstotte.storage.encryption.EncryptedStorageConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import java.net.URI;

@Configuration
@Import({RestClientConfigration.class, EncryptedStorageConfiguration.class})
public class AttachmentConfiguration {

    @Bean
    public AttachmentToStorableFormatConverter converter(ImageConversionService imageConversionService) {
        return new AttachmentToStorableFormatConverter(imageConversionService);
    }

    @Bean
    public ImageConversionService imageConversionService(
            @Named("client") Client client,
            @Value("${SOKNAD_PDF_SVG_SUPPORT_GENERATOR_URL}") URI imageToPdfEndpointBaseUrl) {
        return new ImageConversionService(client, imageToPdfEndpointBaseUrl);
    }

    @Bean("attachmentStorage")
    public Storage attachmentStorage(
            @Named("encryptedStorage") Storage storage,
            AttachmentToStorableFormatConverter storableFormatConverter) {
        return new AttachmentStorage(storage, storableFormatConverter);
    }
}
