package com.automation.framework.utils.ui.playwright;

import com.microsoft.playwright.*;
import java.util.List;
public class PW_BrowserUtils {
    
    // Navigate to URL
    public static void openUrl(Page page, String url) {
        page.navigate(url);
    }

    // Go back in history
    public static void goBack(Page page) {
        page.goBack();
    }

    // Go forward in history
    public static void goForward(Page page) {
        page.goForward();
    }

    // Refresh page
    public static void refreshPage(Page page) {
        page.reload();
    }

    // Get current URL
    public static String getCurrentUrl(Page page) {
        return page.url();
    }

    // Get page title
    public static String getPageTitle(Page page) {
        return page.title();
    }

    // Maximize window (Playwright doesn't have native maximize, so we set a large viewport)
    public static void maximizeWindow(Page page) {
        page.setViewportSize(1920, 1080);
    }

    // Fullscreen window
    public static void fullscreenWindow(Page page) {
        // Playwright doesn't support OS-level fullscreen, but you can maximize viewport
        page.setViewportSize(1920, 1440);
    }

    // Switch to window/tab by index
    public static void switchToWindowByIndex(BrowserContext context, int index) {
        List<Page> pages = context.pages();
        if (index >= 0 && index < pages.size()) {
            pages.get(index).bringToFront();
        }
    }

    // Get all pages/windows from context
    public static List<Page> getAllPages(BrowserContext context) {
        return context.pages();
    }

    // Get current page (the one with focus)
    public static Page getCurrentPage(BrowserContext context) {
        List<Page> pages = context.pages();
        return pages.isEmpty() ? null : pages.get(0);
    }

    // Count open tabs/windows
    public static int getWindowCount(BrowserContext context) {
        return context.pages().size();
    }

    // Close specific page
    public static void closePage(Page page) {
        page.close();
    }

    // Wait for new page to open (useful for handling target="_blank" links)
    public static Page waitForNewPage(BrowserContext context, Runnable action) {
        Page newPage = context.waitForPage(() -> {
            action.run();
        });
        return newPage;
    }

}
