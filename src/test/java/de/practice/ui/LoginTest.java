package de.practice.ui;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.assertions.PageAssertions;
import de.practice.ui.pages.InventoryPage;
import de.practice.ui.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class LoginTest extends BaseUiTest {

    @Test
    @DisplayName("Locked-out user gets an error message")
    void lockedOutUserSeesError() {
        LoginPage login = new LoginPage(page).open(BASE_URL);
        login.loginAs("locked_out_user", PASSWORD);

        assertThat(login.errorMessage()).containsText("locked out");
    }

    @ParameterizedTest(name = "{0} can log in and sees the product list")
    @ValueSource(strings = {
            "standard_user",
            "problem_user",
            "performance_glitch_user",  // login is deliberately delayed, see timeout below
            "error_user",
            "visual_user"
    })
    void usersCanLogin(String user) {
        InventoryPage inventory = new LoginPage(page)
                .open(BASE_URL)
                .loginAs(user, PASSWORD);

        assertThat(page).hasURL(BASE_URL + "/inventory.html",
                new PageAssertions.HasURLOptions().setTimeout(10_000));
        assertThat(inventory.title()).hasText("Products");
    }

    @ParameterizedTest(name = "login works at {0}x{1}")
    @CsvSource({
            "1280, 720", // desktop
            "390, 844" // mobile
    })
    void standardUserCanLogIn_onViewport(int width, int height) {
        page.setViewportSize(width, height); //iPhone 14 size

        InventoryPage inventory = new LoginPage(page)
                .open(BASE_URL)
                .loginAs("standard_user", PASSWORD);

        assertThat(page).hasURL(BASE_URL + "/inventory.html");
        assertThat(inventory.title()).hasText("Products");
    }
}
