package com.automation.framework.ui_pages.playwright.cookiedent;

import com.automation.framework.utils.ui.playwright.PW_ElementActions;
import com.microsoft.playwright.Page;

public class CD_CheckoutPage {

    private final Page page;
    private final PW_ElementActions elementActions;

    // ── Contact ───────────────────────────────────────────────────────────────
    private static final String EMAIL = "xpath=//input[@id='email']";

    // ── Delivery Address ──────────────────────────────────────────────────────
    private static final String COMPANY = "xpath=//input[@id='company']";
    private static final String FIRST_NAME = "xpath=//input[@id='firstName']";
    private static final String LAST_NAME = "xpath=//input[@id='lastName']";
    private static final String ADDRESS = "xpath=//input[@id='address']";
    private static final String POSTAL_CODE = "xpath=//input[@id='postalCode']";
    private static final String CITY = "xpath=//input[@id='city']";

    // ── Country dropdown ──────────────────────────────────────────────────────
    private static final String COUNTRY_TRIGGER = "xpath=//button[@id='country']";
    private static final String COUNTRY_OPTION = "xpath=//option[@value='%s']/../.."; // fallback
    private static final String COUNTRY_COMBOBOX = "xpath=//button[@role='combobox'][@id='country']";

    // ── Billing checkbox ──────────────────────────────────────────────────────
    private static final String USE_SAME_AS_BILLING_CHECKBOX = "xpath=//button[@id='useSameAsBilling']";

    // ── Submit ────────────────────────────────────────────────────────────────
    private static final String PLACE_ORDER_BUTTON = "xpath=//button[@type='submit']";

    // ── Breadcrumb ────────────────────────────────────────────────────────────
    private static final String BREADCRUMB_CART_LINK = "xpath=//a[@href='/checkout/en/cart']";

    public CD_CheckoutPage(Page page) {
        this.page = page;
        this.elementActions = new PW_ElementActions(page);
    }

    // ── Contact ───────────────────────────────────────────────────────────────

    /**
     * Enter email address
     */
    public void enterEmail(String email) {
        page.locator(EMAIL).fill(email);
    }

    /**
     * Get current value of email field
     */
    public String getEmail() {
        return page.locator(EMAIL).inputValue();
    }

    // ── Delivery Address ──────────────────────────────────────────────────────

    /**
     * Enter company name (optional field)
     */
    public void enterCompany(String company) {
        page.locator(COMPANY).fill(company);
    }

    /**
     * Get current value of company field
     */
    public String getCompany() {
        return page.locator(COMPANY).inputValue();
    }

    /**
     * Enter first name
     */
    public void enterFirstName(String firstName) {
        page.locator(FIRST_NAME).fill(firstName);
    }

    /**
     * Get current value of first name field
     */
    public String getFirstName() {
        return page.locator(FIRST_NAME).inputValue();
    }

    /**
     * Enter last name
     */
    public void enterLastName(String lastName) {
        page.locator(LAST_NAME).fill(lastName);
    }

    /**
     * Get current value of last name field
     */
    public String getLastName() {
        return page.locator(LAST_NAME).inputValue();
    }

    /**
     * Enter street address
     */
    public void enterAddress(String address) {
        page.locator(ADDRESS).fill(address);
    }

    /**
     * Get current value of address field
     */
    public String getAddress() {
        return page.locator(ADDRESS).inputValue();
    }

    /**
     * Enter postal code
     */
    public void enterPostalCode(String postalCode) {
        page.locator(POSTAL_CODE).fill(postalCode);
    }

    /**
     * Get current value of postal code field
     */
    public String getPostalCode() {
        return page.locator(POSTAL_CODE).inputValue();
    }

    /**
     * Enter city
     */
    public void enterCity(String city) {
        page.locator(CITY).fill(city);
    }

    /**
     * Get current value of city field
     */
    public String getCity() {
        return page.locator(CITY).inputValue();
    }

    // ── Country ───────────────────────────────────────────────────────────────

    /**
     * Select country from the combobox dropdown.
     * Pass the visible label e.g. "Germany", "Austria", "Switzerland"
     */
    public void selectCountry(String countryLabel) {
        page.locator(COUNTRY_TRIGGER).click();
        page.getByRole(com.microsoft.playwright.options.AriaRole.OPTION,
                new Page.GetByRoleOptions().setName(countryLabel)).click();
    }

    /**
     * Get the currently selected country label
     */
    public String getSelectedCountry() {
        return page.locator(COUNTRY_COMBOBOX).locator("xpath=.//span").textContent().trim();
    }

    // ── Billing checkbox ──────────────────────────────────────────────────────

    /**
     * Check whether "Use shipping address as billing address" is checked
     */
    public boolean isUseSameAsBillingChecked() {
        String state = page.locator(USE_SAME_AS_BILLING_CHECKBOX).getAttribute("data-state");
        return "checked".equals(state);
    }

    /**
     * Click the "Use shipping address as billing address" checkbox to toggle it
     */
    public void toggleUseSameAsBilling() {
        page.locator(USE_SAME_AS_BILLING_CHECKBOX).click();
    }

    // ── Submit ────────────────────────────────────────────────────────────────

    /**
     * Click the Place Order button
     */
    public void clickPlaceOrder() {
        elementActions.click(page.locator(PLACE_ORDER_BUTTON));
    }

    /**
     * Check whether the Place Order button is enabled
     */
    public boolean isPlaceOrderEnabled() {
        return page.locator(PLACE_ORDER_BUTTON).isEnabled();
    }

    // ── Breadcrumb ────────────────────────────────────────────────────────────

    /**
     * Click the Cart breadcrumb link to navigate back to cart
     */
    public void clickBreadcrumbCart() {
        elementActions.click(page.locator(BREADCRUMB_CART_LINK));
    }

    // ── Convenience: fill entire form in one call ─────────────────────────────

    /**
     * Fill all mandatory delivery fields in one call.
     * Pass null or empty string to skip optional fields (company).
     */
    public void fillDeliveryAddress(String email, String company, String firstName,
            String lastName, String address, String postalCode,
            String city, String country) {
        enterEmail(email);
        if (company != null && !company.isEmpty()) {
            enterCompany(company);
        }
        enterFirstName(firstName);
        enterLastName(lastName);
        enterAddress(address);
        enterPostalCode(postalCode);
        enterCity(city);
        selectCountry(country);
    }

}