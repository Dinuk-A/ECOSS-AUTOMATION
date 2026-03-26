package com.automation.framework.utils.api.playwright;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;

import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

public class PW_API_Utils {

        private final APIRequestContext apiContext;

        public PW_API_Utils(APIRequestContext apiContext) {
                this.apiContext = apiContext;
        }

        // =========================================================
        // INTERNAL HELPER
        // =========================================================

        // Build RequestOptions with headers, query params, and body
        private RequestOptions buildOptions(
                        Map<String, String> headers,
                        Map<String, String> queryParams,
                        Object body) {
                RequestOptions options = RequestOptions.create();

                if (headers != null) {
                        headers.forEach(options::setHeader);
                }

                if (queryParams != null) {
                        queryParams.forEach(options::setQueryParam);
                }

                if (body != null) {
                        options.setData(body);
                }

                return options;
        }

        // =========================================================
        // GET REQUESTS
        // =========================================================

        // Basic GET request
        public APIResponse get(String endpoint) {
                return apiContext.get(endpoint);
        }

        // GET with query parameters
        public APIResponse getWithQueryParams(
                        String endpoint,
                        Map<String, String> queryParams) {

                return apiContext.get(
                                endpoint,
                                buildOptions(null, queryParams, null));
        }

        // GET with custom headers only - original
        public APIResponse getWithHeaders(
                        String endpoint,
                        Map<String, String> headers) {

                return apiContext.get(
                                endpoint,
                                buildOptions(headers, null, null));
        }

        // GET with both headers and query parameters
        public APIResponse getWithHeadersAndParams(
                        String endpoint,
                        Map<String, String> headers,
                        Map<String, String> queryParams) {

                return apiContext.get(
                                endpoint,
                                buildOptions(headers, queryParams, null));
        }

        // GET with Bearer Token authentication
        public APIResponse getWithBearerToken(
                        String endpoint,
                        String bearerToken) {

                return getWithHeaders(
                                endpoint,
                                Map.of("Authorization", "Bearer " + bearerToken));
        }

        // GET with Basic Authentication (username and password)
        public APIResponse getWithBasicAuth(
                        String endpoint,
                        String username,
                        String password) {

                String basicAuth = Base64.getEncoder()
                                .encodeToString((username + ":" + password).getBytes());

                return getWithHeaders(
                                endpoint,
                                Map.of("Authorization", "Basic " + basicAuth));
        }

        // GET with API Key in header
        public APIResponse getWithApiKey(
                        String endpoint,
                        String apiKeyName,
                        String apiKeyValue) {

                return getWithHeaders(
                                endpoint,
                                Map.of(apiKeyName, apiKeyValue));
        }

        // GET with cookies
        public APIResponse getWithCookies(
                        String endpoint,
                        Map<String, String> cookies) {

                String cookieHeader = cookies.entrySet()
                                .stream()
                                .map(e -> e.getKey() + "=" + e.getValue())
                                .collect(Collectors.joining("; "));

                return getWithHeaders(
                                endpoint,
                                Map.of("Cookie", cookieHeader));
        }

        // =========================================================
        // POST REQUESTS
        // =========================================================

        // POST with raw JSON (string body)
        public APIResponse postJson(
                        String endpoint,
                        String jsonBody) {

                return apiContext.post(
                                endpoint,
                                buildOptions(
                                                Map.of("Content-Type", "application/json"),
                                                null,
                                                jsonBody));
        }

        // POST with headers and JSON body
        public APIResponse postWithHeadersAndJson(
                        String endpoint,
                        Map<String, String> headers,
                        String jsonBody) {

                headers.putIfAbsent("Content-Type", "application/json");

                return apiContext.post(
                                endpoint,
                                buildOptions(headers, null, jsonBody));
        }

        // POST with headers, query parameters, and JSON body
        public APIResponse postWithHeadersAndParams(
                        String endpoint,
                        Map<String, String> headers,
                        Map<String, String> queryParams,
                        String jsonBody) {

                headers.putIfAbsent("Content-Type", "application/json");

                return apiContext.post(
                                endpoint,
                                buildOptions(headers, queryParams, jsonBody));
        }

