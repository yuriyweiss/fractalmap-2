package org.fractal.map.util;

public abstract class StoppableThread extends Thread {
    private boolean needStop = false;
    protected volatile boolean stopped = false;

    protected synchronized boolean canRun() {
        return !needStop;
    }

    public void terminate() {
        synchronized ( this ) {
            needStop = true;
        }
        this.interrupt();
        while ( !stopped ) {
            ThreadUtils.sleep( 25 );
        }
    }
}
