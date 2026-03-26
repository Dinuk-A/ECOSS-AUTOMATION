package com.automation.framework.utils.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.io.File;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReader {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Reads a JSON file and returns its full content as a String.
     *
     * Steps:
     * 1. Takes the file path of the JSON file as input.
     * 2. Uses Java NIO's Files.readAllBytes() to read the entire content of the
     * file as bytes.
     * 3. Converts the byte array into a string using new String().
     * 4. Returns the string content (raw JSON) to the caller.
     * 
     * Usage: Used when you simply want the raw JSON file content without
     * modifications.
     */
    public static String readStaticJsonFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    /**
     * Reads a JSON file and replaces any {{placeholder}} text with actual values
     * from a map.
     *
     * Steps:
     * 1. Takes the file path and a map of placeholders as input (e.g.,
     * {{username}}, {{password}}).
     * 2. Reads the entire file content into a string, just like the method above.
     * 3. Iterates through each entry in the placeholder map.
     * 4. For each key-value pair in the map, replaces all occurrences of `{{key}}`
     * in the JSON string with `value`.
     * 5. Returns the updated JSON string with all placeholders replaced.
     * 
     * Usage: Useful for dynamic JSON payloads in tests, where values need to be
     * injected at runtime.
     */

    public static String loadAndReplaceJsonPlaceholders(String filePath, Map<String, String> placeholders) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(filePath)));

            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                json = json.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }

            return json;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    /**
     * Reads a JSON file and returns the value associated with a specific key.
     *
     * Steps:
     * 1. Takes the file path of the JSON file and a key to look for.
     * 2. Uses Jackson's ObjectMapper to parse the JSON file into a JsonNode tree.
     * 3. Retrieves the node that matches the provided key.
     * 4. Converts the found JsonNode into a Java Object (String, Integer, Boolean,
     * etc.).
     * 5. Returns the object if the key exists, or null if it doesn’t.
     * 
     * Usage: Useful when you want to extract a specific value (e.g., "username",
     * "password")
     * from a static JSON file without loading the entire structure into memory
     * manually.
     */
    public static Object fetchJsonValueByKey(String filePath, String keyString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(new File(filePath));
            JsonNode valueNode = rootNode.get(keyString);
            return valueNode != null ? mapper.treeToValue(valueNode, Object.class) : null;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    /**
     * Reads JSON file and deserializes it to the specified class type
     * 
     * @param filePath  Path to JSON file
     * @param valueType Class type to deserialize to
     * @return Deserialized object
     * @throws RuntimeException if reading or parsing fails
     */
    public static <T> T readJsonFileOnce(String filePath, Class<T> valueType) {
        try {
            File jsonFile = new File(filePath);
            if (!jsonFile.exists()) {
                throw new RuntimeException("JSON file not found: " + filePath);
            }
            return objectMapper.readValue(jsonFile, valueType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON from file: " + filePath, e);
        }
    }

}
