package com.automation.framework.ui_pages.playwright.cookiedent;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

public class CD_ProductDetailsPage {

        private final Page page;

        // locators
        // Product Details fields (NOT USED DIRECTLY, but via methods)
        private final Locator productNameLocator;
        private final Locator skuLocator;
        private final Locator descriptionLocator;
        private final Locator categoryLocator;
        private final Locator colorLocator;
        private final Locator indicationLocator;
        private final Locator alloyTypeLocator;
        private final Locator pointTwoPercentYieldStrengthInMPaCuredLocator;
        private final Locator vickersHardnessLocator;
        private final Locator elongationAtBreakInPercentLocator;
        private final Locator meltingIntervalCelsiusLocator;
        private final Locator elementFeInPercentLocator;
        private final Locator elementMnInPercentLocator;
        private final Locator elementCInPercentLocator;
        private final Locator elementCoInPercentLocator;
        private final Locator elementCrInPercentLocator;
        private final Locator elementMoInPercentLocator;
        private final Locator elementSiInPercentLocator;
        private final Locator elementWInPercentLocator;
        private final Locator priceEURInCentLocator;
        private final Locator priceDisplayLocator;
        private final Locator imgLocator;

        // interactive elements
        private final Locator wishListBtnLocator;
        private final Locator shareBtnLocator;
        private final Locator qtyValueLocator;
        private final Locator minusBtnLocator;
        private final Locator plusBtnLocator;
        private final Locator addToCartBtnLocator;

        // constructor
        public CD_ProductDetailsPage(Page page) {

                this.page = page;

                // (Following locators are defined but not used directly)

                // Product Name
                this.productNameLocator = page.locator("xpath=//h1[contains(@class, 'text-3xl')]");

                // SKU
                this.skuLocator = page.locator("xpath=//span[contains(text(), 'SKU:')]");

                // Description
                this.descriptionLocator = page.locator(
                                "xpath=//p[contains(@class, 'text-sm') and contains(@class, 'text-gray-700')]");

                // Image locator
                this.imgLocator = page.locator("//div[contains(@class,'aspect-square')]//img");

                // Category
                this.categoryLocator = page.locator("xpath=//li[contains(., 'Category')]/span[2]");

                // Color
                this.colorLocator = page.locator("xpath=//li[contains(., 'Color')]/span[2]");

                // Indication
                this.indicationLocator = page.locator("xpath=//li[contains(., 'Indication')]/span[2]");

                // AlloyType
                this.alloyTypeLocator = page.locator("xpath=//li[contains(., 'AlloyType')]/span[2]");

                // PointTwoPercentYieldStrengthInMPaCured
                this.pointTwoPercentYieldStrengthInMPaCuredLocator = page
                                .locator("xpath=//li[contains(., 'PointTwoPercentYieldStrengthInMPaCured')]/span[2]");

                // VickersHardness
                this.vickersHardnessLocator = page.locator("xpath=//li[contains(., 'VickersHardness')]/span[2]");

                // ElongationAtBreakInPercent
                this.elongationAtBreakInPercentLocator = page
                                .locator("xpath=//li[contains(., 'ElongationAtBreakInPercent')]/span[2]");

                // MeltingIntervalCelsius
                this.meltingIntervalCelsiusLocator = page
                                .locator("xpath=//li[contains(., 'MeltingIntervalCelsius')]/span[2]");

                // ElementFeInPercent
                this.elementFeInPercentLocator = page.locator("xpath=//li[contains(., 'ElementFeInPercent')]/span[2]");

                // ElementMnInPercent
                this.elementMnInPercentLocator = page.locator("xpath=//li[contains(., 'ElementMnInPercent')]/span[2]");

                // ElementCInPercent
                this.elementCInPercentLocator = page.locator("xpath=//li[contains(., 'ElementCInPercent')]/span[2]");

                // ElementCoInPercent
                this.elementCoInPercentLocator = page.locator("xpath=//li[contains(., 'ElementCoInPercent')]/span[2]");

                // ElementCrInPercent
                this.elementCrInPercentLocator = page.locator("xpath=//li[contains(., 'ElementCrInPercent')]/span[2]");

                // ElementMoInPercent
                this.elementMoInPercentLocator = page.locator("xpath=//li[contains(., 'ElementMoInPercent')]/span[2]");

                // ElementSiInPercent
                this.elementSiInPercentLocator = page.locator("xpath=//li[contains(., 'ElementSiInPercent')]/span[2]");

                // ElementWInPercent
                this.elementWInPercentLocator = page.locator("xpath=//li[contains(., 'ElementWInPercent')]/span[2]");

                // PriceEURInCent
                this.priceEURInCentLocator = page.locator("xpath=//li[contains(., 'PriceEURInCent')]/span[2]");

                // Price Display
                this.priceDisplayLocator = page
                                .locator("xpath=//div[contains(@class, 'text-4xl') and contains(@class, 'font-bold')]");

                // Interactive elements
                // Wishlist button
                this.wishListBtnLocator = page.locator("xpath=//button[@aria-label='Add to wishlist']");

                // Share button
                this.shareBtnLocator = page.locator("xpath=//button[@aria-label='Share product']");

                // Quantity Value
                this.qtyValueLocator = page.locator("xpath=//input[@aria-label='Quantity']");

                // Minus button for quantity
                this.minusBtnLocator = page.locator("xpath=//button[@aria-label='Decrease quantity']");

                // Plus button for quantity
                this.plusBtnLocator = page.locator("xpath=//button[@aria-label='Increase quantity']");

                // Add to Cart button
                this.addToCartBtnLocator = page.locator("xpath=//button[contains(text(), 'Add To Cart') or .//text()[normalize-space()='Add To Cart']]");

        }

