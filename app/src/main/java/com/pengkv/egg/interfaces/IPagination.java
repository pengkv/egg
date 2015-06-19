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
 * 分页加载规范接口
 *
 * @author Liam
 * @date 2014/6/17
 */
public interface IPagination {

    /**
     * 取新的分页数据，通常在该方法里对页索引做+1操作
     *
     * @param tag 标识，区分不同的分页加载
     */
    void fetchNewData(int tag);

}
