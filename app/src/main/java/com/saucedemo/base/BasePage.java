package com.saucedemo.base;

import com.saucedemo.utils.DriverManager;
import com.saucedemo.utils.WaitUtils;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * BasePage
 * --------
 * Every Page Object class extends BasePage.
 * It provides helper methods that all pages share:
 *   - click()
 *   - sendKeys()
 *   - getText()
 *   - isDisplayed()
 *
 * This keeps the page objects clean – they only contain
 * locators and business-level methods (e.g. login(), addToCart()).
 */
public class BasePage {

    // -------------------------------------------------------
    // Protected driver reference so child pages can use it
    // -------------------------------------------------------
    protected AndroidDriver driver;

    // -------------------------------------------------------
    // Constructor – grab the driver from DriverManager
    // -------------------------------------------------------
    public BasePage() {
        this.driver = DriverManager.getDriver();
    }

    // -------------------------------------------------------
    // CLICK an element (waits for it to be clickable first)
    // -------------------------------------------------------
    protected void click(By locator) {
        WaitUtils.waitForClickability(locator).click();
    }

    // -------------------------------------------------------
    // TYPE text into an input field
    // -------------------------------------------------------
    protected void sendKeys(By locator, String text) {
        WebElement element = WaitUtils.waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    // -------------------------------------------------------
    // GET the visible text of an element
    // -------------------------------------------------------
    protected String getText(By locator) {
        return WaitUtils.waitForVisibility(locator).getText();
    }

    // -------------------------------------------------------
    // CHECK if an element is currently shown on screen
    // -------------------------------------------------------
    protected boolean isDisplayed(By locator) {
        try {
            return WaitUtils.waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            return false;  // Element not found → return false
        }
    }

    // -------------------------------------------------------
    // CHECK if an element is present in the DOM (may be hidden)
    // -------------------------------------------------------
    protected boolean isPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
