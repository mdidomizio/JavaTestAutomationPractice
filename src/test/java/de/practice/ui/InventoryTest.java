package de.practice.ui;

import de.practice.ui.pages.CartPage;
import de.practice.ui.pages.InventoryPage;
import de.practice.ui.pages.LoginPage;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class InventoryTest extends BaseUiTest {

    @Test
    void addingTwoProductsUpdatesCartBadge(){
        InventoryPage inventory = new LoginPage(page)
                .open(BASE_URL)
                .loginAs("standard_user", PASSWORD);

        inventory.addToCart("Sauce Labs Backpack")
                .addToCart("Sauce Labs Bike Light");

        assertThat(inventory.removeButtonFor("Sauce Labs Bike Light")).isVisible();
        assertThat(inventory.cartBadge()).hasText("2");
    }

    @Test
    void addedProductAppearsInCart(){
        CartPage cart = new LoginPage(page)
                .open(BASE_URL)
                .loginAs("standard_user", PASSWORD)
                .addToCart("Sauce Labs Backpack")
                .openCart();

        assertThat(page).hasURL(BASE_URL + "/cart.html");
        assertThat(cart.title()).hasText("Your Cart");
        assertThat(cart.itemNames()).hasText(new String[] {"Sauce Labs Backpack"});
    }

    @Test
    void removingLastProductHidesCartBadge(){
        InventoryPage inventory = new LoginPage(page)
                .open(BASE_URL)
                .loginAs("standard_user", PASSWORD)
                .addToCart("Sauce Labs Backpack")
                .removeFromCart("Sauce Labs Backpack");

        assertThat(inventory.cartBadge()).isHidden();
    }
}
