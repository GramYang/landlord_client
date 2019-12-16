package com.gram.landlord_client.sdk.socket.connection;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class OkSocketSSLConfig {
    private String mProtocol;
    private TrustManager[] mTrustManagers;
    private KeyManager[] mKeyManagers;
    private SSLSocketFactory mCustomSSLFactory;

    private OkSocketSSLConfig() {
    }

    public KeyManager[] getKeyManagers() {
        return mKeyManagers;
    }

    public String getProtocol() {
        return mProtocol;
    }

    public TrustManager[] getTrustManagers() {
        return mTrustManagers;
    }

    public SSLSocketFactory getCustomSSLFactory() {
        return mCustomSSLFactory;
    }

    public static class Builder {
        private OkSocketSSLConfig mConfig = new OkSocketSSLConfig();

        public Builder() {
        }

        public OkSocketSSLConfig.Builder setProtocol(String protocol) {
            mConfig.mProtocol = protocol;
            return this;
        }

        public OkSocketSSLConfig.Builder setTrustManagers(TrustManager[] trustManagers) {
            mConfig.mTrustManagers = trustManagers;
            return this;
        }

        public OkSocketSSLConfig.Builder setKeyManagers(KeyManager[] keyManagers) {
            mConfig.mKeyManagers = keyManagers;
            return this;
        }

        public OkSocketSSLConfig.Builder setCustomSSLFactory(SSLSocketFactory customSSLFactory) {
            mConfig.mCustomSSLFactory = customSSLFactory;
            return this;
        }

        public OkSocketSSLConfig build() {
            return mConfig;
        }
    }
}