package no.nav.kontantstotte.storage.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class S3Initializer {

    private static final Logger log = LoggerFactory.getLogger(S3Initializer.class);

    private final AmazonS3 s3;

    S3Initializer(AmazonS3 s3) {
        this.s3 = s3;
    }

    void initializeBucket(String bucketName) {

        if (s3.listBuckets().stream().noneMatch(bucket -> bucket.getName().equals(bucketName))) {
            createBucket(bucketName);
        }

        log.info("Initializing bucket {}", bucketName);
        s3.setBucketLifecycleConfiguration(bucketName,
                new BucketLifecycleConfiguration().withRules(
                        new BucketLifecycleConfiguration.Rule()
                                .withId("soknad-retention-policy-1")
                                .withFilter(new LifecycleFilter())
                                .withStatus(BucketLifecycleConfiguration.ENABLED)
                                .withExpirationInDays(1)
                ));

    }

    private void createBucket(String bucketName) {
        log.info("Bucket {} doesn't exist. Creating", bucketName);
        s3.createBucket(new CreateBucketRequest(bucketName).withCannedAcl(CannedAccessControlList.Private));
    }

}
