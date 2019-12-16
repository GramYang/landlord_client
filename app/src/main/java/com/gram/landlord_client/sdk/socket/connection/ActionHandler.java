package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.socket.exception.ManuallyDisconnectException;
import com.gram.landlord_client.sdk.socket.interfaces.IConnectionManager;
import com.gram.landlord_client.sdk.socket.interfaces.IRegister;
import com.gram.landlord_client.sdk.socket.interfaces.ISocketActionListener;

public class ActionHandler extends SocketActionAdapter {
    private IConnectionManager mManager;
    private OkSocketOptions.IOThreadMode mCurrentThreadMode;
    private boolean iOThreadIsCalledDisconnect = false;

    public ActionHandler() {
    }

    public void attach(IConnectionManager manager, IRegister<ISocketActionListener, IConnectionManager> register) {
        mManager = manager;
        register.registerReceiver(this);
    }

    public void detach(IRegister register) {
        register.unRegisterReceiver(this);
    }

    public void onSocketIOThreadStart(String action) {
        if (mManager.getOption().getIOThreadMode() != mCurrentThreadMode) {
            mCurrentThreadMode = mManager.getOption().getIOThreadMode();
        }

        iOThreadIsCalledDisconnect = false;
    }

    public void onSocketIOThreadShutdown(String action, Exception e) {
        if (mCurrentThreadMode == mManager.getOption().getIOThreadMode() && !iOThreadIsCalledDisconnect) {
            iOThreadIsCalledDisconnect = true;
            if (!(e instanceof ManuallyDisconnectException)) {
                mManager.disconnect(e);
            }
        }

    }

    public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
        mManager.disconnect(e);
    }
}