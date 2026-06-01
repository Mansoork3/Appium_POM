package com.saucedemo.utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitUtils
 * ---------
 * Central place for all explicit-wait logic.
 * Using explicit waits instead of Thread.sleep() makes tests faster
 * and more reliable.
 */
public class WaitUtils {

    // How many seconds to wait before giving up (from config)
    private static final int EXPLICIT_WAIT_SECONDS =
            ConfigReader.getInt("explicit.wait");

    // -------------------------------------------------------
    // Wait until an element is VISIBLE on screen
    // -------------------------------------------------------
    public static WebElement waitForVisibility(By locator) {
        WebDriverWait wait = buildWait();
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // -------------------------------------------------------
    // Wait until an element is CLICKABLE
    // -------------------------------------------------------
    public static WebElement waitForClickability(By locator) {
        WebDriverWait wait = buildWait();
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // -------------------------------------------------------
    // Wait until an element is INVISIBLE (e.g., loading spinner gone)
    // -------------------------------------------------------
    public static boolean waitForInvisibility(By locator) {
        WebDriverWait wait = buildWait();
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // -------------------------------------------------------
    // Wait until a text appears inside an element
    // -------------------------------------------------------
    public static boolean waitForText(By locator, String text) {
        WebDriverWait wait = buildWait();
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    // -------------------------------------------------------
    // Simple fixed pause (use sparingly – prefer explicit waits)
    // -------------------------------------------------------
    public static void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // -------------------------------------------------------
    // Internal helper – creates a WebDriverWait with our timeout
    // -------------------------------------------------------
    private static WebDriverWait buildWait() {
        AndroidDriver driver = DriverManager.getDriver();
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_SECONDS));
    }
}
