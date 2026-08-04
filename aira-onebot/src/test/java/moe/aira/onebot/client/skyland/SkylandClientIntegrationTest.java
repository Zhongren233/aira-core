package moe.aira.onebot.client.skyland;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 真实接口集成测试：需要设置环境变量 YJ_TOKEN（鹰角通行证 token）才会执行。
 * 覆盖 dId 生成 → token 校验 → 角色列表 → 签到 完整链路。
 */
class SkylandClientIntegrationTest {
    private final SkylandClient client = new SkylandClient();

    @Test
    void fullFlow() {
        String token = System.getenv("YJ_TOKEN");
        Assumptions.assumeTrue(token != null && !token.isBlank(), "未设置环境变量 YJ_TOKEN，跳过集成测试");

        SkylandClient.CredInfo cred = client.getCredByToken(token);
        assertNotNull(cred.token());
        assertNotNull(cred.cred());

        List<SkylandClient.Character> characters = client.getBindingList(cred);
        assertFalse(characters.isEmpty(), "应至少有一个明日方舟绑定角色");
        for (SkylandClient.Character character : characters) {
            SkylandClient.SignResult result = client.signCharacter(cred, character);
            System.out.println("SIGN RESULT: " + result.message());
            assertNotNull(result.message());
            assertTrue(result.message().contains(character.nickName()));
            // "今日已签到/签到成功" 均视为正常，不标记为失败
            if (result.message().contains("签到失败")) {
                assertFalse(result.success());
            } else {
                assertTrue(result.success());
            }
        }
    }

    @Test
    void invalidTokenFails() {
        SkylandApiException exception = org.junit.jupiter.api.Assertions.assertThrows(
                SkylandApiException.class, () -> client.getCredByToken("invalid-token-123"));
        assertEquals("获得认证代码失败：Key: 'GrantV2Req.token' Error:Field validation for 'token' failed on the 'base64' tag",
                exception.getMessage());
    }
}
