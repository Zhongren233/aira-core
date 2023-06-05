package moe.aira.core.client.hekk;

import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.entity.hekk.AccessTokenResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component

public interface AccessTokenClient {

    @Request(dataType = "binary", url = "https://api.boysm.hekk.org/access_token", type = "POST")
    byte[] accessToken(@Header("Authorization") String token, @Query Map<?, ?> queryMap);
}
