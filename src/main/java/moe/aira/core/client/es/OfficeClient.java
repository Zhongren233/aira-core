package moe.aira.core.client.es;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Request;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.config.EnsembleStarsInterceptor;
import org.springframework.stereotype.Component;

@Component
@BaseRequest(interceptor = EnsembleStarsInterceptor.class)
public interface OfficeClient {
    @Request(url = "https://saki-server.happyelements.cn/get/office", type = "POST")
    JsonNode office();

    /**
     * 事务所小人对话
     */
    @Request(url = "https://saki-server.happyelements.cn/offices/commu/0", type = "PUT")
    JsonNode commu();

}
