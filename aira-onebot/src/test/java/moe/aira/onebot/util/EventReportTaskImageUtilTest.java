package moe.aira.onebot.util;

import moe.aira.onebot.manager.IEventConfigManager;
import moe.aira.onebot.task.EventReportTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventReportTaskImageUtilTest {
    @Autowired
    EventReportTask eventReportTask;
    @Autowired
    IEventConfigManager eventConfigManager;

    @Test
    void name() throws Exception {
        BufferedImage bufferedImage = eventReportTask.getBufferedImage(eventConfigManager.fetchEventConfig());
        assert bufferedImage != null;
        ImageIO.write(bufferedImage, "PNG", new File("./image.png"));
    }
}