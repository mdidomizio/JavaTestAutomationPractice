package de.practice.ui;

import de.practice.ui.pages.InventoryPage;
import de.practice.ui.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class LoginTest extends BaseUiTest {

    private static final String PASSWORD = "secret_sauce"; // public demo password on saucedemo.com

    @Test
    @DisplayName("Standard user logs in and sees the product list")
    void standardUserCanLogIn() {
        InventoryPage inventory = new LoginPage(page)
                .open(BASE_URL)
                .loginAs("standard_user", PASSWORD);

        // Playwright's assertThat waits and retries until the condition is true (or times out),
        // so no Thread.sleep is needed. This is a big part of avoiding flaky tests.
        assertThat(page).hasURL(BASE_URL + "/inventory.html");
        assertThat(inventory.title()).hasText("Products");
    }

    @Test
    @DisplayName("Locked-out user gets an error message")
    void lockedOutUserSeesError() {
        LoginPage login = new LoginPage(page).open(BASE_URL);
        login.loginAs("locked_out_user", PASSWORD);

        assertThat(login.errorMessage()).containsText("locked out");
    }

    // TODO: @ParameterizedTest over several users (standard_user, problem_user, ...)
    // TODO: same login test in a mobile viewport: browser.newContext(new Browser.NewContextOptions().setViewportSize(390, 844))
}
