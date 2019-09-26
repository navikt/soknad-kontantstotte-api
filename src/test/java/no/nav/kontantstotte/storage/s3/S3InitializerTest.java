package no.nav.kontantstotte.storage.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Testcontainers
@DisabledIfEnvironmentVariable(named="CIRCLECI", matches="true")
public class S3InitializerTest {

    private static final int SIZE_MB = 20;

    @Container
    public LocalStackContainer localStackContainer = new LocalStackContainer().withServices(S3);

    private AmazonS3 s3;

    @BeforeEach
    public void setUp() {
        s3 = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(S3))
                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
                .enablePathStyleAccess()
                .build();
    }

    @Test
    public void bucketIsCreatedwhenMissing() {
        assertThat(s3.listBuckets()).hasSize(0);

        new S3Storage(s3, SIZE_MB);
        assertThat(s3.listBuckets()).hasSize(1);

        new S3Storage(s3, SIZE_MB);
        assertThat(s3.listBuckets()).hasSize(1);

    }
}
