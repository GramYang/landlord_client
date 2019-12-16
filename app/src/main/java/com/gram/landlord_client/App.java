package com.gram.landlord_client;

import android.app.Application;
import android.support.annotation.Nullable;

import com.gram.landlord_client.api.AgentHandler;
import com.gram.landlord_client.api.LoginHandler;
import com.gram.landlord_client.sdk.entity.PingAck;
import com.gram.landlord_client.sdk.protocol.Constants;
import com.gram.landlord_client.sdk.socket.connection.ConnectionInfo;
import com.gram.landlord_client.sdk.socket.connection.OkSocket;
import com.gram.landlord_client.sdk.socket.interfaces.IConnectionManager;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class App extends Application {
    private static App app;
    private IConnectionManager loginManager;
    private IConnectionManager agentManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return priority != Logger.DEBUG;
            }
        });
        app = this;
        connectLogin();
    }

    public void connectLogin() {
        if(loginManager == null) {
            ConnectionInfo loginInfo = new ConnectionInfo(Constants.loginHost, Constants.loginPort);
            loginManager = OkSocket.open(loginInfo);
            loginManager.registerReceiver(new LoginHandler());
            loginManager.connect();
        }
    }

    public void connectAgent(String ip, int port) {
        if(agentManager == null) {
            ConnectionInfo agentInfo = new ConnectionInfo(ip, port);
            agentManager = OkSocket.open(agentInfo);
            agentManager.registerReceiver(new AgentHandler());
            agentManager.connect();
        }
    }

    public void pingAgent() {
        if(agentManager != null) {
            agentManager.getPulseManager().setPulseSendable(new PingAck()).pulse();
        }
    }

    public static App getApp() {return app;}

    public IConnectionManager getLogin() {return loginManager;}

    public IConnectionManager getAgent() {return agentManager;}

}
