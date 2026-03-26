package com.automation.framework.utils.ui;

import java.util.Objects;

import org.testng.asserts.SoftAssert;
import io.qameta.allure.Allure;

import com.automation.framework.models.ProductDetailsModel;
import com.automation.framework.ui_pages.playwright.cookiedent.CD_ProductDetailsPage;

public class ProductDetailsPageUtils {

        // ==================== MAPPER METHODS ====================

        /**
         * Maps UI values from page object to ProductDetailsModel
         */
        public static ProductDetailsModel mapFromUI(CD_ProductDetailsPage page) {

                ProductDetailsModel actual = new ProductDetailsModel();

                actual.setSku(parseIntSafely(page.getSKU()));
                actual.setName(page.getProductName());
                actual.setDescription(page.getDescription());
                actual.setCategory(page.getCategory());
                actual.setColor(page.getColor());
                actual.setIndication(page.getIndication());
                actual.setAlloyType(parseIntSafely(page.getAlloyType()));
                actual.setPointTwoPercentYieldStrengthInMPaCured(
                                parseIntSafely(page.getPointTwoPercentYieldStrengthInMPaCured()));
                actual.setVickersHardness(parseIntSafely(page.getVickersHardness()));
                actual.setElongationAtBreakInPercent(
                                parseIntSafely(page.getElongationAtBreakInPercent()));
                actual.setMeltingIntervalCelsius(page.getMeltingIntervalCelsius());
                actual.setElementMnInPercent(page.getElementMnInPercent());
                actual.setElementFeInPercent(page.getElementFeInPercent());
                actual.setElementCInPercent(page.getElementCInPercent());
                actual.setElementCoInPercent(page.getElementCoInPercent());
                actual.setElementCrInPercent(page.getElementCrInPercent());
                actual.setElementMoInPercent(page.getElementMoInPercent());
                actual.setElementSiInPercent(page.getElementSiInPercent());
                actual.setElementWInPercent(page.getElementWInPercent());
                actual.setPriceEURInCent(parseIntSafely(page.getPriceEURInCent()));

                return actual;
        }

        /**
         * Safely parse string to int, return 0 if parsing fails
         */
        private static int parseIntSafely(String value) {
                try {
                        return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                        System.err.println("Failed to parse: " + value);
                        return 0;
                }
        }

        // ==================== VALIDATION METHODS ====================

        /**
         * Validates expected product details against actual product details from UI
         */
        public static void validate(ProductDetailsModel expected, ProductDetailsModel actual) {

                SoftAssert softAssert = new SoftAssert();

                Allure.addAttachment(
                                "Comparison Summary",
                                "text/html",
                                compare(expected, actual));

                softAssert.assertEquals(actual.getSku(), expected.getSku(), "SKU mismatch");
                softAssert.assertEquals(actual.getName(), expected.getName(), "Name mismatch");
                softAssert.assertEquals(actual.getDescription(), expected.getDescription(), "Description mismatch");
                softAssert.assertEquals(actual.getCategory(), expected.getCategory(), "Category mismatch");
                softAssert.assertEquals(actual.getColor(), expected.getColor(), "Color mismatch");
                softAssert.assertEquals(actual.getIndication(), expected.getIndication(), "Indication mismatch");
                softAssert.assertEquals(actual.getAlloyType(), expected.getAlloyType(), "AlloyType mismatch");
                softAssert.assertEquals(
                                actual.getPointTwoPercentYieldStrengthInMPaCured(),
                                expected.getPointTwoPercentYieldStrengthInMPaCured(),
                                "Yield strength mismatch");
                softAssert.assertEquals(actual.getVickersHardness(),
                                expected.getVickersHardness(), "VickersHardness mismatch");
                softAssert.assertEquals(actual.getElongationAtBreakInPercent(),
                                expected.getElongationAtBreakInPercent(), "Elongation mismatch");
                softAssert.assertEquals(actual.getMeltingIntervalCelsius(),
                                expected.getMeltingIntervalCelsius(), "Melting interval mismatch");
                softAssert.assertEquals(actual.getElementMnInPercent(),
                                expected.getElementMnInPercent(), "Mn mismatch");
                softAssert.assertEquals(actual.getElementFeInPercent(),
                                expected.getElementFeInPercent(), "Fe mismatch");
                softAssert.assertEquals(actual.getElementCInPercent(),
                                expected.getElementCInPercent(), "C mismatch");
                softAssert.assertEquals(actual.getElementCoInPercent(),
                                expected.getElementCoInPercent(), "Co mismatch");
                softAssert.assertEquals(actual.getElementCrInPercent(),
                                expected.getElementCrInPercent(), "Cr mismatch");
                softAssert.assertEquals(actual.getElementMoInPercent(),
                                expected.getElementMoInPercent(), "Mo mismatch");
                softAssert.assertEquals(actual.getElementSiInPercent(),
                                expected.getElementSiInPercent(), "Si mismatch");
                softAssert.assertEquals(actual.getElementWInPercent(),
                                expected.getElementWInPercent(), "W mismatch");
                softAssert.assertEquals(actual.getPriceEURInCent(),
                                expected.getPriceEURInCent(), "Price mismatch");

                softAssert.assertAll();
                Allure.step("All product details validated successfully");
        }

