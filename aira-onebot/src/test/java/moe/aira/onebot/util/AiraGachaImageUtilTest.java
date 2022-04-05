package moe.aira.onebot.util;

import moe.aira.entity.aira.Card;
import moe.aira.onebot.entity.AiraGachaResultDto;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class AiraGachaImageUtilTest {

    @Test
    void generateImage() throws IOException {
        AiraGachaResultDto data = new AiraGachaResultDto();
        data.setType(AiraGachaResultDto.ResultType.RAINBOW);
        ArrayList<Card> cards = new ArrayList<>();
        Card card = new Card();
        card.setCardId(3017);
        for (int i = 0; i < 10; i++) {
            cards.add(card);
        }
        data.setCards(cards);
        BufferedImage image = AiraGachaImageUtil.generateImage(data);
        ImageIO.write(image, "png", new File("./test-1.png"));
        ImageIO.write(ImageUtil.bufferedImageToJpg(image), "jpg", new File("./test-2.jpg"));
        ImageIO.write(ImageUtil.bufferedImageToJpg(image, 0.8), "jpg", new File("./test-3.jpg"));

    }
}