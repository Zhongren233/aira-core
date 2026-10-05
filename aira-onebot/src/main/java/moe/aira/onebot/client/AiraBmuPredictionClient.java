package moe.aira.onebot.client;

import moe.aira.onebot.client.dto.PointPrediction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${aira.bmu-service-url}", name = "bmu-prediction-service", dismiss404 = true)
public interface AiraBmuPredictionClient {

    /**
     * @param horizons 逗号分隔的预测时间轴(小时), 如 6,24,72
     * @param atEnd    是否额外返回结算点的预测
     */
    @GetMapping("/events/current/point-prediction")
    PointPrediction fetchCurrentPointPrediction(@RequestParam("horizons") String horizons,
                                                @RequestParam("at_end") boolean atEnd);

}
