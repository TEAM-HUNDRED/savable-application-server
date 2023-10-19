package net.app.savable.global.common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional(readOnly = false)
    public String saveFile(MultipartFile multipartFile, String fileName) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private InputStream resizeImage(InputStream originalInputStream, int width, int height) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(originalInputStream)
                .size(width, height)
                .outputFormat("jpg")
                .toOutputStream(outputStream);

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Transactional(readOnly = false)
    public String saveImageUrl(Path tempFile, String fileName) throws IOException {

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, tempFile.toFile());
        amazonS3.putObject(putObjectRequest);

        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
