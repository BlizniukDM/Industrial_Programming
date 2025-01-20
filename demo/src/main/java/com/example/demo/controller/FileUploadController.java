package com.example.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public ResponseEntity<byte[]> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            // Сохранение файла на сервере
            Path filePath = Files.createTempFile("uploaded_", "_" + file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            // Определяем расширение файла
            String fileName = file.getOriginalFilename();
            if (fileName == null) {
                throw new IllegalArgumentException("Файл должен иметь имя!");
            }
            String fileExtension = getFileExtension(fileName);

            // Обрабатываем файл
            byte[] processedFile;
            String resultFileName;
            switch (fileExtension) {
                case "txt" -> {
                    processedFile = processTxt(filePath);
                    resultFileName = "results.txt";
                }
                case "yaml", "yml" -> {
                    processedFile = processYaml(filePath);
                    resultFileName = "results.yaml";
                }
                case "json" -> {
                    processedFile = processJson(filePath);
                    resultFileName = "results.json";
                }
                case "xml" -> {
                    processedFile = processXml(filePath);
                    resultFileName = "results.xml";
                }
                default -> throw new IllegalArgumentException("Формат файла не поддерживается: " + fileExtension);
            }

            // Возвращаем обработанный файл для скачивания
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resultFileName)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(processedFile);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(("Ошибка при обработке файла: " + e.getMessage()).getBytes());
        }
    }

    private byte[] processTxt(Path filePath) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        List<String> results = processLines(lines);

        return String.join("\n", results).getBytes();
    }

    private byte[] processYaml(Path filePath) throws IOException {
        Yaml yaml = new Yaml();
        List<String> lines = yaml.load(Files.newInputStream(filePath));
        List<String> results = processLines(lines);

        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml resultYaml = new Yaml(options);

        StringWriter writer = new StringWriter();
        resultYaml.dump(results, writer);

        return writer.toString().getBytes();
    }

    private byte[] processJson(Path filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> lines = mapper.readValue(Files.newInputStream(filePath), List.class);
        List<String> results = processLines(lines);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(results);
    }

    private byte[] processXml(Path filePath) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        List<String> lines = xmlMapper.readValue(Files.newInputStream(filePath), List.class);
        List<String> results = processLines(lines);

        return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(results);
    }

    private List<String> processLines(List<String> lines) {
        List<String> results = new ArrayList<>();
        for (String line : lines) {
            try {
                // Парсим строку и выполняем вычисления
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

                results.add(line + " = " + calculation);
            } catch (Exception e) {
                results.add("Ошибка в строке: " + line);
            }
        }
        return results;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1).toLowerCase();
    }
}
