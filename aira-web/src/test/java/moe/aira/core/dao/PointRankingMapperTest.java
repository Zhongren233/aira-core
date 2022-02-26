package moe.aira.core.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointRankingMapperTest {
    @Autowired
    PointRankingMapper pointRankingMapper;

    @Test
    void test() {
        pointRankingMapper.selectPage(new Page<>(1, 50), null).getRecords().forEach(System.out::println);

    }
}