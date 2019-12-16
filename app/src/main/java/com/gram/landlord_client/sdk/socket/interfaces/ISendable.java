package com.gram.landlord_client.sdk.socket.interfaces;

import java.io.Serializable;

public interface ISendable extends Serializable {
    byte[] parse();
}
