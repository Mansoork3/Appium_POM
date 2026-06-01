package com.saucedemo.pages;

import com.saucedemo.base.BasePage;
import com.saucedemo.utils.WaitUtils;
import org.openqa.selenium.By;

/**
 * MenuPage
 * --------
 * Manages the hamburger side-navigation menu.
 * The menu is accessed from any screen via the top-left icon.
 */
public class MenuPage extends BasePage {

    // ============================================================
    // LOCATORS
    // ============================================================

    // Hamburger icon (top-left of every screen)
    private final By hamburgerIcon = By.xpath(
            "//android.view.ViewGroup[@content-desc='open menu']/android.widget.ImageView");

    // Menu items
    private final By menuLoginItem  = By.xpath(
            "//android.view.ViewGroup[@content-desc='menu item log in']");

    private final By menuLogoutItem = By.xpath(
            "//android.view.ViewGroup[@content-desc='menu item log out']");

    private final By menuCatalogItem = By.xpath(
            "//android.view.ViewGroup[@content-desc='menu item catalog']");

    private final By menuWebviewItem = By.xpath(
            "//android.view.ViewGroup[@content-desc='menu item webview']");

    // Logout confirmation popup – "OK" button
    private final By logoutConfirmOkButton = By.xpath(
            "//android.widget.Button[@resource-id='android:id/button1']");

    // Close / dismiss button for the menu (X icon)
    private final By closeMenuButton = By.xpath(
            "//android.view.ViewGroup[@content-desc='close menu']");

    // ============================================================
    // ACTIONS
    // ============================================================

    /**
     * Open the side menu.
     */
    public MenuPage openMenu() throws InterruptedException {
        click(hamburgerIcon);
        WaitUtils.pause(1500);
        return this;
    }

    /**
     * Tap "Log In" from the menu.
     */
    public MenuPage tapLogin() throws InterruptedException {
        click(menuLoginItem);
        WaitUtils.pause(1500);
        return this;
    }

    /**
     * Tap "Log Out" from the menu and confirm in the popup.
     */
    public MenuPage tapLogout() throws InterruptedException {
        click(menuLogoutItem);
        WaitUtils.pause(1500);
        // Confirm the logout alert dialog
        click(logoutConfirmOkButton);
        WaitUtils.pause(2000);
        return this;
    }

    /**
     * Tap "Catalog" from the menu.
     */
    public MenuPage tapCatalog() throws InterruptedException {
        click(menuCatalogItem);
        WaitUtils.pause(1500);
        return this;
    }

    /**
     * Close the menu without selecting anything.
     */
    public MenuPage closeMenu() throws InterruptedException {
        if (isDisplayed(closeMenuButton)) {
            click(closeMenuButton);
            WaitUtils.pause(1000);
        }
        return this;
    }

    // ============================================================
    // VERIFICATIONS
    // ============================================================

    /** Returns true if the menu panel is open (login item visible). */
    public boolean isMenuOpen() {
        return isDisplayed(menuLoginItem) || isDisplayed(menuLogoutItem);
    }

    /** Returns true if the "Log Out" option is visible (user is logged in). */
    public boolean isLogoutOptionVisible() {
        return isDisplayed(menuLogoutItem);
    }

    /** Returns true if the "Log In" option is visible (user is NOT logged in). */
    public boolean isLoginOptionVisible() {
        return isDisplayed(menuLoginItem);
    }
}
