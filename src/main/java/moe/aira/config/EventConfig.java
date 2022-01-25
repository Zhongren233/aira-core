package moe.aira.config;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import moe.aira.enums.EventStatus;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@Data
public class EventConfig {
    private Integer eventId;
    private EventStatus eventStatus;



    public boolean isAvailable() {
        return eventStatus != EventStatus.Open && eventStatus != EventStatus.CountingEnd;
    }

    @XxlJob("updateEventStatus")
    public void updateEventStatus() {
        String jobParam = XxlJobHelper.getJobParam();
        try {
            eventStatus = EventStatus.valueOf(jobParam);
        } catch (IllegalArgumentException e) {
            log.error("未知的活动类型", e);
            XxlJobHelper.handleFail("未知的活动类型");
        }
        log.info("ok");
        XxlJobHelper.handleSuccess("ok");
    }


}

