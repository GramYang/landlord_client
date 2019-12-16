package com.gram.landlord_client.widget;

import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gram.landlord_client.App;
import com.gram.landlord_client.R;
import com.gram.landlord_client.sdk.entity.JsonReq;
import com.gram.landlord_client.sdk.protocol.Constants;
import com.gram.landlord_client.sdk.protocol.request.GameResultRequest;
import com.gram.landlord_client.sdk.protocol.response.GameResultResponse;
import com.gram.landlord_client.util.SharedPreferencesUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.nio.charset.StandardCharsets;

/**
 * 游戏结束后的弹窗
 */
public class EndGamePopupWindow extends PopupWindow {
    private View rootView;
    private boolean isLandlordWin;
    private boolean isYouWin;
    private int doubleNum;
    private long yourMoney;
    private TextView money1;
    private TextView money2;
    private Button confirm;
    private int result;

    public EndGamePopupWindow(View contentView, int width, int height,
                              boolean isLandlordWin, boolean isYouWin, int doubleNum, String yourMoney) {
        super(contentView, width, height);
        setContentView(contentView);
        this.rootView = contentView;
        this.isLandlordWin = isLandlordWin;
        this.isYouWin = isYouWin;
        this.doubleNum = doubleNum;
        this.yourMoney = Long.valueOf(yourMoney);
        init();
    }

    private void init() {
        Gson gson = new Gson();
        TextView landlordWin = rootView.findViewById(R.id.landlord_win);
        TextView farmerWin = rootView.findViewById(R.id.farmer_win);
        TextView youWin = rootView.findViewById(R.id.is_you_win);
        money1 = rootView.findViewById(R.id.money_result1);
        money2 = rootView.findViewById(R.id.money_result2);
        confirm = rootView.findViewById(R.id.confirm);
        if(isLandlordWin) {
            landlordWin.setVisibility(View.VISIBLE);
            farmerWin.setVisibility(View.GONE);
        } else {
            landlordWin.setVisibility(View.GONE);
            farmerWin.setVisibility(View.VISIBLE);
        }
        GameResultRequest request = new GameResultRequest();
        if(isYouWin) {
            if(isLandlordWin) result = doubleNum * 100 * 2;
            else result = doubleNum * 100;
            request.setResult(true);
            request.setUserName(SharedPreferencesUtil.getUsername());
            request.setPassword(SharedPreferencesUtil.getPassword());
            request.setMoney(result);
        } else {
            youWin.setText("你输了");
            if(isLandlordWin) result = doubleNum * 100 * 2;
            else result = doubleNum * 100;
            request.setResult(false);
            request.setUserName(SharedPreferencesUtil.getUsername());
            request.setPassword(SharedPreferencesUtil.getPassword());
            request.setMoney(result);
        }
        App.getApp().getAgent().send(new JsonReq(25, gson.toJson(request).getBytes(StandardCharsets.UTF_8)));

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGameResultResponse(GameResultResponse response) {
        if(response.getStatus() == Constants.GAMERESULT_ACK_STATUS.UPDATE_SUCCESS) {
            money1.append(result + "");
            yourMoney = yourMoney + result;
            SharedPreferencesUtil.saveMoney(yourMoney + "");
            money2.append(yourMoney + "");
            confirm.setOnClickListener(v -> dismiss());
        } else if(response.getStatus() == Constants.GAMERESULT_ACK_STATUS.UPDATE_PASSWORD_ERROR) {
            Logger.e("客户端持有的用户名密码错误！！");
        } else if(response.getStatus() == Constants.GAMERESULT_ACK_STATUS.UPDATE_SERVER_ERROR) {
            Logger.e("agent服务器错误");
        }
    }
}
