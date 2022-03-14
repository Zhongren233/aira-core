package moe.aira.core.service;

import moe.aira.core.entity.aira.GachaResult;

import java.util.List;

public interface IAiraGachaService {
    List<GachaResult> gachaTenCount(String gachaId, Long qqNumber);
}
