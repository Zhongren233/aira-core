package moe.aira.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AiraWeiboController {
    @ResponseBody
    @GetMapping("/weibo/code")
    public String weiboToken(@RequestParam String code) {

        return "success" + code;
    }

}
