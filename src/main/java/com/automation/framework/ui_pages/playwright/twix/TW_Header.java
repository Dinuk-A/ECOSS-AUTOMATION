package com.automation.framework.ui_pages.playwright.twix;

import com.automation.framework.utils.ui.playwright.PW_ElementActions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class TW_Header {

    private final Locator element;
    private final PW_ElementActions elementActions;

    // ===== Logo =====
    private static final String LOGO_LINK = "a[href='/']";
    private static final String LOGO_IMAGE = "a[href='/'] img";

    // ===== Cart =====
    private static final String CART_LINK = "a[href='/checkout'][aria-label='Shopping cart']";

    // ===== User Menu =====
    private static final String USER_MENU_BUTTON = "button[aria-haspopup='menu']";
    private static final String USER_WELCOME_TEXT = "button[aria-haspopup='menu'] span.text-header-foreground\\/80";
    private static final String USER_NAME_TEXT = "button[aria-haspopup='menu'] span.text-primary";

    // ===== Mobile Menu =====
    private static final String MOBILE_MENU_BUTTON = "button[aria-label='Open menu']";

    // ===== Pre-Login Buttons (visible before authentication) =====
    private static final String REGISTER_LINK = "a[href='/account/registration']";
    private static final String SIGN_IN_BUTTON = "button:has(span:text('Sign-in'))";

    public TW_Header(Page page, Locator element) {
        this.element = element;
        this.elementActions = new PW_ElementActions(page);
    }

    // ==============================
    // Logo
    // ==============================

    public void clickLogo() {
        elementActions.click(element.locator(LOGO_LINK));
    }

    public String getLogoImageSrc() {
        return element.locator(LOGO_IMAGE).getAttribute("src");
    }

    public String getLogoImageAlt() {
        return element.locator(LOGO_IMAGE).getAttribute("alt");
    }

    // ==============================
    // Cart
    // ==============================

    public void clickCart() {
        elementActions.click(element.locator(CART_LINK));
    }

    public boolean isCartVisible() {
        return element.locator(CART_LINK).isVisible();
    }

    // ==============================
    // User Menu
    // ==============================

    /**
     * Returns the "Welcome" label text — expected: "Welcome"
     */
    public String getWelcomeText() {
        return element.locator(USER_WELCOME_TEXT).textContent().trim();
    }

    /**
     * Returns the logged-in username — e.g. "FetchseedTwix"
     */
    public String getLoggedInUsername() {
        return element.locator(USER_NAME_TEXT).textContent().trim();
    }

    public void clickUserMenu() {
        elementActions.click(element.locator(USER_MENU_BUTTON));
    }

    public boolean isUserMenuExpanded() {
        String state = element.locator(USER_MENU_BUTTON).getAttribute("data-state");
        return "open".equals(state);
    }

    public boolean isUserMenuVisible() {
        return element.locator(USER_MENU_BUTTON).isVisible();
    }

    // ==============================
    // Mobile Menu
    // ==============================

    public void clickMobileMenu() {
        elementActions.click(element.locator(MOBILE_MENU_BUTTON));
    }

    public boolean isMobileMenuButtonVisible() {
        return element.locator(MOBILE_MENU_BUTTON).isVisible();
    }

    // ==============================
    // Pre-Login Actions
    // ==============================

    public void clickRegister() {
        elementActions.click(element.locator(REGISTER_LINK));
    }

    public void clickSignIn() {
        elementActions.click(element.locator(SIGN_IN_BUTTON));
    }

    public boolean isRegisterLinkVisible() {
        return element.locator(REGISTER_LINK).isVisible();
    }

    public boolean isSignInButtonVisible() {
        return element.locator(SIGN_IN_BUTTON).isVisible();
    }
}