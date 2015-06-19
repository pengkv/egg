package com.pengkv.egg.utils;

import android.content.Context;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

/**
 * Created by peng on 2015/6/15.
 * 吐司工具类
 */
public class ToastUtil {
    private static Toast TOAST;
    private static boolean mIsDebug = true;//是否是调试环境
    private static final String TAG = "ToastUtil";


    //短时间吐司
    public static void show(Context context, int resourceID) {
        show(context, resourceID, Toast.LENGTH_SHORT);
    }

    //短时间吐司
    public static void show(Context context, String text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    //自定义时长吐司
    public static void show(Context context, Integer resourceID, int duration) {
        String text = context.getResources().getString(resourceID);// 用于显示的文字
        show(context, text, duration);
    }

    //自定义时长吐司
    public static void show(@NotNull final Context context, @NotNull final String text, final int duration) {

//        try {
            if (TOAST == null) {
                TOAST = Toast.makeText(context, text, duration);
            } else {
                TOAST.setText(text);
                TOAST.setDuration(duration);
            }

            TOAST.show();
//        } catch (Exception e) {
//            e.printStackTrace();
////            LogUtil.appendFormat("ToastUtil/%s/%s", context.toString(), e.toString());
//        }
    }
}
