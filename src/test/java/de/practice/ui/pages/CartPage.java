package de.practice.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CartPage {
    private final Locator title;
    private final Locator items;

    public CartPage(Page page) {
        this.title = page.locator("[data-test='title']");
        this.items = page.locator("[data-test='inventory-item-name']");
    }

    public Locator title() { return title; }
    public Locator itemNames() { return items; }
}
