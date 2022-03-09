package moe.aira.core.entity.aira;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("aira_help")
public class Help {
    @TableId
    private Integer id;
    @TableField
    private String helpName;
    @TableField
    private String helpLevel;
    @TableField
    private String parentHelpId;
    @TableField
    private String helpDetail;
    @TableField
    private String helpInfo;
}
