package com.automation.framework.ui_pages.playwright.twix;

import com.automation.framework.utils.ui.playwright.PW_ElementActions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class TW_CartItem {

    private final Locator element;
    private final PW_ElementActions elementActions;

    // ===== Product Info =====
    private static final String ITEM_NAME          = "h3.font-bold.text-gray-800";
    private static final String ITEM_SKU           = "p.text-xs.text-gray-400";
    private static final String ITEM_IMAGE         = "img";

    // ===== Pricing =====
    private static final String PRICE_PER_UNIT     = "p.text-gray-600.font-medium";
    private static final String ITEM_TOTAL         = "p.text-gray-900.font-bold.text-lg";

    // ===== Quantity Stepper =====
    private static final String QUANTITY_DISPLAY   = "span.px-4.py-1.text-sm.font-medium";
    private static final String INCREASE_BUTTON    = "button.border-l.border-gray-300";
    private static final String DECREASE_BUTTON    = "button.border-r.border-gray-300";

    // ===== Remove =====
    private static final String REMOVE_BUTTON      = "button.text-orange-400";

    public TW_CartItem(Page page, Locator element) {
        this.element = element;
        this.elementActions = new PW_ElementActions(page);
    }

    // ==============================
    // Product Info Getters
    // ==============================

    public String getItemName() {
        return element.locator(ITEM_NAME).textContent().trim();
    }

    /**
     * Returns SKU value only e.g. "81027484" — strips the "SKU: " prefix.
     */
    public String getItemSku() {
        return element.locator(ITEM_SKU).textContent()
                .replace("SKU", "")
                .replace(":", "")
                .trim();
    }

    public String getImageUrl() {
        return element.locator(ITEM_IMAGE).getAttribute("src");
    }

    public String getImageAlt() {
        return element.locator(ITEM_IMAGE).getAttribute("alt");
    }

    // ==============================
    // Pricing Getters
    // ==============================

    public String getPricePerUnit() {
        return element.locator(PRICE_PER_UNIT).textContent().trim();
    }

    public String getItemTotal() {
        return element.locator(ITEM_TOTAL).textContent().trim();
    }

    // ==============================
    // Quantity Stepper
    // ==============================

    public String getQuantityDisplayed() {
        return element.locator(QUANTITY_DISPLAY).textContent().trim();
    }

    public int getQuantityAsInt() {
        return Integer.parseInt(getQuantityDisplayed().trim());
    }

    public void clickIncreaseQuantity() {
        elementActions.click(element.locator(INCREASE_BUTTON));
    }

    public void clickDecreaseQuantity() {
        elementActions.click(element.locator(DECREASE_BUTTON));
    }

    // ==============================
    // Remove
    // ==============================

    public void clickRemove() {
        elementActions.click(element.locator(REMOVE_BUTTON));
    }

    public boolean isRemoveButtonVisible() {
        return element.locator(REMOVE_BUTTON).isVisible();
    }
}