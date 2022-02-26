package moe.aira.core.client.es;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Request;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.config.EnsembleStarsInterceptor;
import org.springframework.stereotype.Component;

@Component
@BaseRequest(interceptor = EnsembleStarsInterceptor.class)
public interface CharacterClient {
    @Request(url = "https://saki-server.happyelements.cn/get/characters/0", type = "POST")
    JsonNode character(@Body("id") String id);
}
