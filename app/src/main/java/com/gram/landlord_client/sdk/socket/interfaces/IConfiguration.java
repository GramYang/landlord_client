package com.gram.landlord_client.sdk.socket.interfaces;


import com.gram.landlord_client.sdk.socket.connection.OkSocketOptions;

public interface IConfiguration {
    IConnectionManager option(OkSocketOptions var1);

    OkSocketOptions getOption();
}
