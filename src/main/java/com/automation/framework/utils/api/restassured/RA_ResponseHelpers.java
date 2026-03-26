package com.automation.framework.utils.api.restassured;

import java.io.FileWriter;
import java.io.IOException;

import io.restassured.response.Response;

public class RA_ResponseHelpers {

    // Prints the response body to the console in a pretty format
    public static void printResponseBody(Response response) {
        System.out.println("Response Body: ");
        System.out.println(response.getBody().asPrettyString());
    }

    // Prints the response headers to the console
    public static void printResponseHeaders(Response response) {
        System.out.println("Response Headers:");
        response.getHeaders().forEach(System.out::println);
    }

    // Prints the full response: Status Code, Headers, and Body
    public static void printFullResponse(Response response) {
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Headers:");
        response.getHeaders().forEach(System.out::println);
        System.out.println("Body:");
        System.out.println(response.getBody().asPrettyString());
    }

    // Saves the response body to a specified file
    public static void saveResponseToFile(Response response, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(response.getBody().asPrettyString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Retrieves a specific value from the JSON response body
     * using the given JSON path.
     * 
     * Example:
     * Response response = given().get("https://api.example.com/data");
     * Object value = CommonMethods.getJsonValue(response, "$.data.id");
     * System.out.println("The value of data.id is: " + value);
     */
    public static Object getJsonValue(Response response, String jsonPath) {
        return response.jsonPath().get(jsonPath);
    }

}
