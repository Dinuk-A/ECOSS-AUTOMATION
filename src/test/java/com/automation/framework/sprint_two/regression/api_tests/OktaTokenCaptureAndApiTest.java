package com.automation.framework.sprint_two.regression.api_tests;

import com.automation.framework.utils.api.restassured.RA_API_Utils;
import com.automation.framework.utils.api.restassured.RA_AssertionUtils;
import com.automation.framework.utils.api.restassured.RA_ResponseHelpers;
import com.automation.framework.utils.common.ConfigPropertyReader;
import com.automation.framework.utils.common.JsonReader;
import com.automation.framework.utils.ui.core.PlaywrightBaseTest;

import com.microsoft.playwright.options.LoadState;
// import com.microsoft.playwright.BrowserContext;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.automation.framework.ui_pages.playwright.twix.TW_LoginPage;
import com.automation.framework.ui_pages.playwright.twix.TW_LandingPage;
import com.automation.framework.ui_pages.playwright.twix.TW_Header;

/**
 * OktaTokenCaptureAndApiTest
 *
 * Auth mechanism: NextAuth.js — token is stored in a server-side HttpOnly
 * cookie, NOT accessible via JS or request headers.
 *
 * Flow:
 *  1. Navigate to the app and log in via Playwright.
 *  2. Wait for the NextAuth session cookie to be set (not NETWORKIDLE —
 *     NextAuth polls /api/auth/session continuously, which causes timeouts).
 *  3. Extract the session cookie from the browser context.
 *  4. Persist the cookie value to src/test/resources/data/api/okta_token.json.
 *  5. Send the cookie in API calls as the credential (injected via Cookie header).
 */
@Epic("Okta Token Capture & API Validation")
@Feature("Capture NextAuth Session Cookie via UI and validate against API")
public class OktaTokenCaptureAndApiTest extends PlaywrightBaseTest {

    // ── Config ────────────────────────────────────────────────────────────────

    private static final String BASE_URL = ConfigPropertyReader.getProperty(
            "configs/ecos.properties", "twix.frontend.base.url.qa");

    private static final String API_BASE_URL = ConfigPropertyReader.getProperty(
            "configs/ecos.properties", "twix.api.base.url.qa");

    private static final String CART_ENDPOINT = "/api/MyCart";

    /** File where the captured session cookie is stored between phases. */
    private static final String TOKEN_FILE_PATH =
            "src/test/resources/data/api/okta_token.json";

    /** Login credentials file. */
    private static final String LOGIN_DATA_PATH =
            "src/test/resources/data/ui/twix_login.json";

    /**
     * NextAuth sets the session under one of these cookie names depending on
     * whether the site runs on HTTP (dev) or HTTPS (prod/QA).
     *
     *   HTTP  →  next-auth.session-token
     *   HTTPS →  __Secure-next-auth.session-token
     *
     * QA typically runs on HTTPS, so the __Secure- prefix variant is checked
     * first. Both are tried so the same test works on any environment.
     */
    private static final String[] SESSION_COOKIE_NAMES = {
            "__Secure-next-auth.session-token",   // HTTPS environments (QA / prod)
            "next-auth.session-token"              // HTTP environments (local dev)
    };

    // ── Page objects ──────────────────────────────────────────────────────────

    TW_LoginPage   loginPage;
    TW_LandingPage landingPage;
    TW_Header      header;

    // ── Shared session cookie (set by Phase 1, read by Phase 2 & 3) ──────────

    private static String capturedSessionCookie;
    private static String capturedSessionCookieName;

    // ─────────────────────────────────────────────────────────────────────────
    // Setup
    // ─────────────────────────────────────────────────────────────────────────

