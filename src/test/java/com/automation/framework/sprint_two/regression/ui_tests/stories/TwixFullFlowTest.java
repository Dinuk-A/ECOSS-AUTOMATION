package com.automation.framework.sprint_two.regression.ui_tests.stories;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.automation.framework.utils.common.JsonReader;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.automation.framework.ui_pages.playwright.twix.TW_LandingPage;
import com.automation.framework.ui_pages.playwright.twix.TW_LoginPage;
import com.automation.framework.ui_pages.playwright.twix.TW_ProductCard;
import com.automation.framework.ui_pages.playwright.twix.TW_ProductsListingPage;
import com.automation.framework.ui_pages.playwright.twix.TW_OneProductDetailsPage;
import com.automation.framework.utils.common.ConfigPropertyReader;
import com.automation.framework.utils.ui.core.PlaywrightBaseTest;
import com.automation.framework.utils.ui.playwright.PW_ScreenCaptureUtils;
import org.testng.asserts.SoftAssert;
import com.automation.framework.ui_pages.playwright.twix.TW_CartPage;
import com.automation.framework.ui_pages.playwright.twix.TW_Header;
// import com.automation.framework.ui_pages.playwright      .cookiedent.CookiedentLoginPage;
import com.automation.framework.ui_pages.playwright.twix.TW_CartItem;

@Epic("Twix Full Flow Validation")
@Feature("Validate full customer journey in UI")
public class TwixFullFlowTest extends PlaywrightBaseTest {

        private static final String TWIX_BASE_URL_QA = ConfigPropertyReader.getProperty(
                        "configs/ecos.properties",
                        "twix.frontend.base.url.qa");

        TW_LoginPage loginPage;
        TW_LandingPage landingPage;
        TW_Header header;
        TW_ProductsListingPage productsPage;
        TW_OneProductDetailsPage productDetailsPage;

        @BeforeMethod(dependsOnMethods = "createContextAndPage")
        public void initPages() {
                loginPage = new TW_LoginPage(page, page.locator("body"));
                header = new TW_Header(page, page.locator("header"));
                landingPage = new TW_LandingPage(page, page.locator("body"));
                productsPage = new TW_ProductsListingPage(page);
                productDetailsPage = new TW_OneProductDetailsPage(page, page.locator("body")); // ← fixed type
        }

