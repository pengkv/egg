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
 * 显示加载进度动画规范
 *
 * @author Liam
 * @date 2014/6/13
 */
public interface IProgress {

    /**
     * 显式指定加载进度动画的显示和隐藏
     *
     * @param isShow
     */
    void toggleProgress(boolean isShow);

    /**
     * 返还当前的状态，是否正在加载
     *
     * @return
     */
    boolean getLoadingState();
}
