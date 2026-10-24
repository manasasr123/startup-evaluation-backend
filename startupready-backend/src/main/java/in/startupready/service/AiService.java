
package in.startupready.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AiService {

    private final RestClient client;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;

    public AiService(
            ObjectMapper objectMapper,
            @Value("${app.ai.groq.api-key:}") String apiKey,
            @Value("${app.ai.groq.model:openai/gpt-oss-20b}") String model,
            @Value("${app.ai.groq.url:https://api.groq.com/openai/v1/chat/completions}") String url) {

        this.objectMapper = objectMapper;
        this.apiKey = apiKey == null ? "" : apiKey.trim();
        this.model = model;

        this.client = RestClient.builder()
                .baseUrl(url)
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public boolean isEnabled() {
        return !apiKey.isBlank();
    }

    public Optional<String> generate(
            String instruction,
            String context) {

        if (!isEnabled()) {
            System.err.println("Groq API key is missing.");
            return Optional.empty();
        }

        try {
            String prompt = instruction
                    + "\n\nAssessment information:\n"
                    + context;

            Map<String, Object> request = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of(
                                    "role", "system",
                                    "content", "You are StartupReady AI, an assistant that gives clear, practical and professional startup assessment advice."
                            ),
                            Map.of(
                                    "role", "user",
                                    "content", prompt
                            )
                    ),
                    "temperature", 0.3,
                    "max_tokens", 1200
            );

            String responseBody = client.post()
                    .uri("")
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            "Bearer " + apiKey)
                    .body(request)
                    .retrieve()
                    .body(String.class);

            if (responseBody == null || responseBody.isBlank()) {
                return Optional.empty();
            }

            JsonNode response = objectMapper.readTree(responseBody);
            JsonNode content = response.path("choices")
                    .path(0)
                    .path("message")
                    .path("content");

            if (content.isMissingNode()
                    || content.isNull()
                    || content.asText().isBlank()) {
                System.err.println(
                        "Groq returned no message content.");
                return Optional.empty();
            }

            return Optional.of(content.asText().trim());

        } catch (RestClientResponseException exception) {
            System.err.println("Groq HTTP status: "
                    + exception.getStatusCode());
            System.err.println("Groq error response: "
                    + exception.getResponseBodyAsString());
            return Optional.empty();

        } catch (Exception exception) {
            System.err.println("Groq request failed: "
                    + exception.getClass().getSimpleName()
                    + ": " + exception.getMessage());
            return Optional.empty();
        }
    }
}
