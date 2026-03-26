package com.automation.framework.ui_pages.playwright.cookiedent;

import com.automation.framework.utils.ui.playwright.PW_ElementActions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CookiedentLoginPage {

    private final Locator element;
    private final PW_ElementActions elementActions;

    // ===== Username Page =====
    private static final String TITLE = ".//h2[contains(@class,'okta-form-title')]";
    // private static final String USERNAME_INPUT = ".//input[@name='identifier']";
    private static final String USERNAME_INPUT = "xpath=.//input[@name='identifier']";
    private static final String REMEMBER_ME_CHECKBOX = "xpath=.//input[@name='rememberMe']";
    private static final String NEXT_BUTTON = "xpath=.//input[@type='submit' and @value='Next']";
    private static final String ERROR_CONTAINER = "xpath=.//div[@data-se='o-form-error-container']";

    // ===== Password Page =====
    private static final String PASSWORD_INPUT = "xpath=.//input[@name='credentials.passcode']";
    private static final String PASSWORD_ERROR = ".//p[contains(@class,'o-form-input-error')]";
    private static final String PASSWORD_TOGGLE_SHOW = ".//span[contains(@class,'button-show')]";
    private static final String PASSWORD_TOGGLE_HIDE = ".//span[contains(@class,'button-hide')]";

    private static final String VERIFY_BUTTON = "xpath=.//input[@type='submit' and @value='Verify']";

    // ===== Authenticator Selection Page (Password Option Only) =====
    private static final String PASSWORD_AUTHENTICATOR_BUTTON = "xpath=.//div[@data-se='okta_password']//a[@data-se='button']";

    private static final String AUTH_PAGE_IDENTIFIER = "xpath=.//span[@data-se='identifier']";

    private static final String BACK_TO_SIGNIN_BUTTON = "xpath=.//a[@data-se='cancel']";

    public CookiedentLoginPage(Page page, Locator element) {
        this.element = element;
        this.elementActions = new PW_ElementActions(page);
    }

    // ==============================
    // Common
    // ==============================

    public String getTitle() {
        return element.locator(TITLE).textContent().trim();
    }

    public String getErrorMessage() {
        Locator error = element.locator(ERROR_CONTAINER);
        if (error.isVisible()) {
            return error.textContent().trim();
        }
        return null;
    }

    // ==============================
    // Username Actions
    // ==============================

    public void enterUsername(String username) {
        element.locator(USERNAME_INPUT).fill(username);
    }

    public String getUsernameValue() {
        return element.locator(USERNAME_INPUT).inputValue();
    }

    public void checkRememberMe() {
        Locator checkbox = element.locator(REMEMBER_ME_CHECKBOX);
        if (!checkbox.isChecked()) {
            checkbox.check();
        }
    }

    public boolean isRememberMeChecked() {
        return element.locator(REMEMBER_ME_CHECKBOX).isChecked();
    }

    // public void clickNext() {
    // element.locator(NEXT_BUTTON).click();
    // }

    public void clickNext() {
        elementActions.click(element.locator(NEXT_BUTTON));
    }

    // ==============================
    // Authenticator Selection (Password Option)
    // ==============================

    public void selectPasswordAuthenticator() {
        // element.locator(PASSWORD_AUTHENTICATOR_BUTTON).click();
        elementActions.click(element.locator(PASSWORD_AUTHENTICATOR_BUTTON));
    }

    public boolean isPasswordAuthenticatorVisible() {
        return element.locator(PASSWORD_AUTHENTICATOR_BUTTON).isVisible();
    }

    public String getAuthenticatedIdentifier() {
        return element.locator(AUTH_PAGE_IDENTIFIER).textContent().trim();
    }

    // public void clickBackToSignIn() {
    // element.locator(BACK_TO_SIGNIN_BUTTON).click();
    // }

    public void clickBackToSignIn() {
        elementActions.click(element.locator(BACK_TO_SIGNIN_BUTTON));
    }

    // ==============================
    // Password Actions
    // ==============================

    public void enterPassword(String password) {
        element.locator(PASSWORD_INPUT).fill(password);
    }

    public String getPasswordValue() {
        return element.locator(PASSWORD_INPUT).inputValue();
    }

    // public void clickPasswordToggle() {
    // Locator showBtn = element.locator(PASSWORD_TOGGLE_SHOW);
    // Locator hideBtn = element.locator(PASSWORD_TOGGLE_HIDE);

    // if (showBtn.isVisible()) {
    // showBtn.click();
    // } else if (hideBtn.isVisible()) {
    // hideBtn.click();
    // }
    // }

    public void clickPasswordToggle() {

        Locator showBtn = element.locator(PASSWORD_TOGGLE_SHOW);
        Locator hideBtn = element.locator(PASSWORD_TOGGLE_HIDE);

        if (showBtn.isVisible()) {
            elementActions.click(showBtn);
        } else if (hideBtn.isVisible()) {
            elementActions.click(hideBtn);
        }
    }

    public boolean isPasswordFieldInvalid() {
        String value = element.locator(PASSWORD_INPUT).getAttribute("aria-invalid");
        return value != null && value.equals("true");
    }

    public String getPasswordErrorMessage() {
        Locator error = element.locator(PASSWORD_ERROR);
        if (error.isVisible()) {
            return error.textContent().trim();
        }
        return null;
    }

    // public void clickVerify() {
    // element.locator(VERIFY_BUTTON).click();
    // }

    public void clickVerify() {
        elementActions.click(element.locator(VERIFY_BUTTON));
    }

    public boolean isVerifyButtonVisible() {
        return element.locator(VERIFY_BUTTON).isVisible();
    }

    public boolean isVerifyButtonEnabled() {
        return element.locator(VERIFY_BUTTON).isEnabled();
    }
}