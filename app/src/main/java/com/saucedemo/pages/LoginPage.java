package com.saucedemo.pages;

import com.saucedemo.base.BasePage;
import com.saucedemo.utils.WaitUtils;
import org.openqa.selenium.By;

/**
 * LoginPage
 * ---------
 * Contains ALL locators and actions for the Login screen.
 * Tests never write XPaths directly – they call methods here.
 *
 * Flow to reach Login:
 *   1. Open hamburger menu
 *   2. Tap "Log In"
 *   3. Enter credentials → tap Login button
 */
public class LoginPage extends BasePage {

    // ============================================================
    // LOCATORS  (XPaths taken from the working reference script)
    // ============================================================

    // Hamburger / open-menu button on the home screen
    private final By menuButton = By.xpath(
            "//android.view.ViewGroup[@content-desc='open menu']/android.widget.ImageView");

    // "Log In" item inside the side menu
    private final By menuLoginItem = By.xpath(
            "//android.view.ViewGroup[@content-desc='menu item log in']");

    // Username text field
    private final By usernameField = By.xpath(
            "//android.widget.EditText[@content-desc='Username input field']");

    // Password text field
    private final By passwordField = By.xpath(
            "//android.widget.EditText[@content-desc='Password input field']");

    // Login submit button  (second TextView with text "Login")
    private final By loginButton = By.xpath(
            "(//android.widget.TextView[@text='Login'])[2]");

    // Error message shown for invalid credentials
    private final By errorMessage = By.xpath(
            "//android.widget.TextView[@content-desc='generic-error-message']");

    // Any store item – confirms a successful login (home page loaded)
    private final By storeItem = By.xpath(
            "//android.view.ViewGroup[@content-desc='store item']");

    // ============================================================
    // ACTIONS
    // ============================================================

    /**
     * Navigate from the home screen to the Login form.
     * Opens the side menu, then taps the "Log In" menu item.
     */
    public LoginPage navigateToLoginScreen() throws InterruptedException {
        click(menuButton);
        WaitUtils.pause(1500);
        click(menuLoginItem);
        WaitUtils.pause(1500);
        return this;
    }

    /**
     * Fill in the username field.
     */
    public LoginPage enterUsername(String username) {
        sendKeys(usernameField, username);
        return this;
    }

    /**
     * Fill in the password field.
     */
    public LoginPage enterPassword(String password) {
        sendKeys(passwordField, password);
        return this;
    }

    /**
     * Tap the Login button.
     */
    public LoginPage tapLoginButton() throws InterruptedException {
        click(loginButton);
        WaitUtils.pause(2500);
        return this;
    }

    /**
     * Complete login in one call (username + password + submit).
     */
    public LoginPage login(String username, String password) throws InterruptedException {
        enterUsername(username);
        enterPassword(password);
        tapLoginButton();
        return this;
    }

    // ============================================================
    // VERIFICATION HELPERS
    // (Tests call these to check what happened after login)
    // ============================================================

    /**
     * Returns true if at least one store item is visible on screen.
     * This means login was SUCCESSFUL.
     */
    public boolean isLoginSuccessful() {
        return isDisplayed(storeItem);
    }

    /**
     * Returns true if the error message banner is visible.
     * This means login FAILED (negative test cases expect this).
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    /**
     * Returns the text of the error message.
     */
    public String getErrorMessageText() {
        return getText(errorMessage);
    }

    /**
     * Returns true if the login button is still on screen
     * (meaning we are still on the login page – login was rejected).
     */
    public boolean isStillOnLoginPage() {
        return isDisplayed(loginButton);
    }
}
