package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.socket.exception.ManuallyDisconnectException;
import com.gram.landlord_client.sdk.socket.exception.UnConnectException;
import com.gram.landlord_client.sdk.socket.interfaces.IConnectionManager;
import com.gram.landlord_client.sdk.socket.interfaces.IIOManager;
import com.gram.landlord_client.sdk.socket.interfaces.ISendable;
import com.gram.landlord_client.sdk.socket.util.TextUtils;
import com.orhanobut.logger.Logger;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.concurrent.Semaphore;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class ConnectionManagerImpl extends AbsConnectionManager {
    private volatile Socket mSocket;
    private volatile OkSocketOptions mOptions;
    private IIOManager mManager;
    private Thread mConnectThread;
    private ActionHandler mActionHandler;
    private volatile PulseManager mPulseManager;
    private volatile AbsReconnectionManager mReconnectionManager;
    private volatile boolean isConnectionPermitted;
    private volatile boolean isDisconnecting;
    private Semaphore s;

    protected ConnectionManagerImpl(ConnectionInfo info) {
        this(info, (ConnectionInfo)null);
    }

    public ConnectionManagerImpl(ConnectionInfo remoteInfo, ConnectionInfo localInfo) {
        super(remoteInfo, localInfo);
        isConnectionPermitted = true;
        isDisconnecting = false;
        String ip = "";
        String port = "";
        if (remoteInfo != null) {
            ip = remoteInfo.getIp();
            port = remoteInfo.getPort() + "";
        }
        Logger.d("block connection init with:" + ip + ":" + port);
        if (localInfo != null) {
            Logger.d("binding local addr:" + localInfo.getIp() + " port:" + localInfo.getPort());
        }
        s = new Semaphore(1);
    }

    public synchronized void connect() {
        Logger.d("Thread name:" + Thread.currentThread().getName() + " id:" + Thread.currentThread().getId());
        if (isConnectionPermitted) {
            isConnectionPermitted = false;
            if (!isConnect()) {
                isDisconnecting = false;
                if (mRemoteConnectionInfo == null) {
                    isConnectionPermitted = true;
                    throw new UnConnectException("连接参数为空,检查连接参数");
                } else {
                    if (mActionHandler != null) {
                        mActionHandler.detach(this);
                        Logger.d("mActionHandler is detached.");
                    }

                    mActionHandler = new ActionHandler();
                    mActionHandler.attach(this, this);
                    Logger.d("mActionHandler is attached.");
                    if (mReconnectionManager != null) {
                        mReconnectionManager.detach();
                        Logger.d("ReconnectionManager is detached.");
                    }

                    mReconnectionManager = mOptions.getReconnectionManager();
                    if (mReconnectionManager != null) {
                        mReconnectionManager.attach(this);
                        Logger.d("ReconnectionManager is attached.");
                    }

                    String info = mRemoteConnectionInfo.getIp() + ":" + mRemoteConnectionInfo.getPort();
                    mConnectThread = new ConnectionManagerImpl.ConnectionThread(" Connect thread for " + info);
                    mConnectThread.setDaemon(true);
                    mConnectThread.start();
                }
            }
        }
    }

    private synchronized Socket getSocketByConfig() throws Exception {
        if (mOptions.getOkSocketFactory() != null) {
            return mOptions.getOkSocketFactory().createSocket(mRemoteConnectionInfo, mOptions);
        } else {
            OkSocketSSLConfig config = mOptions.getSSLConfig();
            if (config == null) {
                return new Socket();
            } else {
                SSLSocketFactory factory = config.getCustomSSLFactory();
                if (factory == null) {
                    String protocol = "SSL";
                    if (!TextUtils.isEmpty(config.getProtocol())) {
                        protocol = config.getProtocol();
                    }

                    TrustManager[] trustManagers = config.getTrustManagers();
                    if (trustManagers == null || trustManagers.length == 0) {
                        trustManagers = new TrustManager[]{new DefaultX509ProtocolTrustManager()};
                    }

                    try {
                        SSLContext sslContext = SSLContext.getInstance(protocol);
                        sslContext.init(config.getKeyManagers(), trustManagers, new SecureRandom());
                        return sslContext.getSocketFactory().createSocket();
                    } catch (Exception var6) {
                        if (mOptions.isDebug()) {
                            var6.printStackTrace();
                        }

                        Logger.e(var6.getMessage());
                        return new Socket();
                    }
                } else {
                    try {
                        return factory.createSocket();
                    } catch (IOException var7) {
                        if (mOptions.isDebug()) {
                            var7.printStackTrace();
                        }

                        Logger.e(var7.getMessage());
                        return new Socket();
                    }
                }
            }
        }
    }

    private void resolveManager() throws IOException {
        mPulseManager = new PulseManager(this, mOptions);
        mManager = new IOThreadManager(mSocket.getInputStream(), mSocket.getOutputStream(), mOptions, mActionDispatcher);
        mManager.startEngine();
    }

    public void disconnect(Exception exception) {
        synchronized(this) {
            if (isDisconnecting) {
                return;
            }

            isDisconnecting = true;
            if (mPulseManager != null) {
                mPulseManager.dead();
                mPulseManager = null;
            }
        }

        if (exception instanceof ManuallyDisconnectException && mReconnectionManager != null) {
            mReconnectionManager.detach();
            Logger.d("ReconnectionManager is detached.");
        }

        synchronized(this) {
            String info = mRemoteConnectionInfo.getIp() + ":" + mRemoteConnectionInfo.getPort();
            ConnectionManagerImpl.DisconnectThread thread = new ConnectionManagerImpl.DisconnectThread(exception, "Disconnect Thread for " + info);
            thread.setDaemon(true);
            thread.start();
        }
    }

    public void disconnect() {
        disconnect(new ManuallyDisconnectException());
    }

    public synchronized void send(ISendable sendable) {
        if (mManager != null && sendable != null && isConnect()) {
            mManager.send(sendable);
        }
    }

    public void sendAfterConnect(ISendable sendable) {
        new ConnectionManagerImpl.AsyncSendThread(sendable).start();
    }

    public IConnectionManager option(OkSocketOptions okOptions) {
        if (okOptions == null) {
            return this;
        } else {
            mOptions = okOptions;
            if (mManager != null) {
                mManager.setOkOptions(mOptions);
            }

            if (mPulseManager != null) {
                mPulseManager.setOkOptions(mOptions);
            }

            if (mReconnectionManager != null && !mReconnectionManager.equals(mOptions.getReconnectionManager())) {
                if (mReconnectionManager != null) {
                    mReconnectionManager.detach();
                }

                Logger.d("reconnection manager is replaced");
                mReconnectionManager = mOptions.getReconnectionManager();
                mReconnectionManager.attach(this);
            }

            return this;
        }
    }

    public OkSocketOptions getOption() {
        return mOptions;
    }

    public boolean isConnect() {
        if (mSocket == null) {
            return false;
        } else {
            return mSocket.isConnected() && !mSocket.isClosed();
        }
    }

    public boolean isDisconnecting() {
        return isDisconnecting;
    }

    public PulseManager getPulseManager() {
        return mPulseManager;
    }

    public void setIsConnectionHolder(boolean isHold) {
        mOptions = (new OkSocketOptions.Builder(mOptions)).setConnectionHolden(isHold).build();
    }

    public AbsReconnectionManager getReconnectionManager() {
        return mOptions.getReconnectionManager();
    }

    public ConnectionInfo getLocalConnectionInfo() {
        ConnectionInfo local = super.getLocalConnectionInfo();
        if (local == null && isConnect()) {
            InetSocketAddress address = (InetSocketAddress)mSocket.getLocalSocketAddress();
            if (address != null) {
                local = new ConnectionInfo(address.getHostName(), address.getPort());
            }
        }

        return local;
    }

    public void setLocalConnectionInfo(ConnectionInfo localConnectionInfo) {
        if (isConnect()) {
            throw new IllegalStateException("Socket is connected, can't set local info after connect.");
        } else {
            mLocalConnectionInfo = localConnectionInfo;
        }
    }

    private class DisconnectThread extends Thread {
        private Exception mException;

        public DisconnectThread(Exception exception, String name) {
            super(name);
            mException = exception;
        }

        public void run() {
            try {
                if (mManager != null) {
                    mManager.close(mException);
                }

                if (mConnectThread != null && mConnectThread.isAlive()) {
                    mConnectThread.interrupt();

                    try {
                        Logger.i("disconnect thread need waiting for connection thread done.");
                        mConnectThread.join();
                    } catch (InterruptedException var7) {
                        var7.printStackTrace();
                    }

                    Logger.i("connection thread is done. disconnection thread going on");
                    mConnectThread = null;
                }

                if(mManager != null) {
                    mManager.close();
                }

                if (mSocket != null) {
                    try {
                        mSocket.close();
                    } catch (IOException var6) {
                        var6.printStackTrace();
                    }
                }

                if (mActionHandler != null) {
                    mActionHandler.detach(ConnectionManagerImpl.this);
                    Logger.d("mActionHandler is detached.");
                    mActionHandler = null;
                }
            } finally {
                isDisconnecting = false;
                isConnectionPermitted = true;
                if (!(mException instanceof UnConnectException) && mSocket != null) {
                    mException = mException instanceof ManuallyDisconnectException ? null : mException;
                    sendBroadcast("action_disconnection", mException);
                }

                mSocket = null;
                if (mException != null) {
                    Logger.e("socket is disconnecting because: " + mException.getMessage());
                    if (mOptions.isDebug()) {
                        mException.printStackTrace();
                    }
                }

            }

        }
    }

    private class ConnectionThread extends Thread {
        public ConnectionThread(String name) {
            super(name);
        }

        public void run() {
            try {
                s.acquire();
//                Logger.i("connect thread get semaphore");
                try {
                    mSocket = getSocketByConfig();
                } catch (Exception var7) {
                    if (mOptions.isDebug()) {
                        var7.printStackTrace();
                    }

                    throw new UnConnectException("Create socket failed.", var7);
                }

                if (mLocalConnectionInfo != null) {
                    Logger.d("try bind: " + mLocalConnectionInfo.getIp() + " port:" + mLocalConnectionInfo.getPort());
                    mSocket.bind(new InetSocketAddress(mLocalConnectionInfo.getIp(), mLocalConnectionInfo.getPort()));
                }

                Logger.d("Start connect: " + mRemoteConnectionInfo.getIp() + ":" + mRemoteConnectionInfo.getPort() + " socket server...");
                mSocket.connect(new InetSocketAddress(mRemoteConnectionInfo.getIp(), mRemoteConnectionInfo.getPort()), mOptions.getConnectTimeoutSecond() * 1000);
                mSocket.setTcpNoDelay(true);
                resolveManager();
                sendBroadcast("action_connection_success");
//                Logger.i("connect thread release semaphore");
                Logger.i("Socket server: " + mRemoteConnectionInfo.getIp() + ":" + mRemoteConnectionInfo.getPort() + " connect successful!");
            } catch (Exception var8) {
                var8.printStackTrace();
                Exception exception = new UnConnectException(var8);
                Logger.e("Socket server " + mRemoteConnectionInfo.getIp() + ":" + mRemoteConnectionInfo.getPort() + " connect failed! error msg:" + var8.getMessage());
                sendBroadcast("action_connection_failed", exception);
            } finally {
                isConnectionPermitted = true;
                s.release();
            }

        }
    }

    private class AsyncSendThread extends Thread {
        private ISendable msg;

        public AsyncSendThread(ISendable msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            try {
                s.acquire();
//                Logger.i("async send thread get semaphore");
                send(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                s.release();
//                Logger.i("async send thread release semaphore");
            }
        }
    }
}
