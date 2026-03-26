package com.automation.framework.utils.ui.core;

//import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class PlaywrightConfig {

    private static Properties properties;

    // Load properties from playwright-config.properties file
    static {
        try {
            properties = new Properties();
            InputStream input = PlaywrightConfig.class.getClassLoader()
                    .getResourceAsStream("playwright-config.properties");
            if (input != null) {
                properties.load(input);
            } else {
                throw new RuntimeException("playwright-config.properties not found in classpath");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load playwright config file", e);
        }
    }

    // a reusable method to Get property by key
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    // Get browser type
    public static String getBrowser() {
        return properties.getProperty("playwright.browser", "chromium");
    }

    // Get headless mode setting
    public static boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("playwright.headless", "true"));
    }

    // Get default timeout in milliseconds
    public static int getDefaultTimeout() {
        return Integer.parseInt(properties.getProperty("playwright.defaultTimeout", "30000"));
    }

}
