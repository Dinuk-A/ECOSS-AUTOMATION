package com.automation.framework.ui_pages.playwright.cookiedent;

import com.automation.framework.utils.ui.playwright.PW_ElementActions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CD_LandingPage {

    private final Locator element;
    private final PW_ElementActions elementActions;

    // ==============================
    // Header Section
    // ==============================

    private static final String LOGO_LINK = "header a[href='/']";
    private static final String PRODUCTS_LINK = "header a[href='/products']";
    private static final String SERVICES_LINK = "header a[href='/services']";
    private static final String INDUSTRIES_LINK = "header a[href='/checkout/checkout']";
    private static final String COMPANY_LINK = "header a[href='/company']";

    private static final String SEARCH_BUTTON = "header button[aria-label='Search']";
    private static final String CART_BUTTON = "header a[aria-label='Shopping cart']";
    private static final String USER_MENU_BUTTON = "header button[aria-haspopup='menu']";
    private static final String MOBILE_MENU_BUTTON = "header button[aria-label='Open menu']";

    // Login & Register
    private static final String LOGIN_BUTTON = "button:has-text('Sing-in')";
    private static final String REGISTER_BUTTON = "header .lg\\:flex a[href='/account/registration']";

    private static final String WELCOME_BUTTON = "button:has-text('Welcome')";

    private static final String METALBALANCE_BUTTON = "a:has-text('Metal Balance')";
    // ==============================
    // Hero Section
    // ==============================

    private static final String HERO_TITLE = "h1";
    private static final String HERO_DESCRIPTION = "p.text-lg";
    private static final String HERO_CTA_BUTTON = "a[href='/products']:has-text('Our Dental Products')";
    private static final String HERO_IMAGE = "img[src*='cookiedent_hero']";

    public CD_LandingPage(Page page, Locator element) {
        this.element = element;
        this.elementActions = new PW_ElementActions(page);
    }

    // ==============================
    // Header Actions
    // ==============================
    public void clickWelcome() {
        element.locator(WELCOME_BUTTON).click();
    }

    public void clickMetalBalance() {
        element.locator(METALBALANCE_BUTTON).click();
    }

    public void clickLogo() {
        // element.locator(LOGO_LINK).click();
        elementActions.click(element.locator(LOGO_LINK));
    }

    public void clickProducts() {
        // element.locator(PRODUCTS_LINK).click();
        elementActions.click(element.locator(PRODUCTS_LINK));
    }

    public void clickServices() {
        // element.locator(SERVICES_LINK).click();
        elementActions.click(element.locator(SERVICES_LINK));
    }

    public void clickIndustries() {
        // element.locator(INDUSTRIES_LINK).click();
        elementActions.click(element.locator(INDUSTRIES_LINK));
    }

    public void clickCompany() {
        // element.locator(COMPANY_LINK).click();
        elementActions.click(element.locator(COMPANY_LINK));
    }

    public void clickSearch() {
        // element.locator(SEARCH_BUTTON).click();
        elementActions.click(element.locator(SEARCH_BUTTON));
    }

    public void clickCart() {
        // element.locator(CART_BUTTON).click();
        elementActions.click(element.locator(CART_BUTTON));
    }

    public void openUserMenu() {
        // element.locator(USER_MENU_BUTTON).click();
        elementActions.click(element.locator(USER_MENU_BUTTON));
    }

    public void openMobileMenu() {
        // element.locator(MOBILE_MENU_BUTTON).click();
        elementActions.click(element.locator(MOBILE_MENU_BUTTON));
    }

    public void clickLogin() {
        // element.locator(LOGIN_BUTTON).click();
        elementActions.click(element.locator(LOGIN_BUTTON));
    }

    public void clickRegister() {
        // element.locator(REGISTER_BUTTON).click();
        elementActions.click(element.locator(REGISTER_BUTTON));
    }

    // ==============================
    // Hero Section Methods
    // ==============================

    public String getHeroTitle() {
        return element.locator(HERO_TITLE).textContent().trim();
    }

    public String getHeroDescription() {
        return element.locator(HERO_DESCRIPTION).textContent().trim();
    }

    public void clickHeroCTA() {
        // element.locator(HERO_CTA_BUTTON).click();
        elementActions.click(element.locator(HERO_CTA_BUTTON));
    }

    public boolean isHeroImageVisible() {
        return element.locator(HERO_IMAGE).isVisible();
    }
}