package moe.aira.onebot.manager;

import moe.aira.onebot.entity.AiraCardSppDto;

import java.util.List;

public interface IAiraSppManager {

    List<AiraCardSppDto> searchCardsSpp(List<String> params);

}
