package moe.aira.core.client.hekk;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Request;
import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.config.EnsembleStarsMusicInterceptor;
import moe.aira.core.entity.dto.QuizResponse;
import org.springframework.stereotype.Component;

@Component
@BaseRequest(interceptor = EnsembleStarsMusicInterceptor.class, retryCount = 2, connectTimeout = 10000, readTimeout = 10000)

public interface QuizClient {


    @Request(dataType = "text", url = "https://api.boysm.hekk.org/quizzes/play", type = "POST")
    QuizResponse play(@Body("quiz_difficulty_id") Integer quiz_difficulty_id);

}
