package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.listeners.RetryListener;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.MenuPage;
import com.saucedemo.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * LogoutTest
 * ----------
 * Tests the logout functionality.
 *
 * Groups:
 *   "smoke"      → TC_LOGOUT_01 (basic logout)
 *   "logout"     → all logout tests
 *   "regression" → all tests
 */
public class LogoutTest extends BaseTest {

    // ================================================================
    // TC_LOGOUT_01 – Log out successfully
    // ================================================================
    @Test(
        priority   = 1,
        groups     = {"smoke", "logout", "regression"},
        description = "Verify that a logged-in user can log out successfully",
        retryAnalyzer = RetryListener.class
    )
    public void testLogoutSuccessfully() throws Exception {

        // ---- Step 1: Log in ----
        loginAsValidUser();

        // ---- Step 2: Open menu and logout ----
        MenuPage menuPage = new MenuPage();
        menuPage.openMenu()
                .tapLogout();

        // ---- Step 3: Verify – the "Log In" option is now visible in the menu ----
        menuPage.openMenu();

        Assert.assertTrue(
            menuPage.isLoginOptionVisible(),
            "Logout failed – 'Log In' option is NOT visible in the menu after logout."
        );

        System.out.println("[LogoutTest] ✅ TC_LOGOUT_01 PASSED – Logout successful.");
    }

    // ================================================================
    // TC_LOGOUT_02 – Verify logout menu option is visible when logged in
    // ================================================================
    @Test(
        priority   = 2,
        groups     = {"logout", "regression"},
        description = "Verify that the Log Out option is visible in the menu for a logged-in user",
        retryAnalyzer = RetryListener.class
    )
    public void testLogoutOptionVisibleWhenLoggedIn() throws Exception {

        // ---- Step 1: Log in ----
        loginAsValidUser();

        // ---- Step 2: Open menu ----
        MenuPage menuPage = new MenuPage();
        menuPage.openMenu();

        // ---- Step 3: Verify logout option is shown ----
        Assert.assertTrue(
            menuPage.isLogoutOptionVisible(),
            "Log Out option is NOT visible in the menu for a logged-in user."
        );

        System.out.println("[LogoutTest] ✅ TC_LOGOUT_02 PASSED – Logout option is visible.");
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
