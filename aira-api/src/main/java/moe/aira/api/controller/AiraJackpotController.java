package moe.aira.api.controller;

import moe.aira.core.service.IJackpotService;
import moe.aira.entity.api.ApiResult;
import moe.aira.entity.es.CasinoInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AiraJackpotController {
    final
    IJackpotService jackpotService;

    public AiraJackpotController(IJackpotService jackpotService) {
        this.jackpotService = jackpotService;
    }

    @GetMapping("/casinoInfo")
@ResponseBody
    public ApiResult<CasinoInfo> casino() {
        return ApiResult.success(jackpotService.fetchCurrentCasinoInfo());
    }
}
