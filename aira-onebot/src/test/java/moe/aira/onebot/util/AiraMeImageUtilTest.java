package moe.aira.onebot.util;

import moe.aira.entity.aira.AiraEventRanking;
import moe.aira.entity.es.PointRanking;
import moe.aira.entity.es.ScoreRanking;
import moe.aira.entity.es.UserProfile;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

class AiraMeImageUtilTest {

    @Test
    void generatorImage() throws IOException {

        AiraEventRanking eventRanking = new AiraEventRanking();
        eventRanking.setPointUpdateTime(new Date());
        UserProfile userProfile = new UserProfile();
        userProfile.setUserName("称里ワジーの个汉字");
//        userProfile.setUserName("AiraBot");
        eventRanking.setUserProfile(userProfile);
        ScoreRanking scoreRanking = new ScoreRanking();
        scoreRanking.setEventPoint(20000000);
        scoreRanking.setEventRank(235649);
        eventRanking.setScoreRanking(scoreRanking);

        PointRanking pointRanking = new PointRanking();
        pointRanking.setEventPoint(1314114514);
        pointRanking.setEventRank(1);
        eventRanking.setPointRanking(pointRanking);
        BufferedImage bufferedImage = AiraMeImageUtil.generatorImage(eventRanking);
        ImageIO.write(bufferedImage, "png", new File("./test.png"));
    }
}