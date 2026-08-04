package moe.aira.core.client.hekk;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.annotation.Var;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.config.EnsembleStarsMusicInterceptor;
import org.springframework.stereotype.Component;

@Component
@BaseRequest(interceptor = EnsembleStarsMusicInterceptor.class)
public interface ESMIdolRoomClient {

    @Request(dataType = "text", url = "https://api.boysm.hekk.org/idol_rooms/{characterId}", type = "GET")
    JsonNode idolRoomsTop(@Var("characterId") Long characterId);

    @Request(dataType = "text", url = "https://api.boysm.hekk.org/cards", type = "GET")
    JsonNode idolRoomsCards();


}
