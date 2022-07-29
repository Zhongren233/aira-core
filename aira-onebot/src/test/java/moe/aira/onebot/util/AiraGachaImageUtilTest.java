package moe.aira.onebot.util;

import moe.aira.onebot.entity.AiraGachaResultDto;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

class AiraGachaImageUtilTest {

    @Test
    void generateImage() throws IOException {
        AiraGachaResultDto data = new AiraGachaResultDto();
        data.setType(AiraGachaResultDto.ResultType.RAINBOW);
        ArrayList<Map.Entry<String, Integer>> cardIds = new ArrayList<>();
        data.setCardIds(cardIds);
        for (int i = 0; i < 10; i++) {
            cardIds.add(Map.entry("5", 3017));
        }
        BufferedImage image = AiraGachaImageUtil.generateImage(data);
        ImageIO.write(image, "png", new File("./test-1.png"));
        ImageIO.write(ImageUtil.bufferedImageToJpg(image), "jpg", new File("./test-2.jpg"));
        ImageIO.write(ImageUtil.bufferedImageToJpg(image, 0.8), "jpg", new File("./test-3.jpg"));

    }
}