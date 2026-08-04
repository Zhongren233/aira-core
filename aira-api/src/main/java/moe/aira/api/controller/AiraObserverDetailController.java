package moe.aira.api.controller;

import moe.aira.core.dao.AiraObserverDetailMapper;
import moe.aira.entity.aira.AiraObserverDetail;
import moe.aira.entity.aira.AiraTriColorMatsuriInfo;
import moe.aira.entity.api.ApiResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class AiraObserverDetailController {

    private final AiraObserverDetailMapper airaObserverDetailMapper;

    public AiraObserverDetailController(AiraObserverDetailMapper airaObserverDetailMapper) {
        this.airaObserverDetailMapper = airaObserverDetailMapper;
    }

    @ResponseBody
    @GetMapping("/observer/list")
    public ApiResult<List<AiraObserverDetail>> detailApiResult(@RequestParam Integer userId, @RequestParam Integer eventId) {
        return ApiResult.success(airaObserverDetailMapper.selectByUserIdAndEventId(userId, eventId));
    }

}
