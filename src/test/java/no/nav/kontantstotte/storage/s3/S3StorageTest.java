package no.nav.kontantstotte.storage.s3;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.testcontainers.containers.localstack.LocalStackContainer;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

public class S3StorageTest {

    @Rule
    public LocalStackContainer localStackContainer = new LocalStackContainer().withServices(S3);

    private S3Storage storage;

    @Before
    public void setUp() {
        AmazonS3 s3 = new S3StorageConfiguration().s3(
                localStackContainer.getEndpointConfiguration(S3),
                localStackContainer.getDefaultCredentialsProvider());

        storage = new S3Storage(s3, 20);
    }

    @Test
    @DisabledIfEnvironmentVariable(named="CIRCLECI", matches="true")
    public void testStorage() {
        storage.put("dir", "file", new ByteArrayInputStream("asdfasdf".getBytes()));
        storage.put("dir", "file2", new ByteArrayInputStream("asdfasdf2".getBytes()));

        assertThat(storage.get("dir", "file").orElse(null)).isEqualTo("asdfasdf".getBytes());
        assertThat(storage.get("dir", "file2").orElse(null)).isEqualTo("asdfasdf2".getBytes());
    }

}
