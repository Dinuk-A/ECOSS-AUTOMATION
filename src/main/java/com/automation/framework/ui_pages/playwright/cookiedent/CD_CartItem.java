package com.automation.framework.ui_pages.playwright.cookiedent;

import com.automation.framework.utils.ui.playwright.PW_ElementActions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CD_CartItem {

    private final Locator element;
    private final PW_ElementActions elementActions;

    private static final String PRODUCT_NAME = "xpath=.//h3";
    private static final String SKU = "xpath=.//p[contains(., 'SKU')]";
    private static final String QUANTITY = "xpath=.//span[contains(@class, 'px-4') and contains(@class, 'py-1')]";
    private static final String PRICE_PER_UNIT = "xpath=.//p[text()='Price per unit']/following-sibling::p";
    private static final String TOTAL_PRICE = "xpath=.//p[text()='Total']/following-sibling::p";
    private static final String PLUS_BUTTON = "xpath=.//button[.//svg[contains(@class, 'lucide-plus')]]";
    private static final String MINUS_BUTTON = "xpath=.//button[.//svg[contains(@class, 'lucide-minus')]]";
    private static final String DELETE_BUTTON = "xpath=.//button[.//svg[contains(@class, 'lucide-trash2')]]";

    public CD_CartItem(Page page, Locator element) {
        this.element = element;
        this.elementActions = new PW_ElementActions(page);
    }

    public String getProductName() {
        return element.locator(PRODUCT_NAME).textContent().trim();
    }

    public String getSku() {
        String skuText = element.locator(SKU).textContent();
        return skuText.replace("SKU:", "").trim();
    }

    public int getQuantity() {
        return Integer.parseInt(element.locator(QUANTITY).textContent().trim());
    }

    public String getPricePerUnit() {
        return element.locator(PRICE_PER_UNIT).textContent().trim();
    }

    public String getTotalPrice() {
        return element.locator(TOTAL_PRICE).textContent().trim();
    }

    public void clickPlus() {
        elementActions.click(element.locator(PLUS_BUTTON));
    }

    public void clickMinus() {
        elementActions.click(element.locator(MINUS_BUTTON));
    }

    public void clickDelete() {
        elementActions.click(element.locator(DELETE_BUTTON));
    }

}