    @BeforeMethod(dependsOnMethods = "createContextAndPage")
    public void initPages() {
        loginPage   = new TW_LoginPage(page, page.locator("body"));
        header      = new TW_Header(page, page.locator("header"));
        landingPage = new TW_LandingPage(page, page.locator("body"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Phase 1 – UI Login  →  Cookie Capture  →  Persist to disk
    // ─────────────────────────────────────────────────────────────────────────

    @Test(priority = 1)
    @Description("Log in via NextAuth and capture the session cookie from the browser context")
    public void captureOktaTokenViaLogin() {

        // ── 1. Navigate & log in ──────────────────────────────────────────────

        Allure.step("Navigate to the application");
        page.navigate(BASE_URL);
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        // ⚠️  Do NOT use LoadState.NETWORKIDLE — NextAuth polls /api/auth/session
        //     continuously, which means the network never goes fully idle and
        //     Playwright will time out after 30 s waiting for it.

        Allure.step("Click Sign In");
        header.clickSignIn();

        Allure.step("Perform login");
        String username = (String) JsonReader.fetchJsonValueByKey(LOGIN_DATA_PATH, "username");
        String password = (String) JsonReader.fetchJsonValueByKey(LOGIN_DATA_PATH, "password");
        loginPage.login(username, password);

        // ── 2. Wait for the session cookie to appear ──────────────────────────
        //
        // Instead of waiting for NETWORKIDLE (which times out), we poll the
        // browser context cookies until the NextAuth session cookie is present.
        // This is reliable because NextAuth sets the cookie exactly once during
        // the /api/auth/callback/auth0 redirect, before any polling starts.
        // ─────────────────────────────────────────────────────────────────────

        Allure.step("Poll for NextAuth session cookie");
        System.out.println("[SESSION CAPTURE] Waiting for NextAuth session cookie...");

        String sessionToken = null;
        String sessionCookieName = null;
        long deadline = System.currentTimeMillis() + 30_000; // 30 s max

        outer:
        while (System.currentTimeMillis() < deadline) {
            List<com.microsoft.playwright.options.Cookie> cookies =
                    page.context().cookies();

            for (String candidateName : SESSION_COOKIE_NAMES) {
                for (com.microsoft.playwright.options.Cookie c : cookies) {
                    if (candidateName.equals(c.name) && c.value != null && !c.value.isEmpty()) {
                        sessionToken     = c.value;
                        sessionCookieName = c.name;
                        break outer;
                    }
                }
            }

            // Not found yet — wait 500 ms before next poll
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }

        // ── 3. Assert the cookie was found ────────────────────────────────────

        Assert.assertNotNull(sessionToken,
                "NextAuth session cookie not found after 30 s. " +
                "Checked cookie names: " + String.join(", ", SESSION_COOKIE_NAMES) + ". " +
                "Verify the cookie name via DevTools → Application → Cookies.");

        capturedSessionCookie     = sessionToken;
        capturedSessionCookieName = sessionCookieName;

        System.out.println("[SESSION CAPTURE] Cookie name  : " + capturedSessionCookieName);
        System.out.println("[SESSION CAPTURE] Cookie value (first 40 chars): "
                + capturedSessionCookie.substring(0, Math.min(40, capturedSessionCookie.length())) + "…");

        // ── 4. Persist to disk ────────────────────────────────────────────────

        Allure.step("Persist session cookie to: " + TOKEN_FILE_PATH);
        saveTokenToFile(capturedSessionCookieName, capturedSessionCookie, TOKEN_FILE_PATH);

        System.out.println("[SESSION CAPTURE] Cookie saved to: " + TOKEN_FILE_PATH);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Phase 2 – API Validation using the session cookie
    // ─────────────────────────────────────────────────────────────────────────

    @Test(priority = 2, dependsOnMethods = "captureOktaTokenViaLogin")
    @Description("Use the captured NextAuth session cookie to call /api/MyCart and validate the response")
    public void validateCartApiWithCapturedCookie() {

        Allure.step("Load the captured session cookie");
        String[] resolved = resolveToken();
        String cookieName  = resolved[0];
        String cookieValue = resolved[1];
        Assert.assertNotNull(cookieValue, "Session cookie is null — captureOktaTokenViaLogin must run first.");

        // ── Build Cookie header ───────────────────────────────────────────────
        // NextAuth APIs authenticate via the Cookie header, not Authorization.

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookieName + "=" + cookieValue);
        headers.put("Accept", "application/json");

        // ── Call GET /api/MyCart ──────────────────────────────────────────────

        Allure.step("Call GET " + CART_ENDPOINT);
        io.restassured.response.Response cartResponse =
                RA_API_Utils.getReqWithHeaders(API_BASE_URL, CART_ENDPOINT, headers);

        RA_ResponseHelpers.printFullResponse(cartResponse);

        // ── Core assertions ───────────────────────────────────────────────────

        Allure.step("Assert HTTP 200 OK");
        RA_AssertionUtils.assertStatusCode(cartResponse, 200);

        Allure.step("Assert Content-Type is JSON");
        RA_AssertionUtils.assertContentType(cartResponse, "application/json");

        Allure.step("Assert response body is not empty");
        String responseBody = cartResponse.getBody().asString();
        Assert.assertFalse(responseBody.isEmpty(),
                "Response body from " + CART_ENDPOINT + " should not be empty.");

        // ── Schema assertions — adjust JSON paths to your actual response ──────

        Allure.step("Assert first item has an 'id' field");
        RA_AssertionUtils.assertJsonFieldNotNull(cartResponse, "[0].id");

        Allure.step("Assert first item has a 'name' field");
        RA_AssertionUtils.assertJsonFieldNotNull(cartResponse, "[0].name");

        // ── Performance assertion ─────────────────────────────────────────────

        Allure.step("Assert response time is under 5 seconds");
        RA_AssertionUtils.assertResponseTime(cartResponse.getTime(), 5000);

        // ── Save response to disk ─────────────────────────────────────────────

        RA_ResponseHelpers.saveResponseToFile(
                cartResponse,
                "src/test/resources/data/api/cart_response.json");

        System.out.println("[API TEST] " + CART_ENDPOINT + " validation passed.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Phase 3 – Negative: no cookie → should return 401 / 403
    // ─────────────────────────────────────────────────────────────────────────

    @Test(priority = 3)
    @Description("Call /api/MyCart without a session cookie and expect 401 Unauthorized")
    public void validateCartApiReturns401WithoutCookie() {

        Allure.step("Call GET " + CART_ENDPOINT + " with no Cookie header");
        io.restassured.response.Response unauthorizedResponse =
                RA_API_Utils.getRequest(API_BASE_URL, CART_ENDPOINT);

        RA_ResponseHelpers.printResponseBody(unauthorizedResponse);

        Allure.step("Assert 401 Unauthorized");
        RA_AssertionUtils.assertStatusCode(unauthorizedResponse, 401);

        System.out.println("[API TEST] 401 guard on " + CART_ENDPOINT + " confirmed.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Persists the session cookie name + value to a JSON file.
     *
     * Output format:
     * {
     *   "cookie_name":  "__Secure-next-auth.session-token",
     *   "bearer_token": "eyJ..."
     * }
     *
     * The key is kept as "bearer_token" so existing Postman environments
     * that already reference that key continue to work without changes.
     */
    private void saveTokenToFile(String cookieName, String cookieValue, String filePath) {
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
        } catch (IOException e) {
            System.err.println("[TOKEN SAVE] Could not create directories: " + e.getMessage());
        }

        String json = "{\n"
                + "  \"cookie_name\":  \"" + cookieName  + "\",\n"
                + "  \"bearer_token\": \"" + cookieValue + "\"\n"
                + "}";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json);
        } catch (IOException e) {
            System.err.println("[TOKEN SAVE] Failed to write token file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Returns [cookieName, cookieValue].
     * Uses the in-memory values if available (same JVM run), otherwise
     * reads them from the persisted JSON file.
     */
    private String[] resolveToken() {
        if (capturedSessionCookie != null && !capturedSessionCookie.isEmpty()) {
            return new String[]{ capturedSessionCookieName, capturedSessionCookie };
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(TOKEN_FILE_PATH)));

            // Parse cookie_name
            int ns = content.indexOf("cookie_name") + "cookie_name".length();
            ns = content.indexOf('"', ns) + 1;
            int ne = content.indexOf('"', ns);
            String name = content.substring(ns, ne);

            // Parse bearer_token (cookie value)
            int vs = content.indexOf("bearer_token") + "bearer_token".length();
            vs = content.indexOf('"', vs) + 1;
            int ve = content.indexOf('"', vs);
            String value = content.substring(vs, ve);

            return new String[]{ name, value };
        } catch (IOException e) {
            System.err.println("[TOKEN RESOLVE] Could not read token from file: " + e.getMessage());
            return new String[]{ null, null };
        }
    }
}