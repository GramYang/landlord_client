package com.gram.landlord_client.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.gram.landlord_client.App;

import java.util.Map;

public class SharedPreferencesUtil {
    private static final String FILE_NAME = "gram_landlord_data";
    private static final String USERNAME = "username";
    private static final String USER_AVATAR = "user_avatar";
    private static final String PASSWORD = "password";
    private static final String WIN = "win";
    private static final String LOSE = "lose";
    private static final String MONEY = "money";
    private static final String TABLE_NUM = "table_num";

    public static String getUsername() {
        return (String)get(App.getApp(), USERNAME, "");
    }

    public static void saveUsername(String name) {
        put(App.getApp(), USERNAME, name);
    }

    public static String getPassword() {return (String)get(App.getApp(), PASSWORD, "");}

    public static void savePassword(String password) {put(App.getApp(), PASSWORD, password);}

    public static void saveUserAvatar(Uri uri) {put(App.getApp(), USER_AVATAR, uri.toString());}

    public static Uri getUserAvatar() {return Uri.parse((String)get(App.getApp(), USER_AVATAR, ""));}

    public static int getWin() {
        return (Integer)get(App.getApp(), WIN, "");
    }

    public static void saveWin(String win) {
        put(App.getApp(), WIN, Integer.valueOf(win));
    }

    public static int getLose() {
        return (Integer)get(App.getApp(), LOSE, "");
    }

    public static void saveLose(String lose) {
        put(App.getApp(), LOSE, Integer.valueOf(lose));
    }

    public static String getMoney() {
        return get(App.getApp(), MONEY, 0L).toString();
    }

    public static void saveMoney(String money) {
        put(App.getApp(), MONEY, Long.valueOf(money));
    }

    public static void saveTableNum(Integer tableNum) {put(App.getApp(), TABLE_NUM, tableNum);}

    public static Integer getTableNum() {return (Integer)get(App.getApp(), TABLE_NUM, -1);}

    private static void put(Context context, String key, Object obj) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(obj instanceof String) editor.putString(key, (String) obj);
        else if(obj instanceof Integer) editor.putInt(key, (Integer) obj);
        else if(obj instanceof Boolean) editor.putBoolean(key, (Boolean) obj);
        else if(obj instanceof Float) editor.putFloat(key, (Float) obj);
        else if(obj instanceof Long) editor.putLong(key, (Long) obj);
        else editor.putString(key, obj.toString());
        editor.apply();
    }

    private static Object get(Context context, String key, Object defaultObj) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if(defaultObj instanceof String) return sp.getString(key, (String)defaultObj);
        else if(defaultObj instanceof Integer) return sp.getInt(key, (Integer)defaultObj);
        else if(defaultObj instanceof Boolean) return sp.getBoolean(key, (Boolean)defaultObj);
        else if(defaultObj instanceof Float) return sp.getFloat(key, (Float)defaultObj);
        else if(defaultObj instanceof Long) return sp.getLong(key, (Long)defaultObj);
        return defaultObj;
    }

    private static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    private static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    private static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    private static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }
}
