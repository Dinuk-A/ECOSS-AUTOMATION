package com.automation.framework.utils.ui.core;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.List;

public class PlaywrightBaseTest {

    // ThreadLocal to store Playwright per thread
    private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> contextThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeClass
    @Parameters({ "browser", "headless" })
    public void setupPlaywright(
            @Optional("chromium") String browser,
            @Optional("false") String headless) {
        playwright = Playwright.create();
        playwrightThreadLocal.set(playwright);

        boolean isHeadless = Boolean.parseBoolean(headless);

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(isHeadless)
                .setArgs(List.of("--start-maximized"));

        // Launch browser based on parameter
        switch (browser.toLowerCase()) {
            case "firefox":
                this.browser = playwright.firefox().launch(launchOptions);
                break;
            case "webkit":
                this.browser = playwright.webkit().launch(launchOptions);
                break;
            default:
                this.browser = playwright.chromium().launch(launchOptions);
        }

        browserThreadLocal.set(this.browser);
    }

    // Create context and page before each test method
    @BeforeMethod
    public void createContextAndPage() {
        // Get browser from ThreadLocal for current thread
        Browser currentBrowser = browserThreadLocal.get();

        // If browser not initialized for this thread, initialize it now
        if (currentBrowser == null) {
            initializeBrowserForThread();
            currentBrowser = browserThreadLocal.get();
        }

        // Create context and page for this thread
        BrowserContext context = currentBrowser.newContext(
                new Browser.NewContextOptions().setViewportSize(null));
        Page page = context.newPage();
        page.setDefaultTimeout(PlaywrightConfig.getDefaultTimeout());

        // Store in ThreadLocal
        contextThreadLocal.set(context);
        pageThreadLocal.set(page);

        // Also set as instance variables for backward compatibility
        this.context = context;
        this.page = page;
    }

    // Close context and page after each test method
    @AfterMethod
    public void closeContext() {
        BrowserContext currentContext = contextThreadLocal.get();
        if (currentContext != null) {
            currentContext.close();
            contextThreadLocal.remove();
        }

        Page currentPage = pageThreadLocal.get();
        if (currentPage != null) {
            pageThreadLocal.remove();
        }
    }

    // Teardown Playwright and browser after all tests in the class
    @AfterClass
    public void teardownPlaywright() {
        Browser currentBrowser = browserThreadLocal.get();
        if (currentBrowser != null) {
            currentBrowser.close();
            browserThreadLocal.remove();
        }

        Playwright currentPlaywright = playwrightThreadLocal.get();
        if (currentPlaywright != null) {
            currentPlaywright.close();
            playwrightThreadLocal.remove();
        }
    }

    // Initialize browser for current thread (called lazily if needed)
    private synchronized void initializeBrowserForThread() {
        // Double-check pattern to avoid race conditions
        if (browserThreadLocal.get() != null) {
            return;
        }

        Playwright playwright = Playwright.create();
        playwrightThreadLocal.set(playwright);

        String browserType = PlaywrightConfig.getBrowser();
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(PlaywrightConfig.isHeadless())
                .setArgs(List.of("--start-maximized"));

        Browser browser;
        switch (browserType.toLowerCase()) {
            case "firefox":
                browser = playwright.firefox().launch(launchOptions);
                break;
            case "webkit":
                browser = playwright.webkit().launch(launchOptions);
                break;
            default:
                browser = playwright.chromium().launch(launchOptions);
        }

        browserThreadLocal.set(browser);
        this.browser = browser;
    }

}
