package moe.aira.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import moe.aira.core.client.es.StoryClient;
import moe.aira.core.client.hekk.ESMStoryClient;
import moe.aira.core.service.IStoryLineService;
import moe.aira.entity.aira.StoryLine;
import moe.aira.entity.hekk.Chapter;
import moe.aira.entity.hekk.Story;
import moe.aira.entity.hekk.UserStory;
import moe.aira.resp.hekk.CampaignChapterStoriesResponse;
import moe.aira.resp.hekk.CampaignChaptersResponse;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@SpringBootTest
public class StoryLineTest {
    @Autowired
    private IStoryLineService storyLineService;

    @Autowired
    ESMStoryClient esmStoryClient;
    @Autowired
    StoryClient storyClient;
    private List<Chapter> cnChapters;
    ObjectMapper objectMapper = new ObjectMapper();
    ExecutorService executorService = Executors.newFixedThreadPool(1);
    ExecutorService dbService = Executors.newFixedThreadPool(10);

    long[] longs = new long[]{
            22000005,
            22000015,
            22000021,
            22000030,
            22000070,
            22000074,
            22000075,
            22000076,
            22000077,
            22000079,
            22000080,
            22000081,
            22000082,
            22000083,
            22000084,
            22000086,
            22000087,
            22000088,
            22000093,
            22000094,
            22000095,
            22000096,
            22000097,
            22000098,
            22000111,
            22000112,
            22000117,
            22000118,
            22000119,
            22000121,
            22000123,
            22000125,
            22100038,
            22100043,
            22100044,
            22100067,
            22100068,
            22100070,
            22100076,
            22100079,
            22100080,
            22100082,
            22100083,
            22100091,
            22100092,
            22100093,
            22100094


    };

    List<Long> collect;

    @Test
    void test() throws JsonProcessingException, InterruptedException {
        collect = Arrays.stream(longs).boxed().collect(Collectors.toList());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        CampaignChaptersResponse campaignChaptersResponse = esmStoryClient.campaignChapters();
        JsonNode jsonNode = storyClient.campaignChapters();
//        System.out.println(jsonNode);
        JsonNode jsonNode1 = jsonNode.get("chapters");
        cnChapters =
                objectMapper.treeToValue(jsonNode1, new TypeReference<>() {
                });
        int size = campaignChaptersResponse.getChapters().size();
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (Long l : collect) {
            try {
                parseChapter(l, objectMapper);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        countDownLatch.await();

    }

    private void parseChapter(Long c_id, ObjectMapper objectMapper) throws JsonProcessingException, InterruptedException {
        CampaignChapterStoriesResponse x1 = esmStoryClient.campaignChapterStories(c_id);
        CampaignChapterStoriesResponse cn = null;
        boolean cnContains = cnChapters.stream().anyMatch(cnC -> Objects.equals(cnC.id, c_id));
        if (cnContains) {
            JsonNode jsonNode = storyClient.campaignStoryList(c_id.toString());
            cn = objectMapper.treeToValue(jsonNode, CampaignChapterStoriesResponse.class);
        }
        for (Story a : x1.getStories()) {
            Long storyId = a.getId();

            if (cn == null) {
                continue;
            }
            List<UserStory> cnUserStories = cn.getUserStories();
            boolean anyMatch = cnUserStories.stream().anyMatch(userStory -> Objects.equals(userStory.getStoryId(), storyId));
            if (anyMatch || collect.contains(c_id)) {

                QueryWrapper<StoryLine> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("story_id", storyId);
                queryWrapper.eq("lang", "zh-cn");
                long count = storyLineService.count(queryWrapper);
                if (count <= 0) {
                    JsonNode read1;
                    try {
                        read1 = storyClient.read(String.valueOf(storyId), "0");
                        Thread.sleep(1000);

                    } catch (Exception e) {
                        System.out.println("剧情" + storyId + "错误");
                        return;
                    }

                    JsonNode paragraphs = read1.get("paragraphs");
                    List<Story.Paragraph> paragraphs1 = objectMapper.treeToValue(paragraphs, new TypeReference<>() {

                    });

                    ArrayList<StoryLine> entityList = getStoryLines(storyId, paragraphs1, "zh-CN");

                    saveAsync(entityList);
                }
            }


        }
    }


    private void saveAsync(ArrayList<StoryLine> entityList) {
        dbService.submit(() -> {
            storyLineService.saveBatch(entityList);
        });
    }

    private static @NonNull ArrayList<StoryLine> getStoryLines(Long storyId, List<Story.Paragraph> paragraphList, String lang) {

        int line = 1;
        ArrayList<StoryLine> entityList = new ArrayList<>();
        for (Story.Paragraph paragraph : paragraphList) {
            StoryLine storyLine = new StoryLine();
            storyLine.setStoryId(Math.toIntExact(storyId));
            storyLine.setMessage(paragraph.getMessage());
            storyLine.setLineId(line++);
            storyLine.setSpeaker(paragraph.getSpeaker());
            storyLine.setVoice(paragraph.getVoice());
            storyLine.setLang(lang);
            entityList.add(storyLine);
        }
        return entityList;
    }
}
