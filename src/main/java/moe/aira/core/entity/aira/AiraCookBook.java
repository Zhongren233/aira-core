package moe.aira.core.entity.aira;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("aira_cookbook")
public class AiraCookBook {
    @TableId
    private Integer id;

    private String name;

    private String createBy;

    private Boolean status;
}
