package com.automation.framework.sprint_two.regression.ui_tests.stories;

import com.automation.framework.ui_pages.playwright.cookiedent.CD_CartItem;
import com.automation.framework.ui_pages.playwright.cookiedent.CD_CartPage;
import com.automation.framework.ui_pages.playwright.cookiedent.CD_CheckoutPage;
import com.automation.framework.ui_pages.playwright.cookiedent.CD_LandingPage;
import com.automation.framework.ui_pages.playwright.cookiedent.CD_OrderConfirmationPage;
import com.automation.framework.ui_pages.playwright.cookiedent.CD_ProductCard;
import com.automation.framework.ui_pages.playwright.cookiedent.CD_ProductsPage;
import com.automation.framework.ui_pages.playwright.cookiedent.CookiedentLoginPage;
import com.automation.framework.utils.common.ConfigPropertyReader;
import com.automation.framework.utils.common.JsonReader;
import com.automation.framework.utils.ui.core.PlaywrightBaseTest;
import com.automation.framework.utils.ui.playwright.PW_ScreenCaptureUtils;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Cookiedent Full Flow Validation")
@Feature("Validate full customer journey in UI")
public class CookiedentFullFlowTest extends PlaywrightBaseTest {

        private static final String COOKIEDENT_BASE_URL_QA = ConfigPropertyReader.getProperty(
                        "configs/ecos.properties",
                        "cookiedent.frontend.base.url.qa");

