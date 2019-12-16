package com.gram.landlord_client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gram.landlord_client.R;
import com.gram.landlord_client.util.NotificationUtil;
import com.jakewharton.rxbinding2.view.RxView;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class WelcomeActivity extends AppCompatActivity {
    @BindView(R.id.iv_welcome)
    ImageView ivWelcome;
    @BindView(R.id.tv_count_down)
    TextView tvCountDown;
    @BindView(R.id.ll_count_down)
    LinearLayout llCountDown;
    private Unbinder unbinder;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        unbinder = ButterKnife.bind(this);
        compositeDisposable = new CompositeDisposable();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearDisposable();
        unbinder.unbind();
    }

    /**
     * 添加Rx订阅
     */
    private void addDisposable(Disposable... disposables) {
        if (compositeDisposable == null) compositeDisposable = new CompositeDisposable();
        for (Disposable disposable : disposables) {
            compositeDisposable.add(disposable);
        }
    }

    /**
     * 取消所有Rx订阅
     */
    private void clearDisposable() {
        if (compositeDisposable != null) compositeDisposable.clear();
    }

    private void init() {
        ivWelcome.setImageResource(R.mipmap.ic_default_bg);
        addDisposable(
                Flowable.interval(0, 1, TimeUnit.SECONDS)
                        .map(aLong -> 5 - aLong)
                        .take(6)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            tvCountDown.setText(aLong.toString());
                            if(aLong == 0) redirect();
                        }),
                RxView.clicks(llCountDown)
                        .throttleFirst(3, TimeUnit.SECONDS)
                        .subscribe(o -> redirect())
        );
    }

    private void redirect() {
//        NotificationUtil.jumpActivity(this, LoginActivity.class, "跳转到LoginActivity", 1);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
