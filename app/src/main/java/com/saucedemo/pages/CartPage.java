package com.saucedemo.pages;

import com.saucedemo.base.BasePage;
import com.saucedemo.utils.WaitUtils;
import org.openqa.selenium.By;

/**
 * CartPage
 * --------
 * Covers the shopping cart / checkout screens.
 */
public class CartPage extends BasePage {

    // ============================================================
    // LOCATORS
    // ============================================================

    // Cart icon in the top navigation bar
    private final By cartIcon = By.xpath(
            "//android.view.ViewGroup[@content-desc='cart badge']/..  | " +
            "//android.view.ViewGroup[@content-desc='cart button']");

    // Cart item in the cart list
    private final By cartItem = By.xpath(
            "//android.view.ViewGroup[@content-desc='cart item']");

    // "Proceed To Checkout" button
    private final By proceedToCheckoutButton = By.xpath(
            "//android.view.ViewGroup[@content-desc='Proceed To Checkout button']");

    // Remove item button inside cart
    private final By removeItemButton = By.xpath(
            "(//android.view.ViewGroup[@content-desc='remove item'])[1]");

    // Cart title / header
    private final By cartTitle = By.xpath(
            "//android.widget.TextView[@text='My Cart']");

    // "Go Shopping" button (appears when cart is empty)
    private final By goShoppingButton = By.xpath(
            "//android.view.ViewGroup[@content-desc='Go Shopping button']");

    // ============================================================
    // ACTIONS
    // ============================================================

    /**
     * Navigate to the cart screen by tapping the cart icon.
     */
    public CartPage openCart() throws InterruptedException {
        // Tap the cart badge/button in the top bar
        driver.findElement(By.xpath(
                "//android.view.ViewGroup[@content-desc='cart badge']")).click();
        WaitUtils.pause(2000);
        return this;
    }

    /**
     * Remove the first item from the cart.
     */
    public CartPage removeFirstItem() throws InterruptedException {
        click(removeItemButton);
        WaitUtils.pause(1500);
        return this;
    }

    /**
     * Tap "Proceed To Checkout".
     */
    public CartPage proceedToCheckout() throws InterruptedException {
        click(proceedToCheckoutButton);
        WaitUtils.pause(2000);
        return this;
    }

    // ============================================================
    // VERIFICATIONS
    // ============================================================

    /** Returns true if the My Cart screen is visible. */
    public boolean isCartPageDisplayed() {
        return isDisplayed(cartTitle);
    }

    /** Returns true if at least one cart item is shown. */
    public boolean isCartItemPresent() {
        return isDisplayed(cartItem);
    }

    /** Returns true if the cart is empty (Go Shopping button visible). */
    public boolean isCartEmpty() {
        return isDisplayed(goShoppingButton);
    }
}
