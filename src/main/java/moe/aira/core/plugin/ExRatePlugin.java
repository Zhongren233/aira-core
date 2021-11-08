package moe.aira.core.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.service.IExRateService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Slf4j
public class ExRatePlugin extends BotPlugin {
    final
    IExRateService exRateService;

    public ExRatePlugin(IExRateService exRateService) {
        this.exRateService = exRateService;
    }

    @Override
    public int onPrivateMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {
        if (event.getMessage().trim().toUpperCase().endsWith("JPY")) {
            String jpy = event.getMessage().trim().toUpperCase().replace("JPY", "").trim();
            try {
                int integer = Integer.parseInt(jpy);
                JsonNode jsonNode = exRateService.fetchCNYRate();
                BigDecimal bigDecimal = jsonNode.get("rates").get("JPY").decimalValue();
                BigDecimal divide = new BigDecimal(integer).divide(bigDecimal, 2, RoundingMode.UP);
                bot.sendPrivateMsg(event.getUserId(), integer + "日元=" + divide + "人民币", true);
            } catch (NumberFormatException ignored) {
            } catch (Exception e) {
                bot.sendPrivateMsg(event.getUserId(), "获取汇率时发生了异常...", true);
                log.error("获取汇率时发生异常..", e);
            }
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }

    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        if (event.getMessage().trim().toUpperCase().endsWith("JPY")) {
            String jpy = event.getMessage().trim().toUpperCase().replace("JPY", "").trim();
            try {
                int integer = Integer.parseInt(jpy);
                JsonNode jsonNode = exRateService.fetchCNYRate();
                BigDecimal bigDecimal = jsonNode.get("rates").get("JPY").decimalValue();
                BigDecimal divide = new BigDecimal(integer).divide(bigDecimal, 2, RoundingMode.UP);
                bot.sendGroupMsg(event.getGroupId(), integer + "日元=" + divide + "人民币", true);
            } catch (NumberFormatException ignored) {
            } catch (Exception e) {
                bot.sendPrivateMsg(event.getUserId(), "获取汇率时发生了异常...", true);
                log.error("获取汇率时发生异常..", e);
            }
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }
}
