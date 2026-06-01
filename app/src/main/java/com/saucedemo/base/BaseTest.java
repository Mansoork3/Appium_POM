package com.saucedemo.base;

import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.DriverManager;
import com.saucedemo.utils.VideoUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.URL;
import java.time.Duration;
/**
 * BaseTest
 * --------
 * All test classes extend BaseTest.
 * It handles:
 *   - Starting the Appium driver before each test  (@BeforeMethod)
 *   - Stopping  the Appium driver after  each test  (@AfterMethod)
 *   - Starting / stopping video recording
 *
 * The Listeners (ExtentReportListener, ScreenshotListener, etc.)
 * hook in AUTOMATICALLY via the TestNG suite XML files.
 * We do NOT need to annotate them here.
 */
public class BaseTest {

    // -------------------------------------------------------
    // @BeforeMethod – runs before EVERY single @Test method
    // -------------------------------------------------------
    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestResult result) throws Exception {

        System.out.println("\n========================================");
        System.out.println("  STARTING TEST: " + result.getMethod().getMethodName());
        System.out.println("========================================");

        // ---- Build Appium options from config.properties ----
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName(ConfigReader.get("platform.name"));
        options.setAutomationName(ConfigReader.get("automation.name"));
        options.setPlatformVersion(ConfigReader.get("platform.version"));
        options.setDeviceName(ConfigReader.get("device.name"));
        options.setUdid(ConfigReader.get("device.udid"));
        options.setAppPackage(ConfigReader.get("app.package"));
        options.setAppActivity(ConfigReader.get("app.activity"));
        options.setNoReset(ConfigReader.getBoolean("app.no.reset"));

        // ---- Start the driver ----
        URL serverUrl = new URL(ConfigReader.get("appium.server.url"));
        AndroidDriver driver = new AndroidDriver(serverUrl, options);
        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicit.wait")));

        // ---- Store driver in DriverManager ----
        DriverManager.setDriver(driver);

        System.out.println("[BaseTest] Application launched successfully.");

        // ---- Short pause to let the app fully load ----
        WaitHelper.pause(3000);

        // ---- Start video recording ----
        VideoUtils.startRecording();
    }

    // -------------------------------------------------------
    // @AfterMethod – runs after EVERY single @Test method
    // -------------------------------------------------------
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        System.out.println("\n[BaseTest] Tearing down after: "
                + result.getMethod().getMethodName());

        // Note: Screenshot and video saving is handled by listeners
        // We just need to stop the driver here

        if (DriverManager.isDriverAlive()) {
            DriverManager.getDriver().quit();
            DriverManager.removeDriver();
            System.out.println("[BaseTest] Driver quit and removed.");
        }

        System.out.println("========================================\n");
    }

    // -------------------------------------------------------
    // Simple inline pause helper (avoids importing WaitUtils)
    // -------------------------------------------------------
    private static class WaitHelper {
        static void pause(long ms) {
            try { Thread.sleep(ms); } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
