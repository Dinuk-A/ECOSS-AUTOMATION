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

@Epic("E-Commerce Platform – API Tests")
public class GeneralAPITest extends PW_API_BaseTest {

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

        // TC001
        @Test
        @Story("Validate API Requests for ECOS Poc Backend")
        @Description("Validate GET Cart Item Request for ECOS Poc Backend -TC001 - Valid request")
        @Severity(SeverityLevel.CRITICAL)
        public void validateResponseForValidRequest() {

                String endpoint = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.backend.cart.end.url");

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

                // GET request with headers
                Allure.step("Sending GET request");
                Allure.addAttachment("Request URL", "text/plain", baseUrl + endpoint);

                StringBuilder headerAttachment = new StringBuilder();
                headers.forEach((k, v) -> headerAttachment.append(k)
                                .append(": ")
                                .append(v)
                                .append("\n"));

                Allure.addAttachment("Request Headers", "text/plain",
                                headerAttachment.toString(), ".txt");

                APIResponse response = api.getWithHeaders(
                                endpoint, headers);

                Allure.step("Capturing API response");

                Allure.addAttachment("Response Status",
                                String.valueOf(response.status()));

                Allure.addAttachment("Response Body", "application/json",
                                response.text(), ".json");

                // Assertions
                Allure.step("Asserting the response status code and body content");
                Assert.assertEquals(response.status(), 200);
                Assert.assertTrue(response.text().contains("items"));
                Assert.assertTrue(response.text().contains("cartTotal"));

        }

        // TC002
        // Invalid request - incomplete url/endpoint -- localhost:8000/api/Cart instead
        // of localhost:8000/api/MyCart
        @Test
        @Story("Validate API Requests for ECOS Poc Backend")
        @Description("Validate GET Cart Item Request for ECOS Poc Backend -TC002 - Invalid request with incorrect endpoint")
        @Severity(SeverityLevel.NORMAL)
        public void validateResponseForInvalidRequest() {

                // incorrect endpoint
                String endpoint = "/api/Cart";

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

                // GET request with headers
                Allure.step("Sending GET request");
                Allure.addAttachment("Request URL", "text/plain", baseUrl + endpoint);

                StringBuilder headerAttachment = new StringBuilder();
                headers.forEach((k, v) -> headerAttachment.append(k)
                                .append(": ")
                                .append(v)
                                .append("\n"));

                Allure.addAttachment("Request Headers", "text/plain",
                                headerAttachment.toString(), ".txt");

                APIResponse response = api.getWithHeaders(
                                endpoint, headers);

                Allure.step("Capturing API response");
                Allure.addAttachment("Response Status",
                                String.valueOf(response.status()));
                Allure.addAttachment("Response Body", "application/json",
                                response.text(), ".json");

                // Assertions
                Assert.assertEquals(response.status(), 404);
        }

        // TC003
        // Invalid request - missing headers
        @Test
        @Story("Validate API Requests for ECOS Poc Backend")
        @Description("Validate GET Cart Item Request for ECOS Poc Backend -TC003 - Invalid request with missing headers")
        @Severity(SeverityLevel.NORMAL)
        public void validateResponseForRequestWithMissingHeaders() {

                String endpoint = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.backend.cart.end.url");

                // GET request without headers
                Allure.step("Sending GET request without headers");
                Allure.addAttachment("Request URL", "text/plain", baseUrl + endpoint);

                APIResponse response = api.get(endpoint);

                Allure.step("Capturing API response");
                Allure.addAttachment("Response Status",
                                String.valueOf(response.status()));
                Allure.addAttachment("Response Body", "application/json",
                                response.text(), ".json");

                // Assertions
                Assert.assertEquals(response.status(), 400);
        }

