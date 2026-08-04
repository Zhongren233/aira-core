package moe.aira.api.hekk;

import com.fasterxml.jackson.databind.ObjectMapper;
import moe.aira.core.client.hekk.QuizClient;
import moe.aira.core.entity.dto.QuizResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class QuizTest {
    @Autowired
    QuizClient quizClient;

    @Test
    void test() throws Exception {
        HashMap<String, QuizResponse.QuestionPayload> map = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            QuizResponse play = quizClient.play(15);
            List<QuizResponse.QuestionEntry> questions = play.getQuestions();
            for (QuizResponse.QuestionEntry question : questions) {
                String description = question.payload.getDescription();
                System.out.println(description);
                map.put(description, question.payload);
            }
            Thread.sleep(10000);
        }
        System.out.println(map.size());
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(map);
        File file = new File("./quiz-15-n.jsona");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(s);
        fileWriter.close();

    }
}
