package moe.aira.core.client.es;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.callback.OnError;
import com.dtflys.forest.callback.OnSuccess;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.config.EnsembleStarsInterceptor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@BaseRequest(interceptor = EnsembleStarsInterceptor.class)
@Component
public interface PointRankingClient {
    /**
     * 获取pointRanking
     *
     * @param page 页码
     */
    @Request(url = "https://saki-server.happyelements.cn/get/events/point_ranking", type = "POST")
    JsonNode page(@Body("page") int page);

    @Request(url = "https://saki-server.happyelements.cn/get/events/point_ranking", type = "POST")
    JsonNode page(@Body("page") int page, OnSuccess<JsonNode> onSuccess);


    @Request(url = "https://saki-server.happyelements.cn/get/events/point_ranking", type = "POST", async = true, maxRetryInterval = 100)
    void asyncPage(@Body("page") int page, OnSuccess<JsonNode> onSuccess, OnError onError);
}
