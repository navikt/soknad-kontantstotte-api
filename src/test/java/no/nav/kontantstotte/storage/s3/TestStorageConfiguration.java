package no.nav.kontantstotte.storage.s3;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import no.nav.kontantstotte.storage.Storage;
import no.nav.kontantstotte.storage.attachment.AttachmentStorage;
import no.nav.kontantstotte.storage.encryption.EncryptedStorage;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;


@Profile("dev")
@Configuration
public class TestStorageConfiguration {

    @Primary
    @Bean
    public AttachmentStorage attachmentStorage() {
        AttachmentStorage storage = Mockito.mock(AttachmentStorage.class);

        when(storage.get(any(), any())).thenReturn(Optional.of("filinnhold".getBytes()));

        return Mockito.mock(AttachmentStorage.class);
    }


    @Primary
    @Bean
    public EncryptedStorage encryptedStorage() {
        EncryptedStorage storage = Mockito.mock(EncryptedStorage.class);

        when(storage.get(any(), any())).thenReturn(Optional.of("filinnhold".getBytes()));

        return Mockito.mock(EncryptedStorage.class);
    }


    @Primary
    @Bean
    public S3Storage S3Storage() {
        S3Storage storage = Mockito.mock(S3Storage.class);

        when(storage.get(any(), any())).thenReturn(Optional.of("filinnhold".getBytes()));

        return Mockito.mock(S3Storage.class);
    }


    @Bean
    @Primary
    public AwsClientBuilder.EndpointConfiguration enpointConfiguration() {

        return new AwsClientBuilder.EndpointConfiguration("dummy", "dummy");
    }

    @Bean
    @Primary
    public AWSCredentialsProvider credentialsProvider() {

        return Mockito.mock(AWSCredentialsProvider.class);
    }
}
