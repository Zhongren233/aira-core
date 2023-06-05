package moe.aira.core.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.client.es.CasinoClient;
import moe.aira.core.service.IJackpotService;
import moe.aira.entity.es.CasinoInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IJackpotServiceImpl implements IJackpotService {
    final
    CasinoClient casinoClient;

    public IJackpotServiceImpl(CasinoClient casinoClient) {
        this.casinoClient = casinoClient;
    }

    @Override
    public CasinoInfo fetchCurrentCasinoInfo() {
        JsonNode casino = casinoClient.casino();
        CasinoInfo casinoInfo = new CasinoInfo();
        casinoInfo.setOpenJackpot(casino.get("open_jackpot").asBoolean(false));
        casinoInfo.setJackpotPercent(casino.get("jackpot_percent").asInt());
        casinoInfo.setJackpotEndTime(casino.get("jackpot_end_time").asText());
        return casinoInfo;
    }

}
