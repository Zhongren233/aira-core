package moe.aira.onebot.util;

import moe.aira.config.EventConfig;
import moe.aira.enums.EventType;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

class AiraAwardImageUtilTest {

    @Test
    void generateAwardImage() throws Exception {
        EventConfig eventConfig = new EventConfig();
        eventConfig.setEventType(EventType.UNIT);
        HashMap<Integer, Integer> data = new HashMap<>();
        data.put(3500000, 99999);
        data.put(7500000, 2364);
        data.put(11000000, 788);
        data.put(15000000, 114);
        data.put(22000000, 14);
        BufferedImage image = AiraAwardImageUtil.generateAwardImage(eventConfig, data);
        ImageIO.write(image, "png", new File("test.png"));
    }
}