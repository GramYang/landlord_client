package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.socket.exception.ManuallyDisconnectException;
import com.gram.landlord_client.sdk.socket.util.AbsLoopThread;
import com.gram.landlord_client.sdk.socket.util.ThreadUtils;
import com.orhanobut.logger.Logger;

import java.util.Iterator;

public class DefaultReconnectManager extends AbsReconnectionManager {
    private static final int MAX_CONNECTION_FAILED_TIMES = 12;
    private int mConnectionFailedTimes = 0;
    private volatile DefaultReconnectManager.ReconnectTestingThread mReconnectTestingThread = new DefaultReconnectManager.ReconnectTestingThread();

    public DefaultReconnectManager() {
    }

    public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
        if (isNeedReconnect(e)) {
            reconnectDelay();
        } else {
            resetThread();
        }

    }

    public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
        resetThread();
    }

    public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
        if (e != null) {
            ++mConnectionFailedTimes;
            if (mConnectionFailedTimes > MAX_CONNECTION_FAILED_TIMES) {
                resetThread();
                ConnectionInfo originInfo = mConnectionManager.getRemoteConnectionInfo();
                ConnectionInfo backupInfo = originInfo.getBackupInfo();
                if (backupInfo != null) {
                    ConnectionInfo bbInfo = new ConnectionInfo(originInfo.getIp(), originInfo.getPort());
                    backupInfo.setBackupInfo(bbInfo);
                    if (!mConnectionManager.isConnect()) {
                        Logger.d("Prepare switch to the backup line " + backupInfo.getIp() + ":" + backupInfo.getPort() + " ...");
                        synchronized(mConnectionManager) {
                            mConnectionManager.switchConnectionInfo(backupInfo);
                        }

                        reconnectDelay();
                    }
                } else {
                    reconnectDelay();
                }
            } else {
                reconnectDelay();
            }
        }

    }

    private boolean isNeedReconnect(Exception e) {
        synchronized(mIgnoreDisconnectExceptionList) {
            if (e != null && !(e instanceof ManuallyDisconnectException)) {
                Iterator it = mIgnoreDisconnectExceptionList.iterator();

                Class classException;
                do {
                    if (!it.hasNext()) {
                        return true;
                    }

                    classException = (Class)it.next();
                } while(!classException.isAssignableFrom(e.getClass()));

                return false;
            } else {
                return false;
            }
        }
    }

    private synchronized void resetThread() {
        if (mReconnectTestingThread != null) {
            mReconnectTestingThread.shutdown();
        }

    }

    private void reconnectDelay() {
        synchronized(mReconnectTestingThread) {
            if (mReconnectTestingThread.isShutdown()) {
                mReconnectTestingThread.start();
            }

        }
    }

    public void detach() {
        super.detach();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return o != null && getClass() == o.getClass();
        }
    }

    private class ReconnectTestingThread extends AbsLoopThread {
        private long mReconnectTimeDelay;

        private ReconnectTestingThread() {
            mReconnectTimeDelay = 10000L;
        }

        protected void beforeLoop() throws Exception {
            super.beforeLoop();
            if (mReconnectTimeDelay < (long)(mConnectionManager.getOption().getConnectTimeoutSecond() * 1000)) {
                mReconnectTimeDelay = (long)(mConnectionManager.getOption().getConnectTimeoutSecond() * 1000);
            }

        }

        protected void runInLoopThread() throws Exception {
            if (mDetach) {
                Logger.d("ReconnectionManager already detached by framework.We decide gave up this reconnection mission!");
                shutdown();
            } else {
                Logger.d("Reconnect after " + mReconnectTimeDelay + " mills ...");
                ThreadUtils.sleep(mReconnectTimeDelay);
                if (mDetach) {
                    Logger.d("ReconnectionManager already detached by framework.We decide gave up this reconnection mission!");
                    shutdown();
                } else if (mConnectionManager.isConnect()) {
                    shutdown();
                } else {
                    boolean isHolden = mConnectionManager.getOption().isConnectionHolden();
                    if (!isHolden) {
                        detach();
                        shutdown();
                    } else {
                        ConnectionInfo info = mConnectionManager.getRemoteConnectionInfo();
                        Logger.i("Reconnect the server " + info.getIp() + ":" + info.getPort() + " ...");
                        synchronized(mConnectionManager) {
                            if (!mConnectionManager.isConnect()) {
                                mConnectionManager.connect();
                            } else {
                                shutdown();
                            }

                        }
                    }
                }
            }
        }

        protected void loopFinish(Exception e) {
        }
    }
}
