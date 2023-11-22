package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.dto.AiVerificationFlaskResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncService {

    private final RestTemplate restTemplate;

    @Value("${flask.api.port}")
    private Long flaskApiPort;

    private String getFlaskImageCaptioningApiUrl() {
        return "http://localhost:" + flaskApiPort + "/image-captioning";
    }

    private String getFlaskOcrApiUrl() {
        return "http://localhost:" + flaskApiPort + "/ocr";
    }

    @Async
    public CompletableFuture<Boolean> callFlaskImageCaptioningApiAsync(String imageUrl, String prompt) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("image", imageUrl);
        requestBody.put("prompt", prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<AiVerificationFlaskResponse> response = restTemplate.postForEntity(
                    getFlaskImageCaptioningApiUrl(), entity, AiVerificationFlaskResponse.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return CompletableFuture.completedFuture(response.getBody().isData());
            } else {
                return CompletableFuture.completedFuture(false);
            }
        } catch (Exception e) {
            log.error("Error calling Flask API", e);
            return CompletableFuture.completedFuture(false);
        }
    }

    @Async
    public CompletableFuture<Boolean> callFlaskOcrApiAsync(String imageUrl, String prompt) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("image", imageUrl);
        requestBody.put("prompt", prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<AiVerificationFlaskResponse> response = restTemplate.postForEntity(
                    getFlaskOcrApiUrl(), entity, AiVerificationFlaskResponse.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return CompletableFuture.completedFuture(response.getBody().isData());
            } else {
                return CompletableFuture.completedFuture(false);
            }
        } catch (Exception e) {
            log.error("Error calling Flask API", e);
            return CompletableFuture.completedFuture(false);
        }
    }
}
