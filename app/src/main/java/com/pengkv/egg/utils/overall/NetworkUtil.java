package com.pengkv.egg.utils.overall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.pengkv.egg.config.BaseApplication;
import com.pengkv.egg.utils.ToastUtil;

/**
 * 网络监测工具
 * Created by Liam on 2014/11/28.
 */
public class NetworkUtil {

    private static NetworkInfo NETWORK_INFO;

    /**
     * 初始化网络监测
     *
     * @param context
     */
    public static void init(Context context) {
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = null;
//                if (!isAvailable()) {
//                    msg = "当前网络环境：无连接";
//                } else if (isWiFi()) {
//                    msg = "当前网络环境：WiFi";
//                    BaseImageUtil.setQuality(100);
//                } else if (isMobile()) {
//                    msg = "当前网络环境：移动数据";
//                    BaseImageUtil.setQuality(50);
//                }
                if (isWiFi()) {
                    BaseImageUtil.setQuality(100);
                }
                {
                    BaseImageUtil.setQuality(50);
                }

                if (!TextUtils.isEmpty(msg)) ToastUtil.show(context, msg);
            }
        }, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    /**
     * 获取网络连接信息
     *
     * @return
     */
    private static NetworkInfo getNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NETWORK_INFO = cm.getActiveNetworkInfo();

        return NETWORK_INFO;
    }

    /**
     * 检查网络是否可用
     *
     * @return
     */
    public static boolean isAvailable() {
        getNetworkInfo();

        return NETWORK_INFO != null && NETWORK_INFO.isConnectedOrConnecting();
    }

    /**
     * 获取网络连接的类型
     *
     * @return
     */
    public static int getType() {
        return isAvailable() ? NETWORK_INFO.getType() : -1;
    }

    /**
     * 检查当前网络环境是否是WiFi
     *
     * @return
     */
    public static boolean isWiFi() {
        return getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 检查当前网络环境是否是移动数据
     *
     * @return
     */
    public static boolean isMobile() {
        return getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 检查当前网络环境是否是蓝牙
     *
     * @return
     */
    public static boolean isBluetooth() {
        return getType() == ConnectivityManager.TYPE_BLUETOOTH;
    }

}
