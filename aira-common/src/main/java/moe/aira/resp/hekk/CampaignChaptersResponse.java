package moe.aira.resp.hekk;

import lombok.Data;
import moe.aira.entity.hekk.Chapter;
import moe.aira.entity.hekk.Event;

import java.util.List;
@Data
public class CampaignChaptersResponse extends ServerResponse {
    List<Chapter> chapters;
    List<Event> events;

}
