package moe.aira.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChartInfo {

    @JsonProperty("LaneCount")
    private Integer laneCount;
    @JsonProperty("Notes")
    private List<Note> notes;
    @JsonProperty("SpeedChanges")
    private List<SpeedChanges> speedChanges;

    @Data
    public static class Note {

        @JsonProperty("Id")
        private Integer id;
        @JsonProperty("Type")
        private Integer type;
        @JsonProperty("Time")
        private Integer time;
        @JsonProperty("Lane")
        private Integer lane;
        @JsonProperty("FlickDirection")
        private Integer flickDirection;
        @JsonProperty("ParentId")
        private Integer parentId;
        @JsonProperty("GroupId")
        private Integer groupId;
    }

    public class SpeedChanges {
    }
}
