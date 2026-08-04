package moe.aira.onebot.client.skyland;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkylandSignatureTest {
    private static final String TOKEN = "69250778359b3ca50d33d6ac2099d4a3";
    private static final String TIMESTAMP = "1700000000";

    @Test
    void signBindingMatchesPythonReference() {
        SkylandSignature.SignResult result =
                SkylandSignature.signAt(TOKEN, "/api/v1/game/player/binding", "", TIMESTAMP);
        assertEquals("dc9f52ef67e0f01f365cb816adb1f3ce", result.sign());
        assertEquals(TIMESTAMP, result.timestamp());
    }

    @Test
    void signAttendanceMatchesPythonReference() {
        SkylandSignature.SignResult result = SkylandSignature.signAt(TOKEN, "/api/v1/game/attendance",
                "{\"gameId\": 1, \"uid\": \"41584529\"}", TIMESTAMP);
        assertEquals("aec86cc56054545f90088287cee03722", result.sign());
    }
}
