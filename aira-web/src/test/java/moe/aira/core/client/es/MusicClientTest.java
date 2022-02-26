package moe.aira.core.client.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MusicClientTest {
    @Autowired
    MusicClient musicClient;
    @Test
    void musics() {
        System.out.println(musicClient.musics());
    }
}