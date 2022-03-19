package moe.aira.onebot.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtil {
    private ImageUtil() {

    }

    public static String bufferImageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "png", output);
        return "base64://" + Base64.getEncoder().encodeToString(output.toByteArray());
    }


}