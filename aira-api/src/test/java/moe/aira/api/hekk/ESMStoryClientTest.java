package moe.aira.api.hekk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import moe.aira.core.client.es.StoryClient;
import moe.aira.core.client.hekk.ESMStoryClient;
import moe.aira.entity.hekk.Story;
import moe.aira.entity.hekk.UserStory;
import moe.aira.enums.AppStatusCode;
import moe.aira.resp.hekk.CampaignChapterStoriesResponse;
import moe.aira.resp.hekk.CampaignChaptersResponse;
import moe.aira.resp.hekk.MainStoryChapterStoriesResponse;
import moe.aira.resp.hekk.StoryReadResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ESMStoryClientTest {
    @Autowired
    ESMStoryClient client;

//    @Test
//    void testRead() {
//        CampaignChapterStoriesResponse x = client.campaignChapterStories(22000111L);
//        if (x.getAppStatusCode() == AppStatusCode.OK) {
//            System.out.println(x);
//
//            for (Story story : x.getStories()) {
//                StoryReadResponse read = client.read(story.getId());
//                read.getStory().getParagraphs().forEach(a -> System.out.println(a.getSpeaker() + ": " + a.getMessage()));
//            }
//        }
//    }

    @Test
    void testList() {
        CampaignChaptersResponse campaignChaptersResponse = client.campaignChapters();
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(campaignChaptersResponse);
//        campaignChaptersResponse.getChapters().stream()
//                .filter(a -> a.storyLimitedRelease != null).filter(a -> !Paths.get("./story", String.valueOf(a.id)).toFile().exists())
//                .map(a -> client.campaignChapterStories(a.id))
//                .flatMap(a -> a.getStories().stream())
//                .map(a -> client.read(a.getId())).forEach(
//                        a -> {
//                            try {
//                                Path path = Paths.get("./story", a.getStory().getChapterId().toString());
//                                path.toFile().mkdirs();
//
//                                objectMapper.writeValue(path.resolve(a.getStory().getId() + ".json").toFile(), a.getStory());
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                );
    }

    @Test
    void testStoryInfo() {
        JsonNode node = client.storyInfo(21000001L);
        System.out.println(node);
    }


    @Test
    void testMainStory() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode x = client.mainChapters();
        for (JsonNode node : x.get("chapter_main_story_segments")) {
            for (JsonNode chapterId : node.get("chapter_ids")) {
                long l = chapterId.asLong();
                MainStoryChapterStoriesResponse x1 = client.mainChaptersDetail(l);
                x1.getStories().stream().map(a -> client.read(a.getId())).forEach(a -> {
                    try {
                        Path path = Paths.get("./main-story", a.getStory().getChapterId().toString());
                        path.toFile().mkdirs();

                        objectMapper.writeValue(path.resolve(a.getStory().getId() + ".json").toFile(), a.getStory());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }

    @Test
    void newMainStory() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode x = client.mainChapters();
        System.out.println(x);
    }

    @Test
    void testPartStory() {
        ObjectMapper objectMapper = new ObjectMapper();
        MainStoryChapterStoriesResponse x1 = client.mainChaptersDetail(21000031L);
        x1.getStories().stream().map(a -> client.read(a.getId())).forEach(a -> {
            try {
                Path path = Paths.get("./main-story", a.getStory().getChapterId().toString());
                path.toFile().mkdirs();

                objectMapper.writeValue(path.resolve(a.getStory().getId() + ".json").toFile(), a.getStory());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }


    @Test
    void testPartEventStory() {
        ObjectMapper objectMapper = new ObjectMapper();
        CampaignChapterStoriesResponse x1 = client.campaignChapterStories(22000137L);
        List<UserStory> userStories = x1.getUserStories();
        System.out.println(x1);
        System.out.println(userStories);
        userStories.stream().map(a -> client.read(a.getStoryId())).forEach(a -> {
            try {
                Path path = Paths.get("./event-story", a.getStory().getChapterId().toString());
                path.toFile().mkdirs();

                objectMapper.writeValue(path.resolve(a.getStory().getId() + ".json").toFile(), a.getStory());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }


    @SneakyThrows
    @Test
    void parse() {

//        String storyId = "22100116";
        String s = "D:\\IdeaProject\\aira-core\\aira-api\\story\\";
        ObjectMapper mapper = new ObjectMapper();
        for (Path path : Files.list(Path.of(s)).collect(Collectors.toList())) {
            String storyId = path.toFile().getName();

            List<Path> list = Files.list(Path.of(s, storyId)).collect(Collectors.toList());
            Story story = mapper.readValue(list.get(0).toFile(), Story.class);
            BufferedWriter writer = new BufferedWriter(new FileWriter(storyId + "-" + story.getTitle() + ".txt"));

            list.stream().map(a -> {
                try {
                    return mapper.readValue(a.toFile(), Story.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).forEach(a -> {
                try {
                    writer.write("【" + a.getTitle() + " " + a.getSubTitle() + "】\n");
                    for (Story.Paragraph paragraph : a.getParagraphs()) {
                        if (paragraph.getSpeaker() == null) {
                            writer.write(paragraph.getMessage() + "\n");
                        } else {
                            writer.write(paragraph.getSpeaker() + ": " + paragraph.getMessage() + "\n");

                        }
                    }
                    writer.write("\n");
                } catch (Exception e) {

                    throw new RuntimeException(e);
                }


            });
            writer.close();
        }
    }

    @Test
    void featureStory() throws IOException {
        long gachaId = 579L;
        CampaignChapterStoriesResponse campaignChapterStoriesResponse = client.featureGacha(gachaId);
        BufferedWriter writer = new BufferedWriter(new FileWriter(gachaId + ".txt"));


        for (Story a : campaignChapterStoriesResponse.getStories()) {
            StoryReadResponse storyReadResponse = client.read(a.getId());
            Story read = storyReadResponse.getStory();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("【").append(read.getTitle()).append(" ").append(read.getSubTitle()).append("】\n");
            for (Story.Paragraph paragraph : read.getParagraphs()) {
                if (paragraph.getSpeaker() == null) {
                    stringBuilder.append(paragraph.getMessage()).append("\n");
                } else {
                    stringBuilder.append(paragraph.getSpeaker()).append("：").append(paragraph.getMessage()).append("\n");
                }
            }
            stringBuilder.append("\n");
            writer.write(stringBuilder.toString());


        }

        writer.close();

    }

    @Autowired
    StoryClient storyClient;

    @Test
    void test() {
        System.out.println(storyClient.es1Recommend());
    }
}