        @Test(enabled = true)
        @Description("Validate the product details consistency between product listing and product details page")
        public void validateProductDetailsConsistency() {

                // ── Navigate & Login ──────────────────────────────────────────────────────

                page.navigate(TWIX_BASE_URL_QA);
                page.waitForLoadState();

                header.clickSignIn();

                Allure.step("Login");
                String twixLoginFilePath = "src/test/resources/data/ui/ecos/twix_login.json";
                String username = (String) JsonReader.fetchJsonValueByKey(twixLoginFilePath, "username");
                String password = (String) JsonReader.fetchJsonValueByKey(twixLoginFilePath, "password");

                loginPage.login(username, password);

                landingPage.clickBuyProducts();

                page.waitForTimeout(2000);

                Allure.step("Capturing screenshot after login");
                attachScreenshot("Post-Login Screenshot");

                // ── Navigate to Products ──────────────────────────────────────────────────

                page.waitForSelector("p:has-text('SKU:')");
                page.waitForTimeout(2000);

                Allure.step("Capturing screenshot of products listing page");
                attachScreenshot("Products Listing Page Screenshot");

                // ── Capture First Card Details from Listing ───────────────────────────────

                Allure.step("Capture first product details from listing page");
                TW_ProductCard firstCard = productsPage.getCard(0);

                String listingName = firstCard.getProductName();
                String listingSku = firstCard.getProductSku();
                String listingPrice = firstCard.getProductPrice();

                Allure.addAttachment("Listing - Product Name", "text/plain", listingName);
                Allure.addAttachment("Listing - Product SKU", "text/plain", listingSku);
                Allure.addAttachment("Listing - Product Price", "text/plain", listingPrice);

                // ── Navigate to Product Details Page ──────────────────────────────────────

                Allure.step("Click first product card to navigate to details page");
                firstCard.clickProductName();
                page.waitForLoadState();
                page.waitForTimeout(1000);

                Allure.step("Capturing screenshot of product details page");
                attachScreenshot("Product Details Page Screenshot");

                // ── Capture Details Page Values ───────────────────────────────────────────

                Allure.step("Capture product details from details page");
                String detailsName = productDetailsPage.getProductName();
                String detailsSku = productDetailsPage.getProductSku();
                String detailsPrice = productDetailsPage.getProductPrice();

                Allure.addAttachment("Details - Product Name", "text/plain", detailsName);
                Allure.addAttachment("Details - Product SKU", "text/plain", detailsSku);
                Allure.addAttachment("Details - Product Price", "text/plain", detailsPrice);

                // ── Assertions ────────────────────────────────────────────────────────────

                Allure.step("Assert product name matches between listing and details page");
                Assert.assertEquals(
                                detailsName,
                                listingName,
                                "Product name mismatch between listing and details page");

                Allure.step("Assert product SKU matches between listing and details page");
                Assert.assertEquals(
                                detailsSku,
                                listingSku,
                                "Product SKU mismatch between listing and details page");

                Allure.step("Assert product price matches between listing and details page");
                // Listing : "€8.90 inc. VAT" → strip currency symbol + suffix → "8.90"
                // Details : "£8.90" → strip currency symbol → "8.90"
                String listingPriceNormalised = listingPrice
                                .replace(" inc. VAT", "")
                                .replaceAll("[^\\d.]", "") // removes €, £, or any non-numeric character except dot
                                .trim();

                String detailsPriceNormalised = detailsPrice
                                .replaceAll("[^\\d.]", "") // removes £ or any non-numeric character except dot
                                .trim();

                Assert.assertEquals(
                                detailsPriceNormalised,
                                listingPriceNormalised,
                                "Product price mismatch between listing and details page");

                Allure.addAttachment("Details - Product Price", "text/plain", detailsPrice);
                Allure.addAttachment("Details - Product Price Normalised", "text/plain", detailsPriceNormalised);
                Allure.addAttachment("Listing - Product Price", "text/plain", listingPrice);
                Allure.addAttachment("Listing - Product Price Normalised", "text/plain", listingPriceNormalised);
        }

