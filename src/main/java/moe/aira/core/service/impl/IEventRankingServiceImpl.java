package moe.aira.core.service.impl;

import com.dtflys.forest.callback.OnSuccess;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.client.es.EventsClient;
import moe.aira.core.client.es.PointRankingClient;
import moe.aira.core.entity.es.Christmas2020Tree;
import moe.aira.core.entity.es.PointRanking;
import moe.aira.core.entity.es.UserProfile;
import moe.aira.core.dao.PointRankingMapper;
import moe.aira.core.dao.UserProfileMapper;
import moe.aira.core.service.IEventRankingService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class IEventRankingServiceImpl implements IEventRankingService {

}