        // TC004
        // Invalid request - Empty header values
        @Test
        @Story("Validate API Requests for ECOS Poc Backend")
        @Description("Validate GET Cart Item Request for ECOS Poc Backend -TC004 - Invalid request with empty header values")
        @Severity(SeverityLevel.NORMAL)
        public void validateResponseForRequestWithEmptyHeaderValues() {

                String endpoint = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.backend.cart.end.url");

                // SESSION & TENANT HEADERS with empty values
                String sessionKey = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.header.session.key");
                String tenantDomainKey = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.header.tenant.domain.key");

                Map<String, String> headers = Map.of(
                                sessionKey, "",
                                tenantDomainKey, "");

                // GET request with empty header values
                Allure.step("Sending GET request with empty header values");
                Allure.addAttachment("Request URL", "text/plain", baseUrl + endpoint);

                StringBuilder headerAttachment = new StringBuilder();
                headers.forEach((k, v) -> headerAttachment.append(k)
                                .append(": ")
                                .append(v)
                                .append("\n"));

                Allure.addAttachment("Request Headers", "text/plain",
                                headerAttachment.toString(), ".txt");

                APIResponse response = api.getWithHeaders(
                                endpoint, headers);

                Allure.step("Capturing API response");
                Allure.addAttachment("Response Status",
                                String.valueOf(response.status()));
                Allure.addAttachment("Response Body", "application/json",
                                response.text(), ".json");

                // Assertions
                Assert.assertEquals(response.status(), 400);
        }

        // TC005
        //Invalid request - Wrong/incomplete header values
        @Test
        @Story("Validate API Requests for ECOS Poc Backend")
        @Description("Validate GET Cart Item Request for ECOS Poc Backend -TC005 - Invalid request with wrong/incomplete header values")
        @Severity(SeverityLevel.NORMAL)
        public void validateResponseForRequestWithWrongHeaderValues() {

                String endpoint = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.backend.cart.end.url");

                // SESSION & TENANT HEADERS with wrong/incomplete values
                String sessionKey = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.header.session.key");
                String tenantDomainKey = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.header.tenant.domain.key");

                Map<String, String> headers = Map.of(
                                sessionKey, "invalidSessionValue",
                                tenantDomainKey, "invalidTenantDomainValue");

                // GET request with wrong/incomplete header values
                Allure.step("Sending GET request with wrong/incomplete header values");
                Allure.addAttachment("Request URL", "text/plain", baseUrl + endpoint);

                StringBuilder headerAttachment = new StringBuilder();
                headers.forEach((k, v) -> headerAttachment.append(k)
                                .append(": ")
                                .append(v)
                                .append("\n"));

                Allure.addAttachment("Request Headers", "text/plain",
                                headerAttachment.toString(), ".txt");

                APIResponse response = api.getWithHeaders(
                                endpoint, headers);

                Allure.step("Capturing API response");
                Allure.addAttachment("Response Status",
                                String.valueOf(response.status()));
                Allure.addAttachment("Response Body", "application/json",
                                response.text(), ".json");

                // Assertions
                Assert.assertEquals(response.status(), 400);
        }

        // TC006
        // Invalid request - Empty string header values (" ")
        @Test
        @Story("Validate API Requests for ECOS Poc Backend")
        @Description("Validate GET Cart Item Request for ECOS Poc Backend -TC006 - Invalid request with empty string header values")
        @Severity(SeverityLevel.NORMAL)
        public void validateResponseForRequestWithEmptyStringHeaderValues() {

                String endpoint = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.backend.cart.end.url");

                // SESSION & TENANT HEADERS with empty string values
                String sessionKey = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.header.session.key");
                String tenantDomainKey = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.header.tenant.domain.key");

                Map<String, String> headers = Map.of(
                                sessionKey, " ",
                                tenantDomainKey, " ");

                // GET request with empty string header values
                Allure.step("Sending GET request with empty string header values");
                Allure.addAttachment("Request URL", "text/plain", baseUrl + endpoint);

                StringBuilder headerAttachment = new StringBuilder();
                headers.forEach((k, v) -> headerAttachment.append(k)
                                .append(": ")
                                .append(v)
                                .append("\n"));

                Allure.addAttachment("Request Headers", "text/plain",
                                headerAttachment.toString(), ".txt");

                APIResponse response = api.getWithHeaders(
                                endpoint, headers);

                Allure.step("Capturing API response");
                Allure.addAttachment("Response Status",
                                String.valueOf(response.status()));
                Allure.addAttachment("Response Body", "application/json",
                                response.text(), ".json");

                // Assertions
                Assert.assertEquals(response.status(), 400);
        }

