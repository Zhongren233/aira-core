package moe.aira.onebot.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * GET /events/current/point-prediction 响应
 */
@Data
public class PointPrediction {

    @JsonProperty("event_id")
    private Integer eventId;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("began_at")
    private String beganAt;

    @JsonProperty("counting_ended_at")
    private String countingEndedAt;

    /**
     * 活动开始到计分结束的小时数
     */
    @JsonProperty("counting_hours")
    private Integer countingHours;

    /**
     * 活动开始到结算点的小时数，即 at_end 预测对应的 horizon
     */
    @JsonProperty("end_hours")
    private Integer endHours;

    @JsonProperty("last_snapshot_at")
    private String lastSnapshotAt;

    /**
     * 最后快照所对应的活动已进行小时数
     */
    @JsonProperty("anchor_h")
    private Integer anchorH;

    /**
     * 距离结算点剩余小时数
     */
    @JsonProperty("remaining_h")
    private Integer remainingH;

    /**
     * 模型训练时间(UTC)
     */
    @JsonProperty("trained_at")
    private String trainedAt;

    private List<Prediction> predictions;

    @Data
    public static class Prediction {

        private Integer rank;

        /**
         * 相对当前快照的未来小时数
         */
        @JsonProperty("horizon_h")
        private Integer horizonH;

        /**
         * 预测目标所在的活动已进行小时数
         */
        @JsonProperty("target_h")
        private Integer targetH;

        @JsonProperty("target_time")
        private String targetTime;

        @JsonProperty("current_point")
        private Long currentPoint;

        /**
         * 预测点数
         */
        private Double point;

        /**
         * 预测区间下界
         */
        private Double low;

        /**
         * 预测区间上界
         */
        private Double high;
    }
}
