package com.automation.framework.ui_pages.playwright.cookiedent;

import com.automation.framework.utils.ui.playwright.PW_ElementActions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CD_OrderConfirmationPage {

    private final Page page;
    private final PW_ElementActions elementActions;

    private static final String CONFIRMATION_HEADING = "xpath=//h1[contains(text(),'Order Confirmation')]";
    private static final String THANK_YOU_MESSAGE = "xpath=//p[contains(text(),'Thank you for your order')]";
    private static final String RETURN_TO_HOME_LINK = "xpath=//a[contains(text(),'Return to Home')]";

    public CD_OrderConfirmationPage(Page page, Locator element) {
        this.elementActions = new PW_ElementActions(page);
        this.page = page;
    }

    /**
     * Get the confirmation page heading text
     */
    public String getHeading() {
        return page.locator(CONFIRMATION_HEADING).textContent().trim();
    }

    /**
     * Get the thank you message text
     */
    public String getThankYouMessage() {
        return page.locator(THANK_YOU_MESSAGE).textContent().trim();
    }

    /**
     * Check if the order confirmation heading is visible
     */
    public boolean isConfirmationDisplayed() {
        return page.locator(CONFIRMATION_HEADING).isVisible();
    }

    /**
     * Click the Return to Home button
     */
    public void clickReturnToHome() {
        // page.locator(RETURN_TO_HOME_LINK).click();
        elementActions.click(page.locator(RETURN_TO_HOME_LINK));
    }

}