package moe.aira.api.hekk;

import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.client.es.IdolRoomClient;
import moe.aira.core.client.hekk.BaseClient;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

public class IdolRoomsTest {

    @Autowired
    IdolRoomClient idolRoomClient;


    @Test
    void testIdolRoomRetrieval() throws Exception {
        JsonNode jsonNode = idolRoomClient.cardsT();
        BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of("idol.txt"));
        bufferedWriter.write(jsonNode.toString());
        bufferedWriter.close();
    }
}
