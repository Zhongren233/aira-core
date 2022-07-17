package moe.aira.onebot.client;

import moe.aira.entity.aira.AiraEventRanking;
import moe.aira.entity.aira.AiraSSFEventRanking;
import moe.aira.entity.api.ApiResult;
import moe.aira.entity.es.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${aira.service-url}", name = "user-service")
public interface AiraUserClient {
    @GetMapping("/user/info")
    ApiResult<UserInfo> fetchUserInfo(@RequestParam("uidCode") String uidCode);

    @GetMapping("/user/ranking")
    ApiResult<AiraEventRanking> fetchRealTimeAiraEventRanking(@RequestParam("userId") Integer userId);

    @GetMapping(value = "/user/ssf_ranking")
    ApiResult<AiraSSFEventRanking> fetchRealTimeAiraSSFEventRanking(@RequestParam("userId") Integer userId);
}