        // TC007
        // Invalid request - Wrong/incomplete header keys
        @Test
        @Story("Validate API Requests for ECOS Poc Backend")
        @Description("Validate GET Cart Item Request for ECOS Poc Backend -TC007 - Invalid request with wrong/incomplete header keys")
        @Severity(SeverityLevel.NORMAL)
        public void validateResponseForRequestWithWrongHeaderKeys() {

                String endpoint = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.backend.cart.end.url");

                // Wrong/incomplete header keys
                Map<String, String> headers = Map.of(
                                "invalidSessionKey", "someValue",
                                "invalidTenantDomainKey", "someValue");

                // GET request with wrong/incomplete header keys
                Allure.step("Sending GET request with wrong/incomplete header keys");
                Allure.addAttachment("Request URL", "text/plain", baseUrl + endpoint);

                StringBuilder headerAttachment = new StringBuilder();
                headers.forEach((k, v) -> headerAttachment.append(k)
                                .append(": ")
                                .append(v)
                                .append("\n"));

                Allure.addAttachment("Request Headers", "text/plain",
                                headerAttachment.toString(), ".txt");

                APIResponse response = api.getWithHeaders(
                                endpoint, headers);

                Allure.step("Capturing API response");
                Allure.addAttachment("Response Status",
                                String.valueOf(response.status()));
                Allure.addAttachment("Response Body", "application/json",
                                response.text(), ".json");

                // Assertions
                Assert.assertEquals(response.status(), 400);
        }

        // TC008
        // Invalid request - Unrecognized additional header key-value pair (also with original pair)
        @Test
        @Story("Validate API Requests for ECOS Poc Backend")
        @Description("Validate GET Cart Item Request for ECOS Poc Backend -TC008 - Invalid request with additional unrecognized header key-value pair")
        @Severity(SeverityLevel.NORMAL) 
        public void validateResponseForRequestWithAdditionalUnrecognizedHeader() {

                String endpoint = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.backend.cart.end.url");

                // SESSION & TENANT HEADERS
                String sessionKey = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.header.session.key");
                String sessionValue = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.header.session.value");

                String tenantDomainKey = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.header.tenant.domain.key");
                String tenantDomainValue = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.cookiedent.tenant.domain.value");

                // Additional unrecognized header
                String additionalHeaderKey = "X-Unrecognized-Header";
                String additionalHeaderValue = "someValue";

                Map<String, String> headers = Map.of(
                                sessionKey, sessionValue,
                                tenantDomainKey, tenantDomainValue,
                                additionalHeaderKey, additionalHeaderValue);

                // GET request with additional unrecognized header
                Allure.step("Sending GET request with additional unrecognized header");
                Allure.addAttachment("Request URL", "text/plain", baseUrl + endpoint);

                StringBuilder headerAttachment = new StringBuilder();
                headers.forEach((k, v) -> headerAttachment.append(k)
                                .append(": ")
                                .append(v)
                                .append("\n"));

                Allure.addAttachment("Request Headers", "text/plain",
                                headerAttachment.toString(), ".txt");

                APIResponse response = api.getWithHeaders(
                                endpoint, headers);

                Allure.step("Capturing API response");
                Allure.addAttachment("Response Status",
                                String.valueOf(response.status()));
                Allure.addAttachment("Response Body", "application/json",
                                response.text(), ".json");

                // Assertions - Assuming the API should ignore unrecognized headers and process the request successfully
                Assert.assertEquals(response.status(), 200);
        }

        // TC009
        // invalid request - valid url but using POST instead of GET method
        @Test
        @Story("Validate API Requests for ECOS Poc Backend")
        @Description("Validate GET Cart Item Request for ECOS Poc Backend -TC009 - Invalid request with wrong HTTP method")
        @Severity(SeverityLevel.NORMAL)
        public void validateResponseForRequestWithWrongHttpMethod() {

                String endpoint = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.backend.cart.end.url");

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

                // POST request with headers (instead of GET)
                Allure.step("Sending POST request instead of GET");
                Allure.addAttachment("Request URL", "text/plain", baseUrl + endpoint);

                StringBuilder headerAttachment = new StringBuilder();
                headers.forEach((k, v) -> headerAttachment.append(k)
                                .append(": ")
                                .append(v)
                                .append("\n"));

                Allure.addAttachment("Request Headers", "text/plain",
                                headerAttachment.toString(), ".txt");

                APIResponse response = api.postWithHeadersNoBody(
                                endpoint, headers);

                Allure.step("Capturing API response");
                Allure.addAttachment("Response Status",
                                String.valueOf(response.status()));
                Allure.addAttachment("Response Body", "application/json",
                                response.text(), ".json");

                // Assertions
                Assert.assertEquals(response.status(), 405);
        }
       

}
