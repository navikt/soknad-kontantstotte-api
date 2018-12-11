package no.nav.kontantstotte.storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {

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

        return new AwsClientBuilder.EndpointConfiguration(endpoint, region);
    }

    @Bean
    public AWSCredentialsProvider credentialsProvider(
            @Value("${SOKNAD_KONTANTSTOTTE_API_S3_CREDENTIAL_USERNAME}") String accessKey,
            @Value("${SOKNAD_KONTANTSTOTTE_API_S3_CREDENTIAL_PASSWORD}") String secretKey) {

        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        return new AWSStaticCredentialsProvider(awsCredentials);
    }


    @Bean
    public Storage storage(AmazonS3 s3) {
        return new S3Storage(s3);
    }

}
