package com.pengkv.egg.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.pengkv.egg.R;
import com.pengkv.egg.adapter.ViewHolder;
import com.pengkv.egg.config.BaseApplication;
import com.pengkv.egg.fragment.ReloadFragment;
import com.pengkv.egg.interfaces.IProgress;
import com.pengkv.egg.interfaces.IRefresh;
import com.pengkv.egg.interfaces.IReserved;
import com.pengkv.egg.utils.image.ImageManager;
import com.umeng.analytics.MobclickAgent;

import java.util.List;


/**
 * 基础Activity类，实现了动画加载规范接口、重新加载规范接口
 *
 * @author Liam
 * @date 2014/6/20
 */
public class BaseActivity extends AppCompatActivity implements IProgress, IRefresh {

    protected static final String RELOAD_FRAGMENT_TAG = "reload_fragment";  // 刷新按钮所在的Fragment标签
    protected boolean mIsLoading = false;   // 是否显示加载动画的标识

    protected ViewHolder mHolder;   // 维护View引用的类，能通过ViewId快速找到View对象

    public <T> T $(int viewID) {
        return (T) mHolder.get(viewID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Fragment f = getSupportFragmentManager().findFragmentByTag(RELOAD_FRAGMENT_TAG);
            if (f != null) getSupportFragmentManager().beginTransaction().remove(f).commit();
        }

        BaseApplication.getInstance().addActivity(this);
        mHolder = new ViewHolder(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = getStatusBarColor();
            if (color > 0) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(color));
            }
        }

        Log.d("ActivityName---------->", this.getLocalClassName());
    }

    /**
     * 获取状态栏颜色, 0为黑色
     *
     * @return
     */
    protected int getStatusBarColor() {
        return R.color.primary_dark;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 初始化页面时隐藏布局
     */
    public void hideContentView() {
        ((ViewGroup) $(android.R.id.content)).setVisibility(View.GONE);
    }

    /**
     * 页面初始化完毕显示布局
     */
    public void showContentView() {
        ((ViewGroup) $(android.R.id.content)).setVisibility(View.VISIBLE);
    }

    @Override
    public void toggleProgress(boolean isShow) {
        mIsLoading = isShow;
        supportInvalidateOptionsMenu(); // 改变菜单，调用onPrepareOptionsMenu（）
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(Menu.FIRST);

        if (mIsLoading && item == null) {
            item = menu.add(Menu.NONE, Menu.FIRST, Menu.FIRST, "加载中...");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                //   item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);//修改 显示两个menu时使用
                item.setActionView(R.layout.base_circular_progress_bar);
            } else {//显示加载动画的菜单
                MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
                MenuItemCompat.setActionView(item, R.layout.base_circular_progress_bar);
            }
        } else if (!mIsLoading && item != null) {
            menu.removeItem(Menu.FIRST);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void toggleRefresh(boolean isShow) {
        // 处理请求完成时Activity已经销毁或者不需要刷新布局的情况
        if (isFinishing() || getRefreshContainer() < 1) return;

        // 通过添加时设置的TAG找到ReloadFragment
        ReloadFragment reloadFragment = (ReloadFragment) getSupportFragmentManager().findFragmentByTag(RELOAD_FRAGMENT_TAG);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (isShow) {
            hideFragments();
            if (reloadFragment == null) {
                // 原先没有的创建
                ft.add(getRefreshContainer(), ReloadFragment.newInstance(), RELOAD_FRAGMENT_TAG).commitAllowingStateLoss();
            } else {
                // 原先已有的显示
                ft.show(reloadFragment).commitAllowingStateLoss();
            }
        } else {
            // 隐藏
            if (reloadFragment != null) ft.hide(reloadFragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void refresh() {
        toggleProgress(true);
        toggleRefresh(false);
    }

    @Override
    public boolean getLoadingState() {
        return mIsLoading;
    }

    @Override
    public boolean isRefreshLayoutVisible() {
        ReloadFragment reloadFragment = (ReloadFragment) getSupportFragmentManager().findFragmentByTag(RELOAD_FRAGMENT_TAG);
        if (reloadFragment == null) {
            return false;
        } else {
            return reloadFragment.isVisible();
        }
    }

    @Override
    public int getRefreshContainer() {
        return DEFAULT_REFRESH_CONTAINER;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearReferences();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseApplication.getInstance().setCurrentActivity(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ImageManager.clearMemoryCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearReferences();
        BaseApplication.getInstance().popActivity(this);
    }

    private void clearReferences() {
        Activity currActivity = BaseApplication.getInstance().getCurrentActivity();
        if (currActivity != null && currActivity.equals(this)) {
            BaseApplication.getInstance().setCurrentActivity(null);
        }
    }

    /**
     * 隐藏除实现IReserved接口之外的所有Fragment，防止刷新按钮所在的Fragment被覆盖
     */
    protected void hideFragments() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        if (fragments == null || fragments.size() < 1) return;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (Fragment f : fragments) {
            if (f != null && !(f instanceof IReserved) && !(f instanceof ReloadFragment))
                ft.hide(f);
        }
        ft.commitAllowingStateLoss();
    }

}