package com.email.Ai_Email_Writer.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailService {
    private final WebClient webClient;
    @Value("${gemini.api.key}")
    private String geminiApiKEY;
    @Value("${gemini.api.url}")
    private String geminiApiURL;

    public EmailService(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    public String GenerateReply(Email RequestEmail)
    {
        //Building The Prompt ----
        String Prompt= buildPrompt(RequestEmail);
        //Crafting the Prompt
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of(
                                "parts", new Object[] {
                                        Map.of("text", Prompt)
                                }
                        )
                }
        );
        //Crafting The Response using WebClient

        String response=webClient.post()
                .uri(geminiApiURL+geminiApiKEY)
                .header("Content-Type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

          //Extracting The Response from The curl-----

            return ExtractTheResponse(response);
    }

    private String ExtractTheResponse(String response) {
        try
        {
            ObjectMapper objectMapper=new ObjectMapper();
            JsonNode rootNode=objectMapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        }
        catch(Exception e)
        {
            return (e.getMessage());
        }
    }

    private String buildPrompt(Email requestEmail) {
        StringBuilder Prompt=new StringBuilder();
         Prompt.append("Generate The Email Replay in very professional way for The following Email Without SubjectLine ");
        if (requestEmail.getTone()!=null&&!requestEmail.getTone().isEmpty())
        {
            Prompt.append("Use a ").append(requestEmail.getTone()).append(" tone.");
        }
        Prompt.append("\nOriginal Email:\n").append(requestEmail.getEmailContent());

        return Prompt.toString();
    }

}
