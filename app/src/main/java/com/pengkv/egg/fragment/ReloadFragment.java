/*
 * Copyright (C) 2014 Liam Yu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pengkv.egg.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.pengkv.egg.R;
import com.pengkv.egg.interfaces.IRefresh;


/**
 * 显示用于重新加载的Fragment
 *
 * @author Liam
 * @date 2014/8/1
 */
public class ReloadFragment extends Fragment {

    private static final String IS_HIDDEN = "is_hidden";  // 当前Fragment的显示状态
    private Button mRetryBtn;   // 重试按钮
    private IRefresh mHostActivity; // 实现了重新加载接口的宿主Activity

    public static ReloadFragment newInstance() {
        ReloadFragment mFragment = new ReloadFragment();

        return mFragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);

//        if (savedInstanceState != null) {
//            if (savedInstanceState.getBoolean(IS_HIDDEN)) {
//                getActivity().getSupportFragmentManager().beginTransaction().hide(this).commitAllowingStateLoss();
//            }
//        }
//    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putBoolean(IS_HIDDEN, isHidden());
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_reload_fragment, container,
                false);

        mRetryBtn = (Button) view.findViewById(R.id.btn_retry);
        mRetryBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                reload();
            }

        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof IRefresh) {
            mHostActivity = (IRefresh) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + getString(R.string.base_wrong_class));
        }
    }

    @Override
    public void onDetach() {
        mHostActivity = null;
        super.onDetach();
    }

    /**
     * 执行重新加载操作
     */
    private void reload() {
        // 只有当前不在加载，才能执行重新加载操作
        if (!mHostActivity.getLoadingState()) {
            ((FragmentActivity) mHostActivity).getSupportFragmentManager().beginTransaction().hide(this).commitAllowingStateLoss(); // 隐藏本身
            mHostActivity.refresh();
        }
    }

}
