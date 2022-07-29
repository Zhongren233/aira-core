package moe.aira.entity.es;

import lombok.Data;

@Data
public class UserInfo {

    private Integer uid;
    private String nickname;
    private Integer favCardId;
    private Boolean favCardEvolved;
    private Long lastActivedTimestamp;
    private String comment;
    private Integer level;
}
