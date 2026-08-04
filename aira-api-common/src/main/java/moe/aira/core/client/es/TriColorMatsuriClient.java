package moe.aira.core.client.es;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Request;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.config.EnsembleStarsInterceptor;
import org.springframework.stereotype.Component;

@BaseRequest(interceptor = EnsembleStarsInterceptor.class)
@Component
public interface TriColorMatsuriClient {
    @Request(url = "https://saki-server.happyelements.cn/get/tricolor_festival2023", type = "POST", dataType = "text")
    JsonNode tricolorFestival2023(@Body("beginner_random_entry_selected") boolean beginnerRandomEntrySelected);

}
