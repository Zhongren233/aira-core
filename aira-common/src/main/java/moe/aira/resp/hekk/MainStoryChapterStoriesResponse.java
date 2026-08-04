package moe.aira.resp.hekk;

import lombok.Data;
import moe.aira.entity.hekk.Chapter;
import moe.aira.entity.hekk.Event;
import moe.aira.entity.hekk.Story;

import java.util.List;

@Data
public class MainStoryChapterStoriesResponse extends ServerResponse {
    private Chapter chapter;
    private List<Story> stories;


}
