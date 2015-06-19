package com.pengkv.egg.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.L;
import com.pengkv.egg.interfaces.IResponseHandler;
import com.pengkv.egg.utils.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片加载工具类
 * Created by Liam on 2014/11/7.
 */
public class ImageManager {

    public static Config CONFIG;
    public static Options OPTIONS;

    public static String DIR;   // 缓存目录

    public static void init(Context ctx) {
        initUniversalImageLoader(ctx);
    }

    /**
     * 初始化UniversalImageLoader
     */
    public static ImageLoader initUniversalImageLoader(Context ctx) {
//        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(ctx);

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
//        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheOnDisk(true).build();  // 2015/1/30去除了cacheInMemory(true)

        File cache = getCacheDir(ctx);
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ctx).threadPriority(Thread.NORM_PRIORITY - 2)
//                .memoryCache(new WeakMemoryCache()) // 2015/1/30新增
                .denyCacheImageMultipleSizesInMemory()
//                .diskCacheExtraOptions(600, 600, null)
//                .diskCache(new UnlimitedDiscCache(cache)) // default
                .diskCacheSize(80 * 1024 * 1024) // 80 Mb
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheFileCount(300).defaultDisplayImageOptions(displayImageOptions).tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

        return ImageLoader.getInstance();
    }

    /**
     * 得到缓存目录
     */
    public static File getCacheDir(Context ctx) {
        File cache = ctx.getExternalCacheDir();
        if (cache == null) {
            L.w("Can't find External Cache Dir, " + "switching to application specific cache directory");
            cache = ctx.getCacheDir();
        }

        return cache;
    }

    /**
     * 通过url得到Bitmap（如果图片太大可能造成OOM）
     *
     * @param urlPath         图片url
     * @param responseHandler 回调
     * @param tag             回调标识
     * @return
     */
    public static void getBitmapByUrl(@NotNull final String urlPath, final IResponseHandler responseHandler, final int tag) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5 * 1000);
                    if (conn.getResponseCode() == 200) {
                        InputStream inputStream = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(inputStream);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtil.appendFormat("ImageManager/getBitmapByUrl/IOException/%s", e.toString());
                } catch (Exception e1) {
                    e1.printStackTrace();
                    LogUtil.appendFormat("ImageManager/getBitmapByUrl/Exception/%s", e1.toString());
                }
                responseHandler.updateUI(bitmap, tag);
            }
        }).start();
    }

    /**
     * 保存bitmap到文件
     */
    public static String saveBitmapToFile(@NotNull Context ctx, @NotNull Bitmap b, @NotNull String fileName) {
        String path = new File(getCacheDir(ctx), fileName).getAbsolutePath();
        // 写入文件
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(path);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            b.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return path;
    }

    /**
     * 加载图片
     *
     * @param v
     * @param url
     * @param options
     */
    public static void load(String url, final ImageView v, final Options options) {
        if (options != null) {
            if (options.mIsShowProgress) {
                ImageLoader.getInstance().displayImage(url, v);
                return;
            }

            if (options.mDefaultImageResID > 0) {
                if (url.equals(v.getTag())) return; // 相同的图片不再重新加载
                v.setTag(url);  // 利用tag保存Url

//                if (!TextUtils.isEmpty(url))
//                    Glide.with(BaseApplication.getAppContext()).load(url).fallback(options.mDefaultImageResID).thumbnail(0.1f).into(v);

                ImageLoader.getInstance().displayImage(url, v, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                        v.setImageResource(options.mDefaultImageResID);
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        v.setImageResource(options.mDefaultImageResID);
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
            }
        }

        ImageLoader.getInstance().displayImage(url, v);

//        if (!TextUtils.isEmpty(url))
//            Glide.with(BaseApplication.getAppContext()).load(url).thumbnail(0.1f).into(v);

//        if (!TextUtils.isEmpty(url))
//            Picasso.with(BaseApplication.getAppContext()).load(url).into(v);
    }

    /**
     * 清空内存缓存
     */
    public static void clearMemoryCache() {
        ImageLoader.getInstance().clearMemoryCache();
    }

    /**
     * 图片显示选项
     *
     * @param v
     * @param url
     */
    public static void load(String url, ImageView v) {
        load(url, v, null);
    }

    /**
     * 图片显示选项类
     */
    public static class Options {
        public int mDefaultImageResID, mErrorImageResID;    // 默认图片、错误图片
        public boolean mIsShowProgress; // 是否显示加载动画
    }

    /**
     * 图片显示配置类
     */
    public class Config {

    }

}