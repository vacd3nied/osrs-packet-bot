package net.runelite.api;

import java.util.concurrent.Callable;

public class Condition {

    public static boolean wait(final Callable<Boolean> callable, long retryInterval, int retries) {
        for (int i = 0; i < retries; i++) {
            try {
                Thread.sleep(i == 0 ? Math.min(10, retryInterval) : retryInterval);

                if (callable.call()) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    public static boolean wait(final Callable callable, long retryInterval) {
        return wait(callable, retryInterval, Integer.MAX_VALUE);
    }

}
