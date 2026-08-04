package moe.aira.config;


import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.completions.CompletionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {
    @Value("${openai.apiKey}")
    private String apiKey;

    @Value("${openai.baseUrl}")
    private String baseUrl;
    @Value("${openai.model}")
    private String model;


    @Bean
    public OpenAIClient openAIClient() {
        return OpenAIOkHttpClient.builder().apiKey(apiKey).baseUrl(baseUrl).build();
    }
}
