package moe.aira.core.client.es;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Request;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.config.EnsembleStarsInterceptor;
import org.springframework.stereotype.Component;

@Component
@BaseRequest(interceptor = EnsembleStarsInterceptor.class)
public interface IdolRoomClient {
    @Request(url = "https://saki-server.happyelements.cn/get/idol_rooms/cards", type = "POST")
    JsonNode cards(@Body("character_id") String characterId);


    @Request(url = "https://saki-server.happyelements.cn/get/cards", type = "POST")
    JsonNode cardsT();


    @Request(url = "https://saki-server.happyelements.cn/debug/asset_viewer/events/315", type = "POST")
    JsonNode debugVocies( );
}
