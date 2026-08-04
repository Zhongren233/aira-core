package moe.aira.onebot.client;

import moe.aira.onebot.client.dto.BmuAiraRanking;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "${aira.bmu-service-url}", name = "bmu-service", dismiss404 = true)

public interface AiraBmuUserClient {

    @GetMapping("/users/{userId}/ranking")
    BmuAiraRanking fetchRealTimeAiraEventRanking(@PathVariable Integer userId);

}
