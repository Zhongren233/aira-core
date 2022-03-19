package moe.aira.onebot.util;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

class AiraPrImageUtilTest {

    @Test
    void generatorImage() throws IOException {
        Map<Integer, Integer> data = new LinkedHashMap<>();
        data.put(1, 10 * 10000 * 10000);
        data.put(2, 7 * 10000 * 10000);
        data.put(3, 4 * 10000 * 10000);
        data.put(10, 9000 * 10000);
        data.put(100, 900 * 10000);
        data.put(1000, 90 * 10000);
        data.put(5000, 40 * 10000);
        data.put(10000, 40 * 10000);
        BufferedImage bufferedImage = AiraRankingImageUtil.generatorImage(data);
        ImageIO.write(bufferedImage, "png", new File("./test.png"));
    }
}