package com.example.demo.web;

import com.example.demo.service.TextFileProcessor;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/api")
public class FileController {

    @PostMapping("/process")
    public String processFile(@RequestParam String inputPath, @RequestParam String outputPath) {
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        TextFileProcessor processor = new TextFileProcessor();
        processor.process(inputFile, outputFile);
        return "Processing complete.";
    }
}
