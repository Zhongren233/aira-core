package moe.aira.controller;

import com.mikuac.shiro.core.BotContainer;
import moe.aira.entity.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/bot")
public class BotManagerController {
    @Autowired
    BotContainer botContainer;

    @RequestMapping("/currentOnlineBot")
    public ApiResult<Set<Long>> currentBot() {
        Set<Long> longs = botContainer.robots.keySet();
        return ApiResult.success(longs);
    }
}
