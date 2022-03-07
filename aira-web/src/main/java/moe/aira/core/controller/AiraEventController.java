package moe.aira.core.controller;

import moe.aira.core.biz.IAiraEventBiz;
import moe.aira.entity.api.ApiResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("/event")
public class AiraEventController {
    final
    IAiraEventBiz eventBiz;

    public AiraEventController(IAiraEventBiz eventBiz) {
        this.eventBiz = eventBiz;
    }

    @RequestMapping("/awardCount")
    public ApiResult<Map<Integer, Integer>> countEventPoint(Integer[] points) {
        return ApiResult.success(eventBiz.countEventPointWhereGt(points));
    }

    @RequestMapping("/batchAwardCount")
    public ApiResult<Map<Integer, Integer>> countEventPointBatch() {
        return ApiResult.success(eventBiz.countEventPointBatch());
    }
}
