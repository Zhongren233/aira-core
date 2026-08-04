package moe.aira.api.controller;

import moe.aira.entity.aira.AiraTriColorMatsuriInfo;
import moe.aira.entity.api.ApiResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AiraTriColorMatsuriController {

    @ResponseBody
    @GetMapping("/tricolor/info")
    public ApiResult<AiraTriColorMatsuriInfo> tricolor() {
        return ApiResult.success(new AiraTriColorMatsuriInfo());
    }

}
