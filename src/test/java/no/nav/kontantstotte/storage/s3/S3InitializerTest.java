package no.nav.kontantstotte.storage.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

public class S3InitializerTest {

    private static final int SIZE_MB = 20;

    @Rule
    public LocalStackContainer localStackContainer = new LocalStackContainer().withServices(S3);

    private AmazonS3 s3;

    @Before
    public void setUp() {
        s3 = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(S3))
                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
                .enablePathStyleAccess()
                .build();
    }

    @Test
    @DisabledIfEnvironmentVariable(named="CIRCLECI", matches="true")
    public void bucketIsCreatedwhenMissing() {
        assertThat(s3.listBuckets()).hasSize(0);

        new S3Storage(s3, SIZE_MB);
        assertThat(s3.listBuckets()).hasSize(1);

        new S3Storage(s3, SIZE_MB);
        assertThat(s3.listBuckets()).hasSize(1);

    }
}
