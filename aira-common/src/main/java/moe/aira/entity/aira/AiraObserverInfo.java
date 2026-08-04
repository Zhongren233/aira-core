package moe.aira.entity.aira;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("aira_observer_info")
public class AiraObserverInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Date expiredTime;

}
