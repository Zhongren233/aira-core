package moe.aira.core.client.hekk;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.annotation.Var;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.config.EnsembleStarsMusicInterceptor;
import moe.aira.resp.hekk.CampaignChapterStoriesResponse;
import moe.aira.resp.hekk.CampaignChaptersResponse;
import moe.aira.resp.hekk.MainStoryChapterStoriesResponse;
import moe.aira.resp.hekk.StoryReadResponse;
import org.springframework.stereotype.Component;

@Component
@BaseRequest(interceptor = EnsembleStarsMusicInterceptor.class,retryCount = 2,connectTimeout = 10000,readTimeout = 10000)

public interface ESMStoryClient {

    @Request(dataType = "text", url = "https://api.boysm.hekk.org/stories/read/{storyId}", type = "GET")
    StoryReadResponse read(@Var("storyId") Long storyId);

    @Request(dataType = "text", url = "https://api.boysm.hekk.org/stories/{storyId}", type = "GET")
    JsonNode storyInfo(@Var("storyId") Long storyId);


    @Request(dataType = "text", url = "https://api.boysm.hekk.org/stories/campaign_chapters", type = "GET")
    CampaignChaptersResponse campaignChapters();

    @Request(dataType = "text", url = "https://api.boysm.hekk.org/stories/feature_gacha/{gachaId}", type = "GET")
    CampaignChapterStoriesResponse featureGacha(@Var("gachaId") Long gachaId);

    @Request(dataType = "text", url = "https://api.boysm.hekk.org/stories/campaign_chapters/{chapterId}", type = "GET")
    CampaignChapterStoriesResponse campaignChapterStories(@Var("chapterId") Long chapterId);

    @Request(dataType = "text", url = "https://api.boysm.hekk.org/stories/main_chapters", type = "GET")
    JsonNode mainChapters();

    @Request(dataType = "text", url = "https://api.boysm.hekk.org/stories/main_chapters/{chapterId}", type = "GET")
    MainStoryChapterStoriesResponse mainChaptersDetail(@Var("chapterId") Long chapterId);

}
