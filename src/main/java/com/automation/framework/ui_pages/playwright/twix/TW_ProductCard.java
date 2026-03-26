package com.automation.framework.ui_pages.playwright.twix;

import com.automation.framework.utils.ui.playwright.PW_ElementActions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class TW_ProductCard {
    private final Locator element;
    private final PW_ElementActions elementActions;

    // ===== Relative Locators (CSS) =====
    private static final String PRODUCT_IMAGE = "img";
    private static final String PRODUCT_NAME = "h3";
    private static final String PRODUCT_LINK = "a:has(h3)";
    private static final String PRODUCT_SKU = "p.text-muted-foreground:first-of-type";
    private static final String PRODUCT_PRICE = "span.text-lg.font-bold";
    private static final String ADD_BUTTON = "button:has-text('Add')";
    private static final String INCREASE_QTY_BUTTON = "button[aria-label='Increase quantity']";
    private static final String DECREASE_QTY_BUTTON = "button[aria-label='Decrease quantity']";
    private static final String QUANTITY_DISPLAY = "span.border-x";

    public TW_ProductCard(Page page, Locator element) {
        this.element = element;
        this.elementActions = new PW_ElementActions(page);
    }

    // ===== Getters =====

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

    public String getImageAlt() {
        return element.locator(PRODUCT_IMAGE).getAttribute("alt");
    }

    public String getProductHref() {
        return element.locator(PRODUCT_LINK).getAttribute("href");
    }

    public String getQuantityDisplayed() {
        return element.locator(QUANTITY_DISPLAY).textContent().trim();
    }

    // ===== Actions =====

    public void clickAddToCart() {
        elementActions.click(element.locator(ADD_BUTTON));
    }

    public void clickIncreaseQuantity() {
        elementActions.click(element.locator(INCREASE_QTY_BUTTON));
    }

    public void clickDecreaseQuantity() {
        elementActions.click(element.locator(DECREASE_QTY_BUTTON));
    }

    public void clickProductName() {
        elementActions.click(element.locator(PRODUCT_LINK));
    }

    // ===== State Checks =====

    public boolean isAddButtonVisible() {
        return element.locator(ADD_BUTTON).isVisible();
    }

    public boolean isAddButtonEnabled() {
        return element.locator(ADD_BUTTON).isEnabled();
    }

    public boolean isDecreaseQuantityDisabled() {
        return !element.locator(DECREASE_QTY_BUTTON).isEnabled();
    }
}
