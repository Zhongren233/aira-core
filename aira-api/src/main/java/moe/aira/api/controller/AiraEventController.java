package moe.aira.api.controller;

import moe.aira.core.biz.IAiraEventBiz;
import moe.aira.entity.aira.AiraEventPointDto;
import moe.aira.entity.aira.AiraEventScoreDto;
import moe.aira.entity.api.ApiResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AiraEventController {
    final
    IAiraEventBiz eventBiz;

    public AiraEventController(IAiraEventBiz eventBiz) {
        this.eventBiz = eventBiz;
    }

    @RequestMapping("/event/customAwardCount")
    public ApiResult<Map<Integer, Integer>> countEventPoint(Integer[] points) {
        return ApiResult.success(eventBiz.countEventPointBatch(points));
    }

    @RequestMapping("/event/awardCount")
    public ApiResult<Map<Integer, Integer>> countEventPoint() {
        return ApiResult.success(eventBiz.countEventPointBatch());
    }

    @RequestMapping("/event/rankPoint")
    public ApiResult<List<AiraEventPointDto>> fetchCurrentRankPoint() {
        return ApiResult.success(eventBiz.fetchCurrentRankPoint());
    }

    @RequestMapping("/event/customRankPoint")
    public ApiResult<List<AiraEventPointDto>> fetchCurrentRankPoint(Integer[] ranks) {
        return ApiResult.success(eventBiz.fetchCurrentRankPoint(ranks));
    }


    @RequestMapping("/event/rankScore")
    public ApiResult<List<AiraEventScoreDto>> fetchCurrentRankScore() {
        return ApiResult.success(eventBiz.fetchCurrentRankScore());
    }

    @RequestMapping("/event/customRankScore")
    public ApiResult<List<AiraEventScoreDto>> fetchCurrentRankScore(Integer[] ranks) {
        return ApiResult.success(eventBiz.fetchCurrentRankScore(ranks));
    }

    @RequestMapping("/event/ssf_rankScore")
    public ApiResult<List<AiraEventScoreDto>> ssfFetchCurrentRankScore(String colorType) {
        return ApiResult.success(eventBiz.fetchCurrentRankScore(colorType));
    }

    @RequestMapping("/event/ssf_customRankScore")
    public ApiResult<List<AiraEventScoreDto>> ssfFetchCurrentRankScore(String colorType, Integer[] ranks) {
        return ApiResult.success(eventBiz.fetchCurrentRankScore(colorType, ranks));
    }

}
