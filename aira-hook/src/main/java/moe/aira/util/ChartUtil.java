package moe.aira.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

public class ChartUtil {
    public static void main(String[] args) throws IOException {
        double bpm = 140;
        double timeResolution = 240.0;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        ChartInfo chartInfo = objectMapper.readValue(new File("D:\\rider\\ConsoleApp1\\ConsoleApp1\\bin\\Debug\\net6.0\\out\\1484.6bf44517.json"), ChartInfo.class);
        List<ChartInfo.Note> notes = chartInfo.getNotes();

        try (FileWriter fileWriter = new FileWriter("./148-Hamu-Except.csv")) {
            fileWriter.write("id,type,time,lane,flickDirection,parentId,realTime\n");
            for (ChartInfo.Note note : notes) {
                Integer time = note.getTime();
                double realTime = (time / timeResolution) * (60 / bpm);
                StringJoiner stringJoiner = new StringJoiner(",");
                stringJoiner.add(note.getId().toString());
                stringJoiner.add(note.getType().toString());
                stringJoiner.add(note.getTime().toString());
                stringJoiner.add(note.getLane().toString());
                stringJoiner.add(note.getFlickDirection().toString());
                stringJoiner.add(note.getParentId().toString());
                stringJoiner.add(realTime + "");
                fileWriter.write(stringJoiner + "\n");
            }
        }

    }

}
