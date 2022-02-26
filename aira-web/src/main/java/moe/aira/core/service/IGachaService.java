package moe.aira.core.service;

import moe.aira.core.entity.dto.GachaInfo;
import moe.aira.core.entity.dto.GachaPool;

import java.util.List;

public interface IGachaService {
    List<String> fetchCurrentGacha();

    GachaPool fetchGachaPool(String gachaId);

    GachaInfo fetchGachaProbability(String gachaId);

}
