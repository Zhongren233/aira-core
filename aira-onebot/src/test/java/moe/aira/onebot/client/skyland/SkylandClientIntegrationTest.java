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
        String token = "ftgOAa4LDWoCPFVqqwdD4g6C";
        Assumptions.assumeTrue(token != null && !token.isBlank(), "未设置环境变量 YJ_TOKEN，跳过集成测试");

        SkylandClient.CredInfo cred = client.getCredByToken(token);
        assertNotNull(cred.token());
        assertNotNull(cred.cred());

        SkylandClient.BindingApps apps = client.getBindingApps(cred);
        List<SkylandClient.Character> characters = apps.arknightsCharacters();
        assertFalse(characters.isEmpty(), "应至少有一个明日方舟绑定角色");
        for (SkylandClient.Character character : characters) {
            SkylandClient.SignResult result = client.signArknights(cred, character);
            System.out.println("SIGN RESULT: " + result.message());
            assertNotNull(result.message());
            assertTrue(result.message().contains(character.nickName()));
            assertTrue(result.message().startsWith("[明日方舟]"), "日志应标明角色所属游戏: " + result.message());
            // "今日已签到/签到成功" 均视为正常，不标记为失败
            if (result.message().contains("签到失败")) {
                assertFalse(result.success());
            } else {
                assertTrue(result.success());
            }
        }

        // 终末地：绑定角色存在则执行签到，未绑定角色属正常情况；可能绑定多个角色，逐个签到
        for (SkylandClient.EndfieldRole endfieldRole : apps.endfieldRoles()) {
            assertFalse(endfieldRole.nickname().isBlank());
            SkylandClient.SignResult result = client.signEndfield(cred, endfieldRole);
            System.out.println("ENDFIELD SIGN RESULT: " + result.message());
            assertNotNull(result.message());
            assertTrue(result.message().contains(endfieldRole.nickname()));
            assertTrue(result.message().startsWith("[终末地]"), "日志应标明角色所属游戏: " + result.message());
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
