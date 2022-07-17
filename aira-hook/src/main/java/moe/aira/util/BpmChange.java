package moe.aira.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BpmChange {
    private Double bpm;
    private Integer time;
}
