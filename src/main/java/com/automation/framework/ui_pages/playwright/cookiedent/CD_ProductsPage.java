package com.automation.framework.ui_pages.playwright.cookiedent;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CD_ProductsPage {

    private final Page page;

    // ===== Product Cards =====
    private static final String PRODUCT_CARDS = "xpath=//div[contains(@class,'group overflow-hidden')]";

    // private static final String ADD_BUTTON_INSIDE_CARD =
    // ".//button[.//span[text()='Add'] or contains(.,'Add')]";

    private static final String ADD_BUTTON_INSIDE_CARD = "button:has-text('Add')";

    // ===== Pagination =====
    private static final String NEXT_BUTTON = "xpath=//button[@aria-label='Next page']";

    private static final String PREVIOUS_BUTTON = "xpath=//button[@aria-label='Previous page']";

    private static final String PAGE_BUTTONS = "xpath=//button[contains(@aria-label,'Page')]";

    public CD_ProductsPage(Page page) {
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

    public CD_ProductCard getCard(int index) {
        Locator cardElement = getAllCards().nth(index);
        return new CD_ProductCard(page, cardElement);
    }

    // =================================
    // Card Actions
    // =================================

    public void clickAddButton(int cardIndex) {
        Locator card = getAllCards().nth(cardIndex);
        card.locator(ADD_BUTTON_INSIDE_CARD).click();
    }

    public boolean isAddButtonVisible(int cardIndex) {
        Locator card = getAllCards().nth(cardIndex);
        return card.locator(ADD_BUTTON_INSIDE_CARD).isVisible();
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

}