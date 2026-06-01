package com.saucedemo.listeners;

import com.saucedemo.utils.ConfigReader;
import org.apache.commons.io.FileUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestLogListener  [LISTENER 5]
 * -----------------
 * Writes structured log lines to:
 *   reports/logs/execution.log
 *
 * Every test start, end, pass, fail, and skip is recorded
 * with a timestamp so you can review the run history.
 */
public class TestLogListener implements ITestListener {

    private static final String LOG_PATH = "reports/logs/execution.log";
    private static PrintWriter logWriter;

    // -------------------------------------------------------
    // Suite / context start – open the log file
    // -------------------------------------------------------
    @Override
    public void onStart(ITestContext context) {
        try {
            File logFile = new File(LOG_PATH);
            FileUtils.forceMkdirParent(logFile);
            logWriter = new PrintWriter(new FileWriter(logFile, true)); // append mode
            writeLog("====== TEST SUITE STARTED: " + context.getSuite().getName()
                     + " | " + context.getName() + " ======");
        } catch (IOException e) {
            System.err.println("[TestLogListener] Could not open log file: " + e.getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        writeLog("====== TEST SUITE FINISHED: " + context.getName()
                 + " | PASSED=" + context.getPassedTests().size()
                 + " FAILED=" + context.getFailedTests().size()
                 + " SKIPPED=" + context.getSkippedTests().size()
                 + " ======");
        if (logWriter != null) {
            logWriter.flush();
            logWriter.close();
        }
    }

    // -------------------------------------------------------
    // Per-test hooks
    // -------------------------------------------------------
    @Override
    public void onTestStart(ITestResult result) {
        writeLog("[START]   " + buildTestLabel(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        writeLog("[PASS]    " + buildTestLabel(result)
                 + " | Duration: " + getDuration(result) + "ms");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        writeLog("[FAIL]    " + buildTestLabel(result)
                 + " | Duration: " + getDuration(result) + "ms"
                 + " | Error: " + result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        writeLog("[SKIP]    " + buildTestLabel(result));
    }

    // -------------------------------------------------------
    // Helpers
    // -------------------------------------------------------
    private void writeLog(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String line = "[" + timestamp + "] " + message;
        System.out.println(line);
        if (logWriter != null) {
            logWriter.println(line);
            logWriter.flush();
        }
    }

    private String buildTestLabel(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName()
               + "." + result.getMethod().getMethodName();
    }

    private long getDuration(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }

    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
}
