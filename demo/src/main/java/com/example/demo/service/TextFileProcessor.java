package com.example.demo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextFileProcessor implements FileProcessor {
    @Override
    public void process(File inputFile, File outputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                // Обработка строки
                // Здесь вам нужно правильно обработать строки, чтобы результат соответствовал ожидаемому
                // Например, если строки представляют числа, можно оставить только числовые значения
                writer.write(processLine(line)); // Используем метод для обработки строки
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String processLine(String line) {
        // Замените эту логику на ту, которая соответствует вашей задаче
        // Например, если вы ожидаете только числовые значения, удалите все ненужные символы
        line = line.replaceAll("[^\\d.]", ""); // Удалить все кроме цифр и точки
        return line.isEmpty() ? "0" : line; // Если строка пустая, возвращаем "0"
    }
}
