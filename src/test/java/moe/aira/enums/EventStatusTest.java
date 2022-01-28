package moe.aira.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventStatusTest {

    @Test
    void valueOf() {
        EventStatus open = EventStatus.valueOf("Open");
        System.out.println(open);
    }
}