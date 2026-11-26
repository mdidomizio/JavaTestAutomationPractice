package de.practice.ui;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

/**
 * One browser for the whole test class, a fresh context (cookies, storage) per test.
 * A new context per test keeps tests independent of each other.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseUiTest {

    protected static final String BASE_URL = "https://www.saucedemo.com";

    private Playwright playwright;
    private Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    void launchBrowser() {
        playwright = Playwright.create();
        boolean headless = !"false".equals(System.getProperty("headless"));
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @AfterAll
    void closeBrowser() {
        browser.close();
        playwright.close();
    }
}
