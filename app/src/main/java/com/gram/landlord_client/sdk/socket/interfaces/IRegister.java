package com.gram.landlord_client.sdk.socket.interfaces;

public interface IRegister<T, E> {
    E registerReceiver(T socketActionListener);

    E unRegisterReceiver(T socketActionListener);
}
