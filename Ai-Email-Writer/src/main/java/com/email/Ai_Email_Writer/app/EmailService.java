package com.email.Ai_Email_Writer.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final WebClient.Builder webClientBuilder;

    @Value("${gemini.api.key}")
    private String geminiApiKEY;

    @Value("${gemini.api.url}")
    private String geminiApiURL;

    public String GenerateReply(Email requestEmail) {
        String prompt = buildPrompt(requestEmail);

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        String response = webClientBuilder.build()
                .post()
                .uri(geminiApiURL + "?key=" + geminiApiKEY)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();


        return extractTheResponse(response);
    }

    private String extractTheResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            return "Error parsing response: " + e.getMessage();
        }
    }

    private String buildPrompt(Email requestEmail) {
        StringBuilder prompt = new StringBuilder("Generate The Email Reply in a very professional way for the following Email (without subject line). ");
        if (requestEmail.getTone() != null && !requestEmail.getTone().isEmpty()) {
            prompt.append("Use a ").append(requestEmail.getTone()).append(" tone. ");
        }
        prompt.append("\nOriginal Email:\n").append(requestEmail.getEmailContent());
        return prompt.toString();
    }
}
