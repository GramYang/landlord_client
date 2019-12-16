package com.gram.landlord_client.sdk.protocol;

import com.gram.landlord_client.R;
import com.gram.landlord_client.sdk.protocol.request.CancelReadyRequest;
import com.gram.landlord_client.sdk.protocol.request.CardsOutRequest;
import com.gram.landlord_client.sdk.protocol.request.ChatMsgRequest;
import com.gram.landlord_client.sdk.protocol.request.EndGameRequest;
import com.gram.landlord_client.sdk.protocol.request.EndGrabLandlordRequest;
import com.gram.landlord_client.sdk.protocol.request.EnterTableRequest;
import com.gram.landlord_client.sdk.protocol.request.ExitHallRequest;
import com.gram.landlord_client.sdk.protocol.request.ExitSeatRequest;
import com.gram.landlord_client.sdk.protocol.request.GameResultRequest;
import com.gram.landlord_client.sdk.protocol.request.GiveUpLandlordRequest;
import com.gram.landlord_client.sdk.protocol.request.InitHallRequest;
import com.gram.landlord_client.sdk.protocol.request.LandlordMultipleWagerRequest;
import com.gram.landlord_client.sdk.protocol.request.LoginRequest;
import com.gram.landlord_client.sdk.protocol.request.MultipleWagerRequest;
import com.gram.landlord_client.sdk.protocol.request.ReadyRequest;
import com.gram.landlord_client.sdk.protocol.request.UserInfoRequest;
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

import java.util.HashMap;

public interface Constants {
    String loginHost = "192.168.101.18";
    int loginPort = 6789;
    short JSON_REQ_TYPE = 20000;
    short JSON_ACK_TYPE = 20001;
    short PING_ACK_TYPE = 16241;
    short LOGIN_REQ_TYPE = 18888;
    short LOGIN_ACK_TYPE = 18889;
    short VERIFY_REQ_TYPE = 13457;
    short VERIFY_ACK_TYPE = 13458;

    interface LOGIN_ACK_STATUS {
        int LOGIN_SUCCESS = 200;
        int LOGIN_WRONG_USERNAME = 201;
        int LOGIN_WRONG_PASSWORD = 202;
        int AGENT_NOT_FOUND = 203;
        int AGENT_ADDRESS_ERROR = 204;
        int GAME_NOT_FOUND = 205;
    }

    interface VERIFY_ACK_STATUS {
        int VERIFY_SUCCESS = 100;
    }

    interface GAMERESULT_ACK_STATUS {
        int UPDATE_SUCCESS = 300;
        int UPDATE_SERVER_ERROR = 301;
        int UPDATE_PASSWORD_ERROR = 302;
    }

    HashMap<Integer, Class<?>> jsonType = new HashMap<Integer, Class<?>>() {
        {
            put(10, CancelReadyRequest.class); put(11, CardsOutRequest.class); put(12, ChatMsgRequest.class);
            put(13, EndGameRequest.class); put(14, EndGrabLandlordRequest.class); put(15, EnterTableRequest.class);
            put(16, ExitHallRequest.class); put(17, ExitSeatRequest.class); put(18, GiveUpLandlordRequest.class);
            put(19, InitHallRequest.class); put(20, LandlordMultipleWagerRequest.class); put(21, LoginRequest.class);
            put(22, MultipleWagerRequest.class); put(23, ReadyRequest.class); put(24, UserInfoRequest.class);
            put(25, GameResultRequest.class);

            put(100, CancelReadyResponse.class); put(101, CardsOutResponse.class); put(102, ChatMsgResponse.class);
            put(103, EndGameResponse.class); put(104, EndGrabLandlordResponse.class); put(105, EnterTableResponse.class);
            put(106, ExitSeatResponse.class); put(107, GiveUpLandlordResponse.class); put(108, GrabLandlordResponse.class);
            put(109, InitHallResponse.class); put(110, LandlordMultipleWagerResponse.class);
            put(112, MultipleWagerResponse.class); put(113, ReadyResponse.class); put(114, RefreshHallResponse.class);
            put(115, UserInfoResponse.class); put(116, GameResultResponse.class);
        }

    };

    HashMap<Integer, Integer> cardsResource = new HashMap<Integer, Integer>(54) {
        {
            put(12, R.mipmap.cluba); put(13, R.mipmap.club2); put(1, R.mipmap.club3); put(2, R.mipmap.club4);
            put(3, R.mipmap.club5); put(4, R.mipmap.club6); put(5, R.mipmap.club7); put(6, R.mipmap.club8);
            put(7, R.mipmap.club9); put(8, R.mipmap.club10); put(9, R.mipmap.clubj); put(10, R.mipmap.clubq);
            put(11, R.mipmap.clubk);
            put(32, R.mipmap.spadea); put(33, R.mipmap.spade2); put(21, R.mipmap.spade3); put(22, R.mipmap.spade4);
            put(23, R.mipmap.spade5); put(24, R.mipmap.spade6); put(25, R.mipmap.spade7); put(26, R.mipmap.spade8);
            put(27, R.mipmap.spade9); put(28, R.mipmap.spade10); put(29, R.mipmap.spadej); put(30, R.mipmap.spadeq);
            put(31, R.mipmap.spadek);
            put(52, R.mipmap.diamonda); put(53, R.mipmap.diamond2); put(41, R.mipmap.diamond3); put(42, R.mipmap.diamond4);
            put(43, R.mipmap.diamond5); put(44, R.mipmap.diamond6); put(45, R.mipmap.diamond7); put(46, R.mipmap.diamond8);
            put(47, R.mipmap.diamond9); put(48, R.mipmap.diamond10); put(49, R.mipmap.diamondj); put(50, R.mipmap.diamondq);
            put(51, R.mipmap.diamondk);
            put(72, R.mipmap.hearta); put(73, R.mipmap.heart2); put(61, R.mipmap.heart3); put(62, R.mipmap.heart4);
            put(63, R.mipmap.heart5); put(64, R.mipmap.heart6); put(65, R.mipmap.heart7); put(66, R.mipmap.heart8);
            put(67, R.mipmap.heart9); put(68, R.mipmap.heart10); put(69, R.mipmap.heartj); put(70, R.mipmap.heartq);
            put(71, R.mipmap.heartk);
            put(74, R.mipmap.joker1); put(75, R.mipmap.joker2);
        }
    };
}
