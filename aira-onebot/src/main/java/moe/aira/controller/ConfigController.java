package moe.aira.controller;

import groovy.transform.AutoExternalize;
import moe.aira.config.EventConfig;
import moe.aira.entity.api.ApiResult;
import moe.aira.onebot.manager.IEventConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigController {
    final
    IEventConfigManager eventConfigManager;
    final
    StringRedisTemplate stringRedisTemplate;


    public ConfigController(IEventConfigManager eventConfigManager, StringRedisTemplate stringRedisTemplate) {
        this.eventConfigManager = eventConfigManager;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @GetMapping("/event")
    public ApiResult<EventConfig> configApiResult() {
        EventConfig eventConfig = eventConfigManager.fetchEventConfig();
        return ApiResult.success(eventConfig);
    }


    /**
     * @return userId
     * */
    @GetMapping("/fast-bind")
    public ApiResult<?> fastBind(String accessCode) {
        String s = stringRedisTemplate.opsForValue().get("FastBind:" + accessCode);
        if (s == null) {
            return ApiResult.fail("");
        }else {
            return ApiResult.success(s);
        }
    }
}