        // ==================== ALLURE-COMPATIBLE COMPARISON ====================

        /**
         * Generates comparison HTML that works well with Allure attachments
         */
        public static String compare(ProductDetailsModel expected, ProductDetailsModel actual) {
                StringBuilder html = new StringBuilder();

                // Simplified HTML without <!DOCTYPE> and full html/head structure
                html.append("<div style='font-family: Arial, sans-serif; padding: 20px; background-color: #ffffff;'>");

                html.append("<h3 style='color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px;'>Product Details Comparison</h3>");

                // Calculate statistics
                int totalFields = 19;
                int matchingFields = countMatches(expected, actual);

                // Summary box
                html.append("<div style='margin: 15px 0; padding: 15px; background-color: #e8f5e9; border-left: 4px solid #4CAF50; border-radius: 4px;'>");
                html.append("<span style='margin-right: 20px;'><strong>Total Fields:</strong> ").append(totalFields)
                                .append("</span>");
                html.append("<span style='margin-right: 20px; color: #4CAF50;'><strong>Matching:</strong> ")
                                .append(matchingFields).append("</span>");
                html.append("<span style='margin-right: 20px; color: #f44336;'><strong>Mismatches:</strong> ")
                                .append(totalFields - matchingFields).append("</span>");
                html.append("<span><strong>Match Rate:</strong> ")
                                .append(String.format("%.1f%%", (matchingFields * 100.0 / totalFields)))
                                .append("</span>");
                html.append("</div>");

                // Table
                html.append("<table style='width: 100%; border-collapse: collapse; margin-top: 15px; background-color: #ffffff;'>");

                // Header
                html.append("<thead>");
                html.append("<tr style='background-color: #4CAF50; color: #ffffff;'>");
                html.append("<th style='border: 1px solid #ddd; padding: 12px; text-align: left; width: 25%;'>Field</th>");
                html.append("<th style='border: 1px solid #ddd; padding: 12px; text-align: left; width: 32%;'>Expected</th>");
                html.append("<th style='border: 1px solid #ddd; padding: 12px; text-align: left; width: 32%;'>Actual</th>");
                html.append("<th style='border: 1px solid #ddd; padding: 12px; text-align: center; width: 11%;'>Match</th>");
                html.append("</tr>");
                html.append("</thead>");

                html.append("<tbody>");

                // Section: Basic Information
                addSectionHeader(html, "Basic Information");
                addRow(html, "SKU", expected.getSku(), actual.getSku());
                addRow(html, "Name", expected.getName(), actual.getName());
                addRow(html, "Category", expected.getCategory(), actual.getCategory());
                addRow(html, "Color", expected.getColor(), actual.getColor());

                // Section: Product Details
                addSectionHeader(html, "Product Details");
                addRow(html, "Description", expected.getDescription(), actual.getDescription());
                addRow(html, "Indication", expected.getIndication(), actual.getIndication());
                addRow(html, "Alloy Type", expected.getAlloyType(), actual.getAlloyType());

                // Section: Physical Properties
                addSectionHeader(html, "Physical Properties");
                addRow(html, "Vickers Hardness", expected.getVickersHardness(), actual.getVickersHardness());
                addRow(html, "Elongation At Break (%)", expected.getElongationAtBreakInPercent(),
                                actual.getElongationAtBreakInPercent());
                addRow(html, "Melting Interval (°C)", expected.getMeltingIntervalCelsius(),
                                actual.getMeltingIntervalCelsius());

                // Section: Chemical Composition
                addSectionHeader(html, "Chemical Composition");
                addRow(html, "Mn (%)", expected.getElementMnInPercent(), actual.getElementMnInPercent());
                addRow(html, "Fe (%)", expected.getElementFeInPercent(), actual.getElementFeInPercent());
                addRow(html, "C (%)", expected.getElementCInPercent(), actual.getElementCInPercent());
                addRow(html, "Co (%)", expected.getElementCoInPercent(), actual.getElementCoInPercent());
                addRow(html, "Cr (%)", expected.getElementCrInPercent(), actual.getElementCrInPercent());
                addRow(html, "Mo (%)", expected.getElementMoInPercent(), actual.getElementMoInPercent());
                addRow(html, "Si (%)", expected.getElementSiInPercent(), actual.getElementSiInPercent());
                addRow(html, "W (%)", expected.getElementWInPercent(), actual.getElementWInPercent());

                // Section: Pricing
                addSectionHeader(html, "Pricing");
                addRow(html, "Price (EUR in Cent)", expected.getPriceEURInCent(), actual.getPriceEURInCent());

                html.append("</tbody>");
                html.append("</table>");
                html.append("</div>");

                return html.toString();
        }

