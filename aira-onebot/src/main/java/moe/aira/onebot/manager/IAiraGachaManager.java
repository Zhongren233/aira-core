package moe.aira.onebot.manager;

import moe.aira.entity.aira.AiraGachaInfo;
import moe.aira.onebot.entity.AiraGachaResultDto;

import java.util.Set;

public interface IAiraGachaManager {

    Set<Integer> currentGacha();

    AiraGachaInfo gachaInfo(Integer gachaId);

    AiraGachaResultDto gacha(AiraGachaInfo gachaInfo, Integer count);
}
