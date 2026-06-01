package com.saucedemo.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.saucedemo.utils.ExtentManager;
import com.saucedemo.utils.ScreenshotUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ISuite;
import org.testng.ISuiteListener;

/**
 * ExtentReportListener  [LISTENER 1]
 * ---------------------
 * Hooks into TestNG lifecycle to build an HTML report using
 * ExtentReports (Spark theme).
 *
 * Registered in:  testng-suites/MasterSuite.xml (and others)
 *
 * What it does:
 *   onStart         → nothing (report is lazily created)
 *   onTestStart     → creates a test node in the report
 *   onTestSuccess   → marks node GREEN, attaches screenshot
 *   onTestFailure   → marks node RED,   attaches screenshot + error
 *   onTestSkipped   → marks node ORANGE
 *   onFinish (suite)→ flushes (writes) the HTML file to disk
 */
public class ExtentReportListener implements ITestListener, ISuiteListener {

    // -------------------------------------------------------
    // ISuiteListener – called once when the whole suite ends
    // -------------------------------------------------------
    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.flushReport();
        System.out.println("[ExtentReportListener] HTML report written to disk.");
    }

    // -------------------------------------------------------
    // ITestListener – called at the START of each @Test
    // -------------------------------------------------------
    @Override
    public void onTestStart(ITestResult result) {
        String testName   = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        // Create a node in the HTML report for this test
        ExtentTest test = (description != null && !description.isEmpty())
                ? ExtentManager.createTest(testName, description)
                : ExtentManager.createTest(testName);

        // Tag the test with its group(s) e.g. "smoke", "login"
        String[] groups = result.getMethod().getGroups();
        for (String group : groups) {
            test.assignCategory(group);
        }

        test.info("Test started: " + testName);
    }

    // -------------------------------------------------------
    // ITestListener – PASS
    // -------------------------------------------------------
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentTest test = ExtentManager.getTest();

        // Take screenshot
        String screenshotPath = ScreenshotUtils.captureOnPass(testName);

        if (test != null) {
            try {
                test.pass("Test PASSED",
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } catch (Exception e) {
                test.pass("Test PASSED (screenshot unavailable)");
            }
        }
        System.out.println("[ExtentReportListener] PASS logged for: " + testName);
    }

    // -------------------------------------------------------
    // ITestListener – FAIL
    // -------------------------------------------------------
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentTest test = ExtentManager.getTest();

        // Take screenshot
        String screenshotPath = ScreenshotUtils.captureOnFailure(testName);

        if (test != null) {
            try {
                test.fail("Test FAILED: " + result.getThrowable().getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                test.fail(result.getThrowable()); // Full stack trace
            } catch (Exception e) {
                test.fail("Test FAILED: " + result.getThrowable().getMessage());
            }
        }
        System.out.println("[ExtentReportListener] FAIL logged for: " + testName);
    }

    // -------------------------------------------------------
    // ITestListener – SKIP
    // -------------------------------------------------------
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentTest test = ExtentManager.getTest();

        if (test != null) {
            test.skip("Test SKIPPED: "
                    + (result.getThrowable() != null
                       ? result.getThrowable().getMessage()
                       : "No reason given"));
        }
        System.out.println("[ExtentReportListener] SKIP logged for: " + testName);
    }

    // -------------------------------------------------------
    // Remaining ITestListener methods (not used here)
    // -------------------------------------------------------
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onStart(org.testng.ITestContext context) {}
    @Override public void onFinish(org.testng.ITestContext context) {}
    @Override public void onStart(ISuite suite) {}
}
