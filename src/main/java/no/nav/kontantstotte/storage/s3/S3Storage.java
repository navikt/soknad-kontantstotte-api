package no.nav.kontantstotte.storage.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import no.nav.kontantstotte.storage.Storage;
import no.nav.kontantstotte.storage.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static no.nav.kontantstotte.config.toggle.FeatureToggleConfig.KONTANTSTOTTE_VEDLEGG;
import static no.nav.kontantstotte.config.toggle.UnleashProvider.toggle;

public class S3Storage implements Storage {

    private static final Logger log = LoggerFactory.getLogger(S3Storage.class);

    private static final String VEDLEGG_BUCKET = "kontantstottevedlegg";
    private final int maxFileSizeAfterEncryption;
    private final static double ENCRYPTION_SIZE_FACTOR = 1.5;

    private final AmazonS3 s3;

    private final Counter feilMotS3Put = Metrics.counter("soknad.kontantstotte.S3.feil", "operasjon", "put");
    private final Counter feilMotS3Get = Metrics.counter("soknad.kontantstotte.S3.feil", "operasjon", "get");
    private final Timer S3PutResponstid = Metrics.timer("S3.respons.tid", "operasjon", "put");
    private final Timer S3GetResponstid = Metrics.timer("S3.respons.tid", "operasjon", "get");

    S3Storage(AmazonS3 s3, int maxFileSizeMB) {
        this.s3 = s3;

        new S3Initializer(s3).initializeBucket(VEDLEGG_BUCKET);
        maxFileSizeAfterEncryption = (int) (maxFileSizeMB * 1000 * 1000 * ENCRYPTION_SIZE_FACTOR);
        log.debug("S3 Storage initialized");
    }

    @Override
    public void put(String directory, String key, InputStream data) {

        toggle(KONTANTSTOTTE_VEDLEGG).throwIfDisabled(
                () -> new StorageException("Vedleggsfunksjonalitet er deaktivert"));

        PutObjectRequest request = new PutObjectRequest(VEDLEGG_BUCKET, fileName(directory, key), data, new ObjectMetadata());
        request.getRequestClientOptions().setReadLimit(maxFileSizeAfterEncryption);

        try {
            long startTime = System.nanoTime();
            PutObjectResult result = s3.putObject(request);
            S3PutResponstid.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
            log.debug("Stored file with size {}", result.getMetadata().getContentLength());
        } catch (SdkClientException e) {
            feilMotS3Put.increment();
        }
    }

    @Override
    public Optional<byte[]> get(String directory, String key) {

        toggle(KONTANTSTOTTE_VEDLEGG).throwIfDisabled(
                () -> new StorageException("Vedleggsfunksjonalitet er deaktivert"));

        return Optional.ofNullable(readString(fileName(directory, key)));
    }

    private byte[] readString(String filename) {

        try(InputStream is = fileContent(filename)) {

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int readBytes;
            byte[] data = new byte[4096];

            while ((readBytes = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, readBytes);
            }

            return buffer.toByteArray();
        } catch (IOException e) {
            throw new StorageException("Unable parse " + filename, e);
        }

    }

    private InputStream fileContent(String filename) {

        try {
            long startTime = System.nanoTime();
            S3Object object = s3.getObject(VEDLEGG_BUCKET, filename);
            S3GetResponstid.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
            log.debug("Loading file with size {}", object.getObjectMetadata().getContentLength());
            return object.getObjectContent();
        } catch (SdkClientException e) {
            feilMotS3Get.increment();
            throw new StorageException("Unable to retrieve " + filename + ", it probably doesn't exist", e);
        }

    }

    private String fileName(String directory, String key) {
        return directory + "_" + key;
    }

}