        @Test(enabled = true)
        @Description("Validate cart contents and total price after adding products from listing page")
        public void validateCartContents() {

                // ── Navigate & Login ──────────────────────────────────────────────────────

                page.navigate(TWIX_BASE_URL_QA);
                page.waitForLoadState();

                header.clickSignIn();

                Allure.step("Login");
                String twixLoginFilePath = "src/test/resources/data/ui/ecos/twix_login.json";
                String username = (String) JsonReader.fetchJsonValueByKey(twixLoginFilePath, "username");
                String password = (String) JsonReader.fetchJsonValueByKey(twixLoginFilePath, "password");

                loginPage.login(username, password);

                landingPage.clickBuyProducts();

                page.waitForTimeout(2000);

                Allure.step("Capturing screenshot after login");
                attachScreenshot("Cart Test - Post-Login Screenshot");

                // ── Navigate to Products ──────────────────────────────────────────────────

                page.waitForSelector("p:has-text('SKU:')");
                page.waitForTimeout(1000);

                Allure.step("Capturing screenshot of products listing page 1");
                attachScreenshot("Cart Test - Products Page 1 Screenshot");

                // ── Clear Cart to Ensure Fresh State ────────────────────────────────────────
                clearCart();

                // ── Add First Card from Page 1 ────────────────────────────────────────────

                Allure.step("Capture and add first product from page 1");
                TW_ProductCard page1FirstCard = productsPage.getCard(0);

                String page1Name = page1FirstCard.getProductName();
                String page1Sku = page1FirstCard.getProductSku();
                String page1Price = page1FirstCard.getProductPrice();

                Allure.addAttachment("Page 1 - Added Product Name", "text/plain", page1Name);
                Allure.addAttachment("Page 1 - Added Product SKU", "text/plain", page1Sku);
                Allure.addAttachment("Page 1 - Added Product Price", "text/plain", page1Price);

                page1FirstCard.clickAddToCart();
                page.waitForTimeout(1000);

                // ── Go to Page 2 ─────────────────────────────────────────────────────────

                Allure.step("Navigate to products page 2");
                productsPage.clickNextPage();
                page.waitForSelector("p:has-text('SKU:')");
                page.waitForTimeout(1000);

                Allure.step("Capturing screenshot of products listing page 2");
                attachScreenshot("Cart Test - Products Page 2 Screenshot");

                // ── Add First Card from Page 2 ────────────────────────────────────────────

                Allure.step("Capture and add first product from page 2");
                TW_ProductCard page2FirstCard = productsPage.getCard(0);

                String page2Name = page2FirstCard.getProductName();
                String page2Sku = page2FirstCard.getProductSku();
                String page2Price = page2FirstCard.getProductPrice();

                Allure.addAttachment("Page 2 - Added Product Name", "text/plain", page2Name);
                Allure.addAttachment("Page 2 - Added Product SKU", "text/plain", page2Sku);
                Allure.addAttachment("Page 2 - Added Product Price", "text/plain", page2Price);

                page2FirstCard.clickAddToCart();
                page.waitForTimeout(1000);

                // ── Navigate to Cart ──────────────────────────────────────────────────────

                Allure.step("Navigate to cart page");
                // landingPage.clickCart();
                header.clickCart();
                page.waitForLoadState();
                page.waitForTimeout(1000);

                Allure.step("Capturing screenshot of cart page");
                attachScreenshot("Cart Test - Cart Page Screenshot");

                // ── Capture Cart State ────────────────────────────────────────────────────

                Allure.step("Capture cart contents");
                TW_CartPage cartPage = new TW_CartPage(page);

                int expectedItemCount = 2;
                int actualItemCount = cartPage.getCartItemCount();
                int headerItemCount = cartPage.getCartItemCountFromHeader();

                TW_CartItem cartItem1 = cartPage.getItem(0);
                TW_CartItem cartItem2 = cartPage.getItem(1);

                String cart1Name = cartItem1.getItemName();
                String cart1Sku = cartItem1.getItemSku();
                String cart1Price = cartItem1.getPricePerUnit();

                String cart2Name = cartItem2.getItemName();
                String cart2Sku = cartItem2.getItemSku();
                String cart2Price = cartItem2.getPricePerUnit();

                String cartSubtotal = cartPage.getSubtotal();
                String cartTotal = cartPage.getTotal();

                Allure.addAttachment("Cart - Item Count (DOM)", "text/plain", String.valueOf(actualItemCount));
                Allure.addAttachment("Cart - Item Count (Header)", "text/plain", String.valueOf(headerItemCount));
                Allure.addAttachment("Cart - Item 1 Name", "text/plain", cart1Name);
                Allure.addAttachment("Cart - Item 1 SKU", "text/plain", cart1Sku);
                Allure.addAttachment("Cart - Item 1 Price", "text/plain", cart1Price);
                Allure.addAttachment("Cart - Item 2 Name", "text/plain", cart2Name);
                Allure.addAttachment("Cart - Item 2 SKU", "text/plain", cart2Sku);
                Allure.addAttachment("Cart - Item 2 Price", "text/plain", cart2Price);
                Allure.addAttachment("Cart - Subtotal", "text/plain", cartSubtotal);
                Allure.addAttachment("Cart - Total", "text/plain", cartTotal);

                // ── Soft Assertions ───────────────────────────────────────────────────────

                Allure.step("Soft assert cart contents and total");
                SoftAssert softAssert = new SoftAssert();

                // Item count
                softAssert.assertEquals(actualItemCount, expectedItemCount,
                                "Cart item count (DOM) mismatch");
                softAssert.assertEquals(headerItemCount, expectedItemCount,
                                "Cart item count (header) mismatch");

                // Item 1 — name and SKU
                softAssert.assertEquals(cart1Name, page1Name.toUpperCase(),
                                "Cart item 1 name mismatch");
                softAssert.assertEquals(cart1Sku, page1Sku,
                                "Cart item 1 SKU mismatch");

                // Item 2 — name and SKU
                softAssert.assertEquals(cart2Name, page2Name.toUpperCase(),
                                "Cart item 2 name mismatch");
                softAssert.assertEquals(cart2Sku, page2Sku,
                                "Cart item 2 SKU mismatch");

                // Price comparison — strip currency symbols for both listing and cart
                String page1PriceNormalised = page1Price.replace(" inc. VAT", "").replaceAll("[^\\d.]", "").trim();
                String page2PriceNormalised = page2Price.replace(" inc. VAT", "").replaceAll("[^\\d.]", "").trim();
                String cart1PriceNormalised = cart1Price.replaceAll("[^\\d.]", "").trim();
                String cart2PriceNormalised = cart2Price.replaceAll("[^\\d.]", "").trim();

                Allure.addAttachment("Page 1 Price Normalised", "text/plain", page1PriceNormalised);
                Allure.addAttachment("Page 2 Price Normalised", "text/plain", page2PriceNormalised);
                Allure.addAttachment("Cart Item 1 Price Normalised", "text/plain", cart1PriceNormalised);
                Allure.addAttachment("Cart Item 2 Price Normalised", "text/plain", cart2PriceNormalised);

                softAssert.assertEquals(cart1PriceNormalised, page1PriceNormalised,
                                "Cart item 1 price mismatch");
                softAssert.assertEquals(cart2PriceNormalised, page2PriceNormalised,
                                "Cart item 2 price mismatch");

                // Total price — sum of both normalised prices and compare against cart total

                BigDecimal page1PriceBD = new BigDecimal(page1PriceNormalised);
                BigDecimal page2PriceBD = new BigDecimal(page2PriceNormalised);

                BigDecimal expectedTotalBD = page1PriceBD.add(page2PriceBD)
                                .setScale(2, RoundingMode.HALF_UP);

                BigDecimal actualTotalBD = new BigDecimal(
                                cartTotal.replaceAll("[^\\d.]", "").trim()).setScale(2, RoundingMode.HALF_UP);

                Allure.addAttachment("Expected Total (rounded)", "text/plain", expectedTotalBD.toString());
                Allure.addAttachment("Actual Total (rounded)", "text/plain", actualTotalBD.toString());

                softAssert.assertEquals(actualTotalBD, expectedTotalBD,
                                "Cart total price mismatch — expected sum of both products");

                softAssert.assertAll();
        }
        // ── Helper
        // ────────────────────────────────────────────────────────────────────

