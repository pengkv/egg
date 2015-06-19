package com.pengkv.egg.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.pengkv.egg.utils.LogUtil;
import com.pengkv.egg.utils.storage.FileUtil;
import com.pengkv.egg.utils.storage.SDCardFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片压缩处理
 */
public class ImageCompressUtils {

    private static List<String> IMAGE_LIST = new ArrayList<>();    // 临时图片列表
    private static String IMAGE = ""; // 单个临时图片

    /**
     * 压缩单张图片
     *
     * @param context
     * @param img
     * @param callBack
     */
    public static void processImg(final Context context, final String img,
                                  final ProcessImgCallBack callBack) {
        if (!TextUtils.isEmpty(IMAGE)) IMAGE = "";

        if (TextUtils.isEmpty(img)) {
            callBack.compressSuccess("");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 用于文件发送的临时存放目录
                if (FileUtil.isImage(img)) {
                    try {
                        IMAGE = ImageCompressUtils.compressImage(context, img);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.appendFormat("ImageCompressUtils/Single/%s", e.toString());
                    }
                }
                callBack.compressSuccess(IMAGE);
            }
        }).start();
    }

    /**
     * 压缩多张图片
     *
     * @param context
     * @param imgList
     * @param callBack
     */
    public static void processImgList(final Context context, final List<String> imgList,
                                      final ProcessImgListCallBack callBack) {
        if (IMAGE_LIST.size() > 0) IMAGE_LIST.clear();

        if (imgList == null || imgList.isEmpty()) {
            callBack.compressSuccess(IMAGE_LIST);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < imgList.size(); i++) {
                    // 用于文件发送的临时存放目录
                    String tempPath = "";
                    if (FileUtil.isImage(imgList.get(i))) {
                        try {
                            tempPath = ImageCompressUtils.compressImage(context, imgList.get(i));
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtil.appendFormat("ImageCompressUtils/Multiple/%s", e.toString());
                        }
                        IMAGE_LIST.add(tempPath);
                    }
                }
                callBack.compressSuccess(IMAGE_LIST);
            }
        }).start();
    }

    /**
     * 压缩多张图片
     *
     * @param context
     * @param imgArr
     * @param callBack
     */
    public static void processImgList(final Context context, String[] imgArr,
                                      final ProcessImgListCallBack callBack) {
        List<String> files = new ArrayList<>();
        for (int i = 0; i < imgArr.length; i++) {
            if (TextUtils.isEmpty(imgArr[i])) continue;
            files.add(imgArr[i]);
        }

        processImgList(context, files, callBack);
    }

    /**
     * 压缩图片至指定大小
     *
     * @param imagePath 源文件路径
     * @return
     */
    private static String compressImage(Context ctx, String imagePath) {
        if (TextUtils.isEmpty(imagePath))
            return imagePath;

        File f = new File(imagePath);
        if (!f.exists())
            return imagePath;

        if (f.length() < 1)
            return null;

        int degree = ImageRotateUtil.getBitmapDegree(imagePath); // 检查图片的旋转角度

        File tempFile = SDCardFileUtils.getDiskCacheDir(ctx, System.currentTimeMillis() + "_tmp.jpg");
        String outImagePath = tempFile.getAbsolutePath(); // 输出图片文件路径

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        preCalculate(ctx, options); // 先计算支持的inSampleSize
        // 如果压缩到指定大小所用的inSampleSize小于支持的inSampleSize，则使用支持的inSampleSize，防止oom（即哪个inSampleSize大用哪个）
        //        int inSampleSize = (int) Math.sqrt(actualSize / 200);
        //        if (inSampleSize > options.inSampleSize) {
        //            options.inSampleSize = inSampleSize;
        //        }

        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inScaled = false;
        options.inPurgeable = true;
        options.inInputShareable = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap scaledBitmap;
        try {
            scaledBitmap = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            LogUtil.appendFormat("ImageCompressUtils/compressImage/%s", e.toString());
            options.inSampleSize = calculateInSampleSize(options, 600, 600);
            scaledBitmap = BitmapFactory.decodeFile(imagePath, options);
        }

        // 旋转
        if (degree > 0) {
            scaledBitmap = ImageRotateUtil.rotateBitmapByDegree(scaledBitmap, degree);
        }

        NativeUtil.compressBitmap(scaledBitmap, 70, outImagePath, true);

        if (scaledBitmap != null) scaledBitmap.recycle();


        return outImagePath;
    }

    private static void preCalculate(Context ctx, BitmapFactory.Options options) {
        //应用程序已获得内存 
        long freeMemory = Runtime.getRuntime().totalMemory();
        double supportResolution = (freeMemory * 3 / 5) / 4; // 在RGB8888格式下，一个像素占用4个字节，申请3/5的可用heapMemory，得出最大支持的分辨率
        double requestResolution = 1575 * 200; // 用质量80%的png格式压缩成200k的情况
        supportResolution = supportResolution < requestResolution ? supportResolution
                : requestResolution;
        double imageWidth = options.outWidth;
        double imageHeight = options.outHeight;
        double imageResolution = options.outWidth * options.outHeight;
        //        double screenResolution = DimensionUtil.getWidth(ctx) * DimensionUtil.getHeight(ctx);
        if (imageResolution > supportResolution) {
            // 当图片分辨率高于设备分辨率时，缩小图片
            double scale = Math.sqrt(imageResolution / supportResolution);

            if (scale > 1) {
                imageWidth = imageWidth / scale;
                imageHeight = imageHeight / scale;
                options.inSampleSize = calculateInSampleSize(options, (int) imageWidth, (int) imageHeight);
            }
        }
        //        options.inSampleSize = calculateInSampleSize(options, 480, 800);
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that is a power of 2 and will result in the final decoded bitmap
     * having a width and height equal to or larger than the requested width and height.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     *                  method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // BEGIN_INCLUDE (calculate_sample_size)
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
        // END_INCLUDE (calculate_sample_size)
    }

//    /**
//     * 清理临时文件
//     */
//    public static void cleanUpFile() {
//        File f = new File(IMAGE);
//        if (f.exists()) f.delete();
//        IMAGE = "";
//    }
//
//    /**
//     * 清理临时文件列表
//     */
//    public static void cleanUpFiles() {
//        for (String s : IMAGE_LIST) {
//            File f = new File(s);
//            if (f.exists()) f.delete();
//        }
//        IMAGE_LIST.clear();
//    }

    /**
     * 清理缓存文件夹
     *
     * @param ctx
     */
    public static void clearCache(Context ctx) {
        File file = new File(SDCardFileUtils.getDiskCacheDir(ctx));
        File[] childFile = file.listFiles();
        if (childFile == null || childFile.length == 0) {
            return;
        }

        for (File f : childFile) {
            f.delete(); // 循环删除子文件
        }

        IMAGE = "";
        IMAGE_LIST.clear();
    }

    public interface ProcessImgListCallBack {
        void compressSuccess(List<String> list);
    }

    public interface ProcessImgCallBack {
        void compressSuccess(String image);
    }

}
