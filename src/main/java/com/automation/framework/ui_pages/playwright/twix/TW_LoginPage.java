package com.automation.framework.ui_pages.playwright.twix;

import com.automation.framework.utils.ui.playwright.PW_ElementActions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class TW_LoginPage {

    private final Locator element;
    private final PW_ElementActions elementActions;

    // ===== Email Field =====
    private static final String EMAIL_INPUT         = "input#username";
    private static final String EMAIL_LABEL         = "label#username-label";
    private static final String EMAIL_REQUIRED_ERROR = "#error-cs-username-required";
    private static final String EMAIL_PATTERN_ERROR  = "#error-cs-pattern-mismatch";

    // ===== Password Field =====
    private static final String PASSWORD_INPUT          = "input#password";
    private static final String PASSWORD_LABEL          = "label#password-label";
    private static final String PASSWORD_REQUIRED_ERROR = "#error-cs-password-required";
    private static final String PASSWORD_WEAK_ERROR     = "#error-cs-password-password-too-weak";
    private static final String PASSWORD_TOGGLE_BUTTON  = "button[aria-label='Show password']";

    // ===== Actions =====
    private static final String CONTINUE_BUTTON    = "button[type='submit'][name='action']";
    private static final String RESET_PASSWORD_LINK = "a:has-text('Reset password')";

    public TW_LoginPage(Page page, Locator element) {
        this.element = element;
        this.elementActions = new PW_ElementActions(page);
    }

    // ==============================
    // Email
    // ==============================

    public void enterEmail(String email) {
        element.locator(EMAIL_INPUT).fill(email);
    }

    public String getEmailValue() {
        return element.locator(EMAIL_INPUT).inputValue();
    }

    public String getEmailLabelText() {
        return element.locator(EMAIL_LABEL).textContent().trim();
    }

    public String getEmailRequiredError() {
        Locator error = element.locator(EMAIL_REQUIRED_ERROR);
        if (error.isVisible()) {
            return error.textContent().trim();
        }
        return null;
    }

    public String getEmailPatternError() {
        Locator error = element.locator(EMAIL_PATTERN_ERROR);
        if (error.isVisible()) {
            return error.textContent().trim();
        }
        return null;
    }

    public boolean isEmailFieldInvalid() {
        String value = element.locator(EMAIL_INPUT).getAttribute("aria-required");
        return value != null && value.equals("true")
                && element.locator(EMAIL_REQUIRED_ERROR).isVisible();
    }

    // ==============================
    // Password
    // ==============================

    public void enterPassword(String password) {
        element.locator(PASSWORD_INPUT).fill(password);
    }

    public String getPasswordValue() {
        return element.locator(PASSWORD_INPUT).inputValue();
    }

    public String getPasswordLabelText() {
        return element.locator(PASSWORD_LABEL).textContent().trim();
    }

    public String getPasswordRequiredError() {
        Locator error = element.locator(PASSWORD_REQUIRED_ERROR);
        if (error.isVisible()) {
            return error.textContent().trim();
        }
        return null;
    }

    public String getPasswordWeakError() {
        Locator error = element.locator(PASSWORD_WEAK_ERROR);
        if (error.isVisible()) {
            return error.textContent().trim();
        }
        return null;
    }

    /**
     * Toggles password visibility.
     * The button's aria-label stays as "Show password" in the DOM regardless of state —
     * current visibility state is tracked via aria-checked="true/false" on the button.
     */
    public void clickPasswordToggle() {
        elementActions.click(element.locator(PASSWORD_TOGGLE_BUTTON));
    }

    public boolean isPasswordVisible() {
        // When toggled ON, aria-checked becomes "true" and input type switches to "text"
        String inputType = element.locator(PASSWORD_INPUT).getAttribute("type");
        return "text".equals(inputType);
    }

    // ==============================
    // Actions
    // ==============================

    public void clickContinue() {
        elementActions.click(element.locator(CONTINUE_BUTTON));
    }

    public void clickResetPassword() {
        elementActions.click(element.locator(RESET_PASSWORD_LINK));
    }

    // ==============================
    // State Checks
    // ==============================

    public boolean isContinueButtonVisible() {
        return element.locator(CONTINUE_BUTTON).isVisible();
    }

    public boolean isContinueButtonEnabled() {
        return element.locator(CONTINUE_BUTTON).isEnabled();
    }

    public boolean isResetPasswordLinkVisible() {
        return element.locator(RESET_PASSWORD_LINK).isVisible();
    }

    // ==============================
    // Combined Login Action
    // ==============================

    /**
     * Fills email and password then clicks Continue in one call.
     * Use this in tests where login is a precondition, not the subject under test.
     */
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickContinue();
    }
}