package moe.aira.onebot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.aira.onebot.entity.AiraUser;
import moe.aira.onebot.entity.SkylandSignLog;
import moe.aira.onebot.mapper.AiraUserMapper;
import moe.aira.onebot.mapper.SkylandSignLogMapper;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 真实接口 + 真实数据库集成测试：需要设置环境变量 YJ_TOKEN 才会执行。
 * 使用临时用户，验证签到结果写入日志表，结束后清理。
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SkylandSignServiceTest {
    private static final Long TEST_QQ = 999999999L;

    @Autowired
    SkylandSignService skylandSignService;
    @Autowired
    AiraUserMapper airaUserMapper;
    @Autowired
    SkylandSignLogMapper skylandSignLogMapper;

    @Test
    void signWritesLogAndReturnsResult() {
        String token = System.getenv("YJ_TOKEN");
        Assumptions.assumeTrue(token != null && !token.isBlank(), "未设置环境变量 YJ_TOKEN，跳过集成测试");

        AiraUser tempUser = new AiraUser().setQqNumber(TEST_QQ).setUserId(0).setPermLevel(1).setYjToken(token);
        airaUserMapper.insert(tempUser);
        try {
            String result = skylandSignService.sign(tempUser);
            System.out.println("SIGN SERVICE RESULT: " + result);
            assertNotNull(result);
            assertFalse(result.isBlank());

            List<SkylandSignLog> logs = skylandSignLogMapper.selectList(
                    new QueryWrapper<SkylandSignLog>().eq("qq_number", TEST_QQ));
            assertFalse(logs.isEmpty(), "应写入签到日志");
            assertTrue(logs.get(0).getResult().contains("角色"), "日志应包含签到结果: " + logs.get(0).getResult());
        } finally {
            airaUserMapper.deleteById(tempUser.getId());
            skylandSignLogMapper.delete(new QueryWrapper<SkylandSignLog>().eq("qq_number", TEST_QQ));
        }
    }
}
