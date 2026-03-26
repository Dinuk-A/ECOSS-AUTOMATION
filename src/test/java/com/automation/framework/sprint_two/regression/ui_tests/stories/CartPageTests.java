package com.automation.framework.sprint_two.regression.ui_tests.stories;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.framework.ui_pages.playwright.cookiedent.CD_CartItem;
import com.automation.framework.ui_pages.playwright.cookiedent.CD_CartPage;
import com.automation.framework.ui_pages.playwright.cookiedent.CD_ProductDetailsPage;
import com.automation.framework.utils.common.ConfigPropertyReader;
import com.automation.framework.utils.ui.core.PlaywrightBaseTest;
import com.automation.framework.utils.ui.playwright.PW_ScreenCaptureUtils;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Epic("E-Commerce Platform")
@Feature("Cart Page UI Tests")
public class CartPageTests extends PlaywrightBaseTest {

        // for products page
        private static final String PRODUCT_ID = ConfigPropertyReader.getProperty("configs/ecos.properties",
                        "cookiedent.sample.product.id");
        private static final String PRODUCT_URL = ConfigPropertyReader.getProperty("configs/ecos.properties",
                        "cookiedent.frontend.base.url") + "/products/" + PRODUCT_ID;

        // for cart page
        private static final String CART_URL = ConfigPropertyReader.getProperty("configs/ecos.properties",
                        "cookiedent.frontend.base.url")
                        + ConfigPropertyReader.getProperty("configs/ecos.properties",
                                        "cookiedent.frontend.cart.end.url");

        @Test
        @Story("S7745 – Validate cart page functionalities")
        @Description("""
                        Story ID 7745 - Cookiedent Cart Page

                        TC001 Add product to cart from product detail page

                        """)
        @Severity(SeverityLevel.CRITICAL)
        public void verifyCartFunctionalities() {

                // initialize POMs
                CD_ProductDetailsPage cookiedentProductDetailsPage = new CD_ProductDetailsPage(page);
                CD_CartPage cookiedentCartPage = new CD_CartPage(page);

                // Navigate to product details page
                page.navigate(PRODUCT_URL);
                page.waitForLoadState();

                Allure.step("Navigate to product details page");
                Allure.addAttachment("Product URL", "text/plain", PRODUCT_URL);

                Allure.step("Capturing screenshot of product details page");
                byte[] prodDetailsPageSS = PW_ScreenCaptureUtils.takePageScreenshotAsBytes(page);
                if (prodDetailsPageSS != null) {
                        Allure.addAttachment("Product Details Page Screenshot", "image/png",
                                        new java.io.ByteArrayInputStream(prodDetailsPageSS), "png");
                }

                // click add to cart button
                cookiedentProductDetailsPage.clickAddToCartButton();

                // take a ss of the add to cart button after clicked
                Allure.step("Capturing screenshot after clicking Add to Cart button");

                // wait until button text changes to "Added to cart"
                page.waitForTimeout(750);

                byte[] afterAddToCartSS = PW_ScreenCaptureUtils
                                .takeElementScreenshotAsBytes(cookiedentProductDetailsPage.getAddToCartButtonLocator());
                if (afterAddToCartSS != null) {
                        Allure.addAttachment("After Add to Cart Screenshot", "image/png",
                                        new java.io.ByteArrayInputStream(afterAddToCartSS), "png");
                }

                // navigate to cart page
                page.navigate(CART_URL);
                page.waitForLoadState();

                Allure.step("Capturing screenshot of cart page");
                byte[] cartPageSS = PW_ScreenCaptureUtils.takePageScreenshotAsBytes(page);
                if (cartPageSS != null) {
                        Allure.addAttachment("Cart Page Screenshot", "image/png",
                                        new java.io.ByteArrayInputStream(cartPageSS), "png");
                }

                // check if cart is empty
                if (cookiedentCartPage.isCartEmpty()) {
                        Assert.fail("Cart is empty after adding product to cart from product details page.");
                }

                // get all cart items
                List<CD_CartItem> allItemsInCart = cookiedentCartPage.getAllCartItems();

                // empty array to store SKUs that will be extracted by cart items
                List<String> allSKUsFromCart = new ArrayList<>();

                for (int i = 0; i < allItemsInCart.size(); i++) {
                        allSKUsFromCart.add(allItemsInCart.get(i).getSku());
                }

                // show SKUs list of cart page in allure
                Allure.addAttachment("SKUs found on cart page", "text/plain", allSKUsFromCart.toString());

                // check if the allSKUsFromCart has newly added items SKU
                Assert.assertTrue(allSKUsFromCart.contains(PRODUCT_ID),
                                "Product with ID " + PRODUCT_ID + " should be in cart");

        }
}
