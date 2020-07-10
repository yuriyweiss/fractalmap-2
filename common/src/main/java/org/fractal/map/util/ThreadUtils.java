package org.fractal.map.util;

public class ThreadUtils {

    private ThreadUtils() {
    }

    public static void sleep( long millis ) {
        try {
            Thread.sleep( millis );
        } catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
    }
}
