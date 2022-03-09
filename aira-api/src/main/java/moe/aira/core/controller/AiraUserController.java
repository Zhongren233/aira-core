package moe.aira.core.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

@Api(tags = "玩家相关API")
@RestController
public class AiraUserController {
    final
    IAiraUserBiz airaUserBiz;

    public AiraUserController(IAiraUserBiz airaUserBiz) {
        this.airaUserBiz = airaUserBiz;
    }

    @ApiOperation("获取玩家实时排名")
    @GetMapping(value = "/user/ranking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<AiraEventRanking> fetchRealTimeAiraEventRanking(Integer userId) {
        AiraEventRanking airaEventRanking = airaUserBiz.fetchAiraEventRanking(userId);
        return ApiResult.success(airaEventRanking);
    }

    @ApiOperation("通过好友接口获取玩家信息")
    @GetMapping(value = "/user/info")
    public ApiResult<UserInfo> fetchUserInfo(String uidCode) {
        if (!StringUtils.hasText(uidCode)) {
            throw new AiraIllegalParamsException(MessageFormat.format("不合法的参数:{0}", uidCode));
        }
        UserInfo userInfo = airaUserBiz.fetchUserInfo(uidCode);
        return ApiResult.success(userInfo);
    }
}
