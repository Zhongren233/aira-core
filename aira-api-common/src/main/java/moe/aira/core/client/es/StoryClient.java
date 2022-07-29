package moe.aira.core.client.es;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Request;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.config.EnsembleStarsInterceptor;
import org.springframework.stereotype.Component;

@BaseRequest(interceptor = EnsembleStarsInterceptor.class)
@Component
public interface StoryClient {
    @Request(url = "https://saki-server.happyelements.cn/get/stories/read/0", type = "POST", dataType = "text")
    JsonNode read(@Body("id") String id, @Body("type") String type);

    @Request(url = "https://saki-server.happyelements.cn/get/stories/0", type = "POST", dataType = "text")
    JsonNode storyInfo(@Body("id") String id);

    @Request(url = "https://saki-server.happyelements.cn/get/stories/campaign_chapters", type = "POST", dataType = "text")
    JsonNode campaignChapters();

    @Request(url = "https://saki-server.happyelements.cn/get/stories/campaign_chapters/0", type = "POST", dataType = "text")
    JsonNode campaignStoryList(@Body("id") String id);

    @Request(url = "https://saki-server.happyelements.cn/get/stories/search_condition", type = "POST", dataType = "text")
    JsonNode searchCondition();
}
