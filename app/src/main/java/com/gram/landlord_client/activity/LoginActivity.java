package com.gram.landlord_client.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.google.gson.Gson;
import com.gram.landlord_client.App;
import com.gram.landlord_client.R;
import com.gram.landlord_client.base.BaseActivity;
import com.gram.landlord_client.sdk.entity.LoginAck;
import com.gram.landlord_client.sdk.entity.LoginReq;
import com.gram.landlord_client.sdk.entity.VerifyAck;
import com.gram.landlord_client.sdk.entity.VerifyReq;
import com.gram.landlord_client.sdk.protocol.Constants;
import com.gram.landlord_client.sdk.socket.util.TextUtils;
import com.gram.landlord_client.util.NetworkUtil;
import com.gram.landlord_client.util.NotificationUtil;
import com.gram.landlord_client.util.SharedPreferencesUtil;
import com.gram.landlord_client.util.ToastUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.orhanobut.logger.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.delete_username)
    ImageButton deleteUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private String username;
    private String password;
    private Gson gson;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        gson = new Gson();
        etUsername.setText(SharedPreferencesUtil.getUsername());
        etPassword.setText("");
        addDisposable(
                //用户名点击
                RxView.focusChanges(etUsername)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(hasFocus -> {
                            if(hasFocus) {
                                if(etUsername.getText().length() > 0) visible(deleteUsername);
                                else gone(deleteUsername);
                            } else {
                                gone(deleteUsername);
                            }
                        }),
                //用户名输入
                RxTextView.textChangeEvents(etUsername)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(textViewTextChangeEvent -> {
                            etPassword.setText("");
                            if(textViewTextChangeEvent.count() > 0) visible(deleteUsername);
                            else gone(deleteUsername);
                        }),
                //登录
                RxView.clicks(btnLogin)
                        .throttleFirst(5, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            if(!NetworkUtil.isConnected(App.getApp())) ToastUtil.showCenterSingleToast("当前网络不可用");
                            else login();
                        }),
                //清空用户名和密码
                RxView.clicks(deleteUsername)
                        .throttleFirst(5, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            etUsername.setText("");
                            gone(deleteUsername);
                            etUsername.setFocusable(true);
                            etUsername.setFocusableInTouchMode(true);
                            etUsername.requestFocus();
                        })
        );
    }

    private void login() {
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            ToastUtil.showCenterSingleToast("用户名和密码不能为空");
            return;
        }
//        App.getApp().connectLogin();
        LoginReq request = new LoginReq("1.0","Android",(username+":"+password).getBytes(StandardCharsets.UTF_8));
        App.getApp().getLogin().sendAfterConnect(request);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onLoginResponse(LoginAck ack) {
        EventBus.getDefault().removeStickyEvent(ack);
        if(ack.isSuccessFul()) {
            Logger.i("获取登录反馈，登录成功");
            SharedPreferencesUtil.saveUsername(username);
            SharedPreferencesUtil.savePassword(password);
            App.getApp().getLogin().disconnect(); //Login是短连接
            String[] address = ack.getServerInfo().split(":");
            if (address.length < 2) {
                Logger.e("agent address invalid");
                return;
            }
            App.getApp().connectAgent(address[0], Integer.parseInt(address[1]));
            App.getApp().getAgent().sendAfterConnect(new VerifyReq("", ack.getGameSvcID()));
        } else {
            username = null;
            password = null;
            switch (ack.getResultCode()) {
                case Constants.LOGIN_ACK_STATUS.LOGIN_WRONG_USERNAME:
                    String msg1 = "用户名错误";
                    Logger.i(msg1);
                    ToastUtil.showCenterLongToast(msg1);
                    break;
                case Constants.LOGIN_ACK_STATUS.LOGIN_WRONG_PASSWORD:
                    String msg2 = "密码错误";
                    Logger.i(msg2);
                    ToastUtil.showCenterLongToast(msg2);
                    break;
                case Constants.LOGIN_ACK_STATUS.AGENT_NOT_FOUND:
                    String msg3 = "Agent服务异常";
                    Logger.i(msg3);
                    ToastUtil.showCenterLongToast(msg3);
                    break;
                case Constants.LOGIN_ACK_STATUS.AGENT_ADDRESS_ERROR:
                    String msg4 = "Agent地址错误";
                    Logger.i(msg4);
                    ToastUtil.showCenterLongToast(msg4);
                    break;
                case Constants.LOGIN_ACK_STATUS.GAME_NOT_FOUND:
                    String msg5 = "Game服务异常";
                    Logger.i(msg5);
                    ToastUtil.showCenterLongToast(msg5);
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onVertifyAck(VerifyAck ack) {
        App.getApp().pingAgent();//开始心跳
        if(ack.getResult() != Constants.VERIFY_ACK_STATUS.VERIFY_SUCCESS) return;
//        NotificationUtil.jumpActivity(this, MainActivity.class, "跳转MainActivity", 2);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
