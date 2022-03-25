package moe.aira.api.tasks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiraEventTaskTest {
    @Autowired
    AiraEventTask airaEventTask;

    @Test
    void openEvent() {
        airaEventTask.updateEventId();

    }
}