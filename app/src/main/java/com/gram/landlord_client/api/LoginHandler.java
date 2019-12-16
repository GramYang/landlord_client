package com.gram.landlord_client.api;

import com.gram.landlord_client.sdk.entity.LoginAck;
import com.gram.landlord_client.sdk.proto.Landlord;
import com.gram.landlord_client.sdk.protocol.Constants;
import com.gram.landlord_client.sdk.socket.bean.OriginalData;
import com.gram.landlord_client.sdk.socket.connection.ConnectionInfo;
import com.gram.landlord_client.sdk.socket.connection.SocketActionAdapter;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LoginHandler extends SocketActionAdapter {
    @Override
    public void onSocketIOThreadShutdown(String action, Exception e) {
        Logger.e("Login Request发送失败");
    }

    @Override
    public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
        ByteBuffer bf = ByteBuffer.wrap(data.getTypeBytes());
        short type = bf.order(ByteOrder.LITTLE_ENDIAN).getShort();
        if(type == Constants.LOGIN_ACK_TYPE) {
            try {
                Landlord.LoginACK ack = Landlord.LoginACK.parseFrom(data.getBodyBytes());
                LoginAck response;
                if(ack.getResult() == Landlord.ResultCode.AgentNotFound) {
                    response = new LoginAck("", false, Constants.LOGIN_ACK_STATUS.AGENT_NOT_FOUND, "");
                    EventBus.getDefault().post(response);
                }
                if(ack.getResult() == Landlord.ResultCode.AgentAddressError) {
                    response = new LoginAck("", false, Constants.LOGIN_ACK_STATUS.AGENT_ADDRESS_ERROR,"");
                    EventBus.getDefault().post(response);
                }
                if(ack.getResult() == Landlord.ResultCode.GameNotFound) {
                    response = new LoginAck("", false, Constants.LOGIN_ACK_STATUS.GAME_NOT_FOUND,"");
                    EventBus.getDefault().post(response);
                }
                if(ack.getResult() == Landlord.ResultCode.NoError) {
                    if(ack.getTokenAck() == Constants.LOGIN_ACK_STATUS.LOGIN_SUCCESS) {
                        response = new LoginAck(ack.getServer().getIP() + ":" + ack.getServer().getPort(),
                                true, Constants.LOGIN_ACK_STATUS.LOGIN_SUCCESS, ack.getGameSvcID());
                        EventBus.getDefault().post(response);
                    }
                    response = new LoginAck("",false,ack.getTokenAck(),"");
                    EventBus.getDefault().post(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