        // Helper method to clear the cart before test execution to ensure a fresh state
        private void clearCart() {
                Allure.step("Clear cart before test — ensure fresh state");

                header.clickCart();
                page.waitForLoadState();
                page.waitForTimeout(1000);

                TW_CartPage cartPage = new TW_CartPage(page);

                int itemCount = cartPage.getCartItemCount();
                Allure.addAttachment("Cart - Items found before clearing", "text/plain", String.valueOf(itemCount));

                while (cartPage.getCartItemCount() > 0) {
                        TW_CartItem firstItem = cartPage.getItem(0);
                        firstItem.clickRemove();
                        page.waitForTimeout(500);
                }

                Allure.addAttachment("Cart - Items after clearing", "text/plain",
                                String.valueOf(cartPage.getCartItemCount()));

                // Navigate back to landing page after clearing
                cartPage.clickContinueShopping();
                page.waitForLoadState();
                page.waitForTimeout(1000);
        }

        // Helper method to capture and attach screenshots to Allure reports
        private void attachScreenshot(String label) {
                byte[] screenshot = PW_ScreenCaptureUtils.takePageScreenshotAsBytes(page);
                if (screenshot != null) {
                        Allure.addAttachment(label, "image/png",
                                        new java.io.ByteArrayInputStream(screenshot), "png");
                }
        }
}