package moe.aira.entity.aira;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("es_story_line")
public class StoryLine {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer storyId;
    private Integer lineId;
    private String speaker;
    private String message;
    private String voice;
}
