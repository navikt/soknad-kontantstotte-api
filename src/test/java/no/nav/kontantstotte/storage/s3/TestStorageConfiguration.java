package no.nav.kontantstotte.storage.s3;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;

import no.nav.kontantstotte.storage.Storage;

@Profile("dev")
@Configuration
public class TestStorageConfiguration {

    @Primary
    @Bean(name = {"s3storage", "encryptedStorage", "attachmentStorage"})
    public Storage storage() {
        Storage storage = Mockito.mock(Storage.class);

        when(storage.get(any(), any())).thenReturn(Optional.of("filinnhold".getBytes()));

        return Mockito.mock(Storage.class);
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
