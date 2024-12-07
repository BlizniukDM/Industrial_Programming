package com.example.demo;

import com.example.demo.service.TextFileProcessor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileProcessorTest {
    @Test
    public void testProcessFile() throws Exception {
        File inputFile = new File("src/test/resources/input.txt");
        File outputFile = new File("src/test/resources/output.txt");

        TextFileProcessor processor = new TextFileProcessor();
        processor.process(inputFile, outputFile);

        // Читаем ожидаемый и фактический вывод
        String expected = new String(Files.readAllBytes(Path.of("src/test/resources/expected_output.txt"))).trim();
        String actual = new String(Files.readAllBytes(outputFile.toPath())).trim();

        assertEquals(expected, actual);
    }
}
