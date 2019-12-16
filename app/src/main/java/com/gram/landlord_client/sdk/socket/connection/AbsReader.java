package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.socket.interfaces.IIOCoreOptions;
import com.gram.landlord_client.sdk.socket.interfaces.IReader;
import com.gram.landlord_client.sdk.socket.interfaces.IStateSender;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbsReader implements IReader<IIOCoreOptions> {

    protected volatile IIOCoreOptions mOkOptions;

    protected IStateSender mStateSender;

    protected InputStream mInputStream;

    public AbsReader() {
    }

    @Override
    public void initialize(InputStream inputStream, IStateSender stateSender) {
        mStateSender = stateSender;
        mInputStream = inputStream;
    }

    @Override
    public void setOption(IIOCoreOptions option) {
        mOkOptions = option;
    }


    @Override
    public void close() {
        if (mInputStream != null) {
            try {
                mInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
