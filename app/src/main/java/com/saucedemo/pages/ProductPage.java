package com.saucedemo.pages;

import com.saucedemo.base.BasePage;
import com.saucedemo.utils.WaitUtils;
import org.openqa.selenium.By;

/**
 * ProductPage
 * -----------
 * Covers the product listing screen (after login) and
 * the individual product detail screen.
 */
public class ProductPage extends BasePage {

    // ============================================================
    // LOCATORS
    // ============================================================

    // Any store item on the listing page
    private final By anyStoreItem = By.xpath(
            "//android.view.ViewGroup[@content-desc='store item']");

    // First store item specifically
    private final By firstStoreItem = By.xpath(
            "(//android.view.ViewGroup[@content-desc='store item'])[1]");

    // Plus (+) button on the product detail page (increase quantity)
    private final By counterPlusButton = By.xpath(
            "//android.view.ViewGroup[@content-desc='counter plus button']");

    // Minus (-) button on the product detail page (decrease quantity)
    private final By counterMinusButton = By.xpath(
            "//android.view.ViewGroup[@content-desc='counter minus button']");

    // "Add To Cart" button on product detail
    private final By addToCartButton = By.xpath(
            "//android.view.ViewGroup[@content-desc='Add To Cart button']");

    // Cart badge (the number bubble on the cart icon)
    private final By cartBadge = By.xpath(
            "//android.view.ViewGroup[@content-desc='cart badge']");

    // Product name label on the detail screen
    private final By productTitle = By.xpath(
            "//android.widget.TextView[@content-desc='store item text']");

    // Back button (navigate back to listing)
    private final By backButton = By.xpath(
            "//android.view.ViewGroup[@content-desc='go back']");

    // ============================================================
    // ACTIONS
    // ============================================================

    /**
     * Tap on the first product to open its detail screen.
     */
    public ProductPage openFirstProduct() throws InterruptedException {
        click(firstStoreItem);
        WaitUtils.pause(2000);
        return this;
    }

    /**
     * Increase quantity by tapping "+" once.
     */
    public ProductPage incrementQuantity() throws InterruptedException {
        click(counterPlusButton);
        WaitUtils.pause(800);
        return this;
    }

    /**
     * Tap the "Add To Cart" button.
     */
    public ProductPage addToCart() throws InterruptedException {
        click(addToCartButton);
        WaitUtils.pause(2000);
        return this;
    }

    /**
     * Go back to the product listing screen.
     */
    public ProductPage goBack() throws InterruptedException {
        click(backButton);
        WaitUtils.pause(1000);
        return this;
    }

    // ============================================================
    // VERIFICATIONS
    // ============================================================

    /** Returns true if the product listing is visible. */
    public boolean isProductListingDisplayed() {
        return isDisplayed(anyStoreItem);
    }

    /** Returns true if the cart badge (item count) is visible. */
    public boolean isCartBadgeDisplayed() {
        return isDisplayed(cartBadge);
    }

    /** Returns true if the Add To Cart button is visible (on detail page). */
    public boolean isAddToCartButtonDisplayed() {
        return isDisplayed(addToCartButton);
    }

    /** Returns the product title text from the detail screen. */
    public String getProductTitle() {
        return getText(productTitle);
    }
}
