package net.app.savable.global.common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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

    private final int width = 720;
    private final int height = 720;

    public String saveFile(MultipartFile multipartFile, String fileName) throws IOException {
        try (InputStream resizedInputStream = resize(multipartFile, width, height)) {

            ObjectMetadata metadata = new ObjectMetadata();
            byte[] resizedImageBytes = ((ByteArrayInputStream) resizedInputStream).readAllBytes();
            metadata.setContentLength(resizedImageBytes.length);
            metadata.setContentType(multipartFile.getContentType());

            try {
                amazonS3.putObject(bucket, fileName, new ByteArrayInputStream(resizedImageBytes), metadata);
            } catch (AmazonS3Exception e) {
                // S3 업로드 중에 예외 처리
                throw new RuntimeException("Error uploading file to S3", e);
            }

            return amazonS3.getUrl(bucket, fileName).toString();
        }
    }

    public InputStream resize(MultipartFile multipartFile, int width, int height) throws IOException {
        try (InputStream originalInputStream = multipartFile.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Thumbnails.of(originalInputStream)
                    .size(width, height)
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);

            byte[] resizedImageBytes = outputStream.toByteArray();
            return new ByteArrayInputStream(resizedImageBytes);
        }
    }

    public String saveImageUrl(Path tempFile, String fileName) throws IOException {

        // 이미지 리사이징
        BufferedImage bufferedImage = Thumbnails.of(tempFile.toFile())
                .size(width, height)
                .asBufferedImage();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        InputStream is = new ByteArrayInputStream(outputStream.toByteArray());

        // S3에 업로드
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(outputStream.size());
        metadata.setContentType("image/jpeg");

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, is, metadata);
        amazonS3.putObject(putObjectRequest);

        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
