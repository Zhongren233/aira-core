package moe.aira.onebot.manager.impl;

import moe.aira.onebot.entity.AiraGachaPoolDto;
import moe.aira.onebot.entity.AiraGachaResultDto;
import moe.aira.onebot.manager.IAiraGachaManager;
import org.springframework.stereotype.Component;

@Component
public class IAiraGachaManagerImpl implements IAiraGachaManager {
    @Override
    public AiraGachaResultDto gacha(Integer gachaPoolId, Integer count) {
        return null;
    }

    @Override
    public AiraGachaPoolDto currentGachaPool(Integer gachaPoolId) {
        return null;
    }
}
