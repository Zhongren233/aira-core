package moe.aira.onebot.client;

import moe.aira.entity.aira.AiraLiveChallengeInfo;
import moe.aira.entity.api.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "${aira.service-url}", name = "live-challenge-service")
public interface LiveChallengeClient {
    @GetMapping("/liveChallenge/info")
    ApiResult<AiraLiveChallengeInfo> info();

}