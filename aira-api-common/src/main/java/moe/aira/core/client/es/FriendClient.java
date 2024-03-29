package moe.aira.core.client.es;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Request;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.config.EnsembleStarsInterceptor;
import org.springframework.stereotype.Component;

@Component
@BaseRequest(interceptor = EnsembleStarsInterceptor.class)
public interface FriendClient {
    @Request(dataType = "text", url = "https://saki-server.happyelements.cn/friends/search", type = "POST")
    JsonNode friendList(@Body("uid") String uid);
}
