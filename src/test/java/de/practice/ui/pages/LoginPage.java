package de.practice.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Page Object for the login screen.
 * It knows HOW to talk to the page (selectors, clicks), not WHAT the test expects.
 * Assertions stay in the test classes.
 */
public class LoginPage {

    private final Page page;
    private final Locator usernameInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator errorMessage;

    public LoginPage(Page page) {
        this.page = page;
        // data-test attributes are made for tests: they don't change when the design or text changes
        this.usernameInput = page.locator("[data-test='username']");
        this.passwordInput = page.locator("[data-test='password']");
        this.loginButton = page.locator("[data-test='login-button']");
        this.errorMessage = page.locator("[data-test='error']");
    }

    public LoginPage open(String baseUrl) {
        page.navigate(baseUrl);
        return this;
    }

    public InventoryPage loginAs(String username, String password) {
        usernameInput.fill(username);
        passwordInput.fill(password);
        loginButton.click();
        return new InventoryPage(page);
    }

    public Locator errorMessage() {
        return errorMessage;
    }
}
