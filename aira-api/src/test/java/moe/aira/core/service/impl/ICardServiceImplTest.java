package moe.aira.core.service.impl;

import moe.aira.core.service.ICardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ICardServiceImplTest {
    @Autowired
    ICardService cardService;

    @Test
    void test() {
        ArrayList<String> searchKeys = new ArrayList<>();
        searchKeys.add("bybd");
        cardService.searchCard(searchKeys).forEach(System.out::println);
    }

}