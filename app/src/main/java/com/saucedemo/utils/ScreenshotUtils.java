package com.saucedemo.utils;

import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtils
 * ---------------
 * Takes a screenshot and saves it under:
 *   reports/screenshots/passed/  – when test passes
 *   reports/screenshots/failed/  – when test fails
 *
 * Returns the absolute file path so ExtentReportListener
 * can embed it in the HTML report.
 */
public class ScreenshotUtils {

    private static final String SCREENSHOT_BASE_PATH =
            ConfigReader.get("screenshot.path");

    // -------------------------------------------------------
    // Capture a screenshot and return its file path
    // -------------------------------------------------------
    public static String captureScreenshot(String testName, String status) {

        AndroidDriver driver = DriverManager.getDriver();

        // Build timestamp string for unique filenames
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName  = testName + "_" + timestamp + ".png";

        // Choose sub-folder based on pass/fail status
        String subFolder = status.equalsIgnoreCase("PASSED") ? "passed" : "failed";
        String fullPath  = SCREENSHOT_BASE_PATH + subFolder + File.separator + fileName;

        try {
            // Take screenshot as a temp file
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Copy to our reports folder
            File destFile = new File(fullPath);
            FileUtils.forceMkdirParent(destFile);  // Create directories if missing
            FileUtils.copyFile(srcFile, destFile);

            System.out.println("[ScreenshotUtils] Screenshot saved: " + fullPath);
            return destFile.getAbsolutePath();

        } catch (IOException e) {
            System.err.println("[ScreenshotUtils] Failed to save screenshot: " + e.getMessage());
            return "";
        }
    }

    // -------------------------------------------------------
    // Capture screenshot on test failure (convenience method)
    // -------------------------------------------------------
    public static String captureOnFailure(String testName) {
        return captureScreenshot(testName, "FAILED");
    }

    // -------------------------------------------------------
    // Capture screenshot on test pass (convenience method)
    // -------------------------------------------------------
    public static String captureOnPass(String testName) {
        return captureScreenshot(testName, "PASSED");
    }
}
