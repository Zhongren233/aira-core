package moe.aira.onebot.entity;

import lombok.Data;
import moe.aira.config.EventConfig;
import moe.aira.entity.aira.AiraEventPointDto;
import moe.aira.entity.aira.AiraEventScoreDto;
import moe.aira.onebot.client.dto.BmuAwardStats;

import java.util.List;

@Data
public class EventReportDto {
    private Integer eventId;
    private EventConfig eventConfig;
    private String formatDate;

    private List<BmuAwardStats.Award> awards;

    private List<AiraEventPointDto> eventPoint;

    private List<AiraEventScoreDto> eventScore;


}
