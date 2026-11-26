package de.practice.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.List;

/** Page Object for the product list shown after login. */
public class InventoryPage {

    private final Page page;
    private final Locator title;
    private final Locator cartBadge;
    private final Locator sortDropdown;
    private final Locator itemNames;
    private final Locator itemPrices;


    public InventoryPage(Page page) {
        this.page = page;
        this.title = page.locator("[data-test='title']");
        this.cartBadge = page.locator("[data-test='shopping-cart-badge']");
        this.sortDropdown = page.locator("[data-test='product-sort-container']");
        this.itemNames = page.locator("[data-test='inventory-item-name']");
        this.itemPrices = page.locator("[data-test='inventory-item-price']");
    }

    public Locator title() {return title; }
    public Locator cartBadge() { return cartBadge; }
    public Locator itemNames() { return itemNames; }
    public Locator itemPrices() { return itemPrices; }

    public InventoryPage sortBy(String option) {
        sortDropdown.selectOption(option);
        return this;
    }

    private Locator productCard(String productName){
        return page.locator("[data-test='inventory-item']")
                .filter(new Locator.FilterOptions().setHasText(productName));
    }

    public InventoryPage addToCart(String productName) {
        productCard(productName).getByRole(
                AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Add to cart")).click();
        return this;
    }

    public Locator removeButtonFor(String productName) {
        return productCard(productName).getByRole(
                AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Remove")
        );
    }
    public CartPage openCart() {
        page.locator("[data-test='shopping-cart-link']").click();
        return new CartPage(page);
    }

    public InventoryPage removeFromCart(String productName) {
        removeButtonFor(productName).click();
        return this;
    }
}
