package moe.aira.core.biz;

import moe.aira.entity.aira.AiraObserverDetail;

import java.util.List;

public interface IAiraObserverBiz {
    void observe();

    List<AiraObserverDetail> getObserverDetails(Integer userId);


}
