package moe.aira.task;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.service.IEventRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AiraFetchRankingJob {
    final
    IEventRankingService eventRankingService;

    public AiraFetchRankingJob(IEventRankingService eventRankingService) {
        this.eventRankingService = eventRankingService;
    }

    @XxlJob("fetchRankingJobHandle")
    public void fetchRanking() {
        try {
            eventRankingService.fetchAllEventRanking();
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            log.error("", e);
            XxlJobHelper.handleFail(e.getMessage());
        }
    }

}
