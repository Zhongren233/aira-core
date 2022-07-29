package moe.aira.entity.aira;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@TableName("aira_core_search_dict")
@Data
public class SearchDict {
    @TableId("id")
    private String id;
    @TableField("search_key")
    private String searchKey;
    @TableField("search_column")
    private String searchColumn;
    @TableField("search_value")
    private String searchValue;
}
