package moe.aira.core.client.es;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Request;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.config.EnsembleStarsInterceptor;
import org.springframework.stereotype.Component;

@Component
@BaseRequest(interceptor = EnsembleStarsInterceptor.class)
public interface EventsClient {
    @Request(url = "https://saki-server.happyelements.cn/get/events/point_bonuses", type = "POST")
    JsonNode pointBonuses();

    @Request(url = "https://saki-server.happyelements.cn/get/event/index", type = "POST")
    JsonNode index();
}
