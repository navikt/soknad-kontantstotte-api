package no.nav.kontantstotte.storage.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import no.nav.kontantstotte.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3StorageConfiguration {

    private static final Logger log = LoggerFactory.getLogger(S3StorageConfiguration.class);

    @Bean
    public AmazonS3 s3(
            AwsClientBuilder.EndpointConfiguration endpointConfiguration,
            AWSCredentialsProvider credentialsProvider) {

        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(credentialsProvider)
                .enablePathStyleAccess()
                .build();

    }

    @Bean
    public AwsClientBuilder.EndpointConfiguration enpointConfiguration(
            @Value("${SOKNAD_KONTANTSTOTTE_S3_ENDPOINT}") String endpoint,
            @Value("${SOKNAD_KONTANTSTOTTE_API_S3_REGION}") String region) {

        log.info("Initializing s3 endpoint configuration with enpoint {} and region {}", endpoint, region );

        return new AwsClientBuilder.EndpointConfiguration(endpoint, region);
    }

    @Bean
    public AWSCredentialsProvider credentialsProvider(
            @Value("${SOKNAD_KONTANTSTOTTE_API_S3_CREDENTIAL_USERNAME}") String accessKey,
            @Value("${SOKNAD_KONTANTSTOTTE_API_S3_CREDENTIAL_PASSWORD}") String secretKey) {

        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        return new AWSStaticCredentialsProvider(awsCredentials);
    }


    @Bean(name = "s3storage")
    public Storage storage(AmazonS3 s3) {
        return new S3Storage(s3);
    }

}
