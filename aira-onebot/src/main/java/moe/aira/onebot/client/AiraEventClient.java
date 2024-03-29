package moe.aira.onebot.client;

import moe.aira.entity.aira.AiraEventPointDto;
import moe.aira.entity.aira.AiraEventScoreDto;
import moe.aira.entity.api.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(url = "${aira.service-url}", name = "event-service")
public interface AiraEventClient {
    @GetMapping("/event/customAwardCount")
    ApiResult<Map<Integer, Integer>> countEventPoint(@RequestParam Integer[] points);

    @GetMapping("/event/awardCount")
    ApiResult<Map<Integer, Integer>> countEventPoint();

    @GetMapping("/event/customRankPoint")
    ApiResult<List<AiraEventPointDto>> fetchCurrentRankPoint(@RequestParam Integer[] ranks);

    @GetMapping("/event/customRankScore")
    ApiResult<List<AiraEventScoreDto>> fetchCurrentRankScore(@RequestParam Integer[] ranks);

    @RequestMapping("/event/ssf_rankScore")
    ApiResult<List<AiraEventScoreDto>> ssfFetchCurrentRankScore(@RequestParam String colorType);

    @RequestMapping("/event/ssf_customRankScore")
    ApiResult<List<AiraEventScoreDto>> ssfFetchCurrentRankScore(@RequestParam String colorType, @RequestParam Integer[] ranks);

}
