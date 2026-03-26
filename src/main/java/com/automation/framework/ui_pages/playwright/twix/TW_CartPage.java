package com.automation.framework.ui_pages.playwright.twix;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class TW_CartPage {

    private final Page page;

    // ===== Cart Header =====
    private static final String CART_HEADER = "h2.text-xl.font-semibold.text-gray-800";

    // ===== Cart Items =====
    private static final String CART_ITEMS = "div.divide-y > div.flex.flex-col";

    // ===== Summary Section =====
    private static final String SUBTOTAL_AMOUNT = "p.text-2xl.font-medium.text-gray-900";
    private static final String TOTAL_AMOUNT = "span.text-3xl.font-extrabold.text-gray-900:last-of-type";
    private static final String CHECKOUT_BUTTON = "a:has-text('Proceed To Checkout')";

    // ===== Empty Cart State =====
    private static final String EMPTY_CART_HEADING = "h2.text-3xl.font-bold.text-gray-900";
    private static final String CONTINUE_SHOPPING_BUTTON = "a[href='/products']:has-text('Continue shopping')";

    public TW_CartPage(Page page) {
        this.page = page;
    }

    // ==============================
    // Cart Header
    // ==============================

    /**
     * Returns full header text e.g. "Your Cart (2)"
     */
    public String getCartHeaderText() {
        return page.locator(CART_HEADER).textContent().trim();
    }

    /**
     * Extracts and returns the item count from header e.g. 2
     */
    public int getCartItemCountFromHeader() {
        String header = getCartHeaderText(); // "Your Cart (2)"
        String count = header.replaceAll("[^\\d]", ""); // "2"
        return Integer.parseInt(count);
    }

    // ==============================
    // Cart Items
    // ==============================

    public Locator getAllItemLocators() {
        return page.locator(CART_ITEMS);
    }

    public int getCartItemCount() {
        return getAllItemLocators().count();
    }

    /**
     * Returns a TW_CartItem wrapper for the item at the given 0-based index.
     */
    public TW_CartItem getItem(int index) {
        Locator itemElement = getAllItemLocators().nth(index);
        return new TW_CartItem(page, itemElement);
    }

    // ==============================
    // Summary
    // ==============================

    public String getSubtotal() {
        return page.locator(SUBTOTAL_AMOUNT).textContent().trim();
    }

    public String getTotal() {
        return page.locator(TOTAL_AMOUNT).textContent().trim();
    }

    // ==============================
    // Actions
    // ==============================

    public void clickProceedToCheckout() {
        page.locator(CHECKOUT_BUTTON).click();
    }

    public boolean isCheckoutButtonVisible() {
        return page.locator(CHECKOUT_BUTTON).isVisible();
    }

    // ==============================
    // Empty Cart State
    // ==============================

    public boolean isCartEmpty() {
        return page.locator(CONTINUE_SHOPPING_BUTTON).isVisible();
    }

    public String getEmptyCartHeadingText() {
        return page.locator(EMPTY_CART_HEADING).textContent().trim();
        // Expected: "Your cart is empty"
    }

    public void clickContinueShopping() {
        page.locator(CONTINUE_SHOPPING_BUTTON).click();
    }
}