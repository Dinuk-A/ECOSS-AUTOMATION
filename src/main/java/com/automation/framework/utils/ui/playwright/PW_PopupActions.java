package com.automation.framework.utils.ui.playwright;

import com.microsoft.playwright.Dialog;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;

public class PW_PopupActions {
    private Page page;

    // Constructor to initialize the Page
    public PW_PopupActions(Page page) {
        this.page = page;
    }

    /*
     * Accept an alert, confirm, or prompt dialog.
     * - For alerts: Accepts a simple alert popup
     * - For confirms: Accepts a confirmation dialog (e.g., clicking "OK" on a "Yes/No" confirmation)
     * - For prompts: Accepts a prompt dialog (e.g., clicking "OK" after entering text)
     */
    public void acceptPopup(Dialog dialog) {
        try {
            dialog.accept();
        } catch (PlaywrightException e) {
            System.out.println("Error accepting dialog: " + e.getMessage());
        }
    }

    /*
     * Auto-accept dialogs that appear on the page.
     * Set up a listener before performing an action that triggers a dialog.
     * Example usage:
     *   setupAutoAccept();
     *   driver.click("button.delete"); // Triggers a confirm dialog
     *   // Dialog is automatically accepted
     */
    public void setupAutoAccept() {
        page.onDialog(dialog -> {
            dialog.accept();
        });
    }

    /*
     * Dismiss an alert, confirm, or prompt dialog.
     * - For alerts: Dismisses a simple alert popup
     * - For confirms: Dismisses a confirmation dialog (e.g., clicking "Cancel" on a "Yes/No" confirmation)
     * - For prompts: Dismisses a prompt dialog (e.g., clicking "Cancel" after entering text)
     */
    public void dismissPopup(Dialog dialog) {
        try {
            dialog.dismiss();
        } catch (PlaywrightException e) {
            System.out.println("Error dismissing dialog: " + e.getMessage());
        }
    }

    /*
     * Auto-dismiss dialogs that appear on the page.
     * Set up a listener before performing an action that triggers a dialog.
     * Example usage:
     *   setupAutoDismiss();
     *   driver.click("button.cancel"); // Triggers a confirm dialog
     *   // Dialog is automatically dismissed
     */
    public void setupAutoDismiss() {
        page.onDialog(dialog -> {
            dialog.dismiss();
        });
    }

    /*
     * Retrieve the text/message of an alert, confirm, or prompt dialog.
     * - For alerts: Returns the message of a simple alert popup
     * - For confirms: Returns the message of a confirmation dialog (e.g., "Are you sure you want to delete this?")
     * - For prompts: Returns the message of a prompt dialog (e.g., "Please enter your name")
     */
    public String getPopupText(Dialog dialog) {
        try {
            return dialog.message();
        } catch (PlaywrightException e) {
            System.out.println("Error getting dialog message: " + e.getMessage());
            return null;
        }
    }

    /*
     * Send text to a prompt alert/dialog.
     * - Specifically used for prompt dialogs, where a user is expected to enter text
     * - Does nothing for alerts or confirms as they do not require text input
     */
    public void inputTextToPrompt(Dialog dialog, String text) {
        try {
            dialog.accept(text);
        } catch (PlaywrightException e) {
            System.out.println("Error sending text to prompt: " + e.getMessage());
        }
    }
}
