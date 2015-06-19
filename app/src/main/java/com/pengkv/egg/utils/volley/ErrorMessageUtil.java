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
package com.pengkv.egg.utils.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.pengkv.egg.R;

/**
 * 错误文字提示工具
 *
 * @date 2014/7/31
 * @author Liam
 *
 */
public class ErrorMessageUtil {

    /** 网络错误提示资源ID */
    public static final int NETWORK_ERROR_RESOURCE_ID = R.string.base_network_error;

    /** 超时错误提示资源ID */
    public static final int TIMEOUT_ERROR_RESOURCE_ID = R.string.base_timeout_error;

    /** 服务器错误提示资源ID */
    public static final int SERVER_ERROR_RESOURCE_ID = R.string.base_server_error;

    /** 无连接错误提示资源ID */
    public static final int NO_CONNECTION_ERROR_RESOURCE_ID = R.string.base_no_connection_error;

    /** 默认错误提示资源ID */
    public static final int DEFAULT_ERROR_RESOURCE_ID = R.string.base_default_error;

    private ErrorMessageUtil() {
    }

    /**
     * 通过判断错误类型返回错误语句
     *
     * @param error
     *            VolleyError
     * @return
     */
    public static int getMessage(VolleyError error) {

        // 代表错误提示语的资源ID
        int resourceID = DEFAULT_ERROR_RESOURCE_ID;

        // Handle your error types accordingly.For Timeout & No
        // connection error, you can show 'retry' button.
        // For AuthFailure, you can re login with user
        // credentials.
        // For ClientError, 400 & 401, Errors happening on
        // client side when sending api request.
        // In this case you can check how client is forming the
        // api and debug accordingly.
        // For ServerError 5xx, you can do retry or handle
        // accordingly.
        if (error instanceof NetworkError) {
            resourceID = NETWORK_ERROR_RESOURCE_ID;
        }/*
		 * else if( error instanceof ClientError) { }
		 */else if (error instanceof ServerError) {
            resourceID = SERVER_ERROR_RESOURCE_ID;
        } else if (error instanceof AuthFailureError) {
        } else if (error instanceof ParseError) {
        } else if (error instanceof NoConnectionError) {
            resourceID = NO_CONNECTION_ERROR_RESOURCE_ID;
        } else if (error instanceof TimeoutError) {
            resourceID = TIMEOUT_ERROR_RESOURCE_ID;
        }

        return resourceID;
    }
}
