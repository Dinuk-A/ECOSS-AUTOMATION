package com.automation.framework.utils.ui.playwright;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import java.util.List;

public class PW_TabActions {
    private Page page;
    private BrowserContext context;

    // Constructor to initialize the Page and BrowserContext
    public PW_TabActions(Page page, BrowserContext context) {
        this.page = page;
        this.context = context;
    }

    /**
     * Opens a new tab in the browser.
     * 
     * Returns the newly opened Page object for immediate interaction.
     */
    public Page openNewTab() {
        try {
            // Create a new page in the same context (same browser, new tab)
            Page newPage = context.newPage();
            return newPage;
        } catch (PlaywrightException e) {
            System.out.println("Error opening new tab: " + e.getMessage());
            return null;
        }
    }

    /**
     * Opens a new tab and navigates to a specific URL immediately.
     * 
     * Returns the newly opened Page object.
     */
    public Page openNewTabWithUrl(String url) {
        try {
            Page newPage = context.newPage();
            newPage.navigate(url);
            return newPage;
        } catch (PlaywrightException e) {
            System.out.println("Error opening new tab with URL: " + e.getMessage());
            return null;
        }
    }

    /**
     * Switches the driver to a specific tab by index.
     * 
     * tabIndex - The index of the tab to switch to (starting from 0).
     * Returns the Page object at that index.
     */
    public Page switchToTab(int tabIndex) {
        try {
            List<Page> pages = context.pages();
            if (tabIndex >= 0 && tabIndex < pages.size()) {
                Page targetPage = pages.get(tabIndex);
                targetPage.bringToFront();
                return targetPage;
            }
            throw new PlaywrightException("Tab index " + tabIndex + " not found");
        } catch (PlaywrightException e) {
            System.out.println("Error switching to tab: " + e.getMessage());
            return null;
        }
    }

    /**
     * Switches to a specific tab by Page object reference.
     * 
     * Useful when you have a direct reference to a Page and want to make it active.
     */
    public void switchToPage(Page targetPage) {
        try {
            targetPage.bringToFront();
        } catch (PlaywrightException e) {
            System.out.println("Error switching to page: " + e.getMessage());
        }
    }

    /**
     * Closes the current tab that the driver is focused on.
     * 
     * Note: Closing a tab removes it from the context. Other tabs remain open.
     */
    public void closeCurrentTab() {
        try {
            page.close();
        } catch (PlaywrightException e) {
            System.out.println("Error closing current tab: " + e.getMessage());
        }
    }

    /**
     * Closes a specific tab by Page reference.
     * 
     * Useful for closing tabs you opened earlier without switching to them first.
     */
    public void closeTab(Page targetPage) {
        try {
            targetPage.close();
        } catch (PlaywrightException e) {
            System.out.println("Error closing tab: " + e.getMessage());
        }
    }

    /**
     * Closes all tabs except the main one (first tab).
     * 
     * Useful for cleanup after multi-tab tests.
     */
    public void closeAllTabsExceptMain() {
        try {
            List<Page> pages = context.pages();
            for (int i = pages.size() - 1; i > 0; i--) {
                pages.get(i).close();
            }
            // Switch back to main tab
            if (!pages.isEmpty()) {
                pages.get(0).bringToFront();
            }
        } catch (PlaywrightException e) {
            System.out.println("Error closing tabs: " + e.getMessage());
        }
    }

    /**
     * Switches back to the main window/tab (first tab).
     * 
     * This is useful after switching to a new tab and wanting to return to the
     * original one.
     */
    public Page switchToMainTab() {
        try {
            List<Page> pages = context.pages();
            if (!pages.isEmpty()) {
                Page mainPage = pages.get(0);
                mainPage.bringToFront();
                return mainPage;
            }
            return null;
        } catch (PlaywrightException e) {
            System.out.println("Error switching to main tab: " + e.getMessage());
            return null;
        }
    }

