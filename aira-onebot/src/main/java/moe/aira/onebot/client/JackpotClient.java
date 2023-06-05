package moe.aira.onebot.client;

import moe.aira.entity.aira.AiraGachaInfo;
import moe.aira.entity.api.ApiResult;
import moe.aira.entity.es.CasinoInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(url = "${aira.service-url}", name = "jackpot-service")
public interface JackpotClient {

    @GetMapping("/casinoInfo")

    ApiResult<CasinoInfo> casino();

}