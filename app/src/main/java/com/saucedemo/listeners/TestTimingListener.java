package com.saucedemo.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.saucedemo.utils.ExtentManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.HashMap;
import java.util.Map;

/**
 * TestTimingListener  [LISTENER 7]
 * --------------------
 * Tracks how long each test takes to run.
 * Flags any test that takes longer than a threshold as "SLOW".
 *
 * Useful for:
 *   - Spotting tests that are too slow (maybe Thread.sleep abuse)
 *   - Performance regression detection
 *
 * Threshold: 60 seconds (configurable via SLOW_THRESHOLD_MS)
 */
public class TestTimingListener implements ITestListener {

    // Any test exceeding this duration (ms) is flagged as slow
    private static final long SLOW_THRESHOLD_MS = 60_000L;

    // Track start time per test method name
    private final Map<String, Long> startTimes = new HashMap<>();

    // -------------------------------------------------------
    // Record the start time when the test begins
    // -------------------------------------------------------
    @Override
    public void onTestStart(ITestResult result) {
        String key = buildKey(result);
        startTimes.put(key, System.currentTimeMillis());
    }

    // -------------------------------------------------------
    // On pass – print duration and flag if slow
    // -------------------------------------------------------
    @Override
    public void onTestSuccess(ITestResult result) {
        reportTiming(result, "PASSED");
    }

    // -------------------------------------------------------
    // On fail – print duration
    // -------------------------------------------------------
    @Override
    public void onTestFailure(ITestResult result) {
        reportTiming(result, "FAILED");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("[TestTimingListener] SKIPPED: " + buildKey(result));
    }

    // -------------------------------------------------------
    // Called at the end of a test context – print overall stats
    // -------------------------------------------------------
    @Override
    public void onFinish(ITestContext context) {
        long total = context.getEndDate().getTime() - context.getStartDate().getTime();
        System.out.println("[TestTimingListener] Total execution time for '"
                + context.getName() + "': " + formatDuration(total));
    }

    // -------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------
    private void reportTiming(ITestResult result, String status) {
        String key       = buildKey(result);
        long   startTime = startTimes.getOrDefault(key, result.getStartMillis());
        long   duration  = System.currentTimeMillis() - startTime;

        String formatted = formatDuration(duration);
        boolean isSlow   = duration > SLOW_THRESHOLD_MS;

        String message = "[TestTimingListener] " + status + " | "
                + key + " | Duration: " + formatted
                + (isSlow ? " ⚠️  SLOW TEST!" : "");

        System.out.println(message);

        // Add timing info to the ExtentReport node
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.info("Execution time: " + formatted + (isSlow ? " (SLOW)" : ""));
        }
    }

    private String buildKey(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName()
               + "." + result.getMethod().getMethodName();
    }

    private String formatDuration(long ms) {
        if (ms < 1000) return ms + "ms";
        return String.format("%.2fs", ms / 1000.0);
    }

    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onStart(ITestContext context) {}
}
