package moe.aira.core.biz;

import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.entity.api.FetchCatalogResponse;
import moe.aira.entity.hekk.Chapter;
import moe.aira.entity.hekk.Story;
import moe.aira.resp.hekk.CampaignChapterStoriesResponse;

import java.util.List;

public interface IAiraEnsembleStarsMusicBiz {
    FetchCatalogResponse fetchCatalogInfo();

    Story readStory(Long storyId);

    List<Chapter> campaignChapters();
    List<Story> campaignChapterStories(Long chapterId);

    JsonNode cards();

    CampaignChapterStoriesResponse featureGachaStories(Long gachaId);

}
