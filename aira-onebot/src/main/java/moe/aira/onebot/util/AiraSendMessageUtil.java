package moe.aira.onebot.util;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.action.common.ActionData;
import com.mikuac.shiro.dto.action.common.MsgId;
import com.mikuac.shiro.dto.event.message.WholeMessageEvent;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
public class AiraSendMessageUtil {
    private AiraSendMessageUtil() {
    }

    public static void sendMessage(Bot bot, WholeMessageEvent event, MsgUtils builder) {
        String messageType = event.getMessageType();
        String message = builder.build();
        log.info("回复{}:{}", event.getUserId(), message);
        if (messageType.equals("group")) {
            ActionData<MsgId> actionData = bot.sendGroupMsg(event.getGroupId(), MsgUtils.builder().at(event.getUserId()).text("\n" + message).build(), false);
            log.info("响应信息:{}", actionData);
        } else {
            bot.sendPrivateMsg(event.getUserId(), message, false);
        }
    }

    public static String parseStringToBase64Image(String content) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(700, 370, BufferedImage.TYPE_INT_BGR);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(new Color(209, 248, 245));
        graphics.fillRect(0, 0, 700, 400);
        Font font = new Font("悠哉字体", Font.BOLD, 50);
        graphics.setFont(font);
        String[] split = content.split("\n");
        Stroke tmpStroke = graphics.getStroke();
        for (int i = 0; i < split.length; i++) {
            GlyphVector glyphVector = font.createGlyphVector(graphics.getFontRenderContext(), split[i]);
            Shape outline = glyphVector.getOutline(30, 60 + (i * 70));
            graphics.setStroke(tmpStroke);
            graphics.setColor(new Color(234, 180, 106));
            graphics.fill(outline);
//            graphics.setStroke(new BasicStroke(0.5f));
//            graphics.setColor(new Color(0, 0, 0));
//            graphics.draw(outline);
        }
        graphics.dispose();
        ImageIO.write(bufferedImage, "PNG", new File("./test.png"));

        return "data:image/png;base64," + content;
    }
}
