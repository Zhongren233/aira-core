package moe.aira.core.client.es;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Request;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.config.EnsembleStarsInterceptor;
import org.springframework.stereotype.Component;

@Component
@BaseRequest(interceptor = EnsembleStarsInterceptor.class)
public interface EventsClient {
    @Request(dataType = "text", url = "https://saki-server.happyelements.cn/get/events/point_bonuses", type = "POST")
    JsonNode pointBonuses();

    @Request(dataType = "text", url = "https://saki-server.happyelements.cn/get/event/index", type = "POST")
    JsonNode index();

    @Request(dataType = "text", url = "https://saki-server.happyelements.cn/get/events/tours", type = "POST")
    JsonNode tours();

    @Request(dataType = "text", url = "https://saki-server.happyelements.cn/get/event_announce/index", type = "POST")
    JsonNode eventAnnounce();

    @Request(dataType = "text", url = "https://saki-server.happyelements.cn/get/christmas2020_game", type = "POST")
    JsonNode christmas2020Game();

    @Request(dataType = "text", url = "https://saki-server.happyelements.cn/get/christmas2020_game/tree", type = "POST")
    JsonNode christmas2020GameTree();
}
