package moe.aira.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class AiraAssetTree {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer parentId;
    private String fullPath;
    private String name;
    private String md5;
    private Boolean dictionary;

    @Override
    public String toString() {
        return id + "," + parentId + "," + fullPath + "," + name + "," + md5 + "," + (dictionary ? 1 : 0) + "\n";
    }
}
