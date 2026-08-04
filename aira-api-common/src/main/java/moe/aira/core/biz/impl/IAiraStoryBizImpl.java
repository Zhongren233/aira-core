package moe.aira.core.biz.impl;

import moe.aira.core.biz.IAiraStoryBiz;
import moe.aira.core.client.hekk.ESMStoryClient;
import moe.aira.entity.api.AiraStoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IAiraStoryBizImpl implements IAiraStoryBiz {
    @Autowired
    ESMStoryClient storyClient;

    public AiraStoryResponse readStories() {
        return null;
    }
}
