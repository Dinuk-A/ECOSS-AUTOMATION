package com.automation.framework.utils.api;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.automation.framework.utils.api.restassured.RA_API_Utils;
import com.automation.framework.utils.common.ConfigPropertyReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductsApiHelper {

    // API Configuration
    private static final String API_BASE_URL = ConfigPropertyReader.getProperty("configs/ecos.properties",
            "poc.backend.base.url");
    private static final String API_ENDPOINT = ConfigPropertyReader.getProperty("configs/ecos.properties",
            "poc.backend.products.end.url");

    public static Set<Integer> fetchSKUsFromAPI() {
        Set<Integer> skus = new HashSet<>();

        try {
            // Prepare headers
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Accept", "application/json");
            headers.put("X-Ecos-Tenant-Domain", "cookiedent.local");

            // Prepare query parameters
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("pageNumber", "1");
            queryParams.put("pageSize", "36");

            // Call the API using your custom method with query params
            Response response = RA_API_Utils.getReqWithQueryParamsAndHeaders(
                API_BASE_URL, 
                queryParams, 
                headers, 
                API_ENDPOINT
            );

            // Verify response status
            if (response.getStatusCode() != 200) {
                System.err.println("API call failed with status code: " + response.getStatusCode());
                System.err.println("Response body: " + response.getBody().asString());
                return skus;
            }

            // Parse the JSON response
            String jsonResponse = response.getBody().asString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Navigate to the "items" array
            JsonNode itemsNode = rootNode.get("items");

            if (itemsNode != null && itemsNode.isArray()) {
                // Iterate through each item
                for (JsonNode item : itemsNode) {
                    JsonNode skuNode = item.get("sku");
                    if (skuNode != null) {
                        String skuString = skuNode.asText();
                        try {
                            // Convert SKU string to integer and add to set
                            Integer skuInt = Integer.parseInt(skuString);
                            skus.add(skuInt);
                        } catch (NumberFormatException e) {
                            System.err.println("Failed to parse SKU: " + skuString);
                        }
                    }
                }
            }

            System.out.println("Successfully fetched " + skus.size() + " SKUs from API");

        } catch (Exception e) {
            System.err.println("Error fetching SKUs from API: " + e.getMessage());
            e.printStackTrace();
        }

        return skus;
    }
}