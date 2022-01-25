package moe.aira.core.entity.aira;

import lombok.Data;

import java.util.Date;

@Data
public class AiraBindRelation {
    private Long qqNumber;
    private Integer userId;
    private Boolean ban;
    private Date createTime;
    private Date updateTime;
}
