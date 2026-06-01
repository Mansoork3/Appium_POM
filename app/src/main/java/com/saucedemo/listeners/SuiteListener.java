package com.saucedemo.listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * SuiteListener  [LISTENER 6]
 * ---------------
 * Implements ISuiteListener.
 * Runs at the very start and end of the ENTIRE test suite.
 *
 * On start → prints a header banner with suite name and time.
 * On finish → prints a summary: how many tests passed/failed/skipped.
 *
 * This is the "big picture" listener – great for CI log readability.
 */
public class SuiteListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        System.out.println("\n");
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║          SAUCE DEMO MOBILE AUTOMATION SUITE          ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.printf("║  Suite  : %-42s ║%n", suite.getName());
        System.out.printf("║  Started: %-42s ║%n", startTime);
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("[SuiteListener] Suite started: " + suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Count results from all test contexts in this suite
        int passed  = 0;
        int failed  = 0;
        int skipped = 0;

        for (Map.Entry<String, org.testng.ISuiteResult> entry
                : suite.getResults().entrySet()) {

            org.testng.ITestContext ctx = entry.getValue().getTestContext();
            passed  += ctx.getPassedTests().size();
            failed  += ctx.getFailedTests().size();
            skipped += ctx.getSkippedTests().size();
        }

        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║                 SUITE EXECUTION SUMMARY              ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.printf("║  Suite   : %-41s ║%n", suite.getName());
        System.out.printf("║  Finished: %-41s ║%n", endTime);
        System.out.println("║                                                      ║");
        System.out.printf("║  ✅ PASSED : %-39d ║%n", passed);
        System.out.printf("║  ❌ FAILED : %-39d ║%n", failed);
        System.out.printf("║  ⚠️ SKIPPED: %-39d ║%n", skipped);
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();
    }
}
