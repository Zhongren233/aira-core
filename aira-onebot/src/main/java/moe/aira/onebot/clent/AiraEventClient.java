package moe.aira.onebot.clent;

import moe.aira.entity.api.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(url = "${aira.service-url}", name = "event-service")
public interface AiraEventClient {
    @GetMapping("/event/customAwardCount")
    ApiResult<Map<Integer, Integer>> countEventPoint(@RequestParam Integer[] points);

    @GetMapping("/event/customRankPoint")
    ApiResult<Map<Integer, Integer>> fetchCurrentRankPoint(@RequestParam Integer[] ranks);

    @GetMapping("/event/customRankScore")
    ApiResult<Map<Integer, Integer>> fetchCurrentRankScore(@RequestParam Integer[] ranks);

}
