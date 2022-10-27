package moe.aira.onebot.util;

import moe.aira.onebot.manager.IEventConfigManager;
import moe.aira.onebot.task.EventReportTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventReportTaskImageUtilTest {
    @Autowired
    EventReportTask eventReportTask;
    @Autowired
    IEventConfigManager eventConfigManager;

    @Test
    void name() throws Exception {
        eventReportTask.report();
    }
}