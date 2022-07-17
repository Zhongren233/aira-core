package moe.aira.onebot.entity;

import lombok.Data;
import moe.aira.config.EventConfig;
import moe.aira.entity.aira.AiraEventPointDto;
import moe.aira.entity.aira.AiraEventScoreDto;

import java.util.List;
import java.util.Map;

@Data
public class EventReportDto {
    private Integer eventId;
    private EventConfig eventConfig;
    private String formatDate;

    private Map<Integer, Integer> countMap;

    private List<AiraEventPointDto> eventPoint;

    private List<AiraEventScoreDto> eventScore;


}
