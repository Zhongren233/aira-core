package moe.aira.onebot.util;

import moe.aira.enums.ColorType;
import moe.aira.onebot.entity.AiraCardSppDto;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class AiraSppImageUtilTest {

    @Test
    void generateImage() throws IOException {
        ArrayList<AiraCardSppDto> data = new ArrayList<>();
        AiraCardSppDto e = new AiraCardSppDto();
        e.setCardId("2195");
        e.setSongId("91");
        e.setSongColorType(ColorType.SPARKLE);
        e.setCardName("踏向的前方，世界的未知");
        e.setIdolName("乙狩阿多尼斯");
        e.setSongNameJp("メテオ・スクランブル☆流星隊!");
        e.setSongNameCn("流星・紧急出动☆流星队!");
        data.add(e);

        AiraCardSppDto e2 = new AiraCardSppDto();
        e2.setCardId("2197");
        e2.setSongId("91");
        e2.setSongColorType(ColorType.GLITTER);
        e2.setCardName("踏向的前方");
        e2.setIdolName("乙狩阿多尼斯");
        e2.setSongNameJp("メテオ・スクランブル☆流星隊!");
        e2.setSongNameCn("流星・紧急出动☆流星队!");
        data.add(e2);

        AiraCardSppDto e3 = new AiraCardSppDto();
        e3.setCardId("2197");
        e3.setSongId("12");
        e3.setSongColorType(ColorType.ALL);
        e3.setCardName("踏");
        e3.setIdolName("乙狩阿多尼斯");
        e3.setSongNameJp("メテオ・スクランブル☆流星隊!");
        e3.setSongNameCn("流星・紧急出动☆流星队!");
        data.add(e3);

        BufferedImage image = AiraSppImageUtil.generateImage(data);

        ImageIO.write(image, "png", new File("./test-1.png"));
        ImageIO.write(ImageUtil.bufferedImageToJpg(image), "jpg", new File("./test-2.jpg"));
        ImageIO.write(ImageUtil.bufferedImageToJpg(image, 0.8), "jpg", new File("./test-3.jpg"));
    }
}