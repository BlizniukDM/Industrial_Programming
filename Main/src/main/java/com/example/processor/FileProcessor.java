package com.example.processor;

import com.example.strategy.ExpressionEvaluator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileProcessor {
    private final ExpressionEvaluator evaluator;

    public FileProcessor(ExpressionEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public void process(String inputFilePath, String outputFilePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String result = evaluator.evaluate(line);
                writer.write(result);
                writer.newLine();
            }
        }
    }
}
