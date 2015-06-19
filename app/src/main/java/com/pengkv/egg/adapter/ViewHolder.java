package com.pengkv.egg.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pengkv.egg.utils.overall.Rosemary;


/**
 * 配合Activity/ListAdapter使用，减少代码量
 * source: http://blog.csdn.net/kroclin/article/details/40868291
 * Created by Liam on 2014/11/7.
 */
public class ViewHolder {

    private View mConvertView;

    private Activity mActivity;

    private SparseArray<View> mViewArray;   // 包含了View引用的SparseArray

    private ViewHolder() {
    }

    public ViewHolder(View convertView) {
        this.mConvertView = convertView;
    }

    public ViewHolder(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 通过ViewId得到View
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T get(int viewId) {
        if (mActivity == null) {
            // ListAdapter的ViewHolder
            SparseArray<View> viewHolder = (SparseArray<View>) mConvertView.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<>();
                mConvertView.setTag(viewHolder);
            }
            View childView = viewHolder.get(viewId);
            if (childView == null) {
                childView = mConvertView.findViewById(viewId);
                viewHolder.put(viewId, childView);
            }
            return (T) childView;
        } else {
            // Activity的ViewHolder
            if (mViewArray == null) {
                mViewArray = new SparseArray<>();
            }
            View childView = mViewArray.get(viewId);
            if (childView == null) {
                childView = mActivity.findViewById(viewId);
                mViewArray.put(viewId, childView);
            }
            return (T) childView;
        }
    }

    //通过ViewId设置Text
    public void setText(int viewId, String text) {
        ((TextView) get(viewId)).setText(text);
    }

    //通过ViewId设置Text
    public void setText(int viewId, Spanned text) {
        ((TextView) get(viewId)).setText(text);
    }

    //通过ViewId设置TextColor
    public void setTextColor(int viewId, int color) {
        ((TextView) get(viewId)).setTextColor(color);
    }

    //通过ViewId设置图片
    public void setImageResource(int viewId, int resId) {
        ((ImageView) get(viewId)).setImageResource(resId);
    }

    //过ViewId设置图片
    public void setImageBitmap(int viewId, Bitmap bm) {
        ((ImageView) get(viewId)).setImageBitmap(bm);
    }

    //通过ViewId设置图片
    public void setImageDrawable(int viewId, Drawable drawable) {
        ((ImageView) get(viewId)).setImageDrawable(drawable);
    }

    //通过ViewId设置网络图片
    public void setImageUrl(int id, String url) {
        Rosemary.loadImage(url, (ImageView) get(id));
    }

    /**
     * 通过ViewId设置网络图片
     *
     * @param id
     * @param url
     * @param resId 默认图片
     */
    public void setImageUrl(int id, String url, int resId) {
        Rosemary.loadImage(url, (ImageView) get(id), resId);
    }

    /**
     * 通过ViewId设置隐藏和显示
     *
     * @param viewId
     * @param visibility
     */
    public void setVisibility(int viewId, int visibility) {
        get(viewId).setVisibility(visibility);
    }

    /**
     * 通过ViewId设置点击监听
     *
     * @param viewId
     * @param l
     */
    public void setOnClickListener(int viewId, View.OnClickListener l) {
        get(viewId).setOnClickListener(l);
    }

}
