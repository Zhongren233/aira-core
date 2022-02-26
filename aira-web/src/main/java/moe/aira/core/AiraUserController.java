package moe.aira.core;

import moe.aira.core.biz.IAiraUserBiz;
import moe.aira.entity.aira.AiraEventRanking;
import moe.aira.entity.api.ApiResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiraUserController {
    final
    IAiraUserBiz airaUserBiz;

    public AiraUserController(IAiraUserBiz airaUserBiz) {
        this.airaUserBiz = airaUserBiz;
    }

    @RequestMapping(value = "/user/ranking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<AiraEventRanking> fetchRealTimeAiraEventRanking(Integer userId) {
        AiraEventRanking airaEventRanking = airaUserBiz.fetchAiraEventRanking(userId);
        return ApiResult.success(airaEventRanking);
    }


}
