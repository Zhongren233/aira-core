package moe.aira.core.service;

import moe.aira.entity.es.UserInfo;

import java.util.List;

public interface IFriendService {
    /**
     * 获取好友搜索列表
     *
     * @param uid uid或昵称
     */
    List<UserInfo> fetchFriendSearchList(String uid);
}
