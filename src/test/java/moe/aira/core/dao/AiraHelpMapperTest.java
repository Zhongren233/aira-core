package moe.aira.core.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AiraHelpMapperTest {
    @Autowired
    AiraHelpMapper airaHelpMapper;

    @Test
    void help() {
        System.out.println(airaHelpMapper.selectList(new QueryWrapper<>()));
    }
}