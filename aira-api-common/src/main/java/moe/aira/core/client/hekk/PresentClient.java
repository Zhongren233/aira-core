package moe.aira.core.client.hekk;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Request;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.config.EnsembleStarsMusicInterceptor;
import org.springframework.stereotype.Component;

@Component
@BaseRequest(interceptor = EnsembleStarsMusicInterceptor.class)
public interface PresentClient {
    @Request(url = "https://api.boysm.hekk.org/presents", type = "GET")
    JsonNode presents(@Body("sort_type_id") String sortTypeId);

    @Request(url = "https://api.boysm.hekk.org/presents/receive_all", type = "PUT")
    JsonNode receiveAll( );
}
