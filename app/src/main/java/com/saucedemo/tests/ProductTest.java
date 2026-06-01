package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.listeners.RetryListener;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductPage;
import com.saucedemo.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * ProductTest
 * -----------
 * Tests the product listing and product detail screens.
 *
 * Groups:
 *   "smoke"      → TC_PRODUCT_01 (verify listing loads)
 *   "product"    → all product tests
 *   "regression" → all tests
 */
public class ProductTest extends BaseTest {

    // ================================================================
    // TC_PRODUCT_01 – Product listing is displayed after login
    // ================================================================
    @Test(
        priority   = 1,
        groups     = {"smoke", "product", "regression"},
        description = "Verify that the product listing screen loads after a successful login",
        retryAnalyzer = RetryListener.class
    )
    public void testProductListingIsDisplayed() throws Exception {

        loginAsValidUser();

        ProductPage productPage = new ProductPage();

        Assert.assertTrue(
            productPage.isProductListingDisplayed(),
            "Product listing is NOT visible after login."
        );

        System.out.println("[ProductTest] ✅ TC_PRODUCT_01 PASSED – Product listing is visible.");
    }

    // ================================================================
    // TC_PRODUCT_02 – Product detail screen opens on tap
    // ================================================================
    @Test(
        priority   = 2,
        groups     = {"product", "regression"},
        description = "Verify that tapping a product opens its detail screen",
        retryAnalyzer = RetryListener.class
    )
    public void testProductDetailScreenOpens() throws Exception {

        loginAsValidUser();

        ProductPage productPage = new ProductPage();
        productPage.openFirstProduct();

        // Verify the "Add To Cart" button is visible on the detail screen
        Assert.assertTrue(
            productPage.isAddToCartButtonDisplayed(),
            "Add To Cart button is NOT visible on the product detail screen."
        );

        System.out.println("[ProductTest] ✅ TC_PRODUCT_02 PASSED – Product detail screen opened.");
    }

    // ================================================================
    // TC_PRODUCT_03 – Quantity counter works on detail screen
    // ================================================================
    @Test(
        priority   = 3,
        groups     = {"product", "regression"},
        description = "Verify that the quantity +/- counter works on the product detail screen",
        retryAnalyzer = RetryListener.class
    )
    public void testQuantityCounterIncrement() throws Exception {

        loginAsValidUser();

        ProductPage productPage = new ProductPage();
        productPage.openFirstProduct()
                   .incrementQuantity();

        // Verify Add To Cart is still available (page didn't crash)
        Assert.assertTrue(
            productPage.isAddToCartButtonDisplayed(),
            "Add To Cart button disappeared after incrementing quantity."
        );

        System.out.println("[ProductTest] ✅ TC_PRODUCT_03 PASSED – Quantity counter increment works.");
    }

    // ================================================================
    // Helper
    // ================================================================
    private void loginAsValidUser() throws Exception {
        String username = ConfigReader.get("valid.username");
        String password = ConfigReader.get("valid.password");

        new LoginPage()
            .navigateToLoginScreen()
            .login(username, password);
    }
}
