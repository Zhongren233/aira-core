package moe.aira.onebot.plugin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import moe.aira.entity.aira.AiraBirthdayView;
import moe.aira.onebot.mapper.AiraBirthdayViewMapper;
import moe.aira.onebot.util.AiraBotPlugin;
import moe.aira.onebot.util.AiraSendMessageUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BirthdayPlugin extends AiraBotPlugin {
    @Autowired
    AiraBirthdayViewMapper airaBirthdayViewMapper;

    @Override
    public boolean checkMessage(@NotNull Bot bot, @NotNull MessageEvent event) {
        String message = event.getMessage().replaceFirst("！", "!");
        return message.startsWith("!birthday");

    }

    @Override
    public Runnable doCommand(Bot bot, MessageEvent event) {
        return () -> {

            String substring = event.getMessage().substring(9);
            String trim = substring.trim();
            List<AiraBirthdayView> airaBirthdayViews;
            if (trim.isBlank()) {
                QueryWrapper<AiraBirthdayView> queryWrapper = new QueryWrapper<>();
                queryWrapper.orderByAsc("next_birthday_countdown");
                queryWrapper.last("limit 6");
                airaBirthdayViews = airaBirthdayViewMapper.selectList(queryWrapper);
            } else {
                //check 月份有效
                String replace = trim.replace("月", "");
                try {
                    int i = Integer.parseInt(replace);
                    if (i <= 0 || i >= 13) {
                        throw new IllegalArgumentException();
                    }
                    QueryWrapper<AiraBirthdayView> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("birth_month", i);
                    airaBirthdayViews = airaBirthdayViewMapper.selectList(queryWrapper);
                } catch (IllegalArgumentException e) {
                    AiraSendMessageUtil.sendMessage(bot, event, "不合法的月份");
                    return;
                }
            }
            String returnMessage = renderReturnString(airaBirthdayViews);
            AiraSendMessageUtil.sendMessage(bot, event, returnMessage);
        };
    }


    private String renderReturnString(List<AiraBirthdayView> airaBirthdayViews) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<Integer, List<AiraBirthdayView>> collect = airaBirthdayViews.stream().collect(Collectors.groupingBy(AiraBirthdayView::getNextBirthdayCountdown));
        List<AiraBirthdayView> todayBirthday = collect.get(0);
        List<AiraBirthdayView> tomorrowBirthday = collect.get(1);
        if (todayBirthday != null) {
            stringBuilder.append("꒰ঌ ℍ𝕒𝕡𝕡𝕪 𝔹𝕚𝕣𝕥𝕙𝕕𝕒𝕪 ໒꒱\n今天是").append(todayBirthday.stream().map(AiraBirthdayView::getCharacterNameCn).collect(Collectors.joining(" 和 "))).append("的生日\n\n");
        }
        if (tomorrowBirthday != null) {
            stringBuilder.append("明天是").append(tomorrowBirthday.stream().map(AiraBirthdayView::getCharacterNameCn).collect(Collectors.joining(" 和 "))).append("的生日\n");
        }
        SimpleDateFormat mmdd = new SimpleDateFormat("MM/dd");
        airaBirthdayViews.removeIf(airaBirthdayView -> airaBirthdayView.getNextBirthdayCountdown() < 2);
        airaBirthdayViews.forEach(
                airaBirthdayView -> {
                    stringBuilder.append("距离");
                    stringBuilder.append(airaBirthdayView.getCharacterNameCn());
                    stringBuilder.append("的生日(");
                    stringBuilder.append(mmdd.format(airaBirthdayView.getNextBirthday()));
                    stringBuilder.append(")还有");
                    stringBuilder.append(airaBirthdayView.getNextBirthdayCountdown());
                    stringBuilder.append("天\n");
                }
        );
        return stringBuilder.toString();
    }
}
