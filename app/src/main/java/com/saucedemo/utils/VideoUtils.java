package com.saucedemo.utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.screenrecording.CanRecordScreen;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 * VideoUtils
 * ----------
 * Uses Appium's built-in screen recording API.
 * Appium server records the screen; when we stop, we get
 * a Base64-encoded MP4 back and save it to disk.
 *
 * Saved under:
 *   reports/videos/passed/  – passing tests
 *   reports/videos/failed/  – failing tests
 */
public class VideoUtils {

    private static final String VIDEO_BASE_PATH =
            ConfigReader.get("video.path");

    // Flag so we know if recording is active
    private static boolean isRecording = false;

    // -------------------------------------------------------
    // Start recording
    // -------------------------------------------------------
    public static void startRecording() {
        try {
            AndroidDriver driver = DriverManager.getDriver();
            ((CanRecordScreen) driver).startRecordingScreen();
            isRecording = true;
            System.out.println("[VideoUtils] Screen recording started.");
        } catch (Exception e) {
            System.err.println("[VideoUtils] Could not start recording: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // Stop recording and save the video file
    // Returns the path to the saved file (or empty string on error)
    // -------------------------------------------------------
    public static String stopAndSaveRecording(String testName, String status) {
        if (!isRecording) {
            System.out.println("[VideoUtils] No active recording to stop.");
            return "";
        }

        try {
            AndroidDriver driver = DriverManager.getDriver();

            // Appium returns a Base64-encoded MP4
            String base64Video = ((CanRecordScreen) driver).stopRecordingScreen();
            isRecording = false;

            // Decode and save
            byte[] videoBytes = Base64.getDecoder().decode(base64Video);

            String timestamp  = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName   = testName + "_" + timestamp + ".mp4";
            String subFolder  = status.equalsIgnoreCase("PASSED") ? "passed" : "failed";
            String fullPath   = VIDEO_BASE_PATH + subFolder + File.separator + fileName;

            File destFile = new File(fullPath);
            FileUtils.forceMkdirParent(destFile);
            FileUtils.writeByteArrayToFile(destFile, videoBytes);

            System.out.println("[VideoUtils] Video saved: " + fullPath);
            return destFile.getAbsolutePath();

        } catch (Exception e) {
            System.err.println("[VideoUtils] Failed to save video: " + e.getMessage());
            isRecording = false;
            return "";
        }
    }
}
