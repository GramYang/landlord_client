package com.gram.landlord_client.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import org.greenrobot.eventbus.EventBus;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearDisposable();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    protected abstract int getLayoutId();

    protected void init() {}

    protected void addDisposable(Disposable... disposables) {
        if(compositeDisposable == null) compositeDisposable = new CompositeDisposable();
        for(Disposable disposable : disposables) {
            compositeDisposable.add(disposable);
        }
    }

    public void clearDisposable() {
        if(compositeDisposable != null) compositeDisposable.clear();
    }

    /**
     * 根据resid隐藏view
     */
    protected void gone(@IdRes int... id) {
        if(id != null && id.length > 0) {
            for(int resId : id) {
                View view = findViewById(resId);
                if(view != null) gone(view);
            }
        }
    }

    /**
     * 根据view实例隐藏view
     */
    protected void gone(View... views) {
        if(views != null && views.length > 0) {
            for(View view : views) {
                if(view != null && view.getVisibility() == View.VISIBLE) view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 根据resid显示view
     */
    protected void visible(@IdRes int... id) {
        if(id != null && id.length > 0) {
            for(int resId : id) {
                View view = findViewById(resId);
                if(view != null) visible(view);
            }
        }
    }

    /**
     * 根据view实例显示view
     */
    protected void visible(View... views) {
        if(views != null && views.length > 0) {
            for(View view : views) {
                if(view != null && view.getVisibility() == View.GONE) view.setVisibility(View.VISIBLE);
            }
        }
    }
}
