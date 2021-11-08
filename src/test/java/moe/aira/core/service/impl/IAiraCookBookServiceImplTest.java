package moe.aira.core.service.impl;

import moe.aira.core.entity.aira.AiraCookBook;
import moe.aira.core.service.IAiraCookBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IAiraCookBookServiceImplTest {
    @Autowired
    IAiraCookBookService airaCookBookService;
    @Test
    void fetchCookBook() {
        for (int i = 0; i < 5; i++) {
            AiraCookBook airaCookBook = airaCookBookService.fetchCookBook();
            System.out.println("airaCookBook = " + airaCookBook);
        }

    }
}