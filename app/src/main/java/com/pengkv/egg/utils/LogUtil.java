package com.pengkv.egg.utils;

import android.os.Environment;

import com.pengkv.egg.utils.storage.FileUtil;
import com.pengkv.egg.utils.time.DateUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Log工具
 * Created by Liam on 2014/9/17.
 */
public class LogUtil {

    /**
     * 追加log到文件
     */
    public static void append(String text) {
//        if(!BuildConfig.DEBUG) return;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File logFile = new File(FileUtil.newFileInFilesDir("log-" + DateUtil.getCurrentTimeString("yyyy-MM-dd") + ".txt"));

            try {
                // BufferedWriter for performance, true to set append to file flag
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.append(DateUtil.getCurrentCnTimeString());
                buf.newLine();
                buf.append(text);
                buf.newLine();
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 以一定的格式追加log到文件
     */
    public static void appendFormat(String text, Object... args) {
        try {
            append(String.format(text, args));
        } catch (Exception e) {
            e.printStackTrace();
            append(String.format("LogUtil/appendFormat/%s", e.toString()));
        }
    }

}
