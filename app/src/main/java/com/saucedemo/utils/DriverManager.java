package com.saucedemo.utils;

import io.appium.java_client.android.AndroidDriver;

/**
 * DriverManager
 * -------------
 * Stores the AndroidDriver in a ThreadLocal so that parallel
 * test execution is safe (each thread has its own driver copy).
 *
 * Lifecycle:
 *   BaseTest.setUp()     → DriverManager.setDriver(driver)
 *   BaseTest.tearDown()  → DriverManager.removeDriver()
 *   Everywhere else      → DriverManager.getDriver()
 */
public class DriverManager {

    // ThreadLocal means each thread has its own driver instance
    private static final ThreadLocal<AndroidDriver> driverThreadLocal = new ThreadLocal<>();

    // -------------------------------------------------------
    // Store the driver for the current thread
    // -------------------------------------------------------
    public static void setDriver(AndroidDriver driver) {
        driverThreadLocal.set(driver);
    }

    // -------------------------------------------------------
    // Get the driver for the current thread
    // -------------------------------------------------------
    public static AndroidDriver getDriver() {
        AndroidDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "[DriverManager] Driver has not been initialized for this thread. " +
                    "Make sure BaseTest.setUp() ran before calling getDriver().");
        }
        return driver;
    }

    // -------------------------------------------------------
    // Clean up – always call this in @AfterMethod
    // -------------------------------------------------------
    public static void removeDriver() {
        driverThreadLocal.remove();
    }

    // -------------------------------------------------------
    // Null-safe check
    // -------------------------------------------------------
    public static boolean isDriverAlive() {
        return driverThreadLocal.get() != null;
    }
}
