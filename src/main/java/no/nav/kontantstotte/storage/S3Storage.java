package no.nav.kontantstotte.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_VEDLEGG;
import static no.nav.kontantstotte.config.toggle.UnleashProvider.toggle;

public class S3Storage implements Storage {

    private static final Logger log = LoggerFactory.getLogger(S3Storage.class);

    private static final String VEDLEGG_BUCKET = "kontantstottevedlegg";

    private final AmazonS3 s3;

    S3Storage(AmazonS3 s3) {
        this.s3 = s3;

        new S3Initializer(s3).initializeBucket(VEDLEGG_BUCKET);

        log.debug("S3 Storage initialized");
    }

    @Override
    public void put(String directory, String key, String value) {

        toggle(KONTANTSTOTTE_VEDLEGG).throwIfDisabled(
                () -> new IllegalStateException("Vedleggsfunksjonalitet er deaktivert"));

        s3.putObject(VEDLEGG_BUCKET, fileName(directory, key), value);
    }

    @Override
    public Optional<String> get(String directory, String key) {

        toggle(KONTANTSTOTTE_VEDLEGG).throwIfDisabled(
                () -> new IllegalStateException("Vedleggsfunksjonalitet er deaktivert"));

        return Optional.ofNullable(readString(fileName(directory, key)));
    }

    private String readString(String filename) {
        try {
            S3Object object = s3.getObject(VEDLEGG_BUCKET, filename);
            return new BufferedReader(new InputStreamReader(object.getObjectContent()))
                    .lines()
                    .collect(joining("\n"));
        } catch (AmazonS3Exception ex) {
            log.info("Unable to retrieve " + filename + ", it probably doesn't exist");
            return null;
        }

    }

    private String fileName(String directory, String key) {
        return directory + "_" + key;
    }

}
