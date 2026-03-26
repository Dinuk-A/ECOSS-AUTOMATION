package com.automation.framework.ui_pages.playwright.cookiedent;

import com.automation.framework.utils.ui.playwright.PW_ElementActions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CD_ProductCard {

    private final Locator element;
     private final PW_ElementActions elementActions;

    // ===== Relative Locators (CSS) =====
    private static final String PRODUCT_IMAGE = "img";
    private static final String PRODUCT_NAME = "h3";
    private static final String PRODUCT_SKU = "p:has-text('SKU:')";
    private static final String PRODUCT_PRICE = "span:has-text('€')";
    private static final String ADD_BUTTON = "button:has-text('Add')";

    public CD_ProductCard(Page page,Locator element) {
        this.element = element;
        this.elementActions = new PW_ElementActions(page);
    }

    public String getProductName() {
        return element.locator(PRODUCT_NAME).textContent().trim();
    }

    public String getProductSku() {
        return element.locator(PRODUCT_SKU).textContent()
                .replace("SKU:", "")
                .trim();
    }

    public String getProductPrice() {
        return element.locator(PRODUCT_PRICE).textContent().trim();
    }

    public String getImageUrl() {
        return element.locator(PRODUCT_IMAGE).getAttribute("src");
    }

    public void clickAddToBasket() {
        // element.locator(ADD_BUTTON).click();
        elementActions.click(element.locator(ADD_BUTTON));
    }

    //get price text
    public String getPrice() {
        return element.locator(PRODUCT_PRICE).textContent().trim();
    }
}