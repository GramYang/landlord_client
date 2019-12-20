package com.gram.landlord_client.activity;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gram.landlord_client.App;
import com.gram.landlord_client.R;
import com.gram.landlord_client.base.BaseActivity;
import com.gram.landlord_client.sdk.entity.JsonReq;
import com.gram.landlord_client.sdk.protocol.Constants;
import com.gram.landlord_client.sdk.protocol.request.CancelReadyRequest;
import com.gram.landlord_client.sdk.protocol.request.CardsOutRequest;
import com.gram.landlord_client.sdk.protocol.request.ChatMsgRequest;
import com.gram.landlord_client.sdk.protocol.request.EndGameRequest;
import com.gram.landlord_client.sdk.protocol.request.EndGrabLandlordRequest;
import com.gram.landlord_client.sdk.protocol.request.ExitSeatRequest;
import com.gram.landlord_client.sdk.protocol.request.GiveUpLandlordRequest;
import com.gram.landlord_client.sdk.protocol.request.LandlordMultipleWagerRequest;
import com.gram.landlord_client.sdk.protocol.request.MultipleWagerRequest;
import com.gram.landlord_client.sdk.protocol.request.ReadyRequest;
import com.gram.landlord_client.sdk.protocol.response.CancelReadyResponse;
import com.gram.landlord_client.sdk.protocol.response.CardsOutResponse;
import com.gram.landlord_client.sdk.protocol.response.ChatMsgResponse;
import com.gram.landlord_client.sdk.protocol.response.EndGameResponse;
import com.gram.landlord_client.sdk.protocol.response.EndGrabLandlordResponse;
import com.gram.landlord_client.sdk.protocol.response.EnterTableResponse;
import com.gram.landlord_client.sdk.protocol.response.ExitSeatResponse;
import com.gram.landlord_client.sdk.protocol.response.GiveUpLandlordResponse;
import com.gram.landlord_client.sdk.protocol.response.GrabLandlordResponse;
import com.gram.landlord_client.sdk.protocol.response.LandlordMultipleWagerResponse;
import com.gram.landlord_client.sdk.protocol.response.MultipleWagerResponse;
import com.gram.landlord_client.sdk.protocol.response.ReadyResponse;
import com.gram.landlord_client.util.LandlordUtil;
import com.gram.landlord_client.util.SharedPreferencesUtil;
import com.gram.landlord_client.util.ToastUtil;
import com.gram.landlord_client.widget.CardsPack;
import com.gram.landlord_client.widget.CircleImageView;
import com.gram.landlord_client.widget.EndGamePopupWindow;
import com.jakewharton.rxbinding2.view.RxView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class GameActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.me_avatar)
    CircleImageView meAvatar;
    @BindView(R.id.me_name)
    TextView meName;
    @BindView(R.id.joker2)
    TextView joker2;
    @BindView(R.id.joker1)
    TextView joker1;
    @BindView(R.id.two)
    TextView two;
    @BindView(R.id.Ace)
    TextView Ace;
    @BindView(R.id.King)
    TextView King;
    @BindView(R.id.Queen)
    TextView Queen;
    @BindView(R.id.Jake)
    TextView Jake;
    @BindView(R.id.ten)
    TextView ten;
    @BindView(R.id.nine)
    TextView nine;
    @BindView(R.id.eight)
    TextView eight;
    @BindView(R.id.seven)
    TextView seven;
    @BindView(R.id.six)
    TextView six;
    @BindView(R.id.five)
    TextView five;
    @BindView(R.id.four)
    TextView four;
    @BindView(R.id.three)
    TextView three;
    @BindView(R.id.table_chat)
    TextView tableChat;
    @BindView(R.id.et_send)
    EditText etSend;
    @BindView(R.id.btn_send)
    TextView btnSend;
    @BindView(R.id.rival_left_avatar)
    CircleImageView rivalLeftAvatar;
    @BindView(R.id.rival_left_name)
    TextView rivalLeftName;
    @BindView(R.id.rival_left_cards_count)
    TextView rivalLeftCardsCount;
    @BindView(R.id.rival_left_cards_out)
    CardsPack rivalLeftCardsOut;
    @BindView(R.id.rival_right_cards_out)
    CardsPack rivalRightCardsOut;
    @BindView(R.id.rival_left_status)
    TextView rivalLeftStatus;
    @BindView(R.id.rival_right_status)
    TextView rivalRightStatus;
    @BindView(R.id.me_cards_out)
    CardsPack meCardsOut;
    @BindView(R.id.me_no_answer)
    Button meNoAnswer;
    @BindView(R.id.me_status)
    TextView meStatus;
    @BindView(R.id.me_answer)
    Button meAnswer;
    @BindView(R.id.rival_right_avatar)
    CircleImageView rivalRightAvatar;
    @BindView(R.id.rival_right_name)
    TextView rivalRightName;
    @BindView(R.id.rival_right_cards_count)
    TextView rivalRightCardsCount;
    @BindView(R.id.me_cards)
    CardsPack meCards;
    @BindView(R.id.rival_left)
    RelativeLayout rivalLeft;
    @BindView(R.id.ready)
    Button ready;
    @BindView(R.id.first_card)
    ImageView firstCard;
    @BindView(R.id.second_card)
    ImageView secondCard;
    @BindView(R.id.third_card)
    ImageView thirdCard;
    @BindView(R.id.three_cards)
    LinearLayout threeCards;
    @BindView(R.id.me_status_choose)
    LinearLayout meStatusChoose;
    @BindView(R.id.rival_right)
    RelativeLayout rivalRight;
    @BindView(R.id.me_no_grab)
    Button meNoGrab;
    @BindView(R.id.me_grab_status)
    TextView meGrabStatus;
    @BindView(R.id.me_grab)
    Button meGrab;
    @BindView(R.id.me_grab_choose)
    LinearLayout meGrabChoose;
    @BindView(R.id.me_no_double)
    Button meNoDouble;
    @BindView(R.id.me_double_status)
    TextView meDoubleStatus;
    @BindView(R.id.me_double2)
    Button meDouble2;
    @BindView(R.id.me_double5)
    Button meDouble5;
    @BindView(R.id.me_double_choose)
    LinearLayout meDoubleChoose;
    @BindView(R.id.me_double_no_agree)
    Button meDoubleNoAgree;
    @BindView(R.id.me_double_response_status)
    TextView meDoubleResponseStatus;
    @BindView(R.id.me_double_agree)
    Button meDoubleAgree;
    @BindView(R.id.me_double_response)
    LinearLayout meDoubleResponse;
    @BindView(R.id.double_num)
    TextView doubleNum;
    @BindView(R.id.account_money)
    TextView accountMoney;
    @BindView(R.id.landlord_left)
    ImageView landlordLeft;
    @BindView(R.id.landlord_me)
    ImageView landlordMe;
    @BindView(R.id.me_panel)
    LinearLayout mePanel;
    @BindView(R.id.landlord_right)
    ImageView landlordRight;

    private int meSeatNum; //我的座位号
    private int meTableNum = SharedPreferencesUtil.getTableNum(); //进入的房间号
    private HashMap<Integer, String> tablePlayers; //房间里玩家的座位号-用户名
    private int landlordSeatNum; //本局地主的座位号
    private int wagerMultipleNum = 1; //本局的加倍数
    private ArrayList<Integer> myCards = new ArrayList<>(); //我持有的牌
    private ArrayList<Integer> myCardsOut = new ArrayList<>(); //我打出的牌，这里是myOutAdapter的一个外部缓存变量
    private ArrayList<Integer> leftCardsOut = new ArrayList<>(); //上家打出的牌
    private ArrayList<Integer> rightCardsOut = new ArrayList<>(); //下家打出的牌
    private CardsPack.CardsPackAdapter leftAdapter; //上家打出的牌
    private CardsPack.CardsPackAdapter rightAdapter; //下家打出的牌
    private CardsPack.CardsPackAdapter myOutAdapter; //我打出的牌
    private CardsPack.CardsPackAdapter myAdapter; //我持有的牌
    private String left; //上家的用户名（如果有的话）
    private String right; //下家的用户名（如果有的话）
    private boolean isReady = false; //是否点击准备开始游戏
    private Gson gson;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game;
    }

    @Override
    protected void init() {
        gson = new Gson();
        accountMoney.setText(String.format("持有金额：%s", SharedPreferencesUtil.getMoney()));
        //初始化第一层
        meAvatar.setImageURI(SharedPreferencesUtil.getUserAvatar());
        meName.setText(SharedPreferencesUtil.getUsername());
        //TextView滑动条
        tableChat.setMovementMethod(ScrollingMovementMethod.getInstance());
        addDisposable(
                //回退
                RxView.clicks(ivBack)
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> onBackPressed()),
                //聊天发送
                RxView.clicks(btnSend)
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            if(!etSend.getText().toString().equals("")) {
                                ChatMsgRequest chatMsgRequest = new ChatMsgRequest(2,
                                        SharedPreferencesUtil.getUsername(), etSend.getText().toString(), meTableNum);
                                App.getApp().getAgent().send(new JsonReq(12, gson.toJson(chatMsgRequest).
                                        getBytes(StandardCharsets.UTF_8)));
                            }
                        }),
                //准备
                RxView.clicks(ready)
                        .throttleFirst(5, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            if (!isReady) {
                                ReadyRequest readyRequest = new ReadyRequest(true);
                                App.getApp().getAgent().send(new JsonReq(23, gson.toJson(readyRequest).
                                        getBytes(StandardCharsets.UTF_8)));
                            } else {
                                CancelReadyRequest cancelReadyRequest = new CancelReadyRequest(true);
                                App.getApp().getAgent().send(new JsonReq(10, gson.toJson(cancelReadyRequest).
                                        getBytes(StandardCharsets.UTF_8)));
                            }

                        })
        );
        //初始化第二、三层
        //初始化CardsPack，一共4个
        LinearLayoutManager layoutManagerLeft = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManagerLeft.setSmoothScrollbarEnabled(false);
        LinearLayoutManager layoutManagerRight = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManagerRight.setSmoothScrollbarEnabled(false);
        LinearLayoutManager layoutManagerMeOut = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManagerMeOut.setSmoothScrollbarEnabled(false);
        LinearLayoutManager layoutManagerMe = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManagerMe.setSmoothScrollbarEnabled(false);
        //上家出牌
        rivalLeftCardsOut.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if(parent.getChildAdapterPosition(view) != state.getItemCount() - 1) outRect.right = -115;
            }
        });
        rivalLeftCardsOut.setLayoutManager(layoutManagerLeft);
        leftAdapter = rivalLeftCardsOut.new CardsPackAdapter(this, 1, leftCardsOut);
        rivalLeftCardsOut.setAdapter(leftAdapter);
        //下家出牌
        rivalRightCardsOut.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if(parent.getChildAdapterPosition(view) != state.getItemCount() - 1) outRect.right = -115;
            }
        });
        rivalRightCardsOut.setLayoutManager(layoutManagerRight);
        rightAdapter = rivalRightCardsOut.new CardsPackAdapter(this, 1, rightCardsOut);
        rivalRightCardsOut.setAdapter(rightAdapter);
        //我的出牌
        meCardsOut.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if(parent.getChildAdapterPosition(view) != state.getItemCount() - 1) outRect.right = -100;
            }
        });
        meCardsOut.setLayoutManager(layoutManagerMeOut);
        myOutAdapter = meCardsOut.new CardsPackAdapter(this, 1, myCardsOut);
        meCardsOut.setAdapter(myOutAdapter);
        //我持有的牌
        meCards.setLayoutManager(layoutManagerMe);
        myAdapter = meCards.new CardsPackAdapter(this, 2, myCards);
        meCards.setAdapter(myAdapter);
        meCards.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if(parent.getChildAdapterPosition(view) < state.getItemCount() - 1) outRect.right = -150;
            }
        });
        //这是游戏的初始状态，只有一个准备键和上下家的头像、名称、牌图片
        refreshPlayers();
        rivalLeftCardsCount.setText("");
        rivalRightCardsCount.setText("");
        onlyShowReady();
    }

    /**
     * 先不改，看看能不能直接后退到hallfragment上。不过退出房间需要发出一个退出请求。
     */
    @Override
    public void onBackPressed() {
        if(isReady) return;
        super.onBackPressed();
        ExitSeatRequest request = new ExitSeatRequest(meSeatNum);
        App.getApp().getAgent().send(new JsonReq(17, gson.toJson(request).
                getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 获取牌桌聊天信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    @SuppressWarnings("unused")
    public void onChatMsgResponse(ChatMsgResponse response) {
        EventBus.getDefault().removeStickyEvent(response);
        if (response.getChatFlag() == 2) {
            tableChat.append("\n" + response.getUserName() + " 说：" + response.getMsg());
            etSend.setText("");
        }
    }

    /**
     * 发送准备信号后接受服务器响应
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    @SuppressWarnings("unused")
    public void onReadyResponse(ReadyResponse response) {
        EventBus.getDefault().removeStickyEvent(response);
        if (response.isReady()) {
            cancelReady();
        }
    }

    /**
     * 发送取消准备信号后接受服务器响应
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    @SuppressWarnings("unused")
    public void onCancelReadyResponse(CancelReadyResponse response) {
        EventBus.getDefault().removeStickyEvent(response);
        if (response.isCancelReady()) {
            getReady();
        }
    }

    /**
     * 处理新玩家进入房间
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    @SuppressWarnings("unused")
    public void onEnterTableResponse(EnterTableResponse response) {
        EventBus.getDefault().removeStickyEvent(response);
        if (response.isSuccess()) {
            tablePlayers = response.getTablePlayers();
            for (Map.Entry<Integer, String> tmp : tablePlayers.entrySet()) {
                if (tmp.getValue().equals(SharedPreferencesUtil.getUsername()))
                    meSeatNum = tmp.getKey();
            }
            refreshPlayers();
        }
    }

    /**
     * 开始抢地主，同时看到发牌
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    @SuppressWarnings("all")
    public void onGrabLandlordResponse(GrabLandlordResponse response) {
        EventBus.getDefault().removeStickyEvent(response);
        tablePlayers = response.getTablePlayers();
        List<Integer> landlordCards = response.getThreeCards();
        //不能退出房间了
        gone(ready);
        //显示三张地主牌
        visible(threeCards);
        thirdCard.setImageResource(Constants.cardsResource.get(landlordCards.get(landlordCards.size() - 1)));
        secondCard.setImageResource(Constants.cardsResource.get(landlordCards.get(landlordCards.size() - 2)));
        firstCard.setImageResource(Constants.cardsResource.get(landlordCards.get(landlordCards.size() - 3)));
        //显示自己手里的牌
        myCards.addAll(response.getCards());
        LandlordUtil.rankCards(myCards);
        myAdapter.notifyDataSetChanged();
        //现在每人17张牌
        rivalLeftCardsCount.setText(String.format("%s", 17));
        rivalRightCardsCount.setText(String.format("%s", 17));
        landlordSwitch(response.getLandlordSeatNum());
    }

    /**
     * 地主顺移下一位
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    @SuppressWarnings("unused")
    public void onGiveUpLandlordResponse(GiveUpLandlordResponse response) {
        EventBus.getDefault().removeStickyEvent(response);
        landlordSwitch(response.getNextLandlordSeatNum());
    }

    /**
     * 抢地主结束，地主确定。地主选择加倍。
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    @SuppressWarnings("unused")
    public void onEndGrabLandlordResponse(EndGrabLandlordResponse response) {
        EventBus.getDefault().removeStickyEvent(response);
        landlordSeatNum = response.getFinalLandlordSeatNum(); //最终确认了地主
        gone(threeCards);
        if(meSeatNum == landlordSeatNum) {
            //标识自己是地主
            visible(landlordMe);
            gone(meGrabChoose);
            //三张地主牌添加进持有牌中
            myCards.addAll(response.getThreeCards());
            LandlordUtil.rankCards(myCards);
            myAdapter.notifyDataSetChanged();
            //显示加倍面板
            visible(meDoubleChoose);
            LandlordUtil.addTemporaryDisposable(
                    //倒计时结束，自动选择不加倍
                    Flowable.interval(0, 1, TimeUnit.SECONDS)
                            .map(aLong -> 300 - aLong)
                            .take(301)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                meDoubleStatus.setText(String.format("%s", aLong));
                                if (aLong == 0) {
                                    LandlordUtil.clearDisposables();
                                    gone(meDoubleChoose);
                                    LandlordMultipleWagerRequest request = new LandlordMultipleWagerRequest(meSeatNum, 1);
                                    App.getApp().getAgent().send(new JsonReq(20, gson.toJson(request).
                                            getBytes(StandardCharsets.UTF_8)));
                                }
                            }),
                    //不加倍
                    RxView.clicks(meNoDouble)
                            .throttleFirst(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(o -> {
                                LandlordUtil.clearDisposables();
                                gone(meDoubleChoose);
                                LandlordMultipleWagerRequest request = new LandlordMultipleWagerRequest(meSeatNum, 1);
                                App.getApp().getAgent().send(new JsonReq(20, gson.toJson(request).
                                        getBytes(StandardCharsets.UTF_8)));
                            }),
                    //加2倍
                    RxView.clicks(meDouble2)
                            .throttleFirst(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(o -> {
                                LandlordUtil.clearDisposables();
                                gone(meDoubleChoose);
                                LandlordMultipleWagerRequest request = new LandlordMultipleWagerRequest(meSeatNum, 2);
                                App.getApp().getAgent().send(new JsonReq(20, gson.toJson(request).
                                        getBytes(StandardCharsets.UTF_8)));
                            }),
                    //加5倍
                    RxView.clicks(meDouble5)
                            .throttleFirst(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(o -> {
                                LandlordUtil.clearDisposables();
                                gone(meDoubleChoose);
                                LandlordMultipleWagerRequest request = new LandlordMultipleWagerRequest(meSeatNum, 5);
                                App.getApp().getAgent().send(new JsonReq(20, gson.toJson(request).
                                        getBytes(StandardCharsets.UTF_8)));
                            })
            );
        } else if(LandlordUtil.getLeftRivalSeatNum(meSeatNum, meTableNum) == landlordSeatNum) {
            //标识上家是地主
            visible(landlordLeft);
            rivalLeftCardsCount.setText(String.format("%s", 20));
        } else if(LandlordUtil.getRightRivalSeatNum(meSeatNum, meTableNum) == landlordSeatNum) {
            //标识下家是地主
            visible(landlordRight);
            rivalRightCardsCount.setText(String.format("%s", 20));
        }
    }

    /**
     * 农民接收地主反馈的加倍请求
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    @SuppressWarnings("unused")
    public void onLandlordMultipleWagerResponse(LandlordMultipleWagerResponse response) {
        EventBus.getDefault().removeStickyEvent(response);
        visible(mePanel);
        visible(meDoubleResponse);
        int doubleNum = response.getMultipleNum();
        LandlordUtil.addTemporaryDisposable(
                //倒计时结束，自动同意加倍
                Flowable.interval(0, 1, TimeUnit.SECONDS)
                        .map(aLong -> 300 - aLong)
                        .take(301)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            meDoubleResponseStatus.setText(String.format("%s", aLong));
                            if (aLong == 0) {
                                LandlordUtil.clearDisposables();
                                gone(meDoubleResponse);
                                MultipleWagerRequest request = new MultipleWagerRequest(-1);
                                App.getApp().getAgent().send(new JsonReq(22, gson.toJson(request).
                                        getBytes(StandardCharsets.UTF_8)));
                            }
                        }),
                //同意加倍
                RxView.clicks(meDoubleAgree)
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            LandlordUtil.clearDisposables();
                            gone(meDoubleResponse);
                            MultipleWagerRequest request = new MultipleWagerRequest(1);
                            App.getApp().getAgent().send(new JsonReq(22, gson.toJson(request).
                                    getBytes(StandardCharsets.UTF_8)));
                        }),
                //不同意加倍
                RxView.clicks(meDoubleNoAgree)
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            LandlordUtil.clearDisposables();
                            gone(meDoubleResponse);
                            MultipleWagerRequest request = new MultipleWagerRequest(-1);
                            App.getApp().getAgent().send(new JsonReq(22, gson.toJson(request).
                                    getBytes(StandardCharsets.UTF_8)));
                        })
        );
    }

    /**
     * 最终确认的加倍请求
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    @SuppressWarnings("unused")
    public void onMultipleWagerResponse(MultipleWagerResponse response) {
        EventBus.getDefault().removeStickyEvent(response);
        wagerMultipleNum = response.getMultipleNum();
        doubleNum.setText(String.format("当前倍数是：%s", wagerMultipleNum));
        if (meSeatNum == landlordSeatNum) { //我是地主，我先出牌
            throwCards(true);
        } else { //我不是地主，我等上家出牌
            gone(meStatusChoose);
        }
    }

    /**
     * 开始游戏，等待上家出牌
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    @SuppressWarnings("unused")
    public void onCardsOutResponse(CardsOutResponse response) {
        EventBus.getDefault().removeStickyEvent(response);
        //接牌出牌情景
        if (response.getFromSeatNum() == meSeatNum &&
                response.getToSeatNum() == LandlordUtil.getRightRivalSeatNum(meSeatNum, meTableNum)) { //我出牌给下家
            gone(meStatusChoose);
            if (response.isPass()) {
                gone(meCardsOut);
            } else {
                //显示我的出牌
                visible(meCardsOut);
                myCardsOut.clear();
                myCardsOut.addAll(response.getCardsOut());
                myOutAdapter.notifyDataSetChanged();
                myAdapter.throwCardsAndRefresh(myCardsOut);
                myCards = myAdapter.getCardsKey(); //刷新牌数，为0的时候触发胜利
                refreshTopTable(response.getThrowOutCards());
            }
        } else if (response.getFromSeatNum() == LandlordUtil.getLeftRivalSeatNum(meSeatNum, meTableNum) &&
                response.getToSeatNum() == meSeatNum) {//上家出牌给我
            if(response.isPass()) {
                gone(rivalLeftCardsOut);
                visible(rivalLeftStatus);
                rivalLeftStatus.setText("不出");
                //用leftCardsOut存储下家的牌
                leftCardsOut.clear();
                leftCardsOut.addAll(response.getCardsOut());
                throwCards(false);
                refreshTopTable(response.getThrowOutCards());
                rivalRightCardsCount.setText(
                        String.format("%s", response.getPlayersCardsCount().get(LandlordUtil.getRightRivalSeatNum(meSeatNum, meTableNum))));
                if(response.isAllPass()) { //连续两个pass，我出的牌没人要的起
                    gone(meCardsOut);
                    visible(meStatusChoose);
                    throwCards(true);
                }
            } else {
                gone(rivalLeftStatus);
                //显示上家出的牌
                visible(rivalLeftCardsOut);
                leftCardsOut.clear();
                leftCardsOut.addAll(response.getCardsOut());
                leftAdapter.notifyDataSetChanged();
                throwCards(false);
                refreshTopTable(response.getThrowOutCards());
                rivalLeftCardsCount.setText(
                        String.format("%s", response.getPlayersCardsCount().get(LandlordUtil.getLeftRivalSeatNum(meSeatNum, meTableNum))));
            }
        } else if (response.getFromSeatNum() == LandlordUtil.getRightRivalSeatNum(meSeatNum, meTableNum) &&
                response.getToSeatNum() == LandlordUtil.getLeftRivalSeatNum(meSeatNum, meTableNum)) { //下家出牌给上家
            if (response.isPass()) {
                gone(rivalRightCardsOut);
                visible(rivalRightStatus);
                rivalRightStatus.setText("不出");
            } else {
                gone(rivalRightStatus);
                visible(rivalRightCardsOut);
                rightCardsOut.clear();
                rightCardsOut.addAll(response.getCardsOut());
                rightAdapter.notifyDataSetChanged();
                refreshTopTable(response.getThrowOutCards());
                rivalRightCardsCount.setText(
                        String.format("%s", response.getPlayersCardsCount().get(LandlordUtil.getRightRivalSeatNum(meSeatNum, meTableNum) - 1)));
            }
        }
        //炸弹加1倍，双王加2倍
        if(LandlordUtil.getBomb(response.getCardsOut()) != -1) {
            wagerMultipleNum += 1;
            doubleNum.setText(String.format("当前倍数是：%s", wagerMultipleNum));
        }
        if(LandlordUtil.getTwoKing(response.getCardsOut())) {
            wagerMultipleNum += 2;
            doubleNum.setText(String.format("当前倍数是：%s", wagerMultipleNum));
        }
        //我的牌出完了
        if (myCards.size() == 0) {
            EndGameRequest request = new EndGameRequest(meSeatNum);
            App.getApp().getAgent().send(new JsonReq(13, gson.toJson(request).
                    getBytes(StandardCharsets.UTF_8)));
        }
    }

    /**
     * 有玩家退出房间
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    @SuppressWarnings("unused")
    public void onExitSeatResponse(ExitSeatResponse response) {
        EventBus.getDefault().removeStickyEvent(response);
        int exit = response.getSeatNum();
        tablePlayers = response.getTablePlayers();
        if (exit == LandlordUtil.getLeftRivalSeatNum(meSeatNum, meTableNum)) {
            gone(rivalLeft);
            gone(rivalLeftCardsOut);
            gone(rivalLeftStatus);
        }
        if (exit == LandlordUtil.getRightRivalSeatNum(meSeatNum, meTableNum)) {
            gone(rivalRight);
            gone(rivalRightCardsOut);
            gone(rivalRightStatus);
        }
    }

    /**
     * 有玩家的牌出完了
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    @SuppressWarnings("unused")
    public void onEndGameResponse(EndGameResponse response) {
        EventBus.getDefault().removeStickyEvent(response);
        //这里直接弹出一个popupwindow，显示加分和扣分情况
        int winner = response.getWinnerSeatNum();
        EndGamePopupWindow popupWindow;
        View contentView = View.inflate(this, R.layout.endgame_window_popup, null);
        if(winner == meSeatNum) {
            if(meSeatNum == landlordSeatNum) {
                popupWindow = new EndGamePopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                        true, true, wagerMultipleNum, SharedPreferencesUtil.getMoney());
            } else {
                popupWindow = new EndGamePopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                        false, true, wagerMultipleNum, SharedPreferencesUtil.getMoney());
            }
        } else {
            if(meSeatNum == landlordSeatNum) {
                popupWindow = new EndGamePopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                        true, false, wagerMultipleNum, SharedPreferencesUtil.getMoney());
            } else {
                if(winner == landlordSeatNum)
                    popupWindow = new EndGamePopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                            false, false, wagerMultipleNum, SharedPreferencesUtil.getMoney());
                else popupWindow = new EndGamePopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                        false, true, wagerMultipleNum, SharedPreferencesUtil.getMoney());
            }
        }
        accountMoney.setText(String.format("持有金额：%s",SharedPreferencesUtil.getMoney()));
        View rootView = View.inflate(this, getLayoutId(), null);
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        //下面这些代码是要放到popupwindow点击继续游戏的回调中的
        gone(landlordMe);
        gone(landlordLeft);
        gone(landlordRight);
        refreshPlayers();
        visible(ivBack);
        refreshTopTable(null);
        rivalLeftCardsCount.setText("");
        rivalRightCardsCount.setText("");
        myCards.clear();
        myAdapter.notifyDataSetChanged();
        //这里不用再设置点击ready跳转开始游戏了，前面的init()已经设置好了
        onlyShowReady();
    }

    /**
     * 刷新玩家信息
     */
    private void refreshPlayers() {
        if(tablePlayers != null) {
            left = tablePlayers.get(LandlordUtil.getLeftRivalSeatNum(meSeatNum, meTableNum));
            right = tablePlayers.get(LandlordUtil.getRightRivalSeatNum(meSeatNum, meTableNum));
        }
        if(left == null) {
            ToastUtil.showCenterSingleToast("上家为空");
            return;
        }
        if(right == null) {
            ToastUtil.showCenterSingleToast("下家为空");
            return;
        }
        if (left.equals("")) {
            gone(rivalLeftName);
            gone(rivalLeftAvatar);
        } else {
            visible(rivalLeftName);
            visible(rivalLeftAvatar);
            rivalLeftName.setText(left);
        }
        if (right.equals("")) {
            gone(rivalRightName);
            gone(rivalRightAvatar);
        } else {
            visible(rivalRightName);
            visible(rivalRightAvatar);
            rivalRightName.setText(right);
        }
    }

    /**
     * 地主顺时针切换
     */
    private void landlordSwitch(int landlordSeatNum) {
        //显示me_panel，隐藏其5个面板
        visible(mePanel);
        gone(meCardsOut);
        gone(meStatusChoose);
        gone(meDoubleChoose);
        gone(meDoubleResponse);
        gone(meGrabChoose);
        rivalRightStatus.setText("");
        rivalLeftStatus.setText("");
        if (landlordSeatNum == meSeatNum) { //我是地主
            //隐藏上下家状态
            gone(rivalLeftStatus);
            gone(rivalRightStatus);
            //只显示抢地主
            visible(meGrabChoose);
            LandlordUtil.addTemporaryDisposable(
                    //倒计时结束，自动放弃抢地主
                    Flowable.interval(0, 1, TimeUnit.SECONDS)
                            .map(aLong -> 300 - aLong)
                            .take(301)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                meGrabStatus.setText(String.format("%s", aLong));
                                if (aLong == 0) {
                                    LandlordUtil.clearDisposables();
                                    gone(meGrabChoose);
                                    GiveUpLandlordRequest request = new GiveUpLandlordRequest(meSeatNum);
                                    App.getApp().getAgent().send(new JsonReq(18, gson.toJson(request).
                                            getBytes(StandardCharsets.UTF_8)));
                                }
                            }),
                    //抢地主成功
                    RxView.clicks(meGrab)
                            .throttleFirst(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(o -> {
                                LandlordUtil.clearDisposables();
                                gone(meGrabChoose);
                                EndGrabLandlordRequest request = new EndGrabLandlordRequest(meSeatNum);
                                App.getApp().getAgent().send(new JsonReq(14, gson.toJson(request).
                                        getBytes(StandardCharsets.UTF_8)));
                            }),
                    //放弃抢地主
                    RxView.clicks(meNoGrab)
                            .throttleFirst(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(o -> {
                                LandlordUtil.clearDisposables();
                                gone(meGrabChoose);
                                GiveUpLandlordRequest request = new GiveUpLandlordRequest(meSeatNum);
                                App.getApp().getAgent().send(new JsonReq(18, gson.toJson(request).
                                        getBytes(StandardCharsets.UTF_8)));
                            })
            );
        } else if (landlordSeatNum == LandlordUtil.getRightRivalSeatNum(meSeatNum, meTableNum)) { //右家是地主
            gone(mePanel);
            visible(rivalRightStatus);
            rivalRightStatus.setText("地主");
        } else if (landlordSeatNum == LandlordUtil.getLeftRivalSeatNum(meSeatNum, meTableNum)) { //左家是地主
            gone(mePanel);
            visible(rivalLeftStatus);
            rivalLeftStatus.setText("地主");
        }
    }

    /**
     * 出牌。分为第一次出牌和接上家的牌
     */
    private void throwCards(boolean isFirst) {
        visible(meStatusChoose);
        if (isFirst) {
            gone(meNoAnswer);
            LandlordUtil.clearDisposables();
            LandlordUtil.addTemporaryDisposable(
                    //倒计时结束，直接出第一张牌
                    Flowable.interval(0, 1, TimeUnit.SECONDS)
                            .map(aLong -> 450 - aLong)
                            .take(451)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                meStatus.setText(String.format("%s", aLong));
//                                if (aLong == 0) {
//                                    LandlordUtil.clearDisposables();
//                                    gone(meStatusChoose);
//                                    Integer firstCard = myCards.get(0);
//                                    myCardsOut.clear();
//                                    myCardsOut.add(firstCard);
//                                    myCards.remove(0);
//                                    myAdapter.notifyDataSetChanged();
//                                    myOutAdapter.notifyDataSetChanged();
//                                    CardsOutRequest request = new CardsOutRequest(false, meSeatNum,
//                                            LandlordUtil.getRightRivalSeatNum(meSeatNum, tablePlayers.keySet()), myCardsOut);
//                                    GameClient.getClient().send(request);
//                                }
                            }),
                    //出牌
                    RxView.clicks(meAnswer)
                            .throttleFirst(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(o -> {
                                ArrayList<Integer> cards = myAdapter.getCardsOutKey();
                                //洗牌后发出CardsOutRequest，myAdapter暂时不减牌，myOutAdapter暂时不显示
                                if(cards.size() != 0) {
                                    boolean isAvailable = LandlordUtil.isAvailable(cards);
                                    Logger.i("出牌：" + cards);
                                    if(isAvailable) {
                                        LandlordUtil.clearDisposables();
                                        gone(meStatusChoose);
                                        CardsOutRequest request = new CardsOutRequest(false, meSeatNum,
                                                LandlordUtil.getRightRivalSeatNum(meSeatNum, meTableNum), cards);
                                        App.getApp().getAgent().send(new JsonReq(11, gson.toJson(request).
                                                getBytes(StandardCharsets.UTF_8)));
                                        myAdapter.clearCardsOutKey();
                                    } else {
                                        ToastUtil.showCenterSingleToast("出牌错误！");
                                    }
                                } else {
                                    ToastUtil.showCenterSingleToast("出牌不能为空");
                                }
                            })
            );
        } else {
            //恢复上一盘隐藏的不出选项
            visible(meNoAnswer);
            LandlordUtil.clearDisposables();
            LandlordUtil.addTemporaryDisposable(
                    //倒计时结束，自动选择不出牌
                    Flowable.interval(0, 1, TimeUnit.SECONDS)
                            .map(aLong -> 900 - aLong)
                            .take(901)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                meStatus.setText(String.format("%s", aLong));
//                                if (aLong == 0) {
//                                    LandlordUtil.clearDisposables();
//                                    gone(meStatusChoose);
//                                    CardsOutRequest request = new CardsOutRequest(true, meSeatNum,
//                                            LandlordUtil.getRightRivalSeatNum(meSeatNum, tablePlayers.keySet()), null);
//                                    GameClient.getClient().send(request);
//                                }
                            }),
                    //出牌
                    RxView.clicks(meAnswer)
                            .throttleFirst(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(o -> {
                                ArrayList<Integer> cards = myAdapter.getCardsOutKey();
                                if(cards.size() != 0) {
                                    boolean isCoverable = LandlordUtil.isCoverable(leftCardsOut, cards);
                                    Logger.i("上家的牌：" + leftCardsOut);
                                    Logger.i("我的出牌："  + cards);
                                    if (isCoverable) { //只有前端判断出接牌逻辑
                                        LandlordUtil.clearDisposables();
                                        gone(meStatusChoose);
                                        CardsOutRequest request = new CardsOutRequest(false, meSeatNum,
                                                LandlordUtil.getRightRivalSeatNum(meSeatNum, meTableNum), cards);
                                        App.getApp().getAgent().send(new JsonReq(11, gson.toJson(request).
                                                getBytes(StandardCharsets.UTF_8)));
                                        myAdapter.clearCardsOutKey();
                                    } else {
                                        ToastUtil.showCenterSingleToast("管不上啊，再看看先！！");
                                    }
                                } else {
                                    ToastUtil.showCenterSingleToast("出牌不能为空");
                                }
                            }),
                    //不出牌
                    RxView.clicks(meNoAnswer)
                            .throttleFirst(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(o -> {
                                LandlordUtil.clearDisposables();
                                gone(meStatusChoose);
                                CardsOutRequest request = new CardsOutRequest(true,
                                        meSeatNum, LandlordUtil.getRightRivalSeatNum(meSeatNum, meTableNum), leftCardsOut);
                                App.getApp().getAgent().send(new JsonReq(11, gson.toJson(request).
                                        getBytes(StandardCharsets.UTF_8)));
                            })
            );
        }
    }

    /**
     * 刷新顶部的表格，表示牌桌上已经打出来的牌
     */
    private void refreshTopTable(HashMap<Integer, Integer> throwOutCards) {
        if (throwOutCards != null) {
            three.setText(String.format("%s", throwOutCards.get(1)));
            four.setText(String.format("%s", throwOutCards.get(2)));
            five.setText(String.format("%s", throwOutCards.get(3)));
            six.setText(String.format("%s", throwOutCards.get(4)));
            seven.setText(String.format("%s", throwOutCards.get(5)));
            eight.setText(String.format("%s", throwOutCards.get(6)));
            nine.setText(String.format("%s", throwOutCards.get(7)));
            ten.setText(String.format("%s", throwOutCards.get(8)));
            Jake.setText(String.format("%s", throwOutCards.get(9)));
            Queen.setText(String.format("%s", throwOutCards.get(10)));
            King.setText(String.format("%s", throwOutCards.get(11)));
            Ace.setText(String.format("%s", throwOutCards.get(12)));
            two.setText(String.format("%s", throwOutCards.get(13)));
            joker1.setText(String.format("%s", throwOutCards.get(14)));
            joker2.setText(String.format("%s", throwOutCards.get(15)));
        } else {
            three.setText("0");
            four.setText("0");
            five.setText("0");
            six.setText("0");
            seven.setText("0");
            eight.setText("0");
            nine.setText("0");
            ten.setText("0");
            Jake.setText("0");
            Queen.setText("0");
            King.setText("0");
            Ace.setText("0");
            two.setText("0");
            joker1.setText("0");
            joker2.setText("0");
        }
    }

    private void onlyShowReady() {
        gone(landlordMe);
        gone(rivalLeftCardsOut);
        gone(rivalRightCardsOut);
        gone(rivalLeftStatus);
        gone(rivalRightStatus);
        gone(threeCards);
        gone(mePanel);
        visible(ready);
        getReady();
    }

    private void getReady() {
        ready.setText(R.string.ready);
        isReady = false;
        visible(ivBack);
    }

    private void cancelReady() {
        ready.setText(R.string.not_ready);
        isReady = true;
        gone(ivBack);
    }
}
