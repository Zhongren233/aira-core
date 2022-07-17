package moe.aira.core.client.es;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.callback.OnError;
import com.dtflys.forest.callback.OnSuccess;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.config.EnsembleStarsInterceptor;
import org.springframework.stereotype.Component;

@BaseRequest(interceptor = EnsembleStarsInterceptor.class)
@Component
public interface ScoreRankingClient {
    @Request(url = "https://saki-server.happyelements.cn/get/events/score_ranking", type = "POST")
    JsonNode page(@Body("page") int page);

    @Request(url = "https://saki-server.happyelements.cn/get/events/score_ranking", type = "POST", async = true, timeout = 200000)
    void asyncPage(@Body("page") int page, OnSuccess<JsonNode> onSuccess, OnError onError);

    /**
     * @param colorTypeId 1= fine 2=eden
     */
    @Request(url = "https://saki-server.happyelements.cn/get/events/ss/score_ranking", type = "POST", dataType = "text")
    JsonNode ssPage(@Body("page") int page, @Body("color_type_id") int colorTypeId);

    /**
     * @param colorTypeId 1= fine 2=eden
     */
    @Request(url = "https://saki-server.happyelements.cn/get/events/ss/score_ranking", type = "POST", async = true, timeout = 200000)
    void ssAsyncPage(@Body("page") int page, @Body("color_type_id") int colorTypeId, OnSuccess<JsonNode> onSuccess, OnError onError);
}
