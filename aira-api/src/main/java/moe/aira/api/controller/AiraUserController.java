package moe.aira.api.controller;

import moe.aira.core.biz.IAiraUserBiz;
import moe.aira.entity.aira.AiraEventRanking;
import moe.aira.entity.api.ApiResult;
import moe.aira.entity.es.UserInfo;
import moe.aira.exception.client.AiraIllegalParamsException;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@RestController
public class AiraUserController {
    final
    IAiraUserBiz airaUserBiz;

    public AiraUserController(IAiraUserBiz airaUserBiz) {
        this.airaUserBiz = airaUserBiz;
    }

    @GetMapping(value = "/user/ranking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<AiraEventRanking> fetchRealTimeAiraEventRanking(Integer userId) {
        AiraEventRanking airaEventRanking = airaUserBiz.fetchAiraEventRanking(userId);
        return ApiResult.success(airaEventRanking);
    }

    @GetMapping(value = "/user/info")
    public ApiResult<UserInfo> fetchUserInfo(String uidCode) {
        if (!StringUtils.hasText(uidCode)) {
            throw new AiraIllegalParamsException(MessageFormat.format("不合法的参数:{0}", uidCode));
        }
        UserInfo userInfo = airaUserBiz.fetchUserInfo(uidCode);
        return ApiResult.success(userInfo);
    }
}
