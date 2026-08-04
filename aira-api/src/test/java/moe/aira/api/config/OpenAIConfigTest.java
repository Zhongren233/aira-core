package moe.aira.api.config;

import com.openai.client.OpenAIClient;
import com.openai.core.JsonValue;
import com.openai.core.http.StreamResponse;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionChunk;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatCompletionUserMessageParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
class OpenAIConfigTest {
    @Autowired
    OpenAIClient openAIClient;

    @Test
    void test() throws Exception {
        String main = Files.readString(Path.of("C:\\Users\\Wang\\Downloads\\ギャンビット.txt"));
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .maxCompletionTokens(8000)
                .addMessage(ChatCompletionUserMessageParam.builder().role(JsonValue.from("system")).content("请你撰写一套画师的约稿评价").build())
//                .addMessage(ChatCompletionUserMessageParam.builder().role(JsonValue.from("user")).content(main).build())
                .model("deepseek-chat")
                .build();

        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        System.out.println(chatCompletion.choices().get(0).message().content());
        try (StreamResponse<ChatCompletionChunk> streaming = openAIClient.chat().completions().createStreaming(params)) {
            streaming.stream()
                    .flatMap(completion -> completion.choices().stream())
                    .flatMap(choice -> choice.delta().content().stream())
                    .forEach(System.out::print);
        }
    }
}