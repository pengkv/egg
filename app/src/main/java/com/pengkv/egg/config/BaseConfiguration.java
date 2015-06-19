package com.pengkv.egg.config;


import com.pengkv.egg.BuildConfig;

/**
 * 基础工具类：根据是否是调试选择接口地址
 * Created by peng on 2015/06/17.
 */
public class BaseConfiguration {

    /**
     * 接口基础地址（开发）
     */
    private static final String DEBUG_URL = "";

    /**
     * 接口基础地址（测试）
     */
//    public static final String TEST_URL = "";

    /**
     * 接口基础地址（发布）
     */
    public static final String RELEASE_URL = "";


    /**
     * 支付宝异步通知地址（开发）
     */
    protected String DEBUG_ALIPAY_URL = "";

    /**
     * 支付宝异步通知地址（发布）
     */
    protected final String RELEASE_ALIPAY_URL = "";


    /**
     * 接口是否加密
     */
    public static final boolean IS_ENCRYPTED = true;

    /**
     * 列表是否是滚动状态
     */
    public static boolean isScrolling;

    /**
     *  根据Debug/Release自动选择接口基础地址
     */
    public static String getBaseUrl() {
        return BuildConfig.DEBUG ? DEBUG_URL : RELEASE_URL;
    }

    /**
     *  根据Debug/Release自动选择支付宝异步通知地址
     */
    public String getAliPayNotifyUrl() {
        return BuildConfig.DEBUG ? DEBUG_ALIPAY_URL : RELEASE_ALIPAY_URL;
    }

}
