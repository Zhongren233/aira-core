package moe.aira.core.entity.aira;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("aira_gacha_record")
public class GachaRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long qqNumber;
    private Integer quickHash;
    private Integer cardId;
    private String gachaId;
    private Date createTime;
}
