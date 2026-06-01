package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.listeners.RetryListener;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * LoginTest
 * ---------
 * Tests the login functionality of Sauce Demo Mobile.
 *
 * Test Cases:
 *   TC_LOGIN_01 – Positive: valid credentials → home page shown
 *   TC_LOGIN_02 – Negative: invalid password  → error shown
 *   TC_LOGIN_03 – Negative: invalid email     → error shown
 *   TC_LOGIN_04 – Negative: blank credentials → error shown / stays on login page
 *
 * Groups:
 *   "smoke"    → TC_LOGIN_01 runs in every suite
 *   "login"    → all login tests
 *   "negative" → TC_LOGIN_02, 03, 04  (NegativeTestListener watches these)
 *   "regression" → all tests run in regression
 *
 * IMPORTANT ABOUT NEGATIVE TESTS:
 *   A negative test is PASSING when the app REJECTS bad input.
 *   We assert that the error message IS displayed, or that the
 *   login page is still shown. If those assertions are TRUE,
 *   TestNG will mark the test as PASSED. ✅
 */
public class LoginTest extends BaseTest {

    // ================================================================
    // TC_LOGIN_01 – POSITIVE: Login with valid credentials
    // ================================================================
    @Test(
        priority   = 1,
        groups     = {"smoke", "login", "regression"},
        description = "Verify that a registered user can log in with correct credentials",
        retryAnalyzer = RetryListener.class
    )
    public void testLoginWithValidCredentials() throws Exception {

        // Arrange
        String validUsername = ConfigReader.get("valid.username");
        String validPassword = ConfigReader.get("valid.password");

        LoginPage loginPage = new LoginPage();

        // Act
        loginPage.navigateToLoginScreen()
                 .login(validUsername, validPassword);

        // Assert – the product listing must be visible after successful login
        Assert.assertTrue(
            loginPage.isLoginSuccessful(),
            "Login FAILED – product listing not visible after valid login."
        );

        System.out.println("[LoginTest] ✅ TC_LOGIN_01 PASSED – Valid login successful.");
    }

    // ================================================================
    // TC_LOGIN_02 – NEGATIVE: Login with invalid password
    // The test PASSES when the app shows an error (rejects bad password)
    // ================================================================
    @Test(
        priority   = 2,
        groups     = {"login", "negative", "regression"},
        description = "Verify that login fails when an incorrect password is used",
        retryAnalyzer = RetryListener.class
    )
    public void testLoginWithInvalidPassword() throws Exception {

        // Arrange – use valid email but WRONG password
        String validUsername  = ConfigReader.get("valid.username");
        String invalidPassword = ConfigReader.get("invalid.password");

        LoginPage loginPage = new LoginPage();

        // Act
        loginPage.navigateToLoginScreen()
                 .login(validUsername, invalidPassword);

        // Assert – the app should REJECT this login
        // Test PASSES if error message is shown OR we are still on login page
        boolean isRejected = loginPage.isErrorMessageDisplayed()
                          || loginPage.isStillOnLoginPage();

        Assert.assertTrue(
            isRejected,
            "SECURITY ISSUE – App accepted an invalid password. It should have shown an error."
        );

        System.out.println("[LoginTest] ✅ TC_LOGIN_02 PASSED – Invalid password correctly rejected."
                + (loginPage.isErrorMessageDisplayed()
                   ? " Error: " + loginPage.getErrorMessageText()
                   : ""));
    }

    // ================================================================
    // TC_LOGIN_03 – NEGATIVE: Login with invalid / unregistered email
    // ================================================================
    @Test(
        priority   = 3,
        groups     = {"login", "negative", "regression"},
        description = "Verify that login fails when an unregistered email is used",
        retryAnalyzer = RetryListener.class
    )
    public void testLoginWithInvalidEmail() throws Exception {

        // Arrange – unknown email + valid-format password
        String invalidUsername = ConfigReader.get("invalid.username");
        String validPassword   = ConfigReader.get("valid.password");

        LoginPage loginPage = new LoginPage();

        // Act
        loginPage.navigateToLoginScreen()
                 .login(invalidUsername, validPassword);

        // Assert – the app should REJECT an unknown username
        boolean isRejected = loginPage.isErrorMessageDisplayed()
                          || loginPage.isStillOnLoginPage();

        Assert.assertTrue(
            isRejected,
            "SECURITY ISSUE – App accepted an unregistered email. It should have shown an error."
        );

        System.out.println("[LoginTest] ✅ TC_LOGIN_03 PASSED – Unregistered email correctly rejected."
                + (loginPage.isErrorMessageDisplayed()
                   ? " Error: " + loginPage.getErrorMessageText()
                   : ""));
    }

    // ================================================================
    // TC_LOGIN_04 – NEGATIVE: Login with both fields empty (blank submit)
    // ================================================================
    @Test(
        priority   = 4,
        groups     = {"login", "negative", "regression"},
        description = "Verify that login fails when both username and password are left blank",
        retryAnalyzer = RetryListener.class
    )
    public void testLoginWithBlankCredentials() throws Exception {

        LoginPage loginPage = new LoginPage();

        // Act – navigate to login and immediately tap the Login button
        loginPage.navigateToLoginScreen()
                 .login("", "");  // Empty strings

        // Assert – the app must stay on the login page or show an error
        boolean isRejected = loginPage.isErrorMessageDisplayed()
                          || loginPage.isStillOnLoginPage();

        Assert.assertTrue(
            isRejected,
            "SECURITY ISSUE – App accepted a blank login. It should have shown a validation error."
        );

        System.out.println("[LoginTest] ✅ TC_LOGIN_04 PASSED – Blank credentials correctly rejected."
                + (loginPage.isErrorMessageDisplayed()
                   ? " Error: " + loginPage.getErrorMessageText()
                   : ""));
    }
}
