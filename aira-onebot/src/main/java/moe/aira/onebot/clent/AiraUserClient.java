package moe.aira.onebot.clent;

import moe.aira.entity.api.ApiResult;
import moe.aira.entity.es.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${aira.service-url}", name = "user-service")
public interface AiraUserClient {
    @GetMapping("/user/info")
    ApiResult<UserInfo> fetchUserInfo(@RequestParam("uidCode") String uidCode);

}
