package moe.aira.onebot.manager;

import moe.aira.onebot.entity.AiraUser;

public interface IAiraUserManager {

    AiraUser findAiraUser(Long qqNumber);

    int updateAiraUser(AiraUser airaUser);

    Boolean cleanCache(Long qqNumber);


}