        // Getter methods for each product detail field
        public String getProductName() {
                return page.locator("xpath=//h1[contains(@class, 'text-3xl')]").textContent().trim();
        }

        public String getSKU() {
                String fullText = page.locator("xpath=//span[contains(text(), 'SKU:')]").textContent();
                return fullText.replace("SKU:", "").trim();
        }

        public String getDescription() {
                return page.locator(
                                "xpath=//p[contains(@class, 'text-sm') and        contains(@class, 'text-gray-700')]")
                                .textContent().trim();
        }

        public String getImgUrlValue(){
                return imgLocator.getAttribute("src").trim();
        }

        public String getElementCrInPercent() {
                return extractValue("//li[contains(., 'ElementCrInPercent')]");
        }

        public String getVickersHardness() {
                return extractValue("//li[contains(., 'VickersHardness')]");
        }

        public String getElementWInPercent() {
                return extractValue("//li[contains(., 'ElementWInPercent')]");
        }

        public String getPointTwoPercentYieldStrengthInMPaCured() {
                return extractValue("//li[contains(., 'PointTwoPercentYieldStrengthInMPaCured')]");
        }

        public String getElementMnInPercent() {
                return extractValue("//li[contains(., 'ElementMnInPercent')]");
        }

        public String getElementFeInPercent() {
                return extractValue("//li[contains(., 'ElementFeInPercent')]");
        }

        public String getElementMoInPercent() {
                return extractValue("//li[contains(., 'ElementMoInPercent')]");
        }

        public String getPriceEURInCent() {
                return extractValue("//li[contains(., 'PriceEURInCent')]");
        }

        public String getElongationAtBreakInPercent() {
                return extractValue("//li[contains(., 'ElongationAtBreakInPercent')]");
        }

        public String getMeltingIntervalCelsius() {
                return extractValue("//li[contains(., 'MeltingIntervalCelsius')]");
        }

        public String getAlloyType() {
                return extractValue("//li[contains(., 'AlloyType')]");
        }

        public String getColor() {
                return extractValue("//li[contains(., 'Color')]");
        }

        public String getCategory() {
                return extractValue("//li[contains(., 'Category')]");
        }

        public String getIndication() {
                return extractValue("//li[contains(., 'Indication')]");
        }

        public String getElementCInPercent() {
                return extractValue("//li[contains(., 'ElementCInPercent')]");
        }

        public String getElementCoInPercent() {
                return extractValue("//li[contains(., 'ElementCoInPercent')]");
        }

        public String getElementSiInPercent() {
                return extractValue("//li[contains(., 'ElementSiInPercent')]");
        }

        public String getPriceDisplay() {
                String priceText = page.locator(
                                "xpath=//div[contains(@class, 'text-4xl') and contains(@class, 'font-bold')]")
                                .textContent();
                return priceText.replace("£", "").trim();
        }

        //get add to cart button locator
        public Locator getAddToCartButtonLocator() {
                return addToCartBtnLocator;
        }

        // ################################# Actions #################################

        // click wish list button
        public void clickWishListButton() {
                wishListBtnLocator.click();
        }

        // click share button
        public void clickShareButton() {
                shareBtnLocator.click();
        }

        // click minus button
        public void clickMinusButton() {
                minusBtnLocator.click();
        }

        // get quantity value
        public String getQuantityValue() {
                return qtyValueLocator.textContent().trim();
        }

        // click plus button
        public void clickPlusButton() {
                plusBtnLocator.click();
        }

        // click Add to Cart button
        public void clickAddToCartButton() {
                addToCartBtnLocator.click();
        }

        // ############################### Helper Methods
        // ################################

        // Helper method to extract value from key-value pair
        private String extractValue(String xpath) {

                String fullText = page.locator("xpath=" + xpath).textContent();
                // Remove bullet symbol and split by colon
                String cleanText = fullText.replace("■", "").trim();
                if (cleanText.contains(":")) {
                        return cleanText.split(":")[1].trim();
                }
                return cleanText;
        }

}