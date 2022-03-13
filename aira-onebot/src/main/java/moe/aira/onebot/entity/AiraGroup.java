package moe.aira.onebot.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AiraGroup {
    private Integer id;

    private String groupName;

    private String remarks;

    private Date createTime;

    private List<String> groupPerms;
}
