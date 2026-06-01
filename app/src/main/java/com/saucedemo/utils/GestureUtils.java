package com.saucedemo.utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Arrays;

/**
 * GestureUtils
 * ------------
 * Provides common touch gestures like swipe and scroll.
 * Uses the W3C Actions API (the modern Appium 2.x approach).
 */
public class GestureUtils {

    // -------------------------------------------------------
    // Swipe from bottom to top (scroll UP the page)
    // -------------------------------------------------------
    public static void swipeUp() {
        AndroidDriver driver = DriverManager.getDriver();
        Dimension size = driver.manage().window().getSize();

        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);   // 80% down the screen
        int endY   = (int) (size.height * 0.2);   // 20% down the screen

        performSwipe(driver, startX, startY, startX, endY);
    }

    // -------------------------------------------------------
    // Swipe from top to bottom (scroll DOWN the page)
    // -------------------------------------------------------
    public static void swipeDown() {
        AndroidDriver driver = DriverManager.getDriver();
        Dimension size = driver.manage().window().getSize();

        int startX = size.width / 2;
        int startY = (int) (size.height * 0.2);
        int endY   = (int) (size.height * 0.8);

        performSwipe(driver, startX, startY, startX, endY);
    }

    // -------------------------------------------------------
    // Swipe left (go to next item in a carousel)
    // -------------------------------------------------------
    public static void swipeLeft() {
        AndroidDriver driver = DriverManager.getDriver();
        Dimension size = driver.manage().window().getSize();

        int startX = (int) (size.width * 0.8);
        int endX   = (int) (size.width * 0.2);
        int y      = size.height / 2;

        performSwipe(driver, startX, y, endX, y);
    }

    // -------------------------------------------------------
    // Core swipe logic using W3C Pointer Actions
    // -------------------------------------------------------
    private static void performSwipe(AndroidDriver driver,
                                     int startX, int startY,
                                     int endX,   int endY) {

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        Sequence swipe = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(
                        Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(
                        Duration.ofMillis(600), PointerInput.Origin.viewport(), endX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(swipe));
    }
}
