package com.pengkv.egg.utils.storage;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.pengkv.egg.config.BaseApplication;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件简易工具类
 * Created by Liam on 2014/9/22.
 */
public class FileUtil {

    public static final String PNG = "png";

    public static final String JPG = "jpg";

    public static final String TXT = "txt";

    /**
     * 创建文件
     *
     * @param dirName   文件目录，格式<dir>（不含尖括号）
     * @param fileName  文件名称
     * @param extension 文件扩展名（请使用预定义的常量，如FileUtil.PNG、FileUtil.TXT）
     * @return 文件路径
     */
    public static String newFile(String dirName, String fileName, String extension) throws IOException {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            throw new IOException("没有内存卡或内存卡处于被占用状态！");
        }

        String dirPath = Environment.getExternalStorageDirectory() + File.separator + dirName + File.separator;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        String file = fileName + "." + extension;
        File f = new File(dirPath, file);
        if (!f.exists()) {
            f.createNewFile();
        }

        return f.getAbsolutePath();
    }

    /**
     * 文件重命名
     *
     * @param from
     * @param to
     */
    public static boolean rename(String from, String to) {
        File f1 = new File(from);
        File f2 = new File(to);

        return f1.renameTo(f2);
    }

    /**
     * 从assets文件夹中获取文件并读取数据
     */
    public static String getFromAssets(Context ctx, String fileName) {
        String result = "";
        try {
            InputStream in = ctx.getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int length = in.available();
            // 创建byte数组
            byte[] buffer = new byte[length];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 在程序缓存目录创建文件并返回路径
     */
    public static String newFileInCacheDir(String uniqueName) {
        File cache = BaseApplication.getAppContext().getExternalCacheDir();
        if (cache == null) cache = BaseApplication.getAppContext().getCacheDir();

        String path = cache.getPath() + File.separator + uniqueName;
        new File(path);

        return path;
    }

    /**
     * 在程序File目录创建文件并返回路径
     */
    public static String newFileInFilesDir(String uniqueName) {
        File f = BaseApplication.getAppContext().getExternalFilesDir(null);
        if (f == null) f = BaseApplication.getAppContext().getFilesDir();

        String path = f.getPath() + File.separator + uniqueName;
        new File(path);

        return path;
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return 文件后缀名
     */
    public static String getFileType(String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            int typeIndex = fileName.lastIndexOf(".");
            if (typeIndex != -1) {
                String fileType = fileName.substring(typeIndex + 1).toLowerCase();
                return fileType;
            }
        }
        return "";
    }

    /**
     * 判断是否是图片
     *
     * @param fileName
     * @return
     */
    public static boolean isImage(String fileName) {
        String type = getFileType(fileName);
        if (!TextUtils.isEmpty(type) && (type.equals("jpg") || type.equals("gif") || type.equals("png") || type.equals("jpeg") || type.equals("bmp") || type.equals("wbmp") || type.equals("ico") || type.equals("jpe"))) {
            return true;
        }
        return false;
    }

}
