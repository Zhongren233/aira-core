package moe.aira.onebot.client;

import moe.aira.onebot.client.dto.BmuAwardStats;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "${aira.bmu-service-url}", name = "bmu-stats-service")
public interface AiraBmuStatsClient {

    @GetMapping("/stats/award")
    BmuAwardStats fetchAwardStats();

}
