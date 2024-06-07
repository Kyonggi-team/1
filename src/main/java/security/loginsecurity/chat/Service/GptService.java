package security.loginsecurity.chat.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import security.loginsecurity.chat.entity.ChatMessage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GptService {

    private final RestTemplate restTemplate;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    @Autowired
    public GptService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getGptResponse(List<ChatMessage> messages, String role) {
        List<GptMessage> gptMessages = messages.stream()
                .map(msg -> new GptMessage("user", msg.getMessage()))
                .collect(Collectors.toList());

        if (role != null && !role.isEmpty()) {
            gptMessages.add(0, new GptMessage("system", role));
        }

        GptRequest request = new GptRequest(gptMessages);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<GptRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<GptResponse> response = restTemplate.exchange(
                apiUrl + "/v1/chat/completions",
                HttpMethod.POST,
                entity,
                GptResponse.class
        );

        return response.getBody().getChoices().get(0).getMessage().getContent();
    }

    public String summarizeMessages(List<ChatMessage> messages) {
        String conversation = messages.stream()
                .map(ChatMessage::getMessage)
                .collect(Collectors.joining("\n"));

        String summaryRequest = "Summarize the following conversation content in the form of a diary in Korean. And don't write down the year, month, and day when you wrote the diary.:\n" + conversation;

        GptMessage summaryMessage = new GptMessage("user", summaryRequest);
        GptRequest request = new GptRequest(Arrays.asList(summaryMessage));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<GptRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<GptResponse> response = restTemplate.exchange(
                apiUrl + "/v1/chat/completions", // Ensure this is the correct endpoint for your request
                HttpMethod.POST,
                entity,
                GptResponse.class
        );

        return response.getBody().getChoices().get(0).getMessage().getContent();
    }
}

class GptRequest {
    private String model = "gpt-4-turbo";
    private List<GptMessage> messages;

    public GptRequest(List<GptMessage> messages) {
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<GptMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<GptMessage> messages) {
        this.messages = messages;
    }
}

class GptMessage {
    private String role;
    private String content;

    public GptMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

class GptResponse {
    private List<Choice> choices;

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    static class Choice {
        private Message message;

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }
    }

    static class Message {
        private String role;
        private String content;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
