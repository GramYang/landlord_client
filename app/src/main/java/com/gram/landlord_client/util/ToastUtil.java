package com.gram.landlord_client.util;

import android.view.Gravity;
import android.widget.Toast;

import com.gram.landlord_client.App;


public class ToastUtil {
    private static Toast mToast;

    /**********************
     * 非连续弹出的Toast
     ***********************/
    public static void showSingleToast(int resId) { //R.string.**
        getSingleToast(resId, Toast.LENGTH_SHORT).show();
    }

    public static void showSingleToast(String text) {
        getSingleToast(text, Toast.LENGTH_SHORT).show();
    }

    public static void showSingleLongToast(int resId) {
        getSingleToast(resId, Toast.LENGTH_LONG).show();
    }

    public static void showSingleLongToast(String text) {
        getSingleToast(text, Toast.LENGTH_LONG).show();
    }

    /***********************
     * 连续弹出的Toast 位置在中央
     ************************/
    public static void showCenterToast(int resId) {
        getCenterToast(resId, Toast.LENGTH_SHORT).show();
    }

    public static void showCenterToast(String text) {
        getCenterToast(text, Toast.LENGTH_SHORT).show();
    }

    public static void showCenterLongToast(int resId) {
        getCenterToast(resId, Toast.LENGTH_LONG).show();
    }

    public static void showCenterLongToast(String text) {
        getCenterToast(text, Toast.LENGTH_LONG).show();
    }

    /**********************
     * 非连续弹出的Toast 位置在中央
     ***********************/
    public static void showCenterSingleToast(int resId) { //R.string.**
        getCenterSingleToast(resId, Toast.LENGTH_SHORT).show();
    }

    public static void showCenterSingleToast(String text) {
        getCenterSingleToast(text, Toast.LENGTH_SHORT).show();
    }

    public static void showCenterSingleLongToast(int resId) {
        getCenterSingleToast(resId, Toast.LENGTH_LONG).show();
    }

    public static void showCenterSingleLongToast(String text) {
        getCenterSingleToast(text, Toast.LENGTH_LONG).show();
    }

    /***********************
     * 连续弹出的Toast
     ************************/
    public static void showToast(int resId) {
        getToast(resId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(String text) {
        getToast(text, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(int resId) {
        getToast(resId, Toast.LENGTH_LONG).show();
    }

    public static void showLongToast(String text) {
        getToast(text, Toast.LENGTH_LONG).show();
    }

    public static Toast getSingleToast(int resId, int duration) { // 连续调用不会连续弹出，只是替换文本
        return getSingleToast(App.getApp().getResources().getText(resId).toString(), duration);
    }

    public static Toast getSingleToast(String text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(App.getApp(), text, duration);
        } else {
            mToast.setText(text);
        }
        return mToast;
    }

    public static Toast getToast(int resId, int duration) { // 连续调用会连续弹出
        return getToast(App.getApp().getResources().getText(resId).toString(), duration);
    }

    public static Toast getToast(String text, int duration) {
        return Toast.makeText(App.getApp(), text, duration);
    }

    public static Toast getCenterSingleToast(int resId, int duration) { // 连续调用不会连续弹出，只是替换文本
        return getCenterSingleToast(App.getApp().getResources().getText(resId).toString(), duration);
    }

    public static Toast getCenterSingleToast(String text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(App.getApp(), text, duration);
        } else {
            mToast.setText(text);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        return mToast;
    }

    public static Toast getCenterToast(int resId, int duration) { // 连续调用会连续弹出
        return getCenterToast(App.getApp().getResources().getText(resId).toString(), duration);
    }

    public static Toast getCenterToast(String text, int duration) {
        mToast = Toast.makeText(App.getApp(), text, duration);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        return mToast;
    }
}
