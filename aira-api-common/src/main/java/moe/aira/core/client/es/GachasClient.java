package moe.aira.core.client.es;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Request;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.config.EnsembleStarsInterceptor;
import org.springframework.stereotype.Component;

@BaseRequest(interceptor = EnsembleStarsInterceptor.class)
@Component
public interface GachasClient {
    @Request(dataType = "text", url = "https://saki-server.happyelements.cn/get/gachas", type = "POST")
    JsonNode gachas();

    @Request(dataType = "text", url = "https://saki-server.happyelements.cn/gachas/results", type = "POST")
    JsonNode results(@Body("gacha_id") String gachaId, @Body("gacha_cost_type_id") String gachaCostTypeId, @Body("execution_count") String executionCount);

    @Request(dataType = "text", url = "https://saki-server.happyelements.cn/get/gachas/cards", type = "POST")
    JsonNode cards(@Body("gacha_id")String gachaId);

}
