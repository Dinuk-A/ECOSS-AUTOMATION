package com.automation.framework.sprint_two.regression.ui_tests.stories;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.framework.utils.ui.core.PlaywrightBaseTest;
import com.automation.framework.utils.ui.playwright.PW_ScreenCaptureUtils;
import com.automation.framework.models.ProductDetailsModel;
import com.automation.framework.utils.api.ProductDetailsApiHelper;
import com.automation.framework.utils.ui.ProductDetailsPageUtils;
import com.automation.framework.ui_pages.playwright.cookiedent.CD_ProductDetailsPage;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

import com.automation.framework.utils.common.ConfigPropertyReader;

@Epic("E-Commerce Platform")
@Feature("Product Details API to Storefront Product Details Validation")
public class ProductDetailsPageTests extends PlaywrightBaseTest {

        private static final String PRODUCT_ID = ConfigPropertyReader.getProperty("configs/ecos.properties",
                        "cookiedent.sample.product.id");
        private static final String PRODUCT_URL = ConfigPropertyReader.getProperty("configs/ecos.properties",
                        "cookiedent.frontend.base.url") + "/products/" + PRODUCT_ID;

        @Test
        @Story("S7774 – Cookiedent Product Listing Page")
        @Description("""
                        Story ID 7774 - Cookiedent Product Listing Page

                        TC001	Verify total number of products is displayed
                        TC009	Display first 12 products and enable pagination when more than 12 products are present
                                """)
        @Severity(SeverityLevel.NORMAL)
        public void verifyProductDetailsInUI() {

                // initialize cookiedentProductDetailsPage POM
                CD_ProductDetailsPage cookiedentProductDetailsPage = new CD_ProductDetailsPage(page);

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

                String cookiedentDomain = ConfigPropertyReader.getProperty("configs/ecos.properties",
                                "poc.cookiedent.tenant.domain.value");

                // Get expected data from API
                Allure.step("Fetching expected data from Product Details API");
                ProductDetailsModel expected = ProductDetailsApiHelper.getProductDetailsFromApi(PRODUCT_ID,
                                cookiedentDomain);

                // Actual values from UI
                Allure.step("Mapping actual product details from UI");
                ProductDetailsModel actual = ProductDetailsPageUtils.mapFromUI(cookiedentProductDetailsPage);

                // Validate expected vs actual
                Allure.step("Validating product details");
                ProductDetailsPageUtils.validate(expected, actual);
        }

        @Test
        @Story("S7774 – Validate product details returned by API are correctly rendered in UI")
        @Description("""
                        Story ID 7774 - Cookiedent Product Listing Page

                        TC002   Verify product image is displayed when available
                                       """)
        @Severity(SeverityLevel.NORMAL)
        public void verifyProductImageRendering() {

                // Navigate to product details page
                page.navigate(PRODUCT_URL);

                // wait for page to load
                page.waitForLoadState();

                // Capture screenshot of the first page and attach to Allure
                byte[] productDetailsScreenshot = PW_ScreenCaptureUtils.takePageScreenshotAsBytes(page);
                if (productDetailsScreenshot != null) {
                        Allure.addAttachment("Product Details Page Screenshot", "image/png",
                                        new java.io.ByteArrayInputStream(productDetailsScreenshot), "png");
                }

                // extract image URL from UI
                CD_ProductDetailsPage cookiedentProductDetailsPage = new CD_ProductDetailsPage(page);
                String uiImageUrl = cookiedentProductDetailsPage.getImgUrlValue();

                Allure.addAttachment("Extracted Image URL from UI", "text/plain", uiImageUrl);

                // extract image URL from API
                Allure.step("Fetching expected image URL from Product Details API");
                String apiImageUrl = ProductDetailsApiHelper.getProductImageUrlFromApi(PRODUCT_ID);

                Allure.addAttachment("Expected Image URL from API", "text/plain", apiImageUrl);

                // Validate
                Allure.step("Validating image URL matches");
                Assert.assertEquals(uiImageUrl, apiImageUrl, "Image URL mismatch between UI and API");

        }

}
