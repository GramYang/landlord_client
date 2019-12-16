package com.gram.landlord_client.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gram.landlord_client.App;
import com.gram.landlord_client.R;
import com.gram.landlord_client.base.BaseActivity;
import com.gram.landlord_client.event.PictureCompressEvent;
import com.gram.landlord_client.fragment.HallFragment;
import com.gram.landlord_client.fragment.RegionFragment;
import com.gram.landlord_client.sdk.entity.JsonReq;
import com.gram.landlord_client.sdk.protocol.request.ExitHallRequest;
import com.gram.landlord_client.sdk.protocol.request.LoginRequest;
import com.gram.landlord_client.sdk.protocol.request.UserInfoRequest;
import com.gram.landlord_client.sdk.protocol.response.UserInfoResponse;
import com.gram.landlord_client.util.AvatarChangeUtil;
import com.gram.landlord_client.util.SharedPreferencesUtil;
import com.gram.landlord_client.util.ToastUtil;
import com.gram.landlord_client.widget.AvatarPopupWindow;
import com.gram.landlord_client.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private CircleImageView userAvatar;
    private TextView name;
    private TextView win;
    private TextView lose;
    private TextView money;
    private AvatarPopupWindow avatarPopupWindow;
    private HallFragment hall;
    private RegionFragment region;
    //拍照照片的uri
    private Uri cameraUri = null;
    //裁剪图片的uri
    private Uri cropUri = null;

    public static final int REQUEST_IMAGE_GET = 0;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_IMAGE_CROP = 2;

    private long firstPressedTime;
    private boolean isRegion = false;
    private Gson gson;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        gson = new Gson();
        toolbar.setTitle("游戏分区");
        setSupportActionBar(toolbar);
        region = new RegionFragment();
        hall = new HallFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_content, region)
                .add(R.id.fl_content, hall)
                .commit();
        hallToRegion();
        navView.setNavigationItemSelectedListener(menuItem -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            switch (menuItem.getItemId()) {
                case R.id.item_info:
//                    startActivity(new Intent(this, InfoActivity.class));
                    return true;
                case R.id.item_settings:
//                    startActivity(new Intent(this, SettingsActivity.class));
                    return true;
            }
            return false;
        });
        View headerView = navView.getHeaderView(0);
        userAvatar = headerView.findViewById(R.id.user_avatar);
        name = headerView.findViewById(R.id.tv_name);
        win = headerView.findViewById(R.id.tv_win);
        lose = headerView.findViewById(R.id.tv_lose);
        money = headerView.findViewById(R.id.tv_money);
        name.setText(SharedPreferencesUtil.getUsername());
        userAvatar.setOnClickListener(v -> {
            View contentView = View.inflate(this, R.layout.avatar_window_popup, null);
            avatarPopupWindow = new AvatarPopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, this);
            View rootView = View.inflate(this, getLayoutId(), null);
            avatarPopupWindow.showAtLocation(rootView,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        });
        UserInfoRequest request = new UserInfoRequest(SharedPreferencesUtil.getUsername());
        App.getApp().getAgent().send(new JsonReq(24,gson.toJson(request).getBytes(StandardCharsets.UTF_8)));
        //申请大厅数据
        LoginRequest request1 = new LoginRequest(SharedPreferencesUtil.getUsername(), SharedPreferencesUtil.getPassword());
        App.getApp().getAgent().send(new JsonReq(21, gson.toJson(request1).getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    avatarPopupWindow.dismiss();
                    AvatarChangeUtil.selectPicture(this);
                } else {
                    avatarPopupWindow.dismiss();
                }
                break;
            case 300:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    avatarPopupWindow.dismiss();
                    AvatarChangeUtil.takePicture(this);
                } else {
                    avatarPopupWindow.dismiss();
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPictureCompressEvent(PictureCompressEvent pictureCompressEvent) {
        //压缩后的图片不大于1MB
        Uri compressUri = AvatarChangeUtil.compress(this, pictureCompressEvent.getPictureUri(), 1024);
        if(compressUri != null) {
            SharedPreferencesUtil.saveUserAvatar(compressUri);
            userAvatar.setImageURI(compressUri);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                //相册选取，不需要压缩，但是需要剪切
                case REQUEST_IMAGE_GET:
                    if(data == null) return;
                    Uri pictureUri = data.getData();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        String path = AvatarChangeUtil.formatUri(this, pictureUri);
                        pictureUri = FileProvider.getUriForFile(this, AvatarChangeUtil.FILEPROVIDER, new File(path));
                        AvatarChangeUtil.crop(this, pictureUri);
                    } else {
                        AvatarChangeUtil.crop(this, pictureUri);
                    }
                    break;
                //拍照，拍照不需要剪切，但是需要压缩
                case REQUEST_IMAGE_CAPTURE:
                    if(cameraUri != null)
                        //压缩需要异步，不能在UI线程
                        EventBus.getDefault().post(new PictureCompressEvent(cameraUri));
                    break;
                //crop进行图片剪切
                case REQUEST_IMAGE_CROP:
                    if(cropUri != null) userAvatar.setImageURI(cropUri);
                    break;
            }
        }
    }

    /**
     * 设置hallfragment回退退出大厅
     */
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - firstPressedTime < 2000) {
            if(!isRegion) {
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                for(Fragment fragment : fragments) {
                    if(fragment instanceof HallFragment) {
                        ExitHallRequest request = new ExitHallRequest(SharedPreferencesUtil.getUsername());
                        App.getApp().getAgent().send(new JsonReq(16, gson.toJson(request).getBytes(StandardCharsets.UTF_8)));
                        hallToRegion();
                        return;
                    }
                }
            } else {
                super.onBackPressed();
            }
        } else {
            ToastUtil.showCenterSingleToast( "再按一次退出");
            firstPressedTime = System.currentTimeMillis();
        }

    }

    /**
     * 玩家信息反馈
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfoResponse(UserInfoResponse response) {
        if(response.getUserName().equals(SharedPreferencesUtil.getUsername())) {
            win.append(response.getWin());
            SharedPreferencesUtil.saveWin(response.getWin());
            lose.append(response.getLose());
            SharedPreferencesUtil.saveLose(response.getLose());
            money.append(response.getMoney());
            SharedPreferencesUtil.saveMoney(response.getMoney());
        }
    }

    public void setCropUri(Uri cropUri) {
        this.cropUri = cropUri;
    }

    public void setCameraUri(Uri cameraUri) {
        this.cameraUri = cameraUri;
    }

    public void regionToHall() {
        getSupportFragmentManager().beginTransaction().hide(region).show(hall).commit();
        isRegion = false;
        toolbar.setTitle("游戏大厅");
    }

    public void hallToRegion() {
        getSupportFragmentManager().beginTransaction().hide(hall).show(region).commit();
        isRegion = true;
        toolbar.setTitle("游戏分区");
    }

}
