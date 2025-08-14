package com.AfsarDev.AfsarHotel.service;

import com.AfsarDev.AfsarHotel.exception.OurException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class AwsS3Service {

    @Value("${storage.bucket.name}")
    private String bucketName;

    @Value("${storage.access.key}")
    private String accessKey;

    @Value("${storage.secret.key}")
    private String secretKey;

    @Value("${storage.endpoint:}") // API endpoint for R2
    private String endpointUrl;

    @Value("${storage.region:auto}")
    private String region;

    public String saveImageToS3(MultipartFile photo) {
        try {
            String fileName = photo.getOriginalFilename();

            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

            AmazonS3 s3Client;

            if (endpointUrl != null && !endpointUrl.isEmpty()) {
                // Cloudflare R2
                s3Client = AmazonS3ClientBuilder.standard()
                        .withEndpointConfiguration(
                                new AwsClientBuilder.EndpointConfiguration(endpointUrl, "auto")
                        )
                        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                        .withPathStyleAccessEnabled(true)
                        .build();
            } else {
                // AWS S3
                s3Client = AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                        .withRegion(region)
                        .build();
            }

            InputStream inputStream = photo.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(photo.getContentType());
            metadata.setContentLength(photo.getSize());

            s3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));

            // ✅ Return correct public URL
            if (endpointUrl != null && !endpointUrl.isEmpty()) {
                return String.format("https://%s.r2.dev/%s", bucketName, fileName); // Public R2 URL
            } else {
                return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new OurException("Unable to upload image: " + e.getMessage());
        }
    }
    }
