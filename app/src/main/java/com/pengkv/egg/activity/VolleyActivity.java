package com.pengkv.egg.activity;

import android.os.Bundle;

import com.android.volley.toolbox.ImageLoader;
import com.pengkv.egg.utils.volley.VolleySingleton;

/**
 * 封装了Volley RequestQueue、ImageLoader的Activity基础类
 *
 * @author Liam
 * @date 2014/6/20
 */
public class VolleyActivity extends BaseActivity {

    protected static final String TAG = "volley_activity";

    public VolleySingleton mVolleySingleton;
    public static ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageLoader = VolleySingleton.getInstance().getImageLoader();
        mVolleySingleton = new VolleySingleton(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVolleySingleton.cancelPendingRequests();
    }

}
