package moe.aira.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("aira_live_result_drop")
@Data
@NoArgsConstructor
public class AiraLiveResultDrop {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer liveId;
    private Integer contentTypeId;
    private Integer contentId;
    private Integer contentsCount;
    private Integer flag;

    public AiraLiveResultDrop(Integer contentTypeId, Integer contentId, Integer contentsCount, Integer flag) {
        this.contentTypeId = contentTypeId;
        this.contentId = contentId;
        this.contentsCount = contentsCount;
        this.flag = flag;
    }
}
