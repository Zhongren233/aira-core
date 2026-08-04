package moe.aira.entity.aira;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("aira_observer_detail")
public class AiraObserverDetail {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer eventId;
    private Long obEventPoint;
    private Date obTime;
}
