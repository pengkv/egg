package com.pengkv.egg.utils.overall;

import android.content.Context;
import android.widget.ImageView;

import com.pengkv.egg.utils.image.ImageManager;
import com.umeng.analytics.AnalyticsConfig;

/**
 * Rosemary单一接口工具集合类
 * Created by Liam on 2014/11/7.
 */
public class Rosemary {

    public static Context mContext;

    public static boolean mIsDebug;

    public static void init(Context ctx, boolean isDebug) {
        mContext = ctx;
        mIsDebug = isDebug;

//        ExceptionHandler.GetInstance().init(mContext, mIsDebug);TODO 错误日志

        ImageManager.init(ctx);

        NetworkUtil.init(ctx);  // 初始化网络监测工具

        AnalyticsConfig.enableEncrypt(true);    // 友盟统计日志加密，默认false(不加密)
    }

    /**
     * 加载图片
     *
     * @param v   ImageView
     * @param url 图片地址
     */
    public static void loadImage(String url, ImageView v, ImageManager.Options options) {
        ImageManager.load(url, v, options);
    }

    /**
     * 加载图片
     *
     * @param v   ImageView
     * @param url 图片地址
     */
    public static void loadImage(String url, ImageView v) {
        ImageManager.load(url, v, null);
    }

    /**
     * 加载图片
     *
     * @param v   ImageView
     * @param url 图片地址
     */
    public static void loadImage(String url, ImageView v, int resID) {
        ImageManager.Options options = new ImageManager.Options();
        options.mDefaultImageResID = resID;
        ImageManager.load(url, v, options);
    }

    /**
     * 加载图片
     *
     * @param v   ImageView
     * @param url 图片地址
     */
    public static void loadImageWithProgress(String url, ImageView v) {
        ImageManager.Options options = new ImageManager.Options();
        options.mIsShowProgress = true;

        ImageManager.load(url, v, options);
    }

    public static void reportException(Throwable ex) {
//        ExceptionHandler.GetInstance().reportException(ex);
    }

}
