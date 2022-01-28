package moe.aira.core.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import moe.aira.core.entity.aira.AiraBindRelation;
import moe.aira.core.service.IAiraBindRelationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AiraBindRelationMapperTest {
    @Autowired
    IAiraBindRelationService airaBindRelationService;
    @Test
    void test() {
        QueryWrapper<AiraBindRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("qq_number", 732713726);
        System.out.println(airaBindRelationService.getOne(queryWrapper));
    }
}