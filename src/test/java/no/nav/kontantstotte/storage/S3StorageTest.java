package no.nav.kontantstotte.storage;

import com.amazonaws.services.s3.AmazonS3;
import no.finn.unleash.FakeUnleash;
import no.finn.unleash.Unleash;
import no.nav.kontantstotte.config.toggle.UnleashProvider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;

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
        storage.put("dir", "file", "asdfasdf");
        storage.put("dir", "file2", "asdfasdf2");

        assertThat(storage.get("dir", "file").orElse(null)).isEqualTo("asdfasdf");
        assertThat(storage.get("dir", "file2").orElse(null)).isEqualTo("asdfasdf2");
    }

}
