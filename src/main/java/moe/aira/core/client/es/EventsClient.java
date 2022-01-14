package moe.aira.core.client.es;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Request;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.config.EnsembleStarsInterceptor;
import org.springframework.stereotype.Component;

@Component
@BaseRequest(interceptor = EnsembleStarsInterceptor.class)
public interface EventsClient {
    @Request(url = "https://saki-server.happyelements.cn/get/events/point_bonuses", type = "POST")
    JsonNode pointBonuses();

    @Request(url = "https://saki-server.happyelements.cn/get/event/index", type = "POST")
    JsonNode index();

    @Request(url = "https://saki-server.happyelements.cn/get/event_announce/index", type = "POST")
    JsonNode eventAnnounce();

    @Request(url = "https://saki-server.happyelements.cn/get/christmas2020_game", type = "POST")
    JsonNode christmas2020Game();

    @Request(url = "https://saki-server.happyelements.cn/get/christmas2020_game/tree", type = "POST")
    JsonNode christmas2020GameTree();
}
