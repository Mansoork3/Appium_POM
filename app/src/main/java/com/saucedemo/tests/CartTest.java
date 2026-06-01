package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.listeners.RetryListener;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.MenuPage;
import com.saucedemo.pages.ProductPage;
import com.saucedemo.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * CartTest
 * --------
 * Tests shopping cart functionality.
 *
 * Pre-condition: user must be logged in before each test.
 * We log in inside each test method (BaseTest starts a fresh app).
 *
 * Groups:
 *   "smoke"      → TC_CART_01 (basic add to cart)
 *   "cart"       → all cart tests
 *   "regression" → all tests
 */
public class CartTest extends BaseTest {

    // ================================================================
    // TC_CART_01 – Add product to cart and verify cart badge
    // ================================================================
    @Test(
        priority   = 1,
        groups     = {"smoke", "cart", "regression"},
        description = "Verify that a product can be added to the cart and the badge updates",
        retryAnalyzer = RetryListener.class
    )
    public void testAddProductToCart() throws Exception {

        // ---- Step 1: Log in ----
        loginAsValidUser();

        // ---- Step 2: Open first product ----
        ProductPage productPage = new ProductPage();
        productPage.openFirstProduct();

        // ---- Step 3: Increase quantity by 1 (so qty = 2) ----
        productPage.incrementQuantity();

        // ---- Step 4: Add to cart ----
        productPage.addToCart();

        // ---- Step 5: Verify cart badge appears ----
        Assert.assertTrue(
            productPage.isCartBadgeDisplayed(),
            "Cart badge not visible after adding a product to the cart."
        );

        System.out.println("[CartTest] ✅ TC_CART_01 PASSED – Product added to cart.");
    }

    // ================================================================
    // TC_CART_02 – Navigate to cart screen and verify item is listed
    // ================================================================
    @Test(
        priority   = 2,
        groups     = {"cart", "regression"},
        description = "Verify that the cart screen shows the item that was added",
        retryAnalyzer = RetryListener.class
    )
    public void testCartItemIsListed() throws Exception {

        // ---- Step 1: Log in ----
        loginAsValidUser();

        // ---- Step 2: Add a product ----
        ProductPage productPage = new ProductPage();
        productPage.openFirstProduct()
                   .addToCart();

        // ---- Step 3: Open the cart ----
        CartPage cartPage = new CartPage();
        cartPage.openCart();

        // ---- Step 4: Verify cart page opened and item exists ----
        Assert.assertTrue(
            cartPage.isCartPageDisplayed(),
            "Cart page did not open."
        );
        Assert.assertTrue(
            cartPage.isCartItemPresent(),
            "No cart item found in the cart after adding a product."
        );

        System.out.println("[CartTest] ✅ TC_CART_02 PASSED – Cart item is listed correctly.");
    }

    // ================================================================
    // TC_CART_03 – Remove item from cart
    // ================================================================
    @Test(
        priority   = 3,
        groups     = {"cart", "regression"},
        description = "Verify that an item can be removed from the cart",
        retryAnalyzer = RetryListener.class
    )
    public void testRemoveItemFromCart() throws Exception {

        // ---- Step 1: Log in ----
        loginAsValidUser();

        // ---- Step 2: Add a product ----
        ProductPage productPage = new ProductPage();
        productPage.openFirstProduct()
                   .addToCart();

        // ---- Step 3: Open the cart ----
        CartPage cartPage = new CartPage();
        cartPage.openCart();

        // ---- Step 4: Remove the item ----
        cartPage.removeFirstItem();

        // ---- Step 5: Cart should now be empty ----
        Assert.assertTrue(
            cartPage.isCartEmpty(),
            "Cart is NOT empty after removing the only item."
        );

        System.out.println("[CartTest] ✅ TC_CART_03 PASSED – Item removed from cart successfully.");
    }

    // ================================================================
    // Helper – logs in with valid credentials (reused across tests)
    // ================================================================
    private void loginAsValidUser() throws Exception {
        String username = ConfigReader.get("valid.username");
        String password = ConfigReader.get("valid.password");

        new LoginPage()
            .navigateToLoginScreen()
            .login(username, password);
    }
}
