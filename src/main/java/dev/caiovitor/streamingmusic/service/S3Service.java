package dev.caiovitor.streamingmusic.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class S3Service {


    private final S3Client S3client;
    private final S3Presigner s3Presigner;

    @Value("${app.aws.s3.bucket-name}")
    private String bucketName;



    public String uploadFile(MultipartFile file, String folder)  {

        String fileName = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        try{

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

            S3client.putObject(objectRequest,
                    RequestBody.fromInputStream(file.getInputStream(),file.getSize()));

            return fileName;

        } catch (IOException ex){
            throw new RuntimeException("Error sending file to S3 ");
        }
    }

    public void deleteFile(String fileName){
        
        DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        S3client.deleteObject(objectRequest);

    }


    public URL presignedUrl(String key){

        GetObjectPresignRequest build = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(b -> b.bucket(bucketName).key(key))
                .build();


        return s3Presigner.presignGetObject(build).url();
    }
}
