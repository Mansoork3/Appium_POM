package com.saucedemo.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

/**
 * ExtentManager
 * -------------
 * Creates and manages a single ExtentReports instance.
 * Also keeps a ThreadLocal<ExtentTest> so parallel tests
 * each write to their own node in the report.
 *
 * Usage:
 *   ExtentManager.getInstance()               → get the report
 *   ExtentManager.createTest("Login Test")    → start a test node
 *   ExtentManager.getTest()                   → get current thread's test
 *   ExtentManager.flushReport()               → write HTML to disk
 */
public class ExtentManager {

    // The single ExtentReports instance
    private static ExtentReports extentReports;

    // Each test thread gets its own ExtentTest object
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    // -------------------------------------------------------
    // Get (or create) the ExtentReports instance
    // -------------------------------------------------------
    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            extentReports = createExtentReports();
        }
        return extentReports;
    }

    // -------------------------------------------------------
    // Build the report – called once at suite start
    // -------------------------------------------------------
    private static ExtentReports createExtentReports() {

        String reportPath = ConfigReader.get("report.path");

        // Create parent directories if they don't exist
        new File(reportPath).getParentFile().mkdirs();

        // ExtentSparkReporter generates a beautiful HTML file
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle("Sauce Demo - Automation Report");
        sparkReporter.config().setReportName("Mobile Automation Test Results");
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setEncoding("utf-8");
        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // System / environment info shown at the top of the report
        extent.setSystemInfo("Application",   "Sauce Demo Mobile (React Native)");
        extent.setSystemInfo("Platform",      ConfigReader.get("platform.name"));
        extent.setSystemInfo("Device",        ConfigReader.get("device.name"));
        extent.setSystemInfo("OS Version",    ConfigReader.get("platform.version"));
        extent.setSystemInfo("Automation",    ConfigReader.get("automation.name"));
        extent.setSystemInfo("Tester",        System.getProperty("user.name"));

        return extent;
    }

    // -------------------------------------------------------
    // Create a new test node in the report
    // -------------------------------------------------------
    public static ExtentTest createTest(String testName) {
        ExtentTest test = getInstance().createTest(testName);
        extentTestThreadLocal.set(test);
        return test;
    }

    // -------------------------------------------------------
    // Create a test with a description
    // -------------------------------------------------------
    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        extentTestThreadLocal.set(test);
        return test;
    }

    // -------------------------------------------------------
    // Get this thread's test node
    // -------------------------------------------------------
    public static ExtentTest getTest() {
        return extentTestThreadLocal.get();
    }

    // -------------------------------------------------------
    // Write the HTML report to disk – call at suite end
    // -------------------------------------------------------
    public static synchronized void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
            System.out.println("[ExtentManager] Report flushed to: "
                    + ConfigReader.get("report.path"));
        }
    }
}
