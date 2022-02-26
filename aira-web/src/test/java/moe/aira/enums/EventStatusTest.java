package moe.aira.enums;

import org.junit.jupiter.api.Test;

class EventStatusTest {

    @Test
    void valueOf() {
        EventStatus open = EventStatus.valueOf("Open");
        System.out.println(open);
    }
}