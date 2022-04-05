package moe.aira.onebot.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import moe.aira.entity.aira.Card;
import moe.aira.onebot.config.AiraConfig;
import moe.aira.onebot.entity.AiraGachaResultDto;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@UtilityClass
public class AiraGachaImageUtil {
    private final static String assetPath;

    private final BufferedImage rainbowBackground;
    private final BufferedImage goldenBackground;
    private final BufferedImage background;

    static {
        assetPath = AiraConfig.getAssetsPath();
        BufferedImage temp = null;
        try {
            temp = ImageIO.read(new ClassPathResource("image/template/gacha/gacha-rainbow.png").getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        rainbowBackground = temp;

        temp = null;
        try {
            temp = ImageIO.read(new ClassPathResource("image/template/gacha/gacha-golden.png").getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
        goldenBackground = temp;

        temp = null;
        try {
            temp = ImageIO.read(new ClassPathResource("image/template/gacha/gacha.png").getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        background = temp;
    }

    public static BufferedImage generateImage(AiraGachaResultDto data) {
        BufferedImage backGround = switch (data.getType()) {
            case GOLDEN -> goldenBackground;
            case RAINBOW -> rainbowBackground;
            default -> background;
        };
        BufferedImage image = new BufferedImage(backGround.getWidth(), backGround.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawImage(backGround, 0, 0, null);
        List<Card> cards = data.getCards();
        int x = 90;
        int y = 100;
        for (Card card : cards) {
            Integer cardId = card.getCardId();
            BufferedImage cardImage = readCardImage(cardId);
            if (cardImage != null) {
                graphics.drawImage(cardImage, x, y, null);
            }
            x += 210;
            if (x > 930) {
                x = 90;
                y = 375;
            }
        }

        return image;
    }

    private static BufferedImage readCardImage(Integer cardId) {
        File file = new File(assetPath + "/Card/rectangle2/card_rectangle2_" + cardId + "_normal.png");
        if (file.isFile()) {
            try {
                BufferedImage read = ImageIO.read(file);
                if (read.getWidth() == 180) {
                    return read;
                }
                //resize
                BufferedImage image = new BufferedImage(180, 225, BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics = image.createGraphics();
                graphics.drawImage(read, 0, 0, null);
                return image;
            } catch (IOException e) {
                log.error("Failed to read card image", e);
                return null;
            }
        } else {
            log.warn("Card image not found: {}", cardId);
            return null;
        }
    }
}


