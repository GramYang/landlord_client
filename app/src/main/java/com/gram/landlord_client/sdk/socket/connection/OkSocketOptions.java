package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.socket.interfaces.IConfiguration;
import com.gram.landlord_client.sdk.socket.interfaces.IIOCoreOptions;
import com.gram.landlord_client.sdk.socket.interfaces.IReaderProtocol;

import java.nio.ByteOrder;

public class OkSocketOptions implements IIOCoreOptions {
    private static boolean isDebug;
    private OkSocketOptions.IOThreadMode mIOThreadMode;
    private boolean isConnectionHolden;
    private ByteOrder mWriteOrder;
    private ByteOrder mReadByteOrder;
    private IReaderProtocol mReaderProtocol;
    private int mWritePackageBytes;
    private int mReadPackageBytes;
    private long mPulseFrequency;
    private int mPulseFeedLoseTimes;
    private int mConnectTimeoutSecond;
    private int mMaxReadDataMB;
    private AbsReconnectionManager mReconnectionManager;
    private OkSocketSSLConfig mSSLConfig;
    private OkSocketFactory mOkSocketFactory;
    private boolean isCallbackInIndependentThread;
    private OkSocketOptions.ThreadModeToken mCallbackThreadModeToken;

    private OkSocketOptions() {
    }

    public static void setIsDebug(boolean isDebug) {
        OkSocketOptions.isDebug = isDebug;
    }

    public OkSocketOptions.IOThreadMode getIOThreadMode() {
        return mIOThreadMode;
    }

    public long getPulseFrequency() {
        return mPulseFrequency;
    }

    public OkSocketSSLConfig getSSLConfig() {
        return mSSLConfig;
    }

    public OkSocketFactory getOkSocketFactory() {
        return mOkSocketFactory;
    }

    public int getConnectTimeoutSecond() {
        return mConnectTimeoutSecond;
    }

    public boolean isConnectionHolden() {
        return isConnectionHolden;
    }

    public int getPulseFeedLoseTimes() {
        return mPulseFeedLoseTimes;
    }

    public AbsReconnectionManager getReconnectionManager() {
        return mReconnectionManager;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public int getWritePackageBytes() {
        return mWritePackageBytes;
    }

    public int getReadPackageBytes() {
        return mReadPackageBytes;
    }

    public ByteOrder getWriteByteOrder() {
        return mWriteOrder;
    }

    public IReaderProtocol getReaderProtocol() {
        return mReaderProtocol;
    }

    public int getMaxReadDataMB() {
        return mMaxReadDataMB;
    }

    public ByteOrder getReadByteOrder() {
        return mReadByteOrder;
    }

    public OkSocketOptions.ThreadModeToken getCallbackThreadModeToken() {
        return mCallbackThreadModeToken;
    }

    public boolean isCallbackInIndependentThread() {
        return isCallbackInIndependentThread;
    }

    public static OkSocketOptions getDefault() {
        OkSocketOptions okOptions = new OkSocketOptions();
        okOptions.mPulseFrequency = 5000L;
        okOptions.mIOThreadMode = OkSocketOptions.IOThreadMode.DUPLEX;
        okOptions.mReaderProtocol = new ReaderProtocol1();
        okOptions.mMaxReadDataMB = 10;
        okOptions.mConnectTimeoutSecond = 3;
        okOptions.mWritePackageBytes = 65535;
        okOptions.mReadPackageBytes = 65535;
        okOptions.mReadByteOrder = ByteOrder.LITTLE_ENDIAN;
        okOptions.mWriteOrder = ByteOrder.LITTLE_ENDIAN;
        okOptions.isConnectionHolden = true;
        okOptions.mPulseFeedLoseTimes = 5;
        okOptions.mReconnectionManager = new DefaultReconnectManager();
        okOptions.mSSLConfig = null;
        okOptions.mOkSocketFactory = null;
        okOptions.isCallbackInIndependentThread = true;
        okOptions.mCallbackThreadModeToken = null;
        return okOptions;
    }

    public static enum IOThreadMode {
        SIMPLEX,
        DUPLEX;

        private IOThreadMode() {
        }
    }

    public static class Builder {
        private OkSocketOptions mOptions;

        public Builder() {
            this(OkSocketOptions.getDefault());
        }

        public Builder(IConfiguration configuration) {
            this(configuration.getOption());
        }

        public Builder(OkSocketOptions okOptions) {
            mOptions = okOptions;
        }

        public OkSocketOptions.Builder setIOThreadMode(OkSocketOptions.IOThreadMode IOThreadMode) {
            mOptions.mIOThreadMode = IOThreadMode;
            return this;
        }

        public OkSocketOptions.Builder setMaxReadDataMB(int maxReadDataMB) {
            mOptions.mMaxReadDataMB = maxReadDataMB;
            return this;
        }

        public OkSocketOptions.Builder setSSLConfig(OkSocketSSLConfig SSLConfig) {
            mOptions.mSSLConfig = SSLConfig;
            return this;
        }

        public OkSocketOptions.Builder setReaderProtocol(IReaderProtocol readerProtocol) {
            mOptions.mReaderProtocol = readerProtocol;
            return this;
        }

        public OkSocketOptions.Builder setPulseFrequency(long pulseFrequency) {
            mOptions.mPulseFrequency = pulseFrequency;
            return this;
        }

        public OkSocketOptions.Builder setConnectionHolden(boolean connectionHolden) {
            mOptions.isConnectionHolden = connectionHolden;
            return this;
        }

        public OkSocketOptions.Builder setPulseFeedLoseTimes(int pulseFeedLoseTimes) {
            mOptions.mPulseFeedLoseTimes = pulseFeedLoseTimes;
            return this;
        }

        /** @deprecated */
        public OkSocketOptions.Builder setWriteOrder(ByteOrder writeOrder) {
            setWriteByteOrder(writeOrder);
            return this;
        }

        public OkSocketOptions.Builder setWriteByteOrder(ByteOrder writeOrder) {
            mOptions.mWriteOrder = writeOrder;
            return this;
        }

        public OkSocketOptions.Builder setReadByteOrder(ByteOrder readByteOrder) {
            mOptions.mReadByteOrder = readByteOrder;
            return this;
        }

        public OkSocketOptions.Builder setWritePackageBytes(int writePackageBytes) {
            mOptions.mWritePackageBytes = writePackageBytes;
            return this;
        }

        public OkSocketOptions.Builder setReadPackageBytes(int readPackageBytes) {
            mOptions.mReadPackageBytes = readPackageBytes;
            return this;
        }

        public OkSocketOptions.Builder setConnectTimeoutSecond(int connectTimeoutSecond) {
            mOptions.mConnectTimeoutSecond = connectTimeoutSecond;
            return this;
        }

        public OkSocketOptions.Builder setReconnectionManager(AbsReconnectionManager reconnectionManager) {
            mOptions.mReconnectionManager = reconnectionManager;
            return this;
        }

        public OkSocketOptions.Builder setSocketFactory(OkSocketFactory factory) {
            mOptions.mOkSocketFactory = factory;
            return this;
        }

        public OkSocketOptions.Builder setCallbackThreadModeToken(OkSocketOptions.ThreadModeToken threadModeToken) {
            mOptions.mCallbackThreadModeToken = threadModeToken;
            return this;
        }

        public OkSocketOptions build() {
            return mOptions;
        }
    }

    public abstract static class ThreadModeToken {
        public ThreadModeToken() {
        }

        public abstract void handleCallbackEvent(ActionDispatcher.ActionRunnable var1);
    }
}
