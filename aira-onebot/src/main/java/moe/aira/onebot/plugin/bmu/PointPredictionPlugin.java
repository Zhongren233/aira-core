package moe.aira.onebot.plugin.bmu;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.onebot.client.AiraBmuPredictionClient;
import moe.aira.onebot.client.dto.PointPrediction;
import moe.aira.onebot.util.AiraBotPlugin;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static moe.aira.onebot.util.AiraSendMessageUtil.sendMessage;

/**
 * 活动点数预测, 数据来自 bmu point-prediction 接口, 解析后的文本缓存 5 分钟
 */
@Component
@Slf4j
public class PointPredictionPlugin extends AiraBotPlugin {

    /**
     * 缓存解析后文本的 redis key
     */
    public static final String CACHE_KEY = "point_prediction";

    /**
     * 缓存有效期
     */
    public static final Duration CACHE_TTL = Duration.ofMinutes(5);

    /**
     * 请求的预测时间轴(小时)
     */
    public static final String HORIZONS = "6,24,72";

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("MM-dd HH:mm");
    private static final ZoneId EVENT_ZONE = ZoneId.of("Asia/Shanghai");

    private final AiraBmuPredictionClient predictionClient;
    private final StringRedisTemplate stringRedisTemplate;

    public PointPredictionPlugin(AiraBmuPredictionClient predictionClient, StringRedisTemplate stringRedisTemplate) {
        this.predictionClient = predictionClient;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean checkMessage(@NotNull Bot bot, @NotNull MessageEvent event) {
        if (bot.getSelfId() != 102198767) {
            return false;
        }
        String message = event.getMessage().trim().replaceFirst("[！/]", "!");
        return message.equals("!预测");
    }

    @Override
    public Runnable doCommand(Bot bot, MessageEvent event) {
        return () -> {
            try {
                String text = stringRedisTemplate.opsForValue().get(CACHE_KEY);
                if (text == null) {
                    PointPrediction prediction = predictionClient.fetchCurrentPointPrediction(HORIZONS, true);
                    if (prediction == null || prediction.getPredictions() == null || prediction.getPredictions().isEmpty()) {
                        sendMessage(bot, event, "暂无活动点数预测数据");
                        return;
                    }
                    text = format(prediction);
                    stringRedisTemplate.opsForValue().set(CACHE_KEY, text, CACHE_TTL);
                }
                sendMessage(bot, event, text);
            } catch (Exception e) {
                log.error("获取活动点数预测失败", e);
                sendMessage(bot, event, "获取活动点数预测失败，请稍后重试");
            }
        };
    }

    /**
     * 将接口响应解析为可读文本
     */
    public static String format(PointPrediction prediction) {
        StringBuilder builder = new StringBuilder();
        Integer remaining = prediction.getRemainingH();
        builder.append("活动点数预测 Event#").append(prediction.getEventId()).append('\n');
        builder.append("活动进度 ").append(prediction.getAnchorH()).append("h/").append(prediction.getEndHours())
                .append("h  剩余 ").append(remaining).append("h\n");
        builder.append("数据快照 ").append(formatTime(prediction.getLastSnapshotAt()))
                .append("  模型训练 ").append(formatTime(prediction.getTrainedAt())).append('\n');

        Map<Integer, List<PointPrediction.Prediction>> grouped = new LinkedHashMap<>();
        for (PointPrediction.Prediction item : prediction.getPredictions()) {
            grouped.computeIfAbsent(item.getHorizonH(), key -> new ArrayList<>()).add(item);
        }
        int rankWidth = rankWidth(prediction.getPredictions());
        for (Map.Entry<Integer, List<PointPrediction.Prediction>> entry : grouped.entrySet()) {
            Integer horizon = entry.getKey();
            List<PointPrediction.Prediction> items = entry.getValue();
            builder.append('\n');
            if (horizon != null && remaining != null && horizon >= remaining) {
                builder.append("【结束 +").append(horizon).append("h】");
            } else {
                builder.append("【+").append(horizon).append("h】");
            }
            builder.append(formatTime(items.get(0).getTargetTime())).append('\n');
            for (PointPrediction.Prediction item : items) {
                long current = item.getCurrentPoint() == null ? 0L : item.getCurrentPoint();
                long point = item.getPoint() == null ? current : Math.round(item.getPoint());
                builder.append(formatRank(item.getRank(), rankWidth)).append(' ')
                        .append(formatPoint(current)).append(" → ").append(formatPoint(point))
                        .append(" (").append(String.format(Locale.ROOT, "%+,d", point - current)).append(")")
                        .append("  预测区间 ").append(formatPoint(item.getLow())).append('~')
                        .append(formatPoint(item.getHigh())).append('\n');
            }
        }
        return builder.toString().stripTrailing();
    }

    private static int rankWidth(List<PointPrediction.Prediction> predictions) {
        int width = 0;
        for (PointPrediction.Prediction item : predictions) {
            width = Math.max(width, ("#" + formatPoint(item.getRank())).length());
        }
        return width;
    }

    private static String formatRank(Integer rank, int width) {
        return String.format(Locale.ROOT, "%-" + width + "s", "#" + formatPoint(rank));
    }

    private static String formatPoint(Number value) {
        if (value == null) {
            return "-";
        }
        return String.format(Locale.ROOT, "%,d", Math.round(value.doubleValue()));
    }

    private static String formatTime(String isoTime) {
        if (isoTime == null || isoTime.isBlank()) {
            return "-";
        }
        try {
            return OffsetDateTime.parse(isoTime).atZoneSameInstant(EVENT_ZONE).format(TIME_FORMAT);
        } catch (DateTimeParseException ignored) {
            // 无时区的本地时间(东京时间), 原样展示
        }
        try {
            return LocalDateTime.parse(isoTime).format(TIME_FORMAT);
        } catch (DateTimeParseException ignored) {
            return isoTime;
        }
    }
}
