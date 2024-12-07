package com.example.demo.controller;

import com.example.demo.service.TextFileProcessor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class MainUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        TextField inputField = new TextField();
        TextField outputField = new TextField();
        Button processButton = new Button("Process");

        processButton.setOnAction(e -> {
            String inputPath = inputField.getText();
            String outputPath = outputField.getText();
            processFile(inputPath, outputPath);
        });

        VBox vbox = new VBox(inputField, outputField, processButton);
        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("File Processor");
        primaryStage.show();
    }

    private void processFile(String inputPath, String outputPath) {
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        TextFileProcessor processor = new TextFileProcessor();
        processor.process(inputFile, outputFile);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
