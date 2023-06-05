package moe.aira.onebot.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import moe.aira.onebot.osu.Mod;
import moe.aira.onebot.osu.UserScore;
import moe.aira.onebot.util.AiraBotPlugin;
import moe.aira.onebot.util.AiraSendMessageUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.StringJoiner;

@Component
public class OSUPracticePlugin extends AiraBotPlugin {
    public OSUPracticePlugin(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean checkMessage(@NotNull Bot bot, @NotNull MessageEvent event) {
        return event.getMessage().trim().startsWith("!mp");
    }

    final
    ObjectMapper objectMapper;

    @Override
    public Runnable doCommand(Bot bot, MessageEvent event) {
        String[] s = event.getMessage().split(" ");

        return () -> {
            try {
                if (s.length != 2) {
                    AiraSendMessageUtil.sendMessage(bot, event, "请输入正确的参数");
                    return;
                }
                URL url = new URL("https://osu.ppy.sh/api/get_match?k=5e309b0ff32b96e39603d3877c642cf3cfea0d39&mp=" + s[1]);
                JsonNode tree = objectMapper.readTree(url);
                StringJoiner stringJoiner = new StringJoiner("\n");
                for (final JsonNode game : tree.get("games")) {

                    String mapId = game.get("beatmap_id").asText();
                    String mods = game.get("mods").textValue();

                    Set<Mod> pubMods = Mod.getMods(Long.parseLong(mods));

                    for (final JsonNode score : game.get("scores")) {
                        UserScore userScore = new UserScore();
                        userScore.setMapId(mapId);
                        userScore.setMods(pubMods);

                        String user_id = score.get("user_id").textValue();
                        String user_score = score.get("score").textValue();
                        String user_mods = score.get("enabled_mods").textValue();
                        if (user_mods != null) {
                            Set<Mod> userMods = Mod.getMods(Long.parseLong(user_mods));
                            userScore.getMods().addAll(userMods);
                        }
                        userScore.setScore(user_score);
                        userScore.setUserId(user_id);
                        stringJoiner.add(userScore.toMessage());
                    }
                }
                AiraSendMessageUtil.sendMessage(bot, event, stringJoiner.toString());

            } catch (Exception e) {
                AiraSendMessageUtil.sendMessage(bot, event, "发生了异常:" + e.getMessage());
            }
        };
    }
}
