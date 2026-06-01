package com.saucedemo.listeners;

import com.saucedemo.utils.DriverManager;
import com.saucedemo.utils.ScreenshotUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * ScreenshotListener  [LISTENER 2]
 * -------------------
 * Dedicated listener for screenshots.
 * It captures a screenshot after EVERY test (pass AND fail)
 * and prints the saved path to the console.
 *
 * This runs independently of ExtentReportListener so that
 * we still have screenshots even if the report crashes.
 */
public class ScreenshotListener implements ITestListener {

    @Override
    public void onTestSuccess(ITestResult result) {
        if (DriverManager.isDriverAlive()) {
            String path = ScreenshotUtils.captureOnPass(result.getMethod().getMethodName());
            System.out.println("[ScreenshotListener] PASS screenshot → " + path);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (DriverManager.isDriverAlive()) {
            String path = ScreenshotUtils.captureOnFailure(result.getMethod().getMethodName());
            System.out.println("[ScreenshotListener] FAIL screenshot → " + path);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("[ScreenshotListener] Test skipped – no screenshot taken: "
                + result.getMethod().getMethodName());
    }

    // -------------------------------------------------------
    // Unused callbacks (required by interface)
    // -------------------------------------------------------
    @Override public void onTestStart(ITestResult result) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onStart(org.testng.ITestContext context) {}
    @Override public void onFinish(org.testng.ITestContext context) {}
}
