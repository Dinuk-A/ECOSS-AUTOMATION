package com.automation.framework.utils.api;

import com.automation.framework.utils.common.ConfigPropertyReader;
import com.automation.framework.utils.api.restassured.RA_API_Utils;
import com.automation.framework.models.ProductDetailsModel;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailsApiHelper {

    
    // API Configuration
    private static final String API_BASE_URL = ConfigPropertyReader.getProperty("configs/ecos.properties",
            "poc.backend.base.url");
    private static final String API_ENDPOINT_TEMPLATE = ConfigPropertyReader.getProperty("configs/ecos.properties",
            "poc.specific.product.end.url");

    /**
     * Fetches product details from API and maps to ProductDetailsModel
     */
    public static ProductDetailsModel getProductDetailsFromApi(String productId,String tenantDomain) {
        String endpoint = API_ENDPOINT_TEMPLATE + productId;

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Ecos-Tenant-Domain", tenantDomain);

        Response response = RA_API_Utils.getReqWithHeaders(API_BASE_URL, endpoint, headers);

        response.then().statusCode(200);

        return extractProductDetailsFromResponse(response);
    }

    /**
     * Extracts values from API response and maps to ProductDetailsModel
     */
    private static ProductDetailsModel extractProductDetailsFromResponse(Response response) {
        JsonPath jsonPath = response.jsonPath();
        ProductDetailsModel model = new ProductDetailsModel();

        // Extract SKU from root level
        model.setSku(parseIntSafely(jsonPath.getString("sku")));

        // Extract name from root level
        model.setName(jsonPath.getString("name"));

        // Extract description from root level
        model.setDescription(jsonPath.getString("description"));

        // Extract price from price object
        model.setPrice(jsonPath.getInt("price.amount"));

        // Extract attributes from array
        List<Map<String, Object>> attributes = jsonPath.getList("attributes");

        for (Map<String, Object> attr : attributes) {
            String key = (String) attr.get("key");
            String value = String.valueOf(attr.get("value"));

            switch (key) {
                case "Category":
                    model.setCategory(value);
                    break;
                case "Color":
                    model.setColor(value);
                    break;
                case "Indication":
                    model.setIndication(value);
                    break;
                case "AlloyType":
                    model.setAlloyType(parseIntSafely(value));
                    break;
                case "PointTwoPercentYieldStrengthInMPaCured":
                    model.setPointTwoPercentYieldStrengthInMPaCured(parseIntSafely(value));
                    break;
                case "VickersHardness":
                    model.setVickersHardness(parseIntSafely(value));
                    break;
                case "ElongationAtBreakInPercent":
                    model.setElongationAtBreakInPercent(parseIntSafely(value));
                    break;
                case "MeltingIntervalCelsius":
                    model.setMeltingIntervalCelsius(value);
                    break;
                case "ElementMnInPercent":
                    model.setElementMnInPercent(value);
                    break;
                case "ElementFeInPercent":
                    model.setElementFeInPercent(value);
                    break;
                case "ElementCInPercent":
                    model.setElementCInPercent(value);
                    break;
                case "ElementCoInPercent":
                    model.setElementCoInPercent(value);
                    break;
                case "ElementCrInPercent":
                    model.setElementCrInPercent(value);
                    break;
                case "ElementMoInPercent":
                    model.setElementMoInPercent(value);
                    break;
                case "ElementSiInPercent":
                    model.setElementSiInPercent(value);
                    break;
                case "ElementWInPercent":
                    model.setElementWInPercent(value);
                    break;
                case "PriceEURInCent":
                    model.setPriceEURInCent(parseIntSafely(value));
                    break;
            }
        }

        return model;
    }

    /**
     * Safely parse string to int
     */
    private static int parseIntSafely(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("Failed to parse: " + value);
            return 0;
        }
    }

    //extract the image URL from API response
    public static String getProductImageUrlFromApi(String productId) {
        String endpoint = API_ENDPOINT_TEMPLATE + productId;

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Ecos-Tenant-Domain", "cookiedent.local");

        Response response = RA_API_Utils.getReqWithHeaders(API_BASE_URL, endpoint, headers);

        response.then().statusCode(200);

        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getString("imageUrls[0]");
    }
}