    /**
     * Prints the title of the current tab to the console.
     */
    public void printCurrentTabTitle() {
        try {
            System.out.println("Current tab title: " + page.title());
        } catch (PlaywrightException e) {
            System.out.println("Error getting current tab title: " + e.getMessage());
        }
    }

    /**
     * Prints the URL of the current tab to the console.
     */
    public void printCurrentTabUrl() {
        try {
            System.out.println("Current tab URL: " + page.url());
        } catch (PlaywrightException e) {
            System.out.println("Error getting current tab URL: " + e.getMessage());
        }
    }

    /**
     * Get the total number of open tabs/pages in the context.
     */
    public int getTabCount() {
        try {
            return context.pages().size();
        } catch (PlaywrightException e) {
            System.out.println("Error getting tab count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Get the current tab index (position in the context.pages() list).
     */
    public int getCurrentTabIndex() {
        try {
            List<Page> pages = context.pages();
            for (int i = 0; i < pages.size(); i++) {
                if (pages.get(i) == page) {
                    return i;
                }
            }
            return -1;
        } catch (PlaywrightException e) {
            System.out.println("Error getting current tab index: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Get all tab titles from open tabs.
     */
    public List<String> getAllTabTitles() {
        try {
            List<Page> pages = context.pages();
            return pages.stream()
                .map(Page::title)
                .collect(java.util.stream.Collectors.toList());
        } catch (PlaywrightException e) {
            System.out.println("Error getting all tab titles: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    /**
     * Get all tab URLs from open tabs.
     */
    public List<String> getAllTabUrls() {
        try {
            List<Page> pages = context.pages();
            return pages.stream()
                .map(Page::url)
                .collect(java.util.stream.Collectors.toList());
        } catch (PlaywrightException e) {
            System.out.println("Error getting all tab URLs: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    /**
     * Switch to a tab by its title (useful if you know the page title).
     * 
     * Returns the Page if found, null otherwise.
     */
    public Page switchToTabByTitle(String title) {
        try {
            List<Page> pages = context.pages();
            for (Page p : pages) {
                if (p.title().equals(title)) {
                    p.bringToFront();
                    return p;
                }
            }
            throw new PlaywrightException("Tab with title '" + title + "' not found");
        } catch (PlaywrightException e) {
            System.out.println("Error switching to tab by title: " + e.getMessage());
            return null;
        }
    }

    /**
     * Switch to a tab by its URL (useful if you know the page URL).
     * 
     * Returns the Page if found, null otherwise.
     */
    public Page switchToTabByUrl(String url) {
        try {
            List<Page> pages = context.pages();
            for (Page p : pages) {
                if (p.url().equals(url)) {
                    p.bringToFront();
                    return p;
                }
            }
            throw new PlaywrightException("Tab with URL '" + url + "' not found");
        } catch (PlaywrightException e) {
            System.out.println("Error switching to tab by URL: " + e.getMessage());
            return null;
        }
    }

    /**
     * Wait for a new tab/page to open (useful for handling target="_blank" links).
     * 
     * Execute an action that opens a new tab, and this method will return the new Page.
     */
    public Page waitForNewTabAndReturn(Runnable action) {
        try {
            Page newPage = context.waitForPage(() -> {
                action.run();
            });
            return newPage;
        } catch (PlaywrightException e) {
            System.out.println("Error waiting for new tab: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get all Page objects from the context (for advanced multi-tab operations).
     */
    public List<Page> getAllPages() {
        try {
            return context.pages();
        } catch (PlaywrightException e) {
            System.out.println("Error getting all pages: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    /**
     * Print details of all open tabs to console (useful for debugging).
     */
    public void printAllTabDetails() {
        try {
            List<Page> pages = context.pages();
            System.out.println("Total tabs open: " + pages.size());
            for (int i = 0; i < pages.size(); i++) {
                System.out.println("Tab " + i + " - Title: " + pages.get(i).title() + 
                                 " | URL: " + pages.get(i).url());
            }
        } catch (PlaywrightException e) {
            System.out.println("Error printing tab details: " + e.getMessage());
        }
    }
}