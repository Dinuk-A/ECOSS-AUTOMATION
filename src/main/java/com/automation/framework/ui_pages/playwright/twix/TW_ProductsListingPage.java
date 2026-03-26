package com.automation.framework.ui_pages.playwright.twix;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class TW_ProductsListingPage {
    private final Page page;

    // ===== Product Cards =====
    // Matches the root div of each card: "group overflow-hidden" classes present
    private static final String PRODUCT_CARDS = "xpath=//div[contains(@class,'group overflow-hidden')]";

    // ===== Add Button (relative, inside a card) =====
    private static final String ADD_BUTTON_INSIDE_CARD = "button:has-text('Add')";

    // ===== Pagination =====
    private static final String NEXT_BUTTON = "xpath=//button[@aria-label='Next page']";
    private static final String PREVIOUS_BUTTON = "xpath=//button[@aria-label='Previous page']";
    private static final String PAGE_BUTTONS = "xpath=//button[contains(@aria-label,'Page')]";

    public TW_ProductsListingPage(Page page) {
        this.page = page;
    }

    // =================================
    // Product Cards
    // =================================

    public Locator getAllCards() {
        return page.locator(PRODUCT_CARDS);
    }

    public int getCardCount() {
        return getAllCards().count();
    }

    /**
     * Returns a TW_ProductCard wrapper for the card at the given 0-based index.
     */
    public TW_ProductCard getCard(int index) {
        Locator cardElement = getAllCards().nth(index);
        return new TW_ProductCard(page, cardElement);
    }

    // =================================
    // Card Actions (direct, without TW_ProductCard)
    // =================================

    public void clickAddButton(int cardIndex) {
        getAllCards().nth(cardIndex)
                .locator(ADD_BUTTON_INSIDE_CARD)
                .click();
    }

    public boolean isAddButtonVisible(int cardIndex) {
        return getAllCards().nth(cardIndex)
                .locator(ADD_BUTTON_INSIDE_CARD)
                .isVisible();
    }

    public boolean isAddButtonEnabled(int cardIndex) {
        return getAllCards().nth(cardIndex)
                .locator(ADD_BUTTON_INSIDE_CARD)
                .isEnabled();
    }

    // =================================
    // Pagination
    // =================================

    public void clickNextPage() {
        page.locator(NEXT_BUTTON).click();
    }

    public void clickPreviousPage() {
        page.locator(PREVIOUS_BUTTON).click();
    }

    public boolean isNextPageEnabled() {
        return page.locator(NEXT_BUTTON).isEnabled();
    }

    public boolean isPreviousPageEnabled() {
        return page.locator(PREVIOUS_BUTTON).isEnabled();
    }

    public int getPageCount() {
        return page.locator(PAGE_BUTTONS).count();
    }

    public void clickPageNumber(int pageNumber) {
        page.locator(
                String.format("xpath=//button[@aria-label='Page %d']", pageNumber)).click();
    }
}
