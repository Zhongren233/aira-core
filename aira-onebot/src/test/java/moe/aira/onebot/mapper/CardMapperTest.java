package moe.aira.onebot.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CardMapperTest {
    @Autowired
    CardMapper cardMapper;

    @Test
    void name() {
        cardMapper.selectList(new QueryWrapper<>()).forEach(System.out::println);
    }
}