        // POST with Bearer Token authentication
        public APIResponse postWithBearerToken(
                        String endpoint,
                        String bearerToken,
                        String jsonBody) {

                return postWithHeadersAndJson(
                                endpoint,
                                Map.of(
                                                "Authorization", "Bearer " + bearerToken,
                                                "Content-Type", "application/json"),
                                jsonBody);
        }

        //POST with headers but no body
        public APIResponse postWithHeadersNoBody(
                        String endpoint,
                        Map<String, String> headers) {

                return apiContext.post(
                                endpoint,
                                buildOptions(headers, null, null));
        }

        // POST with Basic Authentication
        public APIResponse postWithBasicAuth(
                        String endpoint,
                        String username,
                        String password,
                        String jsonBody) {

                String basicAuth = Base64.getEncoder()
                                .encodeToString((username + ":" + password).getBytes());

                return postWithHeadersAndJson(
                                endpoint,
                                Map.of(
                                                "Authorization", "Basic " + basicAuth,
                                                "Content-Type", "application/json"),
                                jsonBody);
        }

        // =========================================================
        // PUT REQUESTS
        // =========================================================

        // PUT with raw JSON (string body)
        public APIResponse putJson(
                        String endpoint,
                        String jsonBody) {

                return apiContext.put(
                                endpoint,
                                buildOptions(
                                                Map.of("Content-Type", "application/json"),
                                                null,
                                                jsonBody));
        }

        // PUT with headers and JSON body
        public APIResponse putWithHeaders(
                        String endpoint,
                        Map<String, String> headers,
                        String jsonBody) {

                headers.putIfAbsent("Content-Type", "application/json");

                return apiContext.put(
                                endpoint,
                                buildOptions(headers, null, jsonBody));
        }

        // PUT with query parameters and JSON body
        public APIResponse putWithQueryParams(
                        String endpoint,
                        Map<String, String> queryParams,
                        String jsonBody) {

                return apiContext.put(
                                endpoint,
                                buildOptions(
                                                Map.of("Content-Type", "application/json"),
                                                queryParams,
                                                jsonBody));
        }

        // PUT with Bearer Token authentication
        public APIResponse putWithBearerToken(
                        String endpoint,
                        String bearerToken,
                        String jsonBody) {

                return putWithHeaders(
                                endpoint,
                                Map.of(
                                                "Authorization", "Bearer " + bearerToken,
                                                "Content-Type", "application/json"),
                                jsonBody);
        }

        // =========================================================
        // PATCH REQUESTS
        // =========================================================

        // PATCH with raw JSON (string body)
        public APIResponse patchJson(
                        String endpoint,
                        String jsonBody) {

                return apiContext.patch(
                                endpoint,
                                buildOptions(
                                                Map.of("Content-Type", "application/json"),
                                                null,
                                                jsonBody));
        }

        // PATCH with headers and JSON body
        public APIResponse patchWithHeaders(
                        String endpoint,
                        Map<String, String> headers,
                        String jsonBody) {

                headers.putIfAbsent("Content-Type", "application/json");

                return apiContext.patch(
                                endpoint,
                                buildOptions(headers, null, jsonBody));
        }

        // PATCH with Bearer Token authorization and JSON body
        public APIResponse patchWithBearerToken(
                        String endpoint,
                        String bearerToken,
                        String jsonBody) {

                return patchWithHeaders(
                                endpoint,
                                Map.of(
                                                "Authorization", "Bearer " + bearerToken,
                                                "Content-Type", "application/json"),
                                jsonBody);
        }

        // =========================================================
        // DELETE REQUESTS
        // =========================================================

        // Basic DELETE request
        public APIResponse delete(
                        String endpoint) {

                return apiContext.delete(endpoint);
        }

        // DELETE with custom headers
        public APIResponse deleteWithHeaders(
                        String endpoint,
                        Map<String, String> headers) {

                return apiContext.delete(
                                endpoint,
                                buildOptions(headers, null, null));
        }

        // DELETE with Bearer Token authentication
        public APIResponse deleteWithBearerToken(
                        String endpoint,
                        String bearerToken) {

                return deleteWithHeaders(
                                endpoint,
                                Map.of("Authorization", "Bearer " + bearerToken));
        }

        // DELETE with query parameters
        public APIResponse deleteWithQueryParams(
                        String endpoint,
                        Map<String, String> queryParams) {

                return apiContext.delete(
                                endpoint,
                                buildOptions(null, queryParams, null));
        }
}
