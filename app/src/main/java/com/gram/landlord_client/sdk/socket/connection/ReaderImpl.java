package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.socket.bean.OriginalData;
import com.gram.landlord_client.sdk.socket.exception.ReadException;
import com.gram.landlord_client.sdk.socket.interfaces.IOAction;
import com.gram.landlord_client.sdk.socket.interfaces.IReaderProtocol;
import com.gram.landlord_client.sdk.socket.util.BytesUtils;
import com.orhanobut.logger.Logger;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class ReaderImpl extends AbsReader {

    private ByteBuffer mRemainingBuf;

    @Override
    public void read() throws RuntimeException {
        OriginalData originalData = new OriginalData();
        IReaderProtocol headerProtocol = mOkOptions.getReaderProtocol();
        int headerLength = headerProtocol.getHeaderLength() + headerProtocol.getTypeLength();
        ByteBuffer headBuf = ByteBuffer.allocate(headerLength);
        headBuf.order(mOkOptions.getReadByteOrder());
        if (mRemainingBuf != null) {
            mRemainingBuf.flip();
            int length = Math.min(mRemainingBuf.remaining(), headerLength);
            //先从mRemainingBuf读取字符
            headBuf.put(mRemainingBuf.array(), 0, length);
            if (length < headerLength) {
                //mRemainingBuf里面的字符不够就从流里面继续读
                mRemainingBuf = null;
                readHeaderFromChannel(headBuf, headerLength - length);
            } else {
                //这里只可能是length=headerLength
                mRemainingBuf.position(headerLength);
            }
        } else {
            //mRemainingBuf没东西，直接从流里面读
            readHeaderFromChannel(headBuf, headerLength);
        }
        //到此，headBuf已经持有length和type
        originalData.setHeadBytes(Arrays.copyOfRange(headBuf.array(), 0, headerProtocol.getHeaderLength()));
        originalData.setTypeBytes(Arrays.copyOfRange(headBuf.array(), headerProtocol.getHeaderLength(), headerLength));
        Logger.d("read head: " + ByteBuffer.wrap(originalData.getHeadBytes()).order(ByteOrder.LITTLE_ENDIAN).getShort());
        Logger.d("read type: " + ByteBuffer.wrap(originalData.getTypeBytes()).order(ByteOrder.LITTLE_ENDIAN).getShort());
        int bodyLength = headerProtocol.getBodyLength(originalData.getHeadBytes(), mOkOptions.getReadByteOrder());
        Logger.d("need read body length: " + bodyLength);
        if (bodyLength > 0) {
            if (bodyLength > mOkOptions.getMaxReadDataMB() * 1024 * 1024) {
                throw new ReadException("Need to follow the transmission protocol.\r\n" +
                        "Please check the client/server code.\r\n" +
                        "According to the packet header data in the transport protocol, the package length is " + bodyLength + " Bytes.\r\n" +
                        "You need check your <ReaderProtocol> definition");
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(bodyLength);
            byteBuffer.order(mOkOptions.getReadByteOrder());
            if (mRemainingBuf != null) {
                //继续从mRemainingBuf读取body
                int bodyStartPosition = mRemainingBuf.position();
                int length = Math.min(mRemainingBuf.remaining(), bodyLength);
                byteBuffer.put(mRemainingBuf.array(), bodyStartPosition, length);
                mRemainingBuf.position(bodyStartPosition + length);
                if (length == bodyLength) {
                    if (mRemainingBuf.remaining() > 0) {//there are data left
                        ByteBuffer temp = ByteBuffer.allocate(mRemainingBuf.remaining());
                        temp.order(mOkOptions.getReadByteOrder());
                        temp.put(mRemainingBuf.array(), mRemainingBuf.position(), mRemainingBuf.remaining());
                        mRemainingBuf = temp;
                    } else {//there are no data left
                        mRemainingBuf = null;
                    }
                    //cause this time data from remaining buffer not from channel.
                    originalData.setBodyBytes(byteBuffer.array());
                    mStateSender.sendBroadcast(IOAction.ACTION_READ_COMPLETE, originalData);
                    return;
                } else {//there are no data left in buffer and some data pieces in channel
                    mRemainingBuf = null;
                }
            }
            readBodyFromChannel(byteBuffer);
            originalData.setBodyBytes(byteBuffer.array());
        } else if (bodyLength == 0) {
            originalData.setBodyBytes(new byte[0]);
            if (mRemainingBuf != null) {
                //the body is empty so header remaining buf need set null
                if (mRemainingBuf.hasRemaining()) {
                    ByteBuffer temp = ByteBuffer.allocate(mRemainingBuf.remaining());
                    temp.order(mOkOptions.getReadByteOrder());
                    temp.put(mRemainingBuf.array(), mRemainingBuf.position(), mRemainingBuf.remaining());
                    mRemainingBuf = temp;
                } else {
                    mRemainingBuf = null;
                }
            }
        } else if (bodyLength < 0) {
            throw new ReadException(
                    "read body is wrong,this socket input stream is end of file read " + bodyLength + " ,that mean this socket is disconnected by server");
        }
        mStateSender.sendBroadcast(IOAction.ACTION_READ_COMPLETE, originalData);
    }

    private void readHeaderFromChannel(ByteBuffer headBuf, int readLength) {
        for (int i = 0; i < readLength; i++) {
            try {
                byte[] bytes = new byte[1];
                int value = mInputStream.read(bytes);
                if (value == -1) {
                    Logger.e("read head is wrong, this socket input stream is end of file read " + value + " ,that mean this socket is disconnected by server");
                }
                headBuf.put(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void readBodyFromChannel(ByteBuffer byteBuffer) {
        while (byteBuffer.hasRemaining()) {
            try {
                byte[] bufArray = new byte[mOkOptions.getReadPackageBytes()];
                int len = mInputStream.read(bufArray);
                if (len == -1) {
                    break;
                }
                int remaining = byteBuffer.remaining();
                if (len > remaining) {
                    byteBuffer.put(bufArray, 0, remaining);
                    mRemainingBuf = ByteBuffer.allocate(len - remaining);
                    mRemainingBuf.order(mOkOptions.getReadByteOrder());
                    mRemainingBuf.put(bufArray, remaining, len - remaining);
                } else {
                    byteBuffer.put(bufArray, 0, len);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Logger.d("read total bytes: " + BytesUtils.toHexStringForLog(byteBuffer.array()));
        Logger.d("read total length:" + (byteBuffer.capacity() - byteBuffer.remaining()));
    }

}
