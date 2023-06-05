package moe.aira.core.client.hekk;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.annotation.Retry;
import com.dtflys.forest.callback.DefaultRetryWhen;
import com.dtflys.forest.callback.RetryWhen;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.config.EnsembleStarsMusicInterceptor;
import moe.aira.core.config.EnsembleStarsRetryWhen;
import moe.aira.entity.hekk.TitleResponse;
import org.springframework.stereotype.Component;

@Component
@BaseRequest(interceptor = EnsembleStarsMusicInterceptor.class)
public interface BaseClient {
    @Request(dataType = "text", url = "https://api.boysm.hekk.org/title", type = "GET")
    @Retry(condition = EnsembleStarsRetryWhen.class, maxRetryCount = "1", maxRetryInterval = "200")

    TitleResponse title();

}
