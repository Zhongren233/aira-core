package moe.aira.entity.hekk;

import lombok.Data;

import java.lang.String;

@Data
public class Chapter {
    public Long id;
    public String name;
    public Integer storyTypeId;
    public Integer storyTypeTargetId;
    public Integer eventAnnounceId;
    public Integer storiesCount;
    public Integer seasonId;
    public String releasableBeganAt;
    public String beganAt;
    public Integer orderNum;
    public Integer timescaleOrderNum;
    public String createdAt;
    public String updatedAt;
    public StoryLimitedRelease storyLimitedRelease;

    @Data
    public static class StoryLimitedRelease {
        private Long id;
        private Long chapterId;
        private String createdAt;
        private String updatedAt;
        private String beganAt;
        private String endedAt;

    }
}
