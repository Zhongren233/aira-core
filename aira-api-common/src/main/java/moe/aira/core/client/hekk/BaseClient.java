package moe.aira.core.client.hekk;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.annotation.Retry;
import com.dtflys.forest.annotation.Var;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.config.EnsembleStarsMusicInterceptor;
import moe.aira.core.config.EnsembleStarsRetryWhen;
import moe.aira.resp.hekk.TitleResponse;
import org.springframework.stereotype.Component;

@Component
@BaseRequest(interceptor = EnsembleStarsMusicInterceptor.class)
public interface BaseClient {
    @Request(dataType = "text", url = "https://api.boysm.hekk.org/title", type = "GET")
    @Retry(condition = EnsembleStarsRetryWhen.class, maxRetryCount = "1", maxRetryInterval = "200")

    TitleResponse title();

    @Request(dataType = "text", url = "https://api.boysm.hekk.org/my_page", type = "GET")
    @Retry(condition = EnsembleStarsRetryWhen.class, maxRetryCount = "1", maxRetryInterval = "200")
    JsonNode mypage();

    @Request(dataType = "text", url = "https://api.boysm.hekk.org/assets", type = "GET")
    @Retry(condition = EnsembleStarsRetryWhen.class, maxRetryCount = "1", maxRetryInterval = "200")
    JsonNode assets();

    @Request(dataType = "text", url = "https://api.boysm.hekk.org/cards", type = "GET")
    @Retry(condition = EnsembleStarsRetryWhen.class, maxRetryCount = "1", maxRetryInterval = "200")

    JsonNode cards();


    @Request(dataType = "text", url = "https://api.boysm.hekk.org/cards/{cardId}", type = "GET")
    JsonNode cardCatalog(@Var("cardId") Integer cardId);
}
