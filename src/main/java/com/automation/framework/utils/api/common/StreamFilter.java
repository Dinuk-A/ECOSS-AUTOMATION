package com.automation.framework.utils.api.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StreamFilter {
    
    public static JsonNode extractFieldFromStream(String streamResponse, String field, String value) {
        ObjectMapper mapper = new ObjectMapper();
        String[] lines = streamResponse.split("\n"); // each line from the stream

        for (String line : lines) {
            if (line.startsWith("data:")) {
                String json = line.substring(5).trim(); // remove "data:" prefix
                try {
                    JsonNode node = mapper.readTree(json);
                    if (value.equals(node.get(field).asText())) {
                        return node;
                    }
                } catch (Exception e) {
                    System.err.println("Invalid JSON in stream: " + json);
                }
            }
        }

        return null;
    }
}

