package com.gram.landlord_client.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gram.landlord_client.R;
import com.gram.landlord_client.activity.MainActivity;
import com.gram.landlord_client.adapter.RegionAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 游戏选区
 */
public class RegionFragment extends Fragment {
    private Unbinder unbinder;
    protected Context context;
    private CompositeDisposable compositeDisposable;

    @BindView(R.id.rv_region)
    RecyclerView rvRegion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getContentViewId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDestroy() {
        clearDisposable();
        unbinder.unbind();
        super.onDestroy();

    }

    /**
     * 添加Rx订阅
     */
    private void addDisposable(Disposable... disposables) {
        if(compositeDisposable == null) compositeDisposable = new CompositeDisposable();
        for(Disposable disposable : disposables) {
            compositeDisposable.add(disposable);
        }
    }

    /**
     * 取消所有Rx订阅
     */
    private void clearDisposable() {
        if(compositeDisposable != null) compositeDisposable.clear();
    }

    private int getContentViewId() {
        return R.layout.fragment_region;
    }

    private void init() {
        rvRegion.setLayoutManager(new LinearLayoutManager(context));
        rvRegion.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL));
        RegionAdapter regionAdapter = new RegionAdapter(context);
        regionAdapter.setOnItemClickListener(((holder) -> {
            if(context != null) {
                ((MainActivity)context).regionToHall();
            }
        }));
        rvRegion.setAdapter(regionAdapter);
    }

}
