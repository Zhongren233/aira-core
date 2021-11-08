package moe.aira.core.client.es;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Request;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.config.EnsembleStarsInterceptor;
import org.springframework.stereotype.Component;

@Component
@BaseRequest(interceptor = EnsembleStarsInterceptor.class)
public interface PresentsClient {
    @Request(url = "https://saki-server.happyelements.cn/get/presents", type = "POST")
    JsonNode presents(@Body("sort_type_id") String sortTypeId);

    @Request(url = "https://saki-server.happyelements.cn/presents/receive_all", type = "PUT")
    JsonNode receiveAll();
}
