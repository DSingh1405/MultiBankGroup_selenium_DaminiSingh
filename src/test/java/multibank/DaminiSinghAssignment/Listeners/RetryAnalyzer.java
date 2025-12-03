package multibank.DaminiSinghAssignment.Listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int attempt = 0;
    private static final int MAX_RETRY = 1; // retry once

    @Override
    public boolean retry(ITestResult result) {

        if (attempt < MAX_RETRY) {
            attempt++;
            System.out.println("Retrying test: " + result.getName()
                    + " | Attempt: " + attempt + "/" + MAX_RETRY);
            return true;
        }

        return false;
    }

    // ---------- ADD THESE TWO METHODS ----------
    public int getAttempt() {
        return attempt;
    }

    public int getMaxRetry() {
        return MAX_RETRY;
    }
}
