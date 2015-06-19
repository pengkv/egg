package com.pengkv.egg.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by peng on 2015/6/15.
 * 编辑框验证工具类
 */
public class VerifyUtil {

    //验证编辑框是否为空
    public boolean isEmptyET(Context context, EditText verifyET, String edittextName) {
        String s = verifyET.getText().toString();

        if (TextUtils.isEmpty(s)) {//编辑框为空
            ToastUtil.show(context, edittextName + "不能为空");
            verifyET.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(s.trim())) {//编辑框全为空格
            ToastUtil.show(context, edittextName + "不能全为空格");
            verifyET.requestFocus();
            return false;
        }
        return true;
    }

    //验证编辑框的长度
    public boolean isCorrectLenfth(Context context, EditText verifyET, int min, int max, String edittextName) {
        String s = verifyET.getText().toString();

        if (s.length() < min) {//不能小于最小长度
            ToastUtil.show(context, edittextName + String.format("不能小于%d位", min));
            verifyET.requestFocus();
            return false;
        } else if (s.length() > max) {//不能大于最大长度
            ToastUtil.show(context, edittextName + String.format("不能大于%d位", max));
            verifyET.requestFocus();
            return false;
        }
        return true;
    }

    //验证编辑框是否只含小数点
    public boolean isOnlyPoint(Context context, EditText verifyET, String edittextName) {
        String s = verifyET.getText().toString();
        if (".".endsWith(s)) {//只含小数点
            ToastUtil.show(context, edittextName + "不能只为小数点");
            verifyET.requestFocus();
            return false;
        }
        return true;
    }

    //验证编辑框的数字大小
    public boolean isOnlyPoint(Context context, EditText verifyET, int min, int max, String edittextName) {
        String s = verifyET.getText().toString();
        long num = Long.getLong(s, 0);
        if (num < min) {//不能小于最小值
            ToastUtil.show(context, edittextName + String.format("不能小于%d元", min));
            verifyET.requestFocus();
            return false;
        } else if (num > max) {//不能大于最大值
            ToastUtil.show(context, edittextName + String.format("不能大于%d元", max));
            verifyET.requestFocus();
            return false;
        }
        return true;
    }

}
