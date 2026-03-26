package com.automation.framework    .sprint_two.regression.ui_tests.stories;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.framework    .utils.api.ProductsApiHelper;
import com.automation.framework    .utils.ui.ProductsPageUtils;
import com.automation.framework.ui_pages.playwright    .cookiedent.CD_ProductDetailsPage;
import com.automation.framework.utils.common.ConfigPropertyReader;
import com.automation.framework.utils.ui.core.PlaywrightBaseTest;
import com.automation.framework.utils.ui.playwright.PW_ScreenCaptureUtils;
import com.microsoft.playwright.Locator;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;

@Epic("E-Commerce Platform – API to UI Data Integrity")
@Feature("Product Details API to Storefront SKU Synchronization")
public class ProductsPageTests extends PlaywrightBaseTest {

    // for compare Product SKUs in product details API with SKUs in Cookiedent store
    // UI
    Set<Integer> productSKUsFromAPI;

    // empty arraylist for store SKUs from UI , later will be converted to a set
    ArrayList<Integer> uiSKUsViaPlaywright = new ArrayList<>();

    // empty set for store SKUs that is missing in UI
    Set<Integer> missingInUI = new HashSet<>();

    // url
    private static final String COOKIEDENT_PRODUCTS_PAGE_URL = ConfigPropertyReader.getProperty(
            "configs/ecos.properties",
            "cookiedent.frontend.base.url")
            + ConfigPropertyReader.getProperty("configs/ecos.properties",
                    "cookiedent.frontend.products.end.url");

    @Test
    @Story("S7774 – Validate product details returned by API are correctly rendered in UI")
    @Description("""
            Story ID 7774 - Cookiedent Product Listing Page

            Validates that product details returned by the product details API are displayed accurately on the Cookiedent product details page.
            The test compares expected values derived from the API response against the actual values rendered in the UI.
                                """)

    @Severity(SeverityLevel.CRITICAL)
    public void verifyProductDetailsRenderedInUI() {

        // Fetch SKUs from API dynamically
        Allure.step("Fetching product SKUs from API");
        productSKUsFromAPI = ProductsApiHelper.fetchSKUsFromAPI();

        // Validate that we got SKUs from API
        if (productSKUsFromAPI.isEmpty()) {
            Assert.fail("Failed to fetch SKUs from API. Cannot proceed with validation.");
        }

        // Navigate to the Cookiedent products page
        page.navigate(COOKIEDENT_PRODUCTS_PAGE_URL);

        // wait for page to load
        page.waitForLoadState();

        // wait for products to be visible
        page.waitForSelector("p:has-text('SKU:')");

        page.waitForTimeout(1000);

        // Capture screenshot of the first page and attach to Allure
        Allure.step("Capturing screenshot of first page");
        byte[] firstPageScreenshot = PW_ScreenCaptureUtils.takePageScreenshotAsBytes(page);
        if (firstPageScreenshot != null) {
            Allure.addAttachment("First Page Screenshot", "image/png",
                    new java.io.ByteArrayInputStream(firstPageScreenshot), "png");
        }

        int currentPage = 1;
        boolean hasNextPage = true;

        // Loop through all pages
        while (hasNextPage) {
            System.out.println("Scraping page " + currentPage + "...");
            Allure.step("Scraping SKUs from page " + currentPage);

            // Scrape SKUs from current page
            ProductsPageUtils.scrapeSKUsFromCurrentPage(page, uiSKUsViaPlaywright);

            // Check if "Next" button is enabled
            Locator nextButton = page.locator("button:has-text('Next')");

            // Check if next button exists and is not disabled
            if (nextButton.count() > 0 && !nextButton.isDisabled()) {
                System.out.println("Moving to next page...");
                nextButton.click();

                // Wait for page to load
                page.waitForLoadState();
                page.waitForSelector("p:has-text('SKU:')");

                // Optional: Add a small delay to ensure content is loaded
                page.waitForTimeout(1000);

                currentPage++;
            } else {
                System.out.println("No more pages. Finished scraping.");
                hasNextPage = false;
            }
        }

        // convert arraylist to set to remove duplicates
        Set<Integer> uiSKUsSet = new HashSet<>(uiSKUsViaPlaywright);

        // print in console for verify
        System.out.println("\nAll SKUs scraped from UI across " + currentPage + " page(s):");
        for (Integer sku : uiSKUsSet) {
            System.out.println(sku);
        }

        // find SKU IDs that in product details API but not in UI and add to missingInUI
        // set
        for (Integer id : productSKUsFromAPI) {
            if (!uiSKUsSet.contains(id)) {
                missingInUI.add(id);
            }
        }

        // show in allure report
        Allure.addAttachment("Page URL", "text/plain", COOKIEDENT_PRODUCTS_PAGE_URL);
        Allure.step("Total pages scraped: " + currentPage);

        Allure.addAttachment("Product Keys in API", "text/plain", productSKUsFromAPI.toString());
        Allure.step("Total Product Keys in API: " + productSKUsFromAPI.size());

        Allure.addAttachment("SKUs(Product IDs) retrieved from Cookiedent UI", "text/plain", uiSKUsSet.toString());
        Allure.step("Total unique SKUs(Product IDs) retrieved from UI: " + uiSKUsSet.size());

        // pass or fail test based on missing IDs count
        if (missingInUI.size() > 0) {
            System.out.println("\nThe following Product Keys are missing in the UI:");
            for (Integer missingId : missingInUI) {
                System.out.println(missingId);
            }

            Allure.addAttachment("Missing Product IDs in UI", "text/plain", missingInUI.toString());
            Allure.step("Total missing Product IDs in UI: " + missingInUI.size());

            // FAIL the test
            Assert.fail("Some Product Keys from product details API are missing in the UI. Missing count: "
                    + missingInUI.size() + ". See attachments for details.");

        } else {
            System.out.println("\nAll Product Keys from product details API are present in the UI.");
            System.out.println("Test Passed!");

            Allure.step("✓ All products validated successfully across " + currentPage + " page(s)");
        }
    }

