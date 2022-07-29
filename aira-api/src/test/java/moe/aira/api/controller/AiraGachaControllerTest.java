package moe.aira.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

class AiraGachaControllerTest {
    @Test
    void name() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Scanner scanner = new Scanner(new FileReader("D:\\output\\pastPointRanking-207-235.json"));
        FileWriter fileWriter = new FileWriter("D:\\output\\pastPointRanking-207-235.json.min");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
//            System.out.println(line);
            String replace = line.trim();
//            System.out.println(replace);
            fileWriter.write(replace);
        }
        fileWriter.close();
    }
}