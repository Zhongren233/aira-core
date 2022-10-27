package moe.aira.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.client.es.PointRankingClient;
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
        for (int i = 21000229; i <= 21000526; i++) {
//            FileWriter fileWriter = new FileWriter("D:/story/SS/" + i + ".json");
            JsonNode read = storyClient.read(i + "", "1");
            System.out.println(read);
//            fileWriter.write(read.toString());
//            fileWriter.close();
            Thread.sleep(5000);

        }
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

    @Test
    void name3() throws Exception {
        File file = new File("./story");
        file.mkdir();
        JsonNode node = storyClient.es1StoryChapterList();
        JsonNode userChapters = node.get("user_chapters");
        for (JsonNode userChapter : userChapters) {
            JsonNode released = userChapter.get("only_limited_released");
            if (released.asBoolean(false)) {
                int id1 = userChapter.get("id").asInt();
                new File("./story/" + id1).mkdir();
                JsonNode chapterStories = storyClient.es1StoryChapterStories(id1 + "");
                for (JsonNode jsonNode : chapterStories.get("stories")) {
                    JsonNode id = jsonNode.get("id");
                    JsonNode storyRead = storyClient.es1StoryRead(id.asInt() + "");
                    try (FileWriter fileWriter = new FileWriter("./story/" + id1 + "/" + id + ".json");
                    ) {
                        fileWriter.write(storyRead.toString());
                    }
                }
            }
        }
    }

    @Test
    void name4() throws Exception {

        String pathname = "D:/story/event/" + "スカウト！ロマンチック？デイト";
        File file = new File(pathname);
        if (file.mkdir()) {
            log.info("创建目录成功:{}", "スカウト！ロマンチック？デイト");
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = storyClient.campaignStoryList("22100046");
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

    @Autowired
    PointRankingClient pointRankingClient;

    @Test
    void test() {
        System.out.println(pointRankingClient.page(1));
    }
}
