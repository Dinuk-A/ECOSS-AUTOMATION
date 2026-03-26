package com.automation.framework.ui_pages.playwright.twix;

import com.automation.framework.utils.ui.playwright.PW_ElementActions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class TW_OneProductDetailsPage {

    private final Locator element;
    private final PW_ElementActions elementActions;

    // ===== Product Info =====
    private static final String PRODUCT_NAME    = "h1";
    private static final String PRODUCT_SKU     = "span.text-sm.text-gray-600";
    private static final String PRODUCT_PRICE   = "div.text-4xl.font-bold.text-gray-900";
    private static final String PRODUCT_IMAGE   = "img";
    private static final String PRODUCT_DESC    = "p.text-sm.text-gray-700.leading-relaxed";

    // ===== Key Features =====
    private static final String KEY_FEATURES_SECTION = "ul.space-y-2\\.5";
    private static final String KEY_FEATURE_ITEMS    = "ul.space-y-2\\.5 li span:last-child";

    // ===== Action Buttons =====
    private static final String WISHLIST_BUTTON     = "button:has-text('Wishlist')";
    private static final String SHARE_BUTTON        = "button:has-text('Share')";
    private static final String ADD_TO_CART_BUTTON  = "button:has-text('Add To Cart')";

    // ===== Quantity Stepper =====
    private static final String INCREASE_QTY_BUTTON = "button[aria-label='Increase quantity']";
    private static final String DECREASE_QTY_BUTTON = "button[aria-label='Decrease quantity']";
    private static final String QUANTITY_DISPLAY    = "span.border-x-2.border-gray-300";

    public TW_OneProductDetailsPage(Page page, Locator element) {
        this.element = element;
        this.elementActions = new PW_ElementActions(page);
    }

    // ==============================
    // Product Info Getters
    // ==============================

    public String getProductName() {
        return element.locator(PRODUCT_NAME).textContent().trim();
    }

    /**
     * Returns SKU value only e.g. "81027484" — strips the "SKU: " prefix.
     */
    public String getProductSku() {
        return element.locator(PRODUCT_SKU).textContent()
                .replace("SKU:", "")
                .trim();
    }

    /**
     * Returns price value only e.g. "£8.90" — strips the React comment artifact.
     */
    public String getProductPrice() {
        return element.locator(PRODUCT_PRICE).textContent().trim();
    }

    public String getProductDescription() {
        return element.locator(PRODUCT_DESC).textContent().trim();
    }

    public String getImageUrl() {
        return element.locator(PRODUCT_IMAGE).getAttribute("src");
    }

    public String getImageAlt() {
        return element.locator(PRODUCT_IMAGE).getAttribute("alt");
    }

    // ==============================
    // Key Features
    // ==============================

    /**
     * Returns the text of a specific key feature item by 0-based index.
     * e.g. index 0 → "Category: Minted Bar"
     */
    public String getKeyFeatureByIndex(int index) {
        return element.locator(KEY_FEATURE_ITEMS).nth(index).textContent().trim();
    }

    public int getKeyFeatureCount() {
        return element.locator(KEY_FEATURE_ITEMS).count();
    }

    // ==============================
    // Quantity Stepper
    // ==============================

    public String getQuantityDisplayed() {
        return element.locator(QUANTITY_DISPLAY).textContent().trim();
    }

    public void clickIncreaseQuantity() {
        elementActions.click(element.locator(INCREASE_QTY_BUTTON));
    }

    public void clickDecreaseQuantity() {
        elementActions.click(element.locator(DECREASE_QTY_BUTTON));
    }

    public boolean isDecreaseQuantityDisabled() {
        return !element.locator(DECREASE_QTY_BUTTON).isEnabled();
    }

    public boolean isIncreaseQuantityDisabled() {
        return !element.locator(INCREASE_QTY_BUTTON).isEnabled();
    }

    // ==============================
    // Action Buttons
    // ==============================

    public void clickAddToCart() {
        elementActions.click(element.locator(ADD_TO_CART_BUTTON));
    }

    public void clickWishlist() {
        elementActions.click(element.locator(WISHLIST_BUTTON));
    }

    public void clickShare() {
        elementActions.click(element.locator(SHARE_BUTTON));
    }

    public boolean isAddToCartEnabled() {
        return element.locator(ADD_TO_CART_BUTTON).isEnabled();
    }

    public boolean isWishlistVisible() {
        return element.locator(WISHLIST_BUTTON).isVisible();
    }

    public boolean isShareVisible() {
        return element.locator(SHARE_BUTTON).isVisible();
    }
}