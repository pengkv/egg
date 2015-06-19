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
package com.pengkv.egg.interfaces;

/**
 * 数据重新加载规范
 *
 * @author Liam
 * @date 2014/6/13
 */
public interface IRefresh {

    /**
     * 默认容器ID，0代表不适用数据重新加载布局
     */
//    int DEFAULT_REFRESH_CONTAINER = android.R.id.content;
    int DEFAULT_REFRESH_CONTAINER = 0;

    /**
     * 重新加载开关（显式）
     *
     * @param isShow 是否显示
     */
    void toggleRefresh(boolean isShow);

    /**
     * 重新加载页面
     */
    void refresh();

    /**
     * 返还当前的状态，是否正在加载
     *
     * @return
     */
    boolean getLoadingState();

    /**
     * 刷新按钮所在布局是否可见
     *
     * @return
     */
    boolean isRefreshLayoutVisible();

    /**
     * 返回刷新布局的容器ID
     */
    int getRefreshContainer();

}
