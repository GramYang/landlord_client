package com.gram.landlord_client.sdk.socket.connection;
import com.gram.landlord_client.sdk.socket.interfaces.IConnectionManager;
import com.gram.landlord_client.sdk.socket.interfaces.IConnectionSwitchListener;
import com.gram.landlord_client.sdk.socket.interfaces.IServerManager;
import com.gram.landlord_client.sdk.socket.interfaces.IServerManagerPrivate;
import com.gram.landlord_client.sdk.socket.util.SPIUtils;
import com.orhanobut.logger.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ManagerHolder {
    private volatile Map<ConnectionInfo, IConnectionManager> mConnectionManagerMap;
    private volatile Map<Integer, IServerManagerPrivate> mServerManagerMap;

    public static ManagerHolder getInstance() {
        return ManagerHolder.InstanceHolder.INSTANCE;
    }

    private ManagerHolder() {
        mConnectionManagerMap = new HashMap();
        mServerManagerMap = new HashMap();
        mConnectionManagerMap.clear();
    }

    public IServerManager getServer(int localPort) {
        IServerManagerPrivate manager = (IServerManagerPrivate)mServerManagerMap.get(localPort);
        if (manager == null) {
            manager = (IServerManagerPrivate) SPIUtils.load(IServerManager.class);
            if (manager == null) {
                String err = "Oksocket.Server() load error. Server plug-in are required! For details link to https://github.com/xuuhaoo/OkSocket";
                Logger.e(err);
                throw new IllegalStateException(err);
            } else {
                synchronized(mServerManagerMap) {
                    mServerManagerMap.put(localPort, manager);
                }

                manager.initServerPrivate(localPort);
                return manager;
            }
        } else {
            return manager;
        }
    }

    public IConnectionManager getConnection(ConnectionInfo info) {
        IConnectionManager manager = (IConnectionManager)mConnectionManagerMap.get(info);
        return manager == null ? getConnection(info, OkSocketOptions.getDefault()) : getConnection(info, manager.getOption());
    }

    public IConnectionManager getConnection(ConnectionInfo info, OkSocketOptions okOptions) {
        IConnectionManager manager = (IConnectionManager)mConnectionManagerMap.get(info);
        if (manager != null) {
            if (!okOptions.isConnectionHolden()) {
                synchronized(mConnectionManagerMap) {
                    mConnectionManagerMap.remove(info);
                }

                return createNewManagerAndCache(info, okOptions);
            } else {
                manager.option(okOptions);
                return manager;
            }
        } else {
            return createNewManagerAndCache(info, okOptions);
        }
    }

    private IConnectionManager createNewManagerAndCache(ConnectionInfo info, OkSocketOptions okOptions) {
        AbsConnectionManager manager = new ConnectionManagerImpl(info);
        manager.option(okOptions);
        manager.setOnConnectionSwitchListener(new IConnectionSwitchListener() {
            public void onSwitchConnectionInfo(IConnectionManager manager, ConnectionInfo oldInfo, ConnectionInfo newInfo) {
                synchronized(mConnectionManagerMap) {
                    mConnectionManagerMap.remove(oldInfo);
                    mConnectionManagerMap.put(newInfo, manager);
                }
            }
        });
        synchronized(mConnectionManagerMap) {
            mConnectionManagerMap.put(info, manager);
            return manager;
        }
    }

    protected List<IConnectionManager> getList() {
        List<IConnectionManager> list = new ArrayList();
        Map<ConnectionInfo, IConnectionManager> map = new HashMap(mConnectionManagerMap);
        Iterator it = map.keySet().iterator();

        while(it.hasNext()) {
            ConnectionInfo info = (ConnectionInfo)it.next();
            IConnectionManager manager = (IConnectionManager)map.get(info);
            if (!manager.getOption().isConnectionHolden()) {
                it.remove();
            } else {
                list.add(manager);
            }
        }

        return list;
    }

    private static class InstanceHolder {
        private static final ManagerHolder INSTANCE = new ManagerHolder();

        private InstanceHolder() {
        }
    }
}
