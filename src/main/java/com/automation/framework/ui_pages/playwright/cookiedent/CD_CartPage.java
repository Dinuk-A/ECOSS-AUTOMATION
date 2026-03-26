package com.automation.framework.ui_pages.playwright.cookiedent;

import com.microsoft.playwright.Page;
import com.automation.framework.utils.ui.playwright.PW_ElementActions;
import com.microsoft.playwright.Locator;
import java.util.ArrayList;
import java.util.List;

public class CD_CartPage {

    private final Page page;
     private final PW_ElementActions elementActions;

    private static final String CART_ITEMS_XPATH = "//div[contains(@class, 'flex') and contains(@class, 'flex-col') and contains(@class, 'md:flex-row') and contains(@class, 'p-6') and contains(@class, 'border-b')]";

    // Order summary locators
    private static final String SUBTOTAL        = "xpath=//span[text()='Subtotal']/ancestor::div[contains(@class,'flex') and contains(@class,'justify-between')]/descendant::p[contains(@class,'text-2xl')]";
    private static final String GRAND_TOTAL     = "xpath=//span[text()='Total']/following-sibling::span[contains(@class,'font-extrabold')]";
    private static final String CHECKOUT_BUTTON = "xpath=//a[contains(text(),'Proceed To Checkout')]";

    public CD_CartPage(Page page) {
        this.page = page;
        this.elementActions = new PW_ElementActions(page);
    }

    /**
     * Get all cart items as a list of CartItem objects
     */
    public List<CD_CartItem> getAllCartItems() {
        List<CD_CartItem> cartItems = new ArrayList<>();
        Locator items = page.locator("xpath=" + CART_ITEMS_XPATH);
        int count = items.count();

        for (int i = 0; i < count; i++) {
            cartItems.add(new CD_CartItem(page, items.nth(i)));
        }
        return cartItems;
    }

    /**
     * Get cart item by position (1-based index)
     */
    public CD_CartItem getCartItemByIndex(int index) {
        String xpath = String.format("xpath=(" + CART_ITEMS_XPATH + ")[%d]", index);
        return new CD_CartItem(page, page.locator(xpath));
    }

    /**
     * Get cart item by SKU — uses contains(., sku) to handle React comment node splitting
     */
    public CD_CartItem getCartItemBySku(String sku) {
        String xpath = String.format(
                "xpath=" + CART_ITEMS_XPATH + "[.//p[contains(., '%s')]]", sku);
        return new CD_CartItem(page, page.locator(xpath));
    }

    /**
     * Get cart item by product name (partial match)
     */
    public CD_CartItem getCartItemByName(String productName) {
        String xpath = String.format(
                "xpath=" + CART_ITEMS_XPATH + "[.//h3[contains(., '%s')]]", productName);
        return new CD_CartItem(page, page.locator(xpath));
    }

    /**
     * Get total number of items in cart
     */
    public int getCartItemsCount() {
        return page.locator("xpath=" + CART_ITEMS_XPATH).count();
    }

    /**
     * Check if cart is empty
     */
    public boolean isCartEmpty() {
        return getCartItemsCount() == 0;
    }

    /**
     * Get product name of specific item by index
     */
    public String getProductName(int index) {
        return getCartItemByIndex(index).getProductName();
    }

    /**
     * Get SKU of specific item by index
     */
    public String getSku(int index) {
        return getCartItemByIndex(index).getSku();
    }

    /**
     * Get quantity of specific item by index
     */
    public int getQuantity(int index) {
        return getCartItemByIndex(index).getQuantity();
    }

    /**
     * Get total price of specific item by index
     */
    public String getTotalPrice(int index) {
        return getCartItemByIndex(index).getTotalPrice();
    }

    /**
     * Click plus button for specific item by index
     */
    public void clickPlusButton(int index) {
        getCartItemByIndex(index).clickPlus();
    }

    /**
     * Click minus button for specific item by index
     */
    public void clickMinusButton(int index) {
        getCartItemByIndex(index).clickMinus();
    }

    /**
     * Click delete button for specific item by index
     */
    public void clickDeleteButton(int index) {
        getCartItemByIndex(index).clickDelete();
    }

    /**
     * Update quantity for item by SKU
     */
    public void updateQuantityBySku(String sku, int newQuantity) {
        CD_CartItem item = getCartItemBySku(sku);
        int currentQty = item.getQuantity();

        if (newQuantity > currentQty) {
            for (int i = 0; i < (newQuantity - currentQty); i++) {
                item.clickPlus();
                page.waitForTimeout(300);
            }
        } else if (newQuantity < currentQty) {
            for (int i = 0; i < (currentQty - newQuantity); i++) {
                item.clickMinus();
                page.waitForTimeout(300);
            }
        }
    }

    /**
     * Remove item from cart by SKU
     */
    public void removeItemBySku(String sku) {
        getCartItemBySku(sku).clickDelete();
    }

    // ── Order Summary ─────────────────────────────────────────────────────────

    /**
     * Get subtotal text from order summary (e.g. "€293.00")
     */
    public String getSubtotal() {
        return page.locator(SUBTOTAL).textContent().trim();
    }

    /**
     * Get grand total text from order summary (e.g. "€293.00")
     */
    public String getGrandTotal() {
        return page.locator(GRAND_TOTAL).textContent().trim();
    }

    /**
     * Click the Proceed To Checkout button
     */
    public void clickProceedToCheckout() {
        // page.locator(CHECKOUT_BUTTON).click();
        elementActions.click(page.locator(CHECKOUT_BUTTON));
    }

}