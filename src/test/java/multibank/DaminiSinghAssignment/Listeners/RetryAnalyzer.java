package multibank.DaminiSinghAssignment.Listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer:
 * ---------------
 * Implements TestNG's retry mechanism for handling flaky tests.
 * 
 * How it works:
 * - A failing test will automatically re-run up to MAX_RETRY times.
 * - In this framework, MAX_RETRY = 1 → meaning at most 1 retry after failure.
 * - Test execution reports will show both attempts, but only final result is recorded.
 * 
 * Usage:
 * Add this to the @Test annotation:
 *      @Test(retryAnalyzer = RetryAnalyzer.class)
 * 
 * When the test fails on first run:
 * - retry(...) returns true → TestNG reruns the same test method again.
 * - If it fails again, retry(...) returns false → TestNG marks it as FAIL.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    // Tracks how many times the test has been retried
    private int attempt = 0;

    // Maximum number of retries allowed for a failed test
    private static final int MAX_RETRY = 1;

    /**
     * Called by TestNG after a test failure.
     * Returns true → rerun test
     * Returns false → stop retrying
     */
    @Override
    public boolean retry(ITestResult result) {

        // Check whether test can be retried
        if (attempt < MAX_RETRY) {
            attempt++;

            System.out.println(
                    "Retrying test: " + result.getName() +
                    " | Attempt: " + attempt + "/" + MAX_RETRY
            );

            return true;   // Trigger retry
        }

        return false;      // No further retries allowed
    }

    /**
     * Utility methods for reporting/debugging.
     * Not required by TestNG but useful when extending the report.
     */
    public int getAttempt() {
        return attempt;
    }

    public int getMaxRetry() {
        return MAX_RETRY;
    }
}
