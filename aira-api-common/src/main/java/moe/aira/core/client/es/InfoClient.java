package moe.aira.core.client.es;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Request;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.config.EnsembleStarsInterceptor;
import org.springframework.stereotype.Component;

@Component
@BaseRequest(interceptor = EnsembleStarsInterceptor.class)
public interface InfoClient {
    @Request(url = "https://saki-server.happyelements.cn/get_info_list", type = "POST")
    JsonNode getInfoList();

    @Request(url = "https://saki-server.happyelements.cn/get/user/base_info", type = "POST")
    JsonNode baseInfo();

}
