package moe.aira.core.biz.impl;

import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.biz.IAiraEnsembleStarsMusicBiz;
import moe.aira.core.client.es.StoryClient;
import moe.aira.core.client.hekk.BaseClient;
import moe.aira.core.client.hekk.ESMStoryClient;
import moe.aira.core.config.EnsembleStarsMusicConfigWrapper;
import moe.aira.entity.api.FetchCatalogResponse;
import moe.aira.entity.hekk.Chapter;
import moe.aira.entity.hekk.Story;
import moe.aira.entity.hekk.UserStory;
import moe.aira.resp.hekk.CampaignChapterStoriesResponse;
import moe.aira.resp.hekk.CampaignChaptersResponse;
import moe.aira.resp.hekk.TitleResponse;
import moe.aira.enums.AppStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component

public class IAiraEnsembleStarsMusicBizImpl implements IAiraEnsembleStarsMusicBiz {
    @Autowired
    BaseClient client;
    @Autowired
    EnsembleStarsMusicConfigWrapper configWrapper;

    @Autowired
    ESMStoryClient storyClient;

    @Override
    public FetchCatalogResponse fetchCatalogInfo() {
        FetchCatalogResponse fetchCatalogResponse = new FetchCatalogResponse();
        TitleResponse title = client.title();
        fetchCatalogResponse.setAppStatusCode(title.getAppStatusCode());
        if (title.getAppStatusCode() == AppStatusCode.OK) {
            List<TitleResponse.CatalogData> assetCatalogs = title.getAssetCatalogs();
            ArrayList<TitleResponse.CatalogData> list = new ArrayList<>(assetCatalogs);

            TitleResponse.CatalogData audioCatalog = title.getAudioCatalog();
            audioCatalog.setUpdatedAt(String.valueOf(audioCatalog.getId()));
            audioCatalog.setName("Audio");
            list.add(audioCatalog);

            TitleResponse.CatalogData movieCatalog = title.getMovieCatalog();
            movieCatalog.setUpdatedAt(String.valueOf(audioCatalog.getId()));
            movieCatalog.setName("Movies");
            list.add(movieCatalog);

            fetchCatalogResponse.setCatalogDataList(list);
            configWrapper.updateCatalog(list, title.getServerVersion());
        }
        return fetchCatalogResponse;
    }

    @Override
    public Story readStory(Long storyId) {
        return storyClient.read(storyId).getStory();
    }

    @Override
    public List<Chapter> campaignChapters() {
        CampaignChaptersResponse campaignChaptersResponse = storyClient.campaignChapters();
        return campaignChaptersResponse.getChapters();
    }

    @Override
    public List<Story> campaignChapterStories(Long chapterId) {
        CampaignChapterStoriesResponse campaignChapterStoriesResponse = storyClient.campaignChapterStories(chapterId);
        List<Story> stories = campaignChapterStoriesResponse.getStories();
        Chapter.StoryLimitedRelease storyLimitedRelease = campaignChapterStoriesResponse.getChapter().storyLimitedRelease;
        ArrayList<Story> stories1 = new ArrayList<>();

        if (storyLimitedRelease != null| campaignChapterStoriesResponse.getChapter().storyTypeId == 1 ) {
            stories1.addAll(stories);
        }else {
            List<UserStory> userStories = campaignChapterStoriesResponse.getUserStories();
            for (Story story : stories) {
                if (userStories.stream().anyMatch(us -> us.getStoryId().equals(story.getId()))) {
                    stories1.add(story);
                }
            }
        }

        return stories1;
    }


    @Override
    public JsonNode cards() {
        JsonNode cards = client.cards();
        return cards.get("cards");

    }

    @Override
    public CampaignChapterStoriesResponse featureGachaStories(Long gachaId) {
        return storyClient.featureGacha(gachaId);
    }

}


