package com.saucedemo.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.saucedemo.utils.ExtentManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * NegativeTestListener  [LISTENER 8]
 * ----------------------
 * Handles negative test cases correctly.
 *
 * THE PROBLEM:
 *   A negative test (e.g. "login with wrong password") is a test
 *   that EXPECTS the app to reject the user. If the rejection
 *   happens correctly, the test PASSES. But if we write the
 *   assertion as "assert error message is shown", then:
 *     - Error shown    → assertion passes → TestNG marks PASS ✅
 *     - No error shown → assertion fails  → TestNG marks FAIL ❌
 *   That is actually the CORRECT behaviour already.
 *
 * WHAT THIS LISTENER ADDS:
 *   It detects tests in the "negative" group.
 *   If such a test FAILS unexpectedly (the app DID accept bad creds),
 *   it logs a detailed message explaining WHY the negative test failed,
 *   making debugging much easier.
 *
 *   It also logs in the report that this was a NEGATIVE / EXPECTED
 *   FAILURE scenario, making the report easier to read.
 */
public class NegativeTestListener implements ITestListener {

    // The group name that marks a test as a negative test
    private static final String NEGATIVE_GROUP = "negative";

    // -------------------------------------------------------
    // When a test STARTS – label negative tests in the report
    // -------------------------------------------------------
    @Override
    public void onTestStart(ITestResult result) {
        if (isNegativeTest(result)) {
            ExtentTest test = ExtentManager.getTest();
            if (test != null) {
                test.info("🔴 NEGATIVE TEST – This test verifies that the app " +
                          "REJECTS invalid input / unauthorised access.");
            }
            System.out.println("[NegativeTestListener] Negative test started: "
                    + result.getMethod().getMethodName());
        }
    }

    // -------------------------------------------------------
    // When a NEGATIVE test PASSES – confirm the rejection worked
    // -------------------------------------------------------
    @Override
    public void onTestSuccess(ITestResult result) {
        if (isNegativeTest(result)) {
            ExtentTest test = ExtentManager.getTest();
            if (test != null) {
                test.log(Status.PASS,
                         "✅ Negative scenario handled correctly – " +
                         "app rejected the invalid input as expected.");
            }
            System.out.println("[NegativeTestListener] ✅ Negative test PASSED (app rejected correctly): "
                    + result.getMethod().getMethodName());
        }
    }

    // -------------------------------------------------------
    // When a NEGATIVE test FAILS – explain what went wrong
    // -------------------------------------------------------
    @Override
    public void onTestFailure(ITestResult result) {
        if (isNegativeTest(result)) {
            ExtentTest test = ExtentManager.getTest();
            if (test != null) {
                test.log(Status.FAIL,
                         "❌ Negative test failed! The app may have accepted " +
                         "invalid credentials/input when it should have rejected them. " +
                         "Error: " + result.getThrowable().getMessage());
            }
            System.out.println(
                    "[NegativeTestListener] ❌ Negative test FAILED – " +
                    "app did NOT reject invalid input as expected: "
                    + result.getMethod().getMethodName()
                    + " | Reason: " + result.getThrowable().getMessage());
        }
    }

    // -------------------------------------------------------
    // Helper – check if this test belongs to the negative group
    // -------------------------------------------------------
    private boolean isNegativeTest(ITestResult result) {
        for (String group : result.getMethod().getGroups()) {
            if (NEGATIVE_GROUP.equalsIgnoreCase(group)) {
                return true;
            }
        }
        return false;
    }

    // -------------------------------------------------------
    // Unused callbacks
    // -------------------------------------------------------
    @Override public void onTestSkipped(ITestResult result) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onStart(ITestContext context) {}
    @Override public void onFinish(ITestContext context) {}
}
