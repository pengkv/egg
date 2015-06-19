package com.pengkv.egg.utils.overall;


import com.pengkv.egg.config.BaseApplication;
import com.pengkv.egg.utils.UnitUtil;

/**
 * Created by Liam on 2014/11/28.
 */
public class BaseImageUtil {

    /**
     * 图片质量百分比
     */
    protected static int QUALITY = 100;

    /**
     * 设置图片质量
     *
     * @param quality
     */
    public static void setQuality(int quality) {
        QUALITY = quality;
    }

    /**
     * 图片裁剪辅助工具（裁剪单位：Pixel）
     *
     * @param imageUrl 图片Url
     * @param width    宽
     * @param height   高
     * @return
     */
    public static String crop(String imageUrl, double width, double height) {
        return null;
    }

    /**
     * 正方形图片裁剪辅助工具（裁剪单位：Pixel）
     *
     * @param imageUrl  图片Url
     * @param edgeWidth 边长
     * @return
     */
    public static String crop(String imageUrl, double edgeWidth) {
        return crop(imageUrl, edgeWidth, edgeWidth);
    }

    /**
     * 图片裁剪辅助工具（裁剪单位：Dp）
     *
     * @param imageUrl 图片Url
     * @param width    宽
     * @param height   高
     * @return
     */
    public static String cropDp(String imageUrl, int width, int height) {
        return crop(imageUrl, UnitUtil.convertDpToPixel(width, BaseApplication.getAppContext()), UnitUtil.convertDpToPixel(height, BaseApplication.getAppContext()));
    }

    /**
     * 正方形图片裁剪辅助工具（裁剪单位：Dp）
     *
     * @param imageUrl  图片Url
     * @param edgeWidth 边长
     * @return
     */
    public static String cropDp(String imageUrl, int edgeWidth) {
        return crop(imageUrl, UnitUtil.convertDpToPixel(edgeWidth, BaseApplication.getAppContext()));
    }

}
