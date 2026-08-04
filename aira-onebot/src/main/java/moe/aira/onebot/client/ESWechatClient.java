package moe.aira.onebot.client;

import moe.aira.onebot.entity.MonthCardInfoResponse;
import moe.aira.onebot.entity.MonthCardRewardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "https://saki-server.happyelements.cn", name = "wechat-service")

public interface ESWechatClient {

    @GetMapping("/wechat/month_card_info")
    MonthCardInfoResponse getMonthCardInfo(@RequestParam("wechat_openid") String wechatOpenid);


    @GetMapping("/wechat/month_card_reward")
    MonthCardRewardResponse getMonthCardReward(@RequestParam("wechat_openid") String wechatOpenid, @RequestParam("type") String type);

}
