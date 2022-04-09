package moe.aira.api.controller;

import moe.aira.core.manager.IGachaManager;
import moe.aira.entity.aira.AiraGachaInfo;
import moe.aira.entity.api.ApiResult;
import moe.aira.exception.client.AiraIllegalParamsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class AiraGachaController {
    private final IGachaManager airaGachaManager;

    public AiraGachaController(IGachaManager airaGachaManager) {
        this.airaGachaManager = airaGachaManager;
    }

    @GetMapping("/gacha/pool")
    public ApiResult<Set<Integer>> gachaPool() {
        return ApiResult.success(airaGachaManager.currentGacha());
    }

    @GetMapping("/gacha/poolInfo")
    public ApiResult<AiraGachaInfo> poolInfo(Integer gachaId) {
        if (gachaId == null) {
            throw new AiraIllegalParamsException("gachaId is null");
        }
        return ApiResult.success(airaGachaManager.gachaInfo(gachaId));
    }


}
