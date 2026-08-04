package moe.aira.resp.hekk;

import lombok.Data;
import lombok.EqualsAndHashCode;
import moe.aira.entity.hekk.Story;

@EqualsAndHashCode(callSuper = true)
@Data
public class StoryReadResponse extends ServerResponse {
    private Story story;

}
