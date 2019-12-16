package com.gram.landlord_client.fragment;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gram.landlord_client.App;
import com.gram.landlord_client.R;
import com.gram.landlord_client.activity.GameActivity;
import com.gram.landlord_client.adapter.HallAdapter;
import com.gram.landlord_client.base.BaseFragment;
import com.gram.landlord_client.sdk.entity.JsonReq;
import com.gram.landlord_client.sdk.protocol.request.ChatMsgRequest;
import com.gram.landlord_client.sdk.protocol.request.EnterTableRequest;
import com.gram.landlord_client.sdk.protocol.request.InitHallRequest;
import com.gram.landlord_client.sdk.protocol.response.ChatMsgResponse;
import com.gram.landlord_client.sdk.protocol.response.EnterTableResponse;
import com.gram.landlord_client.sdk.protocol.response.InitHallResponse;
import com.gram.landlord_client.sdk.protocol.response.RefreshHallResponse;
import com.gram.landlord_client.util.SharedPreferencesUtil;
import com.gram.landlord_client.util.ToastUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class HallFragment extends BaseFragment {
    @BindView(R.id.rv_tables)
    RecyclerView rvTables;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.et_send)
    EditText etSend;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.btn_refresh)
    Button btnRefresh;

    private HallAdapter hallAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_hall;
    }

    @Override
    protected void init() {
        Gson gson = new Gson();
        //牌桌窗口
        hallAdapter = new HallAdapter(context);
        rvTables.setLayoutManager(new GridLayoutManager(context, 2));
        rvTables.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL));
        hallAdapter.setOnItemClickListener(((tableNum) -> {
            Logger.i("进入房间，房间号：%d", tableNum);
            SharedPreferencesUtil.saveTableNum(tableNum);
            EnterTableRequest enterTableRequest = new EnterTableRequest(SharedPreferencesUtil.getUsername(), tableNum);
            App.getApp().getAgent().send(new JsonReq(15, gson.toJson(enterTableRequest).getBytes(StandardCharsets.UTF_8)));
        }));
        rvTables.setAdapter(hallAdapter);
        //TextView滑动条
        tvMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
        //聊天窗口
        addDisposable(
                RxView.clicks(btnSend)
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe(o -> {
                            if(!etSend.getText().toString().equals("")) {
                                ChatMsgRequest chatMsgRequest = new ChatMsgRequest(1,
                                        SharedPreferencesUtil.getUsername(), etSend.getText().toString(), -1);
                                App.getApp().getAgent().send(new JsonReq(12, gson.toJson(chatMsgRequest).getBytes(StandardCharsets.UTF_8)));
                            }
                        }),
                RxView.clicks(btnRefresh)
                        .throttleFirst(5, TimeUnit.SECONDS)
                        .subscribe(o -> {
                            InitHallRequest initHallRequest = new InitHallRequest();
                            App.getApp().getAgent().send(new JsonReq(19, gson.toJson(initHallRequest).getBytes(StandardCharsets.UTF_8)));
                        })
        );
    }

    /**
     * 接收游戏大厅聊天信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onChatMsgResponse(ChatMsgResponse response) {
        if (response.getChatFlag() == 1 && tvMessage != null && etSend != null) {
            tvMessage.append("\n" + response.getUserName() + " 说：" + response.getMsg());
            etSend.setText("");
        }
    }

    /**
     * 初始化游戏大厅
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onInitHallResponse(InitHallResponse response) {
        EventBus.getDefault().removeStickyEvent(response);
        hallAdapter.setSeatMap(response.getTableList());
        hallAdapter.notifyDataSetChanged();
    }

    /**
     * 玩家进出大厅反馈
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onRefreshHallResponse(RefreshHallResponse response) {
        EventBus.getDefault().removeStickyEvent(response);
        hallAdapter.setSeatMap(response.getHallTables());
        hallAdapter.notifyDataSetChanged();
    }


    /**
     * 进入游戏房间反馈
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEnterTableResponse(EnterTableResponse response) {
        if(response.isSuccess()) {
            Intent intent = new Intent(context, GameActivity.class);
            startActivity(intent);
        } else {
            ToastUtil.showCenterLongToast("进入房间失败，看来你晚了人家一步~~~");
        }
    }

}
