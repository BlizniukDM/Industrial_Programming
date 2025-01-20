package com.example.demo.service;

import java.io.*;

public class TextFileProcessor implements FileProcessor {
    @Override
    public void process(File inputFile, File outputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Обработка строки
                String processedLine = processLine(line);
                writer.write(processedLine); // Записываем результат обработки строки
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String processLine(String line) {
        try {
            // Разделяем строку по пробелам, ожидая формат "число оператор число"
            String[] parts = line.split(" ");
            if (parts.length != 3) {
                return "Ошибка: некорректный формат строки: " + line;
            }

            double operand1 = Double.parseDouble(parts[0]);
            String operator = parts[1];
            double operand2 = Double.parseDouble(parts[2]);

            // Выполняем вычисление
            double result = switch (operator) {
                case "+" -> operand1 + operand2;
                case "-" -> operand1 - operand2;
                case "*" -> operand1 * operand2;
                case "/" -> operand2 != 0 ? operand1 / operand2 : Double.NaN; // Проверка деления на 0
                default -> throw new IllegalArgumentException("Неизвестный оператор: " + operator);
            };

            return String.format("%s = %.2f", line, result); // Форматируем результат
        } catch (Exception e) {
            return "Ошибка при обработке строки: " + line;
        }
    }
}
