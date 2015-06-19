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
 * 利用接口数据更新UI的接口规范
 *
 * @author Liam
 * @date 2014/6/16
 *
 * @version 1.1 2014/9/5
 */
public interface IResponseHandler {

    /**
     * tag常用常量，值为1
     */
    int FIRST = 1;

    /**
     * tag常用常量，值为2
     */
    int SECOND = 2;

    /**
     * tag常用常量，值为3
     */
    int THIRD = 3;

    /**
     * tag常用常量，值为4
     */
    int FOURTH = 4;

    /**
     * tag常用常量，值为5
     */
    int FIFTH = 5;

    /**
     * 做取数据操作
     *
     * @param tag 请求标识
     */
    void fetchData(int tag);

    /**
     * 更新UI
     *
     * @param response 响应
     * @param tag 请求标识，用于区分不同的请求
     */
    void updateUI(Object response, int tag);

}