        @Test
        @Story("Cookiedent Full Flow Validation")
        @Severity(SeverityLevel.CRITICAL)
        public void verifyProductDetailsRenderedInUI() {

                CookiedentLoginPage loginPage = new CookiedentLoginPage(page, page.locator("body"));
                CD_LandingPage landingPage = new CD_LandingPage(page, page.locator("body"));

                // Navigate to Cookiedent products page
                page.navigate(COOKIEDENT_BASE_URL_QA);

                // Wait for page to load
                page.waitForLoadState();
                page.reload();

                landingPage.clickLogin();

                Allure.step("Login via Okta");

                String oktaLoginFilePath = "src/test/resources/data/ui/ecos/okta_login.json";
                String username = (String) JsonReader.fetchJsonValueByKey(oktaLoginFilePath, "username");
                String password = (String) JsonReader.fetchJsonValueByKey(oktaLoginFilePath, "password");
                loginPage.enterUsername(username);
                loginPage.clickNext();
                loginPage.selectPasswordAuthenticator();
                loginPage.enterPassword(password);
                loginPage.clickVerify();

                page.waitForTimeout(1000);

                Allure.step("Capturing screenshot of first page");
                byte[] firstPageScreenshot = PW_ScreenCaptureUtils.takePageScreenshotAsBytes(page);
                if (firstPageScreenshot != null) {
                        Allure.addAttachment("First Page Screenshot", "image/png",
                                        new java.io.ByteArrayInputStream(firstPageScreenshot), "png");
                }

                // Click products from header
                landingPage.clickProducts();

                // Wait until SKUs are visible
                page.waitForSelector("p:has-text('SKU:')");

                Allure.step("Capturing screenshot of products page");
                byte[] productsPageScreenshot = PW_ScreenCaptureUtils.takePageScreenshotAsBytes(page);
                if (productsPageScreenshot != null) {
                        Allure.addAttachment("Products Page Screenshot", "image/png",
                                        new java.io.ByteArrayInputStream(productsPageScreenshot), "png");
                }

                CD_ProductsPage productsPage = new CD_ProductsPage(page);

                Allure.step("Capture first product details from page 1");
                CD_ProductCard firstProduct = productsPage.getCard(0);
                String firstProductName = firstProduct.getProductName();
                String firstProductSku = firstProduct.getProductSku();
                String firstProductPrice = firstProduct.getPrice();

                Allure.addAttachment("Product 1 - Captured from Products Page", "text/plain",
                                "Name  : " + firstProductName + "\n" +
                                                "SKU   : " + firstProductSku + "\n" +
                                                "Price : " + firstProductPrice);

                // Add to bucket
                productsPage.clickAddButton(0);

                Allure.step("Navigate to next page");
                productsPage.clickNextPage();

                // Wait for next page load
                page.waitForSelector("p:has-text('SKU:')");

                Allure.step("Capturing screenshot of second page");
                byte[] secondPageScreenshot = PW_ScreenCaptureUtils.takePageScreenshotAsBytes(page);
                if (secondPageScreenshot != null) {
                        Allure.addAttachment("Second Page Screenshot", "image/png",
                                        new java.io.ByteArrayInputStream(secondPageScreenshot), "png");
                }

                Allure.step("Capture first product details from page 2");
                CD_ProductCard secondPageFirstProduct = productsPage.getCard(0);
                String secondProductName = secondPageFirstProduct.getProductName();
                String secondProductSku = secondPageFirstProduct.getProductSku();
                String secondProductPrice = secondPageFirstProduct.getPrice();

                Allure.addAttachment("Product 2 - Captured from Products Page", "text/plain",
                                "Name  : " + secondProductName + "\n" +
                                                "SKU   : " + secondProductSku + "\n" +
                                                "Price : " + secondProductPrice);

                // Add to bucket
                productsPage.clickAddButton(0);

                Allure.step("Navigate to basket");
                landingPage.clickCart();

                // Wait for basket page to load
                page.waitForLoadState();

                Allure.step("Capturing screenshot of basket page");
                byte[] basketPageScreenshot = PW_ScreenCaptureUtils.takePageScreenshotAsBytes(page);
                if (basketPageScreenshot != null) {
                        Allure.addAttachment("Basket Page Screenshot", "image/png",
                                        new java.io.ByteArrayInputStream(basketPageScreenshot), "png");
                }

                // ── Cart assertions ──────────────────────────────────────────────────────

                Allure.step("Validate cart contents");
                CD_CartPage cartPage = new CD_CartPage(page);

                Assert.assertEquals(cartPage.getCartItemsCount(), 2, "Cart should contain exactly 2 items");

                CD_CartItem cartItem1 = cartPage.getCartItemBySku(firstProductSku);
                CD_CartItem cartItem2 = cartPage.getCartItemBySku(secondProductSku);

                String cart1Name = cartItem1.getProductName();
                String cart1Sku = cartItem1.getSku();
                String cart1Price = cartItem1.getPricePerUnit();

                String cart2Name = cartItem2.getProductName();
                String cart2Sku = cartItem2.getSku();
                String cart2Price = cartItem2.getPricePerUnit();

                Allure.step("Assert product 1 name, SKU, and price in cart");
                Allure.addAttachment("Product 1 - Comparison (Products Page vs Cart)", "text/plain",
                                "=== Captured from Products Page ===\n" +
                                                "Name  : " + firstProductName + "\n" +
                                                "SKU   : " + firstProductSku + "\n" +
                                                "Price : " + firstProductPrice + "\n\n" +
                                                "=== Captured from Cart ===\n" +
                                                "Name  : " + cart1Name + "\n" +
                                                "SKU   : " + cart1Sku + "\n" +
                                                "Price : " + cart1Price);

                Assert.assertEquals(cart1Name, firstProductName, "Cart item 1 - product name mismatch");
                Assert.assertEquals(cart1Sku, firstProductSku, "Cart item 1 - SKU mismatch");
                Assert.assertEquals(
                                parsePriceToDouble(cart1Price),
                                parsePriceToDouble(firstProductPrice),
                                0.01, "Cart item 1 - price per unit mismatch");

                Allure.step("Assert product 2 name, SKU, and price in cart");
                Allure.addAttachment("Product 2 - Comparison (Products Page vs Cart)", "text/plain",
                                "=== Captured from Products Page ===\n" +
                                                "Name  : " + secondProductName + "\n" +
                                                "SKU   : " + secondProductSku + "\n" +
                                                "Price : " + secondProductPrice + "\n\n" +
                                                "=== Captured from Cart ===\n" +
                                                "Name  : " + cart2Name + "\n" +
                                                "SKU   : " + cart2Sku + "\n" +
                                                "Price : " + cart2Price);

                Assert.assertEquals(cart2Name, secondProductName, "Cart item 2 - product name mismatch");
                Assert.assertEquals(cart2Sku, secondProductSku, "Cart item 2 - SKU mismatch");
                Assert.assertEquals(
                                parsePriceToDouble(cart2Price),
                                parsePriceToDouble(secondProductPrice),
                                0.01, "Cart item 2 - price per unit mismatch");

                Allure.step("Validate grand total equals sum of individual item totals");

                SoftAssert softAssert = new SoftAssert();

                double item1Total = parsePriceToDouble(cartItem1.getTotalPrice());
                double item2Total = parsePriceToDouble(cartItem2.getTotalPrice());
                double expectedTotal = item1Total + item2Total;

                String grandTotalText = cartPage.getGrandTotal();
                double actualTotal = parsePriceToDouble(grandTotalText);

                Allure.addAttachment("Grand Total Validation", "text/plain",
                                "Item 1 Total     : " + cartItem1.getTotalPrice() + "  ->  " + item1Total + "\n" +
                                                "Item 2 Total     : " + cartItem2.getTotalPrice() + "  ->  "
                                                + item2Total + "\n" +
                                                "Expected Total   : " + expectedTotal + "\n" +
                                                "Actual Total     : " + grandTotalText + "  ->  " + actualTotal);

                softAssert.assertEquals(actualTotal, expectedTotal, 0.01,
                                "Grand total should equal sum of both item totals");

                // Click checkout button
                cartPage.clickProceedToCheckout();

                // Wait for checkout page to load
                page.waitForLoadState();
                page.waitForTimeout(1000);

                Allure.step("Capturing screenshot of checkout page");
                byte[] checkoutPageScreenshot = PW_ScreenCaptureUtils.takePageScreenshotAsBytes(page);
                if (checkoutPageScreenshot != null) {
                        Allure.addAttachment("Checkout Page Screenshot", "image/png",
                                        new java.io.ByteArrayInputStream(checkoutPageScreenshot), "png");
                }

                // ── Fill checkout form ────────────────────────────────────────────────────
                Allure.step("Fill checkout form");
                CD_CheckoutPage checkoutPage = new CD_CheckoutPage(page);

                String checkoutEmail = "test.user@cookiedent.com";
                String checkoutCompany = "Cookiedent QA";
                String checkoutFirstName = "Test";
                String checkoutLastName = "User";
                String checkoutAddress = "Musterstraße 1";
                String checkoutPostalCode = "10115";
                String checkoutCity = "Berlin";
                String checkoutCountry = "Germany";

                checkoutPage.enterEmail(checkoutEmail);
                checkoutPage.enterCompany(checkoutCompany);
                checkoutPage.enterFirstName(checkoutFirstName);
                checkoutPage.enterLastName(checkoutLastName);
                checkoutPage.enterAddress(checkoutAddress);
                checkoutPage.enterPostalCode(checkoutPostalCode);
                checkoutPage.enterCity(checkoutCity);
                checkoutPage.selectCountry(checkoutCountry);

                Allure.addAttachment("Checkout Form - Submitted Values", "text/plain",
                                "Email       : " + checkoutEmail + "\n" +
                                                "Company     : " + checkoutCompany + "\n" +
                                                "First Name  : " + checkoutFirstName + "\n" +
                                                "Last Name   : " + checkoutLastName + "\n" +
                                                "Address     : " + checkoutAddress + "\n" +
                                                "Postal Code : " + checkoutPostalCode + "\n" +
                                                "City        : " + checkoutCity + "\n" +
                                                "Country     : " + checkoutCountry);

                Allure.step("Capturing screenshot of filled checkout form");
                byte[] filledCheckoutScreenshot = PW_ScreenCaptureUtils.takePageScreenshotAsBytes(page);
                if (filledCheckoutScreenshot != null) {
                        Allure.addAttachment("Filled Checkout Page Screenshot", "image/png",
                                        new java.io.ByteArrayInputStream(filledCheckoutScreenshot), "png");
                }

                // ── Place order ───────────────────────────────────────────────────────────
                Allure.step("Click Place Order");
                checkoutPage.clickPlaceOrder();

                page.waitForLoadState();
                page.waitForTimeout(5000);

                Allure.step("Capturing screenshot after placing order");
                byte[] orderConfirmationScreenshot = PW_ScreenCaptureUtils.takePageScreenshotAsBytes(page);
                if (orderConfirmationScreenshot != null) {
                        Allure.addAttachment("Order Confirmation Screenshot", "image/png",
                                        new java.io.ByteArrayInputStream(orderConfirmationScreenshot), "png");
                }

                // ── Order confirmation assertions ─────────────────────────────────────────
                Allure.step("Validate order confirmation page");
                CD_OrderConfirmationPage confirmationPage = new CD_OrderConfirmationPage(page, page.locator("body"));

                Assert.assertTrue(confirmationPage.isConfirmationDisplayed(),
                                "Order confirmation heading should be visible");

                Allure.addAttachment("Order Confirmation - Page Values", "text/plain",
                                "Heading       : " + confirmationPage.getHeading() + "\n" +
                                                "Thank You Msg : " + confirmationPage.getThankYouMessage());

                softAssert.assertAll();
        }

        // ── Helpers ───────────────────────────────────────────────────────────────

        private double parsePriceToDouble(String priceText) {
                String cleaned = priceText.replaceAll("[a-zA-Z€$£¥\\s]", "").trim();
                cleaned = cleaned.replaceAll("\\.+$", "");
                cleaned = cleaned.replaceAll("^\\.+", "");
                if (cleaned.contains(",") && cleaned.contains(".")) {
                        cleaned = cleaned.replace(".", "").replace(",", ".");
                } else if (cleaned.contains(",")) {
                        cleaned = cleaned.replace(",", ".");
                }
                return Double.parseDouble(cleaned);
        }

}
