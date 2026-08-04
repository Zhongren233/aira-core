package moe.aira.onebot.client.skyland;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShumeiDeviceIdTest {

    @Test
    void desEncryptMatchesPythonReference() {
        // 参考值由 SecuritySm.py（cryptography TripleDES 8字节密钥）计算
        assertEquals("Fd5zMeCbJY2nc0J7tHO+PhlIlRR63RY8fYNs9YznImp6jkEBiv0a/tEKhl595d4tcrzH6sfKUs4nKN0nXy6W7J2x+LUZLuaw7FVBddNfZ3O2ToGM9rykP902i7Vt2WXFIk2V2VvLCCiW1v0CkqsLCDzGAvwx5WtxoEbG/lmeDKo=",
                ShumeiDeviceId.encryptField("ua",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36 Edg/129.0.0.0"));
        assertEquals("ZiEGW6ynU0Q=", ShumeiDeviceId.encryptField("timezone", "-480"));
        assertEquals("/DXc2+x/RAQSFJ/sKmDA4aT5QTgP9gzchpbk1IOIfWARsk2FyMpkFBk0Ys1KvCH8sjmMy+0439V4WOCYWmcgAhYmqlXrq33pH8SJKlYvOdUAS9ps75Mp3UTdJurGvbJQNWLlBp0UxQk0Z6A6Lg07Zu8t6PAbcSoGyaa826BG0Mo=",
                ShumeiDeviceId.encryptField("plugins",
                        "MicrosoftEdgePDFPluginPortableDocumentFormatinternal-pdf-viewer1,MicrosoftEdgePDFViewermhjfbmdgcfjbbpaeojofohoefgiehjai1"));
    }

    @Test
    void getTnMatchesReference() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("a", 3);
        map.put("b", 2);
        map.put("c", "x");
        assertEquals("3000020000x", ShumeiDeviceId.getTn(map));
    }
}
