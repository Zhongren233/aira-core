package moe.aira.core.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.aira.core.entity.aira.AiraUser;
import moe.aira.core.service.IAiraBindRelationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AiraUserMapperTest {
    @Autowired
    IAiraBindRelationService airaBindRelationService;
    @Test
    void test() {
        QueryWrapper<AiraUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("qq_number", 732713726);
        System.out.println(airaBindRelationService.getOne(queryWrapper));
    }
}