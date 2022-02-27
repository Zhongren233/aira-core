package moe.aira.core;

import moe.aira.core.biz.IAiraUserBiz;
import moe.aira.entity.aira.AiraEventRanking;
import moe.aira.entity.api.ApiResult;
import moe.aira.exception.AiraIllegalParamsException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiraUserController {
    final
    IAiraUserBiz airaUserBiz;

    public AiraUserController(IAiraUserBiz airaUserBiz) {
        this.airaUserBiz = airaUserBiz;
    }

    @RequestMapping(value = "/user/ranking")
    public ApiResult<AiraEventRanking> fetchRealTimeAiraEventRanking(Integer userId) {
        AiraEventRanking airaEventRanking = airaUserBiz.fetchAiraEventRanking(userId);
        return ApiResult.success(airaEventRanking);
    }

    @RequestMapping(value = "/user/info")
    public ApiResult<?> fetchUserInfo(String uidCode) {
        if (!StringUtils.hasText(uidCode)) {
            throw new AiraIllegalParamsException("uidCode不合法");
        }
        return null;
    }
}
