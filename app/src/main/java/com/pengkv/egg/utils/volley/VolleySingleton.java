package com.pengkv.egg.utils.volley;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.utils.L;
import com.pengkv.egg.config.BaseApplication;

import java.io.File;

/**
 * Volley单例
 *
 * @date 2014/6/20
 */
public class VolleySingleton {
    public static final String TAG = "VolleySingleton";
    // Default maximum disk usage in bytes
    private static final int DEFAULT_DISK_USAGE_BYTES = 25 * 1024 * 1024;
    // Default cache folder name
    private static final String DEFAULT_CACHE_DIR = "photos";
    private static VolleySingleton INSTANCE = new VolleySingleton();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    /**
     * 初始化RequestQueue和ImageLoader
     */
    public VolleySingleton() {
        mRequestQueue = Volley.newRequestQueue(BaseApplication.getAppContext());
        mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
    }

    /**
     * 初始化RequestQueue和ImageLoader
     */
    public VolleySingleton(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
//        mImageLoader = new ImageLoader(newRequestQueue(context), new LruBitmapCache());
    }

    /**
     * 得到Volley实例
     *
     * @return
     */
    public static VolleySingleton getInstance() {
        return INSTANCE;
    }

    // Most code copied from "Volley.newRequestQueue(..)", we only changed cache directory
    private static RequestQueue newRequestQueue(Context context) {
        // define cache folder
        File rootCache = context.getExternalCacheDir();
        if (rootCache == null) {
            L.w("Can't find External Cache Dir, " + "switching to application specific cache directory");
            rootCache = context.getCacheDir();
        }

        File cacheDir = new File(rootCache, DEFAULT_CACHE_DIR);
        cacheDir.mkdirs();

        HttpStack stack = new HurlStack();
        Network network = new BasicNetwork(stack);
        DiskBasedCache diskBasedCache = new DiskBasedCache(cacheDir, DEFAULT_DISK_USAGE_BYTES);
        RequestQueue queue = new RequestQueue(diskBasedCache, network);
        queue.start();

        return queue;
    }

    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return this.mImageLoader;
    }

    /**
     * 添加请求到队列(自定义Tag)
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * 添加请求到队列(默认Tag)
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * 取消所有请求
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * 取消所有请求
     */
    public void cancelPendingRequests() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }
}