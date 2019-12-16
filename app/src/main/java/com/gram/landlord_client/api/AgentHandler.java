package com.gram.landlord_client.api;

import com.google.gson.Gson;
import com.gram.landlord_client.App;
import com.gram.landlord_client.sdk.entity.VerifyAck;
import com.gram.landlord_client.sdk.proto.Landlord;
import com.gram.landlord_client.sdk.protocol.Constants;
import com.gram.landlord_client.sdk.protocol.response.CancelReadyResponse;
import com.gram.landlord_client.sdk.protocol.response.CardsOutResponse;
import com.gram.landlord_client.sdk.protocol.response.ChatMsgResponse;
import com.gram.landlord_client.sdk.protocol.response.EndGameResponse;
import com.gram.landlord_client.sdk.protocol.response.EndGrabLandlordResponse;
import com.gram.landlord_client.sdk.protocol.response.EnterTableResponse;
import com.gram.landlord_client.sdk.protocol.response.ExitSeatResponse;
import com.gram.landlord_client.sdk.protocol.response.GameResultResponse;
import com.gram.landlord_client.sdk.protocol.response.GiveUpLandlordResponse;
import com.gram.landlord_client.sdk.protocol.response.GrabLandlordResponse;
import com.gram.landlord_client.sdk.protocol.response.InitHallResponse;
import com.gram.landlord_client.sdk.protocol.response.LandlordMultipleWagerResponse;
import com.gram.landlord_client.sdk.protocol.response.MultipleWagerResponse;
import com.gram.landlord_client.sdk.protocol.response.ReadyResponse;
import com.gram.landlord_client.sdk.protocol.response.RefreshHallResponse;
import com.gram.landlord_client.sdk.protocol.response.UserInfoResponse;
import com.gram.landlord_client.sdk.socket.bean.OriginalData;
import com.gram.landlord_client.sdk.socket.connection.ConnectionInfo;
import com.gram.landlord_client.sdk.socket.connection.SocketActionAdapter;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AgentHandler extends SocketActionAdapter {

    private Gson gson;

    public AgentHandler() {
        gson = new Gson();
    }

    @Override
    public void onSocketIOThreadShutdown(String action, Exception e) {
        Logger.w("Request发送失败");
    }

    @Override
    public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
        ByteBuffer bf = ByteBuffer.wrap(data.getTypeBytes());
        short type = bf.order(ByteOrder.LITTLE_ENDIAN).getShort();
        switch (type) {
            case Constants.PING_ACK_TYPE://收到心跳包后开始心跳检测
                App.getApp().getAgent().getPulseManager().feed(); //喂狗，也就是检查是否丢失
                break;
            case Constants.JSON_ACK_TYPE://检查type是否是jsonack
                try {
                    Landlord.JsonACK ack = Landlord.JsonACK.parseFrom(data.getBodyBytes());
                    Class<?> class1 = Constants.jsonType.get(ack.getJsonType());
                    if (class1 != null) {
                        String json = new String(ack.getContent().toByteArray());
                        Logger.i("receive jsontype:"+ack.getJsonType()+ "jsonack: " + json);
                        Object object = gson.fromJson(json, class1);
                        if(object instanceof CancelReadyResponse) {
                            EventBus.getDefault().postSticky(object);
                        }
                        if(object instanceof CardsOutResponse) {
                            EventBus.getDefault().postSticky(object);
                        }
                        if(object instanceof ChatMsgResponse) {
                            EventBus.getDefault().postSticky(object);
                        }
                        if(object instanceof EndGameResponse) {
                            EventBus.getDefault().postSticky(object);
                        }
                        if(object instanceof EndGrabLandlordResponse) {
                            EventBus.getDefault().postSticky(object);
                        }
                        if(object instanceof EnterTableResponse) {
                            EventBus.getDefault().postSticky(object);
                        }
                        if(object instanceof ExitSeatResponse) {
                            EventBus.getDefault().postSticky(object);
                        }
                        if(object instanceof GiveUpLandlordResponse) {
                            EventBus.getDefault().postSticky(object);
                        }
                        if(object instanceof GrabLandlordResponse) {
                            EventBus.getDefault().postSticky(object);
                        }
                        if(object instanceof InitHallResponse) {
                            EventBus.getDefault().postSticky(object);
                        }
                        if(object instanceof LandlordMultipleWagerResponse) {
                            EventBus.getDefault().postSticky(object);
                        }
                        if(object instanceof MultipleWagerResponse) {
                            EventBus.getDefault().postSticky(object);
                        }
                        if(object instanceof ReadyResponse) {
                            EventBus.getDefault().postSticky(object);
                        }
                        if(object instanceof RefreshHallResponse) {
                            EventBus.getDefault().postSticky(object);
                        }
                        if(object instanceof UserInfoResponse) {
                            EventBus.getDefault().postSticky(object);
                        }
                        if(object instanceof GameResultResponse) {
                            EventBus.getDefault().postSticky(object);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Constants.VERIFY_ACK_TYPE: //申请agent绑定game
                try {
                    Landlord.VerifyACK ack = Landlord.VerifyACK.parseFrom(data.getBodyBytes());
                    if(ack.getResult() == Landlord.ResultCode.NoError) {
                        Logger.i("receive verifyack");
                        VerifyAck ack1 = new VerifyAck(Constants.VERIFY_ACK_STATUS.VERIFY_SUCCESS);
                        EventBus.getDefault().post(ack1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
