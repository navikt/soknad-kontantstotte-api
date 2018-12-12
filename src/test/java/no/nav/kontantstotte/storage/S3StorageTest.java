package no.nav.kontantstotte.storage;

import com.amazonaws.services.s3.AmazonS3;
import no.finn.unleash.FakeUnleash;
import no.nav.kontantstotte.config.toggle.UnleashProvider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;

import java.io.ByteArrayInputStream;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_VEDLEGG;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

public class S3StorageTest {

    @Rule
    public LocalStackContainer localStackContainer = new LocalStackContainer().withServices(S3);

    private S3Storage storage;

    @Before
    public void setUp() {
        AmazonS3 s3 = new StorageConfiguration().s3(
                localStackContainer.getEndpointConfiguration(S3),
                localStackContainer.getDefaultCredentialsProvider());

        storage = new S3Storage(s3);
    }

    @Test
    public void testStorage() {
        FakeUnleash fakeUnleash = new FakeUnleash();
        fakeUnleash.enable(KONTANTSTOTTE_VEDLEGG);
        UnleashProvider.initialize(fakeUnleash);
        storage.put("dir", "file", new ByteArrayInputStream("asdfasdf".getBytes()));
        storage.put("dir", "file2", new ByteArrayInputStream("asdfasdf2".getBytes()));

        assertThat(storage.get("dir", "file").orElse(null)).isEqualTo("asdfasdf".getBytes());
        assertThat(storage.get("dir", "file2").orElse(null)).isEqualTo("asdfasdf2".getBytes());
    }

}
