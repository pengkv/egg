package com.pengkv.egg.utils;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

/**
 * 初始化ActionBar工具（基于support v7）
 * Created by peng on 2015/06/17.
 */
public class ActionBarUtil {

    private static final String TAG = "ActionBarUtil";

    //初始化ActionBar
    public static void setup(Context ctx, int title) {
        setup(ctx, ctx.getString(title), true);
    }

    //初始化ActionBar
    public static void setup(Context ctx, String title) {
        setup(ctx, title, true);
    }


    //初始化ActionBar, 是否启用返还键
    public static void setup(Context ctx, int title, boolean isHomeEnabled) {
        if (title < -1) {
            Log.e(TAG, "title资源有误");
        } else if (title == 0) {
            setup(ctx, "", isHomeEnabled);
        } else {
            setup(ctx, ctx.getString(title), isHomeEnabled);
        }
    }


    //初始化ActionBar, 是否启用返还键
    public static void setup(Context ctx, String title, boolean isHomeEnabled) {
        if (ctx instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) ctx).getSupportActionBar();

            if (TextUtils.isEmpty(title)) {
                actionBar.setDisplayShowTitleEnabled(false);
            } else {
                actionBar.setTitle(title);
            }
            actionBar.setDisplayHomeAsUpEnabled(isHomeEnabled);
            actionBar.setHomeButtonEnabled(isHomeEnabled);
        } else {
            Log.e(TAG, "初始化ActionBar出错，title为空或者当前Context不是AppCompatActivity的一个实例");
        }
    }

}
