package com.example;

import java.io.IOException;
import com.example.processor.FileProcessor;
import com.example.strategy.ExpressionEvaluator;
import com.example.strategy.Exp4jEvaluator;

public class App {
    public static void main(String[] args) {
        String inputFilePath = "input.txt";
        String outputFilePath = "output.txt";

        // Используем стратегию для вычисления арифметических выражений
        ExpressionEvaluator evaluator = new Exp4jEvaluator();
        FileProcessor processor = new FileProcessor(evaluator);

        try {
            processor.process(inputFilePath, outputFilePath);
            System.out.println("Файл успешно обработан.");
        } catch (IOException e) {
            System.err.println("Ошибка обработки файла: " + e.getMessage());
        }
    }
}
