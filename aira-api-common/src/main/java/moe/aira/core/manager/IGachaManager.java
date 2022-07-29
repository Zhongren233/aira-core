package moe.aira.core.manager;

import moe.aira.entity.aira.AiraGachaInfo;

import java.util.Set;

public interface IGachaManager {

    Set<Integer> currentGacha();

    AiraGachaInfo gachaInfo(Integer gachaId);
}
