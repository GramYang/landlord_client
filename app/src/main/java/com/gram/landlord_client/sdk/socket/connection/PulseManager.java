package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.socket.exception.DogDeadException;
import com.gram.landlord_client.sdk.socket.interfaces.IConnectionManager;
import com.gram.landlord_client.sdk.socket.interfaces.IPulse;
import com.gram.landlord_client.sdk.socket.interfaces.IPulseSendable;
import com.gram.landlord_client.sdk.socket.util.AbsLoopThread;

import java.util.concurrent.atomic.AtomicInteger;

public class PulseManager implements IPulse {
    private volatile IConnectionManager mManager;
    private IPulseSendable mSendable;
    private volatile OkSocketOptions mOkOptions;
    private volatile long mCurrentFrequency;
    private volatile OkSocketOptions.IOThreadMode mCurrentThreadMode;
    private volatile boolean isDead = false;
    private volatile AtomicInteger mLoseTimes = new AtomicInteger(-1);
    private PulseManager.PulseThread mPulseThread = new PulseManager.PulseThread();

    PulseManager(IConnectionManager manager, OkSocketOptions okOptions) {
        mManager = manager;
        mOkOptions = okOptions;
        mCurrentThreadMode = mOkOptions.getIOThreadMode();
//        Logger.i("PulseManager init success");
    }

    public synchronized IPulse setPulseSendable(IPulseSendable sendable) {
//        Logger.i("心跳包已填装");
        if (sendable != null) {
            mSendable = sendable;
        }

        return this;
    }

    public IPulseSendable getPulseSendable() {
        return mSendable;
    }

    public synchronized void pulse() {
//        Logger.i("开始心跳");
        privateDead();
        updateFrequency();
        if (mCurrentThreadMode != OkSocketOptions.IOThreadMode.SIMPLEX && mPulseThread.isShutdown()) {
            mPulseThread.start();
        }

    }

    public synchronized void trigger() {
        if (!isDead) {
            if (mCurrentThreadMode != OkSocketOptions.IOThreadMode.SIMPLEX && mManager != null && mSendable != null) {
                mManager.send(mSendable);
            }

        }
    }

    public synchronized void dead() {
        mLoseTimes.set(0);
        isDead = true;
        privateDead();
    }

    private synchronized void updateFrequency() {
        if (mCurrentThreadMode != OkSocketOptions.IOThreadMode.SIMPLEX) {
            mCurrentFrequency = mOkOptions.getPulseFrequency();
        } else {
            privateDead();
        }

    }

    public synchronized void feed() {
        mLoseTimes.set(-1);
    }

    private void privateDead() {
        if (mPulseThread != null) {
            mPulseThread.shutdown();
        }

    }

    public int getLoseTimes() {
        return mLoseTimes.get();
    }

    protected synchronized void setOkOptions(OkSocketOptions okOptions) {
        mOkOptions = okOptions;
        mCurrentThreadMode = mOkOptions.getIOThreadMode();
        updateFrequency();
    }

    private class PulseThread extends AbsLoopThread {
        private PulseThread() {
        }

        protected void runInLoopThread() throws Exception {
//            Logger.i("心跳线程启动");
            if (isDead) {
                shutdown();
            } else {
                if (mManager != null && mSendable != null) {
                    if (mOkOptions.getPulseFeedLoseTimes() != -1 && mLoseTimes.incrementAndGet() >= mOkOptions.getPulseFeedLoseTimes()) {
                        mManager.disconnect(new DogDeadException("you need feed dog on time,otherwise he will die"));
                    } else {
                        mManager.send(mSendable);
                    }
                }

                Thread.sleep(mCurrentFrequency);
            }
        }

        protected void loopFinish(Exception e) {
        }
    }
}
