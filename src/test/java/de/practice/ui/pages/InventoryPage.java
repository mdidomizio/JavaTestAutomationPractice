package de.practice.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/** Page Object for the product list shown after login. */
public class InventoryPage {

    private final Page page;
    private final Locator title;

    public InventoryPage(Page page) {
        this.page = page;
        this.title = page.locator("[data-test='title']");
    }

    public Locator title() {
        return title;
    }

    // TODO next: addToCart(String productName), cartBadge(), openCart() -> CartPage
}
