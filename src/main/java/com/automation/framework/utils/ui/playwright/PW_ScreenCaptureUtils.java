package com.automation.framework.utils.ui.playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PW_ScreenCaptureUtils {
    
     private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");
    public static String SCREENSHOT_FOLDER = Paths.get(System.getProperty("user.dir"), "screenshots").toString();

    private static String timestamp() {
        return LocalDateTime.now().format(TS_FORMAT);
    }

    /*
     * Take a full-page screenshot (entire page, including scrolled content)
     */
    public static String takePageScreenshot(Page page, String namePrefix) {
        try {
            String filename = String.format("%s_%s.png", namePrefix, timestamp());
            File dest = new File(SCREENSHOT_FOLDER, filename);
            FileUtils.forceMkdirParent(dest);
            
            // fullPage: true captures the entire scrollable page, not just the viewport
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(dest.getAbsolutePath())).setFullPage(true));
            
            return dest.getAbsolutePath();
        } catch (PlaywrightException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Take a viewport screenshot (only what's visible on screen, no scrolling)
     */
    public static String takeViewportScreenshot(Page page, String namePrefix) {
        try {
            String filename = String.format("%s_viewport_%s.png", namePrefix, timestamp());
            File dest = new File(SCREENSHOT_FOLDER, filename);
            FileUtils.forceMkdirParent(dest);
            
            // fullPage: false captures only the visible viewport
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(dest.getAbsolutePath())).setFullPage(false));
            
            return dest.getAbsolutePath();
        } catch (PlaywrightException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Take a screenshot of a specific element (locator-based)
     * Captures only the element and its bounds
     */
    public static String takeElementScreenshot(Locator locator, String namePrefix) {
        try {
            String filename = String.format("%s_element_%s.png", namePrefix, timestamp());
            File dest = new File(SCREENSHOT_FOLDER, filename);
            FileUtils.forceMkdirParent(dest);
            
            locator.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get(dest.getAbsolutePath())));
            
            return dest.getAbsolutePath();
        } catch (PlaywrightException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Take a screenshot and return it as a byte array (useful for reporting/logging)
     */
    public static byte[] takePageScreenshotAsBytes(Page page) {
        try {
            return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        } catch (PlaywrightException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Take a viewport screenshot and return it as a byte array
     */
    public static byte[] takeViewportScreenshotAsBytes(Page page) {
        try {
            return page.screenshot(new Page.ScreenshotOptions().setFullPage(false));
        } catch (PlaywrightException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Take an element screenshot and return it as a byte array
     */
    public static byte[] takeElementScreenshotAsBytes(Locator locator) {
        try {
            return locator.screenshot();
        } catch (PlaywrightException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Take a screenshot with custom options (quality, type, clip region, etc.)
     * Useful for advanced scenarios like capturing specific regions or different formats
     */
    public static String takePageScreenshotWithOptions(Page page, String namePrefix, 
                                                        int quality, String type) {
        try {
            String filename = String.format("%s_%s.%s", namePrefix, timestamp(), type);
            File dest = new File(SCREENSHOT_FOLDER, filename);
            FileUtils.forceMkdirParent(dest);
            
            Page.ScreenshotOptions options = new Page.ScreenshotOptions()
                .setPath(Paths.get(dest.getAbsolutePath()))
                .setFullPage(true)
                .setQuality(quality); // Only works for jpeg (1-100)
            
            if ("png".equalsIgnoreCase(type)) {
                page.screenshot(options);
            } else if ("jpeg".equalsIgnoreCase(type)) {
                page.screenshot(options);
            }
            
            return dest.getAbsolutePath();
        } catch (PlaywrightException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Take a screenshot of a specific clip region on the page
     * Useful for capturing just a portion without taking full-page screenshot
     * Clip values: x, y, width, height
     */
    public static String takeClipRegionScreenshot(Page page, String namePrefix, 
                                                   double x, double y, double width, double height) {
        try {
            String filename = String.format("%s_clip_%s.png", namePrefix, timestamp());
            File dest = new File(SCREENSHOT_FOLDER, filename);
            FileUtils.forceMkdirParent(dest);
            
            Page.ScreenshotOptions options = new Page.ScreenshotOptions()
                .setPath(Paths.get(dest.getAbsolutePath()))
                .setClip(x, y, width, height);
            
            page.screenshot(options);
            
            return dest.getAbsolutePath();
        } catch (PlaywrightException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Take a screenshot with a specific mask (hide certain elements by masking them)
     * Useful for hiding dynamic content (timestamps, ads, etc.) before comparing screenshots
     */
    public static String takeScreenshotWithMask(Page page, String namePrefix, Locator maskLocator) {
        try {
            String filename = String.format("%s_masked_%s.png", namePrefix, timestamp());
            File dest = new File(SCREENSHOT_FOLDER, filename);
            FileUtils.forceMkdirParent(dest);
            
            Page.ScreenshotOptions options = new Page.ScreenshotOptions()
                .setPath(Paths.get(dest.getAbsolutePath()))
                .setFullPage(true)
                .setMask(java.util.List.of(maskLocator));
            
            page.screenshot(options);
            
            return dest.getAbsolutePath();
        } catch (PlaywrightException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Wait for element to be ready, then take a screenshot of it
     * Useful for flaky visual tests where element might not be immediately visible
     */
    //public static String takeElementScreenshotWithWait(Locator locator, String namePrefix, int timeoutMs) {
    //    try {
    //        // Wait for element to be visible before taking screenshot
    //        locator.waitFor(new Locator.WaitForOptions()
    //            .setTimeout(timeoutMs)
    //            .setState(Locator.WaitForState.VISIBLE));
    //        
    //        return takeElementScreenshot(locator, namePrefix);
    //    } catch (PlaywrightException e) {
    //        e.printStackTrace();
    //        return null;
    //    }
    //}

    /*
     * Capture PDF of the entire page (Playwright bonus feature not available in Selenium)
     * Useful for generating reports or capturing print-layout pages
     */
    public static String takePagePdf(Page page, String namePrefix) {
        try {
            String filename = String.format("%s_%s.pdf", namePrefix, timestamp());
            File dest = new File(SCREENSHOT_FOLDER, filename);
            FileUtils.forceMkdirParent(dest);
            
            page.pdf(new Page.PdfOptions().setPath(Paths.get(dest.getAbsolutePath())));
            
            return dest.getAbsolutePath();
        } catch (PlaywrightException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Compare two screenshots for visual regression testing
     * Returns true if images are identical (byte-for-byte)
     */
    public static boolean compareScreenshots(String screenshotPath1, String screenshotPath2) {
        try {
            byte[] file1 = Files.readAllBytes(Paths.get(screenshotPath1));
            byte[] file2 = Files.readAllBytes(Paths.get(screenshotPath2));
            return java.util.Arrays.equals(file1, file2);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * Initialize screenshot folder (ensure it exists)
     */
    public static void initializeScreenshotFolder() {
        try {
            Files.createDirectories(Paths.get(SCREENSHOT_FOLDER));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
