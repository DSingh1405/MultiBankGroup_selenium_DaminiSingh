package multibank.DaminiSinghAssignment.Utilities;

import java.util.HashSet;
import java.util.Set;

public class FlakySimulator {

    private static final Set<String> firstRunSet = new HashSet<>();

    public static synchronized boolean isFirstRun(String testKey) {
        if (!firstRunSet.contains(testKey)) {
            firstRunSet.add(testKey);
            return true;    // first time → simulate failure
        }
        return false;       // subsequent times → let it pass
    }
}