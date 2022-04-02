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
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IStoryLineServiceImplTest {
    @Autowired
    IStoryLineService service;

    @Test
    void name() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("D:\\story\\main");
        ArrayList<StoryLine> storyLines = new ArrayList<>();
        Arrays.stream(Objects.requireNonNull(file.listFiles())).parallel().forEach(listFile -> {
            log.info("读取:{}", listFile.getName());
            try {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        int i = storyLines.size() / 500;
        AtomicInteger atomicInteger = new AtomicInteger(i);
        ArrayList<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        for (int j = 0; j <= i; j++) {
            int finalJ = j;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                log.info("开始插入第{}页", finalJ);
                service.saveBatch(storyLines.subList(finalJ * 500, Math.min((finalJ + 1) * 500, storyLines.size())));
                log.info("剩余{}页", atomicInteger.getAndDecrement());
            });
            completableFutures.add(future);
        }
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();
    }
}