package moe.aira.onebot.manager;

import moe.aira.onebot.entity.AiraGachaPoolDto;
import moe.aira.onebot.entity.AiraGachaResultDto;

public interface IAiraGachaManager {

    AiraGachaResultDto gacha(Integer gachaPoolId, Integer count);

    AiraGachaPoolDto currentGachaPool(Integer gachaPoolId);

}
