package moe.aira.onebot.util;

import moe.aira.entity.aira.AiraEventPointDto;
import moe.aira.entity.aira.AiraEventScoreDto;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

class AiraRankingImageUtilTest {

    @Test
    void generatorPointImage() throws IOException {
        ArrayList<AiraEventPointDto> data = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            AiraEventPointDto airaEventPointDto = new AiraEventPointDto();
            airaEventPointDto.setPoint(ThreadLocalRandom.current().nextInt(1000000,Integer.MAX_VALUE));
            data.add(airaEventPointDto);
        }
        data.sort((o1, o2) -> o2.getPoint() - o1.getPoint());
        BufferedImage bufferedImage = AiraRankingImageUtil.generatorPointImage(data);
        ImageIO.write(bufferedImage, "png", new java.io.File("test.png"));
    }

    @Test
    void generatorScoreImage() throws IOException {
        ArrayList<AiraEventScoreDto> data = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            AiraEventScoreDto airaEventPointDto = new AiraEventScoreDto();
            airaEventPointDto.setScore(ThreadLocalRandom.current().nextInt(1000000,Integer.MAX_VALUE));
            data.add(airaEventPointDto);
        }
        data.sort((o1, o2) -> o2.getScore() - o1.getScore());
        BufferedImage bufferedImage = AiraRankingImageUtil.generatorScoreImage(data);
        ImageIO.write(bufferedImage, "png", new java.io.File("test.png"));
    }
}