package com.pengkv.egg.config;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.ViewConfiguration;

import com.pengkv.egg.utils.LogUtil;
import com.pengkv.egg.utils.image.ImageCompressUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * 基础应用配置
 * Created by peng on 2015/06/17.
 */
public class BaseApplication extends Application {

    protected static BaseApplication mInstance; // Application单例
    protected static Context mContext;
    protected List<Activity> mActivityList = new LinkedList(); // 保存Activity的引用到此栈中，方便结束
    protected Activity mCurrentActivity = null; // 当前Activity
    protected Activity mPreviousActivity = null; // 当前页面的前一个页面

    public static BaseApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mContext;
    }

    public void setAppContext(Context mContext) {
        BaseApplication.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        makeActionOverflowMenuShown();

        mInstance = this;
        this.setAppContext(getApplicationContext());
    }

    /**
     * 让有实体菜单键的手机显示OverFlow菜单
     */
    private void makeActionOverflowMenuShown() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.appendFormat("BaseApplication/makeActionOverflowMenuShown", e.getLocalizedMessage());
        }
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

    /**
     * 得到上一个页面的Activity对象
     */
    public Activity getPreviousActivity() {
        int size = mActivityList.size();
        if (size > 1) {
            mPreviousActivity = mActivityList.get(size - 2);
        }

        return mPreviousActivity;
    }

    public void setPreviousActivity(Activity previousActivity) {
        this.mPreviousActivity = previousActivity;
    }

    /**
     * 添加Activity到容器中
     */
    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    /**
     * 把finish的Activity从ActivityList中删除
     */
    public void popActivity(Activity activity) {
        if (mActivityList != null && mActivityList.size() > 0) {
            mActivityList.remove(activity);
        }
    }

    /**
     * 退出应用
     */
    public void exit(Activity ctx) {
//        if (mCurrentActivity != null) {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(homeIntent);

        ImageCompressUtils.clearCache(this);    // 清除图片磁盘缓存
//        } else {
//            for (Activity activity : mActivityList) {
//                activity.finish();
//            }
//
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
//        }
    }

}
