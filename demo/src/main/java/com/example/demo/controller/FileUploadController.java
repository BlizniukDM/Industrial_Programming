package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.*;

@RestController
public class FileUploadController {

    private static final String UPLOAD_DIR = "uploads/";

    public FileUploadController() {
        // Создаем директорию для загруженных файлов, если её нет
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            // Сохранение файла на сервере
            Path filePath = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            // Обработка файла
            String processedResult = processFile(filePath);

            // Возврат результата
            return "Файл успешно обработан! Результат: \n" + processedResult;
        } catch (Exception e) {
            return "Ошибка при загрузке файла: " + e.getMessage();
        }
    }

    private String processFile(Path filePath) throws IOException {
        // Чтение содержимого файла
        StringBuilder result = new StringBuilder();
        Files.lines(filePath).forEach(line -> {
            try {
                // Обработка строки (например, вычисление выражения)
                String[] parts = line.split(" ");
                double operand1 = Double.parseDouble(parts[0]);
                String operator = parts[1];
                double operand2 = Double.parseDouble(parts[2]);

                double calculation = switch (operator) {
                    case "+" -> operand1 + operand2;
                    case "-" -> operand1 - operand2;
                    case "*" -> operand1 * operand2;
                    case "/" -> operand1 / operand2;
                    default -> throw new IllegalArgumentException("Неизвестный оператор: " + operator);
                };

                result.append(line).append(" = ").append(calculation).append("\n");
            } catch (Exception e) {
                result.append("Ошибка в строке: ").append(line).append("\n");
            }
        });
        return result.toString();
    }
}
