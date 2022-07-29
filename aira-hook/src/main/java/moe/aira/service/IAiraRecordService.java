package moe.aira.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.scheduling.annotation.Async;

public interface IAiraRecordService {
    @Async
    void recordLiveResult(JsonNode node);
}
