package com.example.coursecreation.serviceImpl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.util.logging.Logger;

@Service
public class StorageService {

    private static final Logger logger = Logger.getLogger(StorageService.class.getName());

    @Value("${STORAGE_ACCESS_KEY}")
    private String STORAGE_ACCESS_KEY;

    @Value("${STORAGE_ACCESS_KEY_ID}")
    private String STORAGE_ACCESS_KEY_ID;

    @Value("${STORAGE_ZONE}")
    private String STORAGE_ZONE;

    @Value("${digitalocean.spaces.endpoint}")
    private String spacesEndpoint;

    @Value("${BUCKET_NAME}")
    private String BUCKET_NAME;

    private S3Presigner s3Presigner;
    private S3Client s3Client;

    @PostConstruct
    public void init() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(STORAGE_ACCESS_KEY_ID, STORAGE_ACCESS_KEY);
        s3Presigner = S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .endpointOverride(URI.create(spacesEndpoint))
                .region(Region.of(STORAGE_ZONE))
                .build();

        s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .endpointOverride(URI.create(spacesEndpoint))
                .region(Region.of(STORAGE_ZONE))
                .build();
    }

    public String generatePresignedUrl(String objectKey) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(objectKey)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(z -> z
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(putObjectRequest)
                .build());

        logger.info("Generated presigned URL: " + presignedRequest.url().toString());
        return presignedRequest.url().toString();
    }

    public URL verifyVideoUpload(String bucketName, String objectKey) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
            return new URL(spacesEndpoint + "/" + bucketName + "/" + objectKey);
        } catch (Exception e) {
            logger.severe("Error verifying video upload: " + e.getMessage());
            return null;
        }
    }

    public void uploadFileToS3(MultipartFile file, String presignedUrl) throws IOException {
        HttpURLConnection connection = null;
        String objectKey = null;
        try {
            objectKey = new URL(presignedUrl).getPath().substring(1); // Extract the object key from the presigned URL
            logger.info("Uploading file with key: " + objectKey);

            connection = (HttpURLConnection) new URL(presignedUrl).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", file.getContentType());

            try (OutputStream outputStream = connection.getOutputStream();
                 InputStream inputStream = file.getInputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            int responseCode = connection.getResponseCode();
            logger.info("Response Code: " + responseCode);
            if (responseCode != 200) {
                logger.severe("Failed to upload file to S3: " + responseCode);
                throw new IOException("Failed to upload file to S3: " + responseCode);
            }

            // Set the ACL to public-read after the upload
            setFilePublic(objectKey);

        } catch (IOException e) {
            logger.severe("IOException: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public URL getFileUrl(String fileName) {
        try {
            return s3Client.utilities().getUrl(GetUrlRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(fileName)
                    .build());
        } catch (Exception e) {
            logger.severe("Error getting file URL: " + e.getMessage());
            throw new RuntimeException("Failed to get file URL", e);
        }
    }

    private void setFilePublic(String objectKey) {
        try {
            logger.info("Setting file public with key: " + objectKey);
            s3Client.putObjectAcl(PutObjectAclRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(objectKey)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build());
            logger.info("Set file public: " + objectKey);
        } catch (Exception e) {
            logger.severe("Error setting file public: " + e.getMessage());
            throw new RuntimeException("Failed to set file public", e);
        }
    }
}
