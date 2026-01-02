package com.cloud.project.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

    public void uploadFile(String key, InputStream inputStream, long contentLength) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(inputStream, contentLength)
            );
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to S3", e);
        }
    }

    public String generatePresignedUrl(String key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName)
                    .key(key)
                    .build())
                    .toString();
        }catch (S3Exception e) {
            throw new RuntimeException("Error generating pre-signed URL", e);
        }
    }
}
