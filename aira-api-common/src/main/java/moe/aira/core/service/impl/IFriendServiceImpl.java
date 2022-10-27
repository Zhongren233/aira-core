package moe.aira.core.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.client.es.FriendClient;
import moe.aira.core.service.IFriendService;
import moe.aira.entity.es.UserInfo;
import moe.aira.exception.AiraException;
import moe.aira.exception.server.AiraTimeOutException;
import moe.aira.util.INodeParser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class IFriendServiceImpl implements IFriendService {
    final
    FriendClient friendClient;
    final
    INodeParser nodeParser;
    private final ReentrantLock reentrantLock = new ReentrantLock();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public IFriendServiceImpl(FriendClient friendClient, INodeParser nodeParser) {
        this.friendClient = friendClient;
        this.nodeParser = nodeParser;
    }

    @Override
    public List<UserInfo> fetchFriendSearchList(String uid) {
        try {
            if (reentrantLock.tryLock(10, TimeUnit.SECONDS)) {
                JsonNode node = friendClient.friendList(uid);
                final JsonNode node1 = node.get("search_result");
                if (node1 != null) {
                    return nodeParser.parseToUserInfo(node1);
                }
            }
        } catch (InterruptedException e) {
            throw new AiraException(e);
        } finally {
            reentrantLock.unlock();
        }
        throw new AiraTimeOutException("获取搜索列表超时");

    }

}