    @Test
    @Story("S7774 – Validate product details returned by API are correctly rendered in UI")
    @Description("""
            Story ID 7774 - Cookiedent Product Listing Page

            TC005  Navigate to Product Detail Page from product card
                      """)
    @Severity(SeverityLevel.NORMAL)
    public void verifyProductCardNavigatesToDetailsPage() {

        // Navigate to the Cookiedent products page
        Allure.step("Navigating to Cookiedent products page");
        Allure.addAttachment("Page URL", "text/plain", COOKIEDENT_PRODUCTS_PAGE_URL);
        page.navigate(COOKIEDENT_PRODUCTS_PAGE_URL);

        // wait for page to load
        page.waitForLoadState();

        // get first product card's name
        Locator firstProductNameLocator = page
                .locator("(//div[contains(@class, 'group')]//h3[contains(@class, 'font-semibold')])[1]");
        String firstProductName = firstProductNameLocator.textContent().trim();

        // get first product card's SKU
        Locator firstProductSKULocator = page
                .locator("(//div[contains(@class, 'group')]//p[contains(text(), 'SKU:')])[1]");
        String firstProductSKUText = firstProductSKULocator.textContent().trim();

        // extract SKU number from text
        String firstProductSKU = firstProductSKUText.split("SKU:")[1].trim();

        Allure.step("Extracted Product Card Details:\nName: " + firstProductName + "\nSKU: " + firstProductSKU);

        // click name
        Allure.step("Clicking on the first product's name to navigate to Product Details Page");
        firstProductNameLocator.click();

        // wait for page to load
        page.waitForLoadState();
        page.waitForTimeout(1000);

        // Capture screenshot of the page and attach to Allure
        Allure.step("Capturing screenshot of page");
        byte[] firstPageScreenshot = PW_ScreenCaptureUtils.takePageScreenshotAsBytes(page);
        if (firstPageScreenshot != null) {
            Allure.addAttachment("Product Details Page Screenshot", "image/png",
                    new java.io.ByteArrayInputStream(firstPageScreenshot), "png");
        }

        // Create instance of CookiedentProductDetailsPage
        CD_ProductDetailsPage productDetailsPage = new CD_ProductDetailsPage(page);

        // get actual info on details page
        String productNameOnDetailsPage = productDetailsPage.getProductName();
        String productSKUOnDetailsPage = productDetailsPage.getSKU();
        String productPriceOnDetailsPage = productDetailsPage.getPriceEURInCent();

        Allure.step("Extracted Product Details from Product Details Page:\nName: " + productNameOnDetailsPage
                + "\nSKU: " + productSKUOnDetailsPage
                + "\nPrice: " + productPriceOnDetailsPage);

        // Validate that the details match the card details
        Assert.assertEquals(productNameOnDetailsPage, firstProductName, "Product name does not match");
        Assert.assertEquals(productSKUOnDetailsPage, firstProductSKU, "Product SKU does not match");

        Allure.step("Product details validated successfully");

    }

    @Test
    @Story("S7774 – Validate product details returned by API are correctly rendered in UI")
    @Description("""
            Story ID 7774 - Cookiedent Product Listing Page

            TC010  Navigate to next page in pagination
                      """)
    @Severity(SeverityLevel.NORMAL)
    public void verifyNextButtonFunctionality() {

        // Navigate to the Cookiedent products page
        page.navigate(COOKIEDENT_PRODUCTS_PAGE_URL);

        // wait for page to load
        page.waitForLoadState();
        page.waitForSelector("p:has-text('SKU:')");

        page.waitForTimeout(1000);

        // Capture screenshot of the first page and attach to Allure
        Allure.step("Capturing screenshot of first page");
        byte[] firstPageScreenshot = PW_ScreenCaptureUtils.takePageScreenshotAsBytes(page);
        if (firstPageScreenshot != null) {
            Allure.addAttachment("First Page Screenshot", "image/png",
                    new java.io.ByteArrayInputStream(firstPageScreenshot), "png");
        }

        // Click Next button
        // Locator nextButton = page.locator("//button[@aria-label='Next page']");
        Locator nextButton = page.locator("button:has-text('Next')");
        if (nextButton.count() > 0 && !nextButton.isDisabled()) {
            Allure.step("Clicking Next button to navigate to next page");
            nextButton.click();

            // Wait for page to load
            page.waitForLoadState();
            page.waitForSelector("p:has-text('SKU:')");

            page.waitForTimeout(1000);

            // Capture screenshot of the second page and attach to Allure
            byte[] secondPageScreenshot = PW_ScreenCaptureUtils.takePageScreenshotAsBytes(page);
            if (secondPageScreenshot != null) {
                Allure.addAttachment("Second Page Screenshot", "image/png",
                        new java.io.ByteArrayInputStream(secondPageScreenshot), "png");
            }

            Allure.step("Successfully navigated to the next page using Next button");

        } else {
            Assert.fail("Next button is not available or is disabled.");
        }
    }

}
