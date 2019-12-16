package com.gram.landlord_client.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import com.gram.landlord_client.R;
import com.gram.landlord_client.util.AvatarChangeUtil;

/**
 * 头像选择窗口
 */
public class AvatarPopupWindow extends PopupWindow {
    private View contentView;
    private Context context;

    public AvatarPopupWindow(View contentView, int width, int height, Context context) {
        super(contentView, width, height);
        this.contentView = contentView;
        this.context = context;
        init();
    }

    private void init() {
        Button camera = contentView.findViewById(R.id.btn_camera);
        Button select = contentView.findViewById(R.id.btn_select);
        Button cancel = contentView.findViewById(R.id.btn_cancel);
        //5.0以上应该动态申请相册和相机权限
        select.setOnClickListener((v1)-> {
            if(ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity)context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
            } else {
                dismiss();
                AvatarChangeUtil.selectPicture((Activity)context);
            }
        });
        camera.setOnClickListener((v2)-> {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //动态申请权限，申请码为300
                ActivityCompat.requestPermissions((Activity)context,
                        new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
            } else {
                dismiss();
                AvatarChangeUtil.takePicture((Activity)context);
            }
        });
        cancel.setOnClickListener(v-> dismiss());
        //导入布局
        setContentView(contentView);
        //动画效果
        setAnimationStyle(R.style.popwindow_anim_style);
        //设置控件
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x0000000);
        setBackgroundDrawable(dw);
        contentView.setOnTouchListener((v, event) -> {
            int height = contentView.findViewById(R.id.ll_pop).getTop();
            int y = (int) event.getY();
            if(event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick();
                if(y < height) dismiss();
            }
            return true;
        });
    }

}
