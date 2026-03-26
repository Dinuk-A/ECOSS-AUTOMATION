package com.automation.framework.sprint_two.regression.api_tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.automation.framework.utils.api.playwright.PW_API_BaseTest;
import com.automation.framework.utils.api.playwright.PW_API_Utils;
import com.automation.framework.utils.common.ConfigPropertyReader;
import com.microsoft.playwright.APIResponse;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.Epic;

@Epic("E-Commerce Platform - API Tests")
public class ProductsApiTest extends PW_API_BaseTest {

    // API Utils instance
    private PW_API_Utils api;

    // base URL
    String baseUrl = ConfigPropertyReader.getProperty("configs/ecos.properties",
            "poc.backend.base.url");

    // setup request context
    @BeforeMethod
    public void setupRequest() {

        createApiContext(baseUrl);
        api = new PW_API_Utils(request);
    }

    // TC002
    @Test
    @Story("Validate API Requests for ECOS Poc Products Backend")
    @Description(" TC002 Invalid parameter value test for ECOS Poc Backend - Products API.")
    @Severity(SeverityLevel.CRITICAL)
    public void validateInvalidParamvalue() {

        String endpoint = ConfigPropertyReader.getProperty("configs/ecos.properties",
                "poc.backend.products.end.url");

        // SESSION & TENANT HEADERS
        String sessionKey = ConfigPropertyReader.getProperty("configs/ecos.properties",
                "poc.header.session.key");
        String sessionValue = ConfigPropertyReader.getProperty("configs/ecos.properties",
                "poc.header.session.value");

        String tenantDomainKey = ConfigPropertyReader.getProperty("configs/ecos.properties",
                "poc.header.tenant.domain.key");
        String tenantDomainValue = ConfigPropertyReader.getProperty("configs/ecos.properties",
                "poc.cookiedent.tenant.domain.value");

        Map<String, String> headers = Map.of(
                sessionKey, sessionValue,
                tenantDomainKey, tenantDomainValue);

        // query parameters with invalid page number
        Map<String, String> queryParams = Map.of(
                "pageSize", "10",
                "pageNumber", "-100");

        Allure.step("Sending GET request");
        Allure.addAttachment("Request URL", "text/plain", baseUrl + endpoint);

        APIResponse response = api.getWithHeadersAndParams(endpoint, headers, queryParams);
        Assert.assertEquals(response.status(), 400, "Expected status code 400 for invalid page number");

        String responseBody = response.text();
        Assert.assertTrue(responseBody.contains("pageNumber must be 1 or greater"),
                "Expected error message for invalid page number");

        Allure.step("Capturing API response");

        Allure.addAttachment("Response Status",
                String.valueOf(response.status()));

        Allure.addAttachment("Response Body", "application/json",
                response.text(), ".json");
    }

    //TC003
    @Test
    @Story("Validate API Requests for ECOS Poc Products Backend")
    @Description(" TC003 Missing header test for ECOS Poc Backend - Products API.")
    @Severity(SeverityLevel.CRITICAL)
    public void validateMissingHeaders() {

        String endpoint = ConfigPropertyReader.getProperty("configs/ecos.properties",
                "poc.backend.products.end.url");

        // query parameters
        Map<String, String> queryParams = Map.of(
                "pageSize", "1",
                "pageNumber", "1");

        Allure.step("Sending GET request without required headers");
        Allure.addAttachment("Request URL", "text/plain", baseUrl + endpoint);

        APIResponse response = api.getWithHeadersAndParams(endpoint, null, queryParams);
        Assert.assertEquals(response.status(), 400, "Expected status code 400 for missing headers");

        String responseBody = response.text();
        Assert.assertTrue(responseBody.contains("broken or missing mandatory header."),
                "Expected error message for missing headers");

        Allure.step("Capturing API response");

        Allure.addAttachment("Response Status",
                String.valueOf(response.status()));

        Allure.addAttachment("Response Body", "application/json",
                response.text(), ".json");
    }

}
