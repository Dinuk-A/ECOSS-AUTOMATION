package com.automation.framework.ui_pages.playwright.twix;

import com.automation.framework.utils.ui.playwright.PW_ElementActions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class TW_LandingPage {

    private final Locator element;
    private final PW_ElementActions elementActions;

    // ==============================
    // Landing Action Buttons
    // ==============================

    private static final String BUY_PRODUCTS_BUTTON  = "a[href='/products'] button";
    private static final String RECYCLE_SCRAP_BUTTON = "a[href='/recycling'] button";

    // ==============================
    // Constructor
    // ==============================

    public TW_LandingPage(Page page, Locator element) {
        this.element = element;
        this.elementActions = new PW_ElementActions(page);
    }

    // ==============================
    // Landing Button Actions
    // ==============================

    public void clickBuyProducts() {
        elementActions.click(element.locator(BUY_PRODUCTS_BUTTON));
    }

    public void clickRecycleScrap() {
        elementActions.click(element.locator(RECYCLE_SCRAP_BUTTON));
    }

    // ==============================
    // State Checks
    // ==============================

    public boolean isBuyProductsButtonVisible() {
        return element.locator(BUY_PRODUCTS_BUTTON).isVisible();
    }

    public boolean isRecycleScrapButtonVisible() {
        return element.locator(RECYCLE_SCRAP_BUTTON).isVisible();
    }
}