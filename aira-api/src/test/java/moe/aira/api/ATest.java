package moe.aira.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.client.es.StoryClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@SpringBootTest
public class ATest {
    @Autowired
    StoryClient storyClient;

    @Test
    void name() throws InterruptedException, IOException {
        String id = "21000";
        JsonNode read = storyClient.read("21000001", "0");
        System.out.println(read);
    }

    @Test
    void bTest() throws InterruptedException, IOException {
        JsonNode chapters = storyClient.campaignChapters().get("chapters");
        ObjectMapper mapper = new ObjectMapper();
        for (JsonNode chapter : chapters) {
            String name = chapter.get("name").asText();
            String chapterId = chapter.get("id").asText();
            log.info("开始读取:{},id:{}", name, chapterId);
            String pathname = "D:/story/event/" + name;
            File file = new File(pathname);
            if (file.mkdir()) {
                log.info("创建目录成功:{}", name);
            }
            JsonNode node = storyClient.campaignStoryList(chapterId);
            List<String> storyIds = node.get("user_stories").findValuesAsText("story_id");
            for (String storyId : storyIds) {
                ObjectNode objectNode = mapper.createObjectNode();
                log.info("开始读取storyId: {}", storyId);
                JsonNode r = storyClient.read(storyId, "0");
                Thread.sleep(1000);
                JsonNode x = storyClient.storyInfo(storyId);
                Thread.sleep(1000);
                objectNode.set("paragraphs", r.get("paragraphs"));
                objectNode.set("story_writer_id", x.get("story_writer_id"));
                objectNode.set("actorIds", x.get("actorIds"));
                new FileWriter(pathname + "/" + storyId + ".json", StandardCharsets.UTF_8).append(objectNode.toString()).close();
                log.info("结束读取storyId: {}", storyId);
            }
        }
    }

    @Test
    void name2() {
        System.out.println(storyClient.searchCondition());
    }
}
