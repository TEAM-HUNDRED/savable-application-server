package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.dto.AiVerificationFlaskResponse;
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
    private final String flaskImageCaptioningApiUrl = "http://localhost:5001/image-captioning";
    private final String flaskOcrApiUrl = "http://localhost:5001/ocr";


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
                    flaskImageCaptioningApiUrl, entity, AiVerificationFlaskResponse.class);

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
                    flaskOcrApiUrl, entity, AiVerificationFlaskResponse.class);

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
