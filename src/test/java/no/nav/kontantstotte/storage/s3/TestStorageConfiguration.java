package no.nav.kontantstotte.storage.s3;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import no.nav.kontantstotte.storage.Storage;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestStorageConfiguration {

    @Bean(name = {"s3storage", "encryptedStorage", "attachmentStorage"})
    @Primary
    public Storage storage() {
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
