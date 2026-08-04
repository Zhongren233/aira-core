package moe.aira.controller;

import moe.aira.onebot.service.WeiboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Controller
public class WeiboController {
    @Autowired
    WeiboService weiboService;
    @RequestMapping("weibo/oauth")
    @ResponseBody
    public String weibo(String code) throws IOException, InterruptedException {
        weiboService.updateToken(code);
        return "success";
    }

    @RequestMapping("weibo/message")
    @ResponseBody
    public String message(String code) throws IOException, InterruptedException {
        weiboService.updateToken(code);
        return "success";
    }
}
