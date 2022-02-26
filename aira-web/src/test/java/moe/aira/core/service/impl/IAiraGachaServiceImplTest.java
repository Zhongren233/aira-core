package moe.aira.core.service.impl;

import moe.aira.core.service.IAiraGachaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IAiraGachaServiceImplTest {
    @Autowired
    IAiraGachaService airaGachaService;

    @Test
    void gachaTenCount() {
        for (int i = 0; i <= 10; i++) {
            airaGachaService.gachaTenCount(null, 732713726L).forEach(
                    result-> System.out.println(result.toViewString())
            );
            System.out.println("end");
        }
    }
}