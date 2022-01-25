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

}

