package moe.aira.core.entity.aira;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("aira_config")
@Data
@Accessors(chain = true)
public class AiraConfig {
    @TableId(type = IdType.AUTO)
    private String id;
    @TableField("config_key")
    private String configKey;
    @TableField("config_value")
    private String configValue;
    @TableField("remarks")
    private String remarks;
    @TableField("update_time")
    private String updateTime;

}
