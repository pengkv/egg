package com.pengkv.egg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 简易数据集适配器
 * Created by Liam on 2014/11/7.
 */
public abstract class ListAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;

    protected List<T> mList;   // 数据集

    protected int mLayoutID;    // 布局资源ID

    protected ListAdapter() {
    }

    public ListAdapter(Context ctx, List<T> list, int layoutID) {
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);
        mList = list;
        mLayoutID = layoutID;
    }

    @Override
    public int getCount() {
        if (mList != null && mList.size() > 0) return mList.size();

        return 0;
    }

    @Override
    public T getItem(int position) {
        if (mList != null && mList.size() > 0) return mList.get(position);

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutID, null);
            holder = new ViewHolder(convertView);

            // 创建ConvertView时操作，如根据分辨率调整View大小
            onCreateView(holder);
        } else {
            holder = new ViewHolder(convertView);
        }

        // 绑定数据
        onBind(position, getItem(position), holder);

        return convertView;
    }

    /**
     * 创建ConvertView时操作，如根据分辨率调整View大小
     */
    protected void onCreateView(ViewHolder holder) {
    }

    /**
     * 绑定数据
     */
    protected abstract void onBind(int position, T item, ViewHolder holder);

}
