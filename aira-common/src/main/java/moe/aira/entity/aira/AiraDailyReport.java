package moe.aira.entity.aira;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("aira_daily_report")
public class AiraDailyReport {
    private Integer id;

    private Integer eventId;

    private Integer eventReportNo;

    private String reportKey;

    private String reportValue;

    private Date createTime;

}
