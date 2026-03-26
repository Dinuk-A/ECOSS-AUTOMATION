package com.automation.framework.utils.ui;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.ArrayList;

//this class contains all the util methods needed for Product page related tests
public class ProductsPageUtils {

    public static void scrapeSKUsFromCurrentPage(Page page, ArrayList<Integer> uiSKUsViaPlaywright) {
        // get all SKU elements on current page
        Locator skuElements = page.locator("p:has-text('SKU:')");

        // get count
        int skuCount = skuElements.count();
        System.out.println("Found " + skuCount + " SKUs on current page");

        // loop through all and extract IDs to array
        for (int i = 0; i < skuCount; i++) {
            // get the entire part (with text SKU)
            String wholeTextPart = skuElements.nth(i).textContent();

            // REMOVE THE SKU PART
            String skuID = wholeTextPart.replace("SKU:", "").trim();

            // store in array while passing to an INT
            uiSKUsViaPlaywright.add(Integer.parseInt(skuID));
        }
    }

}
