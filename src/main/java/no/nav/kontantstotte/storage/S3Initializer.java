package no.nav.kontantstotte.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;

class S3Initializer {

    private final AmazonS3 s3;

    S3Initializer(AmazonS3 s3) {
        this.s3 = s3;
    }

    void initializeBucket(String bucketName) {

        if(s3.listBuckets().stream().noneMatch(bucket -> bucket.getName().equals(bucketName))) {
            createBucket(bucketName);
        }

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
        s3.createBucket(new CreateBucketRequest(bucketName).withCannedAcl(CannedAccessControlList.Private));
    }

}
