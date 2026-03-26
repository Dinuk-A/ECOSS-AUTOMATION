package com.automation.framework.utils.ui.selenium;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

public class SE_PopupActions {

    @SuppressWarnings("unused")
    private WebDriver driver;

    // Constructor to initialize the WebDriver
    public SE_PopupActions(WebDriver driver) {
        this.driver = driver;
    }

    // Accept an alert, confirm, or prompt based on the type of popup displayed
    // - For alerts: Accepts a simple alert popup
    // - For confirms: Accepts a confirmation dialog (e.g., clicking "OK" on a
    // "Yes/No" confirmation)
    // - For prompts: Accepts a prompt dialog (e.g., clicking "OK" after entering
    // text)
    public void acceptPopup(Alert alert) {
        try {
            alert.accept();
        } catch (NoAlertPresentException e) {
            System.out.println("No alert to accept.");
        }
    }

    // test following one ?????????????????????????????
    /*
     * public void acceptPopup() {
     * Alert alert = getAlert();
     * if (alert != null) alert.accept();
     * }
     */

    // Dismiss an alert, confirm, or prompt based on the type of popup displayed
    // - For alerts: Dismisses a simple alert popup
    // - For confirms: Dismisses a confirmation dialog (e.g., clicking "Cancel" on a
    // "Yes/No" confirmation)
    // - For prompts: Dismisses a prompt dialog (e.g., clicking "Cancel" after
    // entering text)
    public void dismissPopup(Alert alert) {
        try {
            alert.dismiss();
        } catch (NoAlertPresentException e) {
            System.out.println("No alert, confirm, or prompt present to dismiss.");
        }
    }

    // Retrieve the text of an alert, confirm, or prompt
    // - For alerts: Returns the message of a simple alert popup
    // - For confirms: Returns the message of a confirmation dialog (e.g., "Are you
    // sure you want to delete this?")
    // - For prompts: Returns the message of a prompt dialog (e.g., "Please enter
    // your name")
    public String getPopupText(Alert alert) {
        try {
            return alert.getText();
        } catch (NoAlertPresentException e) {
            return null;
        }
    }

    // Send text to a prompt alert
    // - Specifically used for prompt dialogs, where a user is expected to enter
    // text (e.g., "Please enter your name")
    // - Does nothing for alerts or confirms as they do not require text input
    public void inputTextToPrompt(Alert alert, String text) {
        try {
            alert.sendKeys(text);
        } catch (NoAlertPresentException e) {
            System.out.println("No prompt present to send text to.");
        }
    }

}
