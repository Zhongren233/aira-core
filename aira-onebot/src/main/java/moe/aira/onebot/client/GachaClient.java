package moe.aira.onebot.client;

import moe.aira.entity.aira.AiraGachaInfo;
import moe.aira.entity.api.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(url = "${aira.service-url}", name = "gacha-service")
public interface GachaClient {
    @GetMapping("/gacha/pool")
    ApiResult<Set<Integer>> gachaPool();

    @GetMapping("/gacha/poolInfo")
    ApiResult<AiraGachaInfo> poolInfo(@RequestParam("gachaId") Integer gachaId);
}