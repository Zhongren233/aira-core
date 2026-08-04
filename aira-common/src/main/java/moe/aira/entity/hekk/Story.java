package moe.aira.entity.hekk;

import lombok.Data;
import moe.aira.resp.hekk.StoryReadResponse;

import java.util.List;

@Data
public class Story {
    private Long id;
    private String title;
    private String subTitle;
    private Long chapterId;
    private List<Paragraph> paragraphs;
    private Integer seasonId;
    private Integer orderNum;
    private Integer timescaleOrderNum;
    private Long nextStoryId;

    @Data
    public static class Paragraph {
        private String bg;
        private String bgm;
        private String speaker;
        private String message;
        private String voice;
        private String cName;
        private String cMotion;
        private String lName;
        private String lMotion;
        private String rName;
        private String rMotion;
    }
}
