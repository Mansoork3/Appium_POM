package com.saucedemo.listeners;

import com.saucedemo.utils.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryListener  [LISTENER 3]
 * ---------------
 * Implements IRetryAnalyzer.
 * When a test FAILS, TestNG asks this class: "should I retry?"
 * We retry up to retry.count times (configured in config.properties).
 *
 * Usage: annotate your @Test method OR apply via RetryTransformer.
 *   @Test(retryAnalyzer = RetryListener.class)
 *
 * The retry count is read from config.properties:
 *   retry.count=1
 */
public class RetryListener implements IRetryAnalyzer {

    // Current retry attempt for this test instance
    private int currentRetryCount = 0;

    // Maximum retries from config (default 1)
    private static final int MAX_RETRY_COUNT;

    static {
        int count = 1;
        try {
            count = ConfigReader.getInt("retry.count");
        } catch (Exception e) {
            // Use default if config not found
        }
        MAX_RETRY_COUNT = count;
    }

    // -------------------------------------------------------
    // Called by TestNG after a test failure
    // Returns true  → retry the test
    // Returns false → don't retry, mark as failed
    // -------------------------------------------------------
    @Override
    public boolean retry(ITestResult result) {
        if (currentRetryCount < MAX_RETRY_COUNT) {
            currentRetryCount++;
            System.out.println(
                    "[RetryListener] Retrying test '"
                    + result.getMethod().getMethodName()
                    + "' (attempt " + currentRetryCount + " of " + MAX_RETRY_COUNT + ")");
            return true;
        }
        return false;
    }
}
