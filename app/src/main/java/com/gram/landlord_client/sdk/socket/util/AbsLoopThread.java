package com.gram.landlord_client.sdk.socket.util;

public abstract class AbsLoopThread implements Runnable {
    public volatile Thread thread = null;

    protected volatile String threadName = "";

    private volatile boolean isStop = false;

    private volatile boolean isShutdown = true;

    private volatile Exception ioException = null;

    private volatile long loopTimes = 0;

    public AbsLoopThread() {
        isStop = true;
        threadName = getClass().getSimpleName();
    }

    public AbsLoopThread(String name) {
        isStop = true;
        threadName = name;
    }

    public synchronized void start() {
        if (isStop) {
            thread = new Thread(this, threadName);
            isStop = false;
            loopTimes = 0;
            thread.start();
        }
    }

    @Override
    public final void run() {
        try {
            isShutdown = false;
            beforeLoop();
            while (!isStop) {
                runInLoopThread();
                loopTimes++;
            }
        } catch (Exception e) {
            if (ioException == null) {
                ioException = e;
            }
        } finally {
            isShutdown = true;
            loopFinish(ioException);
            ioException = null;
        }
    }

    public long getLoopTimes() {
        return loopTimes;
    }

    public String getThreadName() {
        return threadName;
    }

    protected void beforeLoop() throws Exception {

    }

    protected abstract void runInLoopThread() throws Exception;

    protected abstract void loopFinish(Exception e);

    public synchronized void shutdown() {
        if (thread != null && !isStop) {
            isStop = true;
            thread.interrupt();
            thread = null;
        }
    }

    public synchronized void shutdown(Exception e) {
        ioException = e;
        shutdown();
    }

    public boolean isShutdown() {
        return isShutdown;
    }

}
