package moe.aira.entity.aira;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("aira_tricolor_log")
public class AiraTricolorLog {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer teamId;
    private Integer battleId;
    private Integer score;
    private Date logTime;
    private Integer periodId;
    private Boolean fever;
}
