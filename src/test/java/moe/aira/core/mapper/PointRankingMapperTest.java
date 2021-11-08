package moe.aira.core.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import moe.aira.core.entity.es.PointRanking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PointRankingMapperTest {
    @Autowired
    PointRankingMapper pointRankingMapper;

    @Test
    void test() {
        pointRankingMapper.selectPage(new Page<>(1, 50), null).getRecords().forEach(System.out::println);

    }
}