        /**
         * Adds a section header row
         */
        private static void addSectionHeader(StringBuilder html, String sectionName) {
                html.append("<tr style='background-color: #e8f5e9;'>");
                html.append("<td colspan='4' style='border: 1px solid #ddd; padding: 10px; font-weight: bold; color: #2e7d32;'>");
                html.append(sectionName);
                html.append("</td>");
                html.append("</tr>");
        }

        /**
         * Adds a comparison row
         */
        private static void addRow(StringBuilder html, String fieldName, Object expectedValue, Object actualValue) {
                boolean matches = Objects.equals(expectedValue, actualValue);
                String bgColor = matches ? "#ffffff" : "#ffebee";
                String icon = matches ? "✓" : "✗";
                String iconColor = matches ? "#4CAF50" : "#f44336";

                html.append("<tr style='background-color: ").append(bgColor).append(";'>");

                // Field name
                html.append("<td style='border: 1px solid #ddd; padding: 10px; font-weight: 500; color: #333;'>");
                html.append(fieldName);
                html.append("</td>");

                // Expected value
                html.append("<td style='border: 1px solid #ddd; padding: 10px; color: #333;'>");
                html.append(formatValue(expectedValue));
                html.append("</td>");

                // Actual value
                html.append("<td style='border: 1px solid #ddd; padding: 10px; color: #333;'>");
                html.append(formatValue(actualValue));
                html.append("</td>");

                // Match indicator
                html.append("<td style='border: 1px solid #ddd; padding: 10px; text-align: center; color: ")
                                .append(iconColor).append("; font-weight: bold; font-size: 18px;'>");
                html.append(icon);
                html.append("</td>");

                html.append("</tr>");
        }

        /**
         * Formats a value for display
         */
        private static String formatValue(Object value) {
                if (value == null) {
                        return "<span style='color: #999; font-style: italic;'>null</span>";
                }
                String strValue = String.valueOf(value);
                // Truncate very long values
                if (strValue.length() > 150) {
                        strValue = strValue.substring(0, 147) + "...";
                }
                return escapeHtml(strValue);
        }

        /**
         * Escapes HTML special characters
         */
        private static String escapeHtml(String value) {
                return value
                                .replace("&", "&amp;")
                                .replace("<", "&lt;")
                                .replace(">", "&gt;")
                                .replace("\"", "&quot;")
                                .replace("'", "&#39;");
        }

        /**
         * Counts matching fields between expected and actual
         */
        private static int countMatches(ProductDetailsModel expected, ProductDetailsModel actual) {
                int count = 0;
                if (Objects.equals(expected.getSku(), actual.getSku()))
                        count++;
                if (Objects.equals(expected.getName(), actual.getName()))
                        count++;
                if (Objects.equals(expected.getDescription(), actual.getDescription()))
                        count++;
                if (Objects.equals(expected.getCategory(), actual.getCategory()))
                        count++;
                if (Objects.equals(expected.getColor(), actual.getColor()))
                        count++;
                if (Objects.equals(expected.getIndication(), actual.getIndication()))
                        count++;
                if (Objects.equals(expected.getAlloyType(), actual.getAlloyType()))
                        count++;
                if (Objects.equals(expected.getVickersHardness(), actual.getVickersHardness()))
                        count++;
                if (Objects.equals(expected.getElongationAtBreakInPercent(), actual.getElongationAtBreakInPercent()))
                        count++;
                if (Objects.equals(expected.getMeltingIntervalCelsius(), actual.getMeltingIntervalCelsius()))
                        count++;
                if (Objects.equals(expected.getElementMnInPercent(), actual.getElementMnInPercent()))
                        count++;
                if (Objects.equals(expected.getElementFeInPercent(), actual.getElementFeInPercent()))
                        count++;
                if (Objects.equals(expected.getElementCInPercent(), actual.getElementCInPercent()))
                        count++;
                if (Objects.equals(expected.getElementCoInPercent(), actual.getElementCoInPercent()))
                        count++;
                if (Objects.equals(expected.getElementCrInPercent(), actual.getElementCrInPercent()))
                        count++;
                if (Objects.equals(expected.getElementMoInPercent(), actual.getElementMoInPercent()))
                        count++;
                if (Objects.equals(expected.getElementSiInPercent(), actual.getElementSiInPercent()))
                        count++;
                if (Objects.equals(expected.getElementWInPercent(), actual.getElementWInPercent()))
                        count++;
                if (Objects.equals(expected.getPriceEURInCent(), actual.getPriceEURInCent()))
                        count++;
                return count;
        }
}
