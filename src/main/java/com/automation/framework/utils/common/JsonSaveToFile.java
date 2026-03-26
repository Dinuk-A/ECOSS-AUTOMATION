package com.automation.framework.utils.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class JsonSaveToFile {

    // Saves the given JSON data to a file at the specified path.
    public static void saveJsonToFile(Object jsonData, String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print

        File file = new File(filePath);

        // Create parent directories if they don't exist
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try {
            objectMapper.writeValue(file, jsonData);
            System.out.println("JSON saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving JSON to file: " + e.getMessage());
        }
    }
}
