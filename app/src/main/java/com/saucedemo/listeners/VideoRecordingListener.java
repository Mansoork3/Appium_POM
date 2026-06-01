package com.saucedemo.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.saucedemo.utils.ExtentManager;
import com.saucedemo.utils.VideoUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * VideoRecordingListener  [LISTENER 4]
 * ------------------------
 * Stops the screen recording after each test and saves the MP4.
 * Video recording is STARTED in BaseTest.setUp() via VideoUtils.
 *
 * On pass → saved to reports/videos/passed/
 * On fail → saved to reports/videos/failed/
 *
 * The video path is also logged to the ExtentReport if available.
 */
public class VideoRecordingListener implements ITestListener {

    @Override
    public void onTestSuccess(ITestResult result) {
        saveVideo(result, "PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        saveVideo(result, "FAILED");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Stop recording even on skip to avoid resource leak
        VideoUtils.stopAndSaveRecording(
                result.getMethod().getMethodName(), "SKIPPED");
    }

    // -------------------------------------------------------
    // Core save logic
    // -------------------------------------------------------
    private void saveVideo(ITestResult result, String status) {
        String testName  = result.getMethod().getMethodName();
        String videoPath = VideoUtils.stopAndSaveRecording(testName, status);

        System.out.println("[VideoRecordingListener] Video saved for '"
                + testName + "' → " + videoPath);

        // Log the video path in the HTML report as well
        ExtentTest test = ExtentManager.getTest();
        if (test != null && !videoPath.isEmpty()) {
            test.info("Video recording: " + videoPath);
        }
    }

    // -------------------------------------------------------
    // Unused callbacks
    // -------------------------------------------------------
    @Override public void onTestStart(ITestResult result) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onStart(org.testng.ITestContext context) {}
    @Override public void onFinish(org.testng.ITestContext context) {}
}
