package moe.aira.onebot.mapper;

import moe.aira.entity.aira.AiraCardValue;
import moe.aira.enums.EvolutionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AiraCardValueMapperTest {
    @Autowired
    AiraCardValueMapper airaCardValueMapper;

    @Test
    void test() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("C:\\Users\\sc\\Downloads\\ES五星数据-Sheet1.csv"));
        String s1 = scanner.nextLine();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String[] split = s.split(",");
            try {
                AiraCardValue oneCard = new AiraCardValue();
                oneCard.setEvolutionType(EvolutionType.ONE);
                oneCard.setCardId(Integer.valueOf(split[1]));
                oneCard.setDaValue(Integer.valueOf(split[2]));
                oneCard.setVoValue(Integer.valueOf(split[3]));
                oneCard.setPfValue(Integer.valueOf(split[4]));
                airaCardValueMapper.insert(oneCard);
                AiraCardValue twoCard = new AiraCardValue();
                twoCard.setEvolutionType(EvolutionType.TWO);
                twoCard.setCardId(Integer.valueOf(split[1]));
                twoCard.setDaValue(Integer.valueOf(split[2 + 3]));
                twoCard.setVoValue(Integer.valueOf(split[3 + 3]));
                twoCard.setPfValue(Integer.valueOf(split[4 + 3]));
                airaCardValueMapper.insert(twoCard);
                AiraCardValue threeCard = new AiraCardValue();
                threeCard.setEvolutionType(EvolutionType.THREE);
                threeCard.setCardId(Integer.valueOf(split[1]));
                threeCard.setDaValue(Integer.valueOf(split[2 + 6]));
                threeCard.setVoValue(Integer.valueOf(split[3 + 6]));
                threeCard.setPfValue(Integer.valueOf(split[4 + 6]));
                airaCardValueMapper.insert(threeCard);
                AiraCardValue fourCard = new AiraCardValue();
                fourCard.setEvolutionType(EvolutionType.FOUR);
                fourCard.setCardId(Integer.valueOf(split[1]));
                fourCard.setDaValue(Integer.valueOf(split[2 + 9]));
                fourCard.setVoValue(Integer.valueOf(split[3 + 9]));
                fourCard.setPfValue(Integer.valueOf(split[4 + 9]));
                airaCardValueMapper.insert(fourCard);
                AiraCardValue fiveCard = new AiraCardValue();
                fiveCard.setEvolutionType(EvolutionType.FIVE);
                fiveCard.setCardId(Integer.valueOf(split[1]));
                fiveCard.setDaValue(Integer.valueOf(split[2 + 9 + 3]));
                fiveCard.setVoValue(Integer.valueOf(split[3 + 9 + 3]));
                fiveCard.setPfValue(Integer.valueOf(split[4 + 9 + 3]));
                airaCardValueMapper.insert(fiveCard);
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                System.out.println(Arrays.toString(split));

            }
        }
    }

}