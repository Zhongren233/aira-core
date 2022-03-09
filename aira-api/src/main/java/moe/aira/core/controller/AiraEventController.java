package moe.aira.core.controller;

import moe.aira.core.biz.IAiraEventBiz;
import moe.aira.entity.api.ApiResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiResult<Map<Integer, Integer>> fetchCurrentRankPoint() {
        return ApiResult.success(eventBiz.fetchCurrentRankPoint());
    }

    @RequestMapping("/event/customRankPoint")
    public ApiResult<Map<Integer, Integer>> fetchCurrentRankPoint(Integer[] ranks) {
        return ApiResult.success(eventBiz.fetchCurrentRankPoint(ranks));

    }


}
