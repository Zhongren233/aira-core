package moe.aira.resp.hekk;

import lombok.Data;
import moe.aira.entity.hekk.Chapter;
import moe.aira.entity.hekk.Event;
import moe.aira.entity.hekk.Story;
import moe.aira.entity.hekk.UserStory;

import java.util.List;

@Data
public class CampaignChapterStoriesResponse extends ServerResponse {
    private Chapter chapter;
    private List<Story> stories;
    private List<UserStory> userStories;
    private Event event;


}
