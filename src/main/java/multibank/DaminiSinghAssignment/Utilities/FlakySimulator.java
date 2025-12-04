package multibank.DaminiSinghAssignment.Utilities;

import java.util.HashSet;
import java.util.Set;

/**
 * FlakySimulator
 * --------------------------
 * Utility class used to simulate flaky test behavior.
 *
 * Purpose:
 *  - Demonstrates TestNG's retry mechanism using RetryAnalyzer.
 *  - Forces a controlled failure the *first time* a test runs,
 *    and allows it to pass on subsequent retries.
 *
 * How it works:
 *  - A static Set (`firstRunSet`) stores test identifiers.
 *  - When `isFirstRun(testKey)` is called:
 *        → If the key is *not* in the set → this is the first run → return TRUE
 *        → Add key to the set to mark that the first run is done
 *        → If the key already exists → return FALSE (not first run)
 *
 * Thread Safety:
 *  - `synchronized` ensures no concurrency issues when tests run in parallel.
 *
 */
public class FlakySimulator {

    // Stores identifiers of tests that have already executed once
    private static final Set<String> firstRunSet = new HashSet<>();

    /**
     * Checks whether the given test key is being executed for the first time.
     *
     * @param testKey Unique identifier for a test (e.g., test method name)
     * @return true  → first execution (simulate failure)
     *         false → subsequent execution (allow pass)
     */
    public static synchronized boolean isFirstRun(String testKey) {

        // First run → mark and return true
        if (!firstRunSet.contains(testKey)) {
            firstRunSet.add(testKey);
            return true;
        }

        // Not first run → no flaky behavior now
        return false;
    }
}
