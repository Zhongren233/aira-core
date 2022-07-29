package moe.aira.onebot.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import moe.aira.entity.aira.StoryLine;
import moe.aira.onebot.service.IStoryLineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IStoryLineServiceImplTest {
    @Autowired
    IStoryLineService service;

    @Test
    void name() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("D:\\story\\main");
        for (File listFile : Objects.requireNonNull(file.listFiles())) {
            log.info("读取:{}", listFile.getName());
            try {
                ArrayList<StoryLine> storyLines = new ArrayList<>();
                JsonNode node = mapper.readTree(listFile);
                Integer storyId = Integer.valueOf(listFile.getName().split("\\.")[0]);
                JsonNode paragraphs = node.get("paragraphs");
                int i = 1;
                for (JsonNode paragraph : paragraphs) {
                    StoryLine storyLine = new StoryLine();
                    storyLine.setLineId(i);
                    storyLine.setStoryId(storyId);
                    storyLine.setMessage(paragraph.get("message").asText());
                    JsonNode node1 = paragraph.get("speaker");
                    if (node1 != null) {
                        storyLine.setSpeaker(node1.asText());
                    }
                    JsonNode node2 = paragraph.get("voice");
                    if (node2 != null) {
                        storyLine.setVoice(node2.asText());
                    }
                    i++;
                    storyLines.add(storyLine);
                }
                service.saveBatch(storyLines);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}