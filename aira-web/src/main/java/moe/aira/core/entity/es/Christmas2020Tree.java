package moe.aira.core.entity.es;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data

@TableName("es_christmas_tree_record")
public class Christmas2020Tree {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("tree_id")
    private Integer treeId;
    @TableField("required_point")
    private Integer requiredPoint;
    @TableField("current_point")
    private Integer currentPoint;
    @TableField("create_time")
    private Long createTime;
    @TableField(exist = false)
    private Integer sizeTypeId;
}
