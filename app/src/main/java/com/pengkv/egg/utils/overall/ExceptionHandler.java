package com.pengkv.egg.utils.overall;//package com.pengkv.egg.utils.overall;
//
//import android.content.Context;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.os.Build;
//import android.os.Environment;
//import android.util.Log;
//
//import com.pengkv.egg.utils.ToastUtil;
//import com.pengkv.egg.utils.storage.FileUtil;
//import com.pengkv.egg.utils.time.DateUtil;
//
//import org.apache.http.Header;
//import org.apache.http.client.HttpClient;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FilenameFilter;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.io.Writer;
//import java.lang.Thread.UncaughtExceptionHandler;
//import java.lang.reflect.Field;
//import java.text.SimpleDateFormat;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//
//
///**
// * 全局处理未捕获异常
// *
// * @date 2014/7/28
// * @source http://blog.csdn.net/liuhe688/article/details/6584143
// * @source http://gundumw100.iteye.com/blog/1182104
// */
//public class ExceptionHandler implements UncaughtExceptionHandler {
//
//    public static final String TAG = "CrashHandler";
//
//    private static final String CRASH_REPORTER_EXTENSION = ".log";
//    /**
//     * CrashHandler实例
//     */
//    private static ExceptionHandler INSTANCE = new ExceptionHandler();
//    private boolean IS_DEBUG;
//    /**
//     * Context对象
//     */
//    private Context mContext;
//    /**
//     * 系统默认的 UncaughtException 处理类
//     */
//    private UncaughtExceptionHandler mDefaultHandler;
//    /**
//     * 用来存储设备信息和异常信息
//     */
//    private Map<String, String> mHashMap = new HashMap<String, String>();
//    /**
//     * 用于格式化日期,作为日志文件名的一部分
//     */
//    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
//            "yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
//
//    /**
//     * 确保只有一个实例
//     */
//    private ExceptionHandler() {
//    }
//
//    /**
//     * 获取 CrashHandler 实例，单例模式
//     *
//     * @return
//     */
//    public static ExceptionHandler GetInstance() {
//        return INSTANCE;
//    }
//
//    public void setDebug(boolean isDebug) {
//        INSTANCE.IS_DEBUG = isDebug;
//    }
//
//    /**
//     * 初始化
//     *
//     * @param ctx
//     */
//    public void init(Context ctx, boolean isDebug) {
//        mContext = ctx;
//        IS_DEBUG = isDebug;
//
//        // 获取系统默认的 UncaughtException 处理器
//        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
//
//        // 设置该 CrashHandler 为程序的默认处理器
//        Thread.setDefaultUncaughtExceptionHandler(this);
//    }
//
//    /**
//     * 当UncaughtException发生时会转入该函数来处理
//     */
//    @Override
//    public void uncaughtException(Thread thread, Throwable ex) {
//        if (!handleException(ex) && mDefaultHandler != null) {
//            // 如果用户没有处理则让系统默认的异常处理器来处理
//            mDefaultHandler.uncaughtException(thread, ex);
//        } else {
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                Log.e(TAG, "error : ", e);
//            }
//
//            // 退出程序
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
//        }
//    }
//
//    /**
//     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
//     *
//     * @param ex
//     * @return true：如果处理了该异常信息；否则返回 false
//     */
//    private boolean handleException(Throwable ex) {
//        // 不管是否调试，都发送错误日志到服务器
//        reportException(ex);
//
////        if(IS_DEBUG) {
//        // 仅当调试时，保存日志文件到本地
//        saveCrashInfo2File(ex);
//
//        ex.printStackTrace();
////        }
//
//        if (ex == null || IS_DEBUG) {
//            return false;
//        }
//
//        // Release时，用 Toast 来显示异常信息
////        new Thread() {
////            @Override
////            public void run() {
////                Looper.prepare();
////                ToastUtil.show(mContext, "很抱歉，程序出现异常，即将退出...",
////                        Toast.LENGTH_LONG);
////                Looper.loop();
////            }
////        }.start();
//
//        // 收集设备参数信息（暂无地方发送设备信息）
////        collectDeviceInfo(mContext);
//
//        return true;
//    }
//
//    /**
//     * 收集设备参数信息
//     *
//     * @param ctx
//     */
//    public void collectDeviceInfo(Context ctx) {
//        try {
//            PackageManager pm = ctx.getPackageManager();
//            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
//                    PackageManager.GET_ACTIVITIES);
//
//            if (pi != null) {
//                String versionName = pi.versionName == null ? "null"
//                        : pi.versionName;
//                String versionCode = pi.versionCode + "";
//                mHashMap.put("versionName", versionName);
//                mHashMap.put("versionCode", versionCode);
//            }
//        } catch (NameNotFoundException e) {
//            Log.e(TAG, "an error occured when collect package info", e);
//        }
//
//        Field[] fields = Build.class.getDeclaredFields();
//        for (Field field : fields) {
//            try {
//                field.setAccessible(true);
//                mHashMap.put(field.getName(), field.get(null).toString());
//                Log.d(TAG, field.getName() + " : " + field.get(null));
//            } catch (Exception e) {
//                Log.e(TAG, "an error occured when collect crash info", e);
//            }
//        }
//    }
//
//    /**
//     * 保存错误信息到文件中
//     *
//     * @param ex
//     * @return 返回文件名称, 便于将文件传送到服务器
//     */
//    private void saveCrashInfo2File(Throwable ex) {
//        StringBuffer sb = new StringBuffer();
//        for (Map.Entry<String, String> entry : mHashMap.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            sb.append(key + "=" + value + "\n");
//        }
//
//        Writer writer = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(writer);
//        ex.printStackTrace(printWriter);
//        Throwable cause = ex.getCause();
//        while (cause != null) {
//            cause.printStackTrace(printWriter);
//            cause = cause.getCause();
//        }
//        printWriter.close();
//
//        String result = writer.toString();
//        sb.append(result);
//        try {
//            if (Environment.getExternalStorageState().equals(
//                    Environment.MEDIA_MOUNTED)) {
//                File file = new File(FileUtil.newFileInFilesDir("crash-" + DateUtil.getCurrentTimeString("yyyy-MM-dd") + ".txt"));
//
//                FileOutputStream fos = new FileOutputStream(file);
//                fos.write(sb.toString().getBytes());
//                fos.close();
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "an error occured while writing file...", e);
//        }
//    }
//
//    /**
//     * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
//     */
//    public void sendPreviousReportsToServer() {
////        reportException(ex);
//    }
//
//    /**
//     * 把错误报告发送给服务器
//     *
//     * @param ex
//     */
//    public void reportException(Throwable ex) {
////        String[] crFiles = getCrashReportFiles(ctx);
////        if (crFiles != null && crFiles.length > 0) {
////            TreeSet<String> sortedFiles = new TreeSet<String>();
////            sortedFiles.addAll(Arrays.asList(crFiles));
////            for (String fileName : sortedFiles) {
////                File log = new File(ctx.getFilesDir(), fileName);
////                postReport(log);
////                // 删除已发送的报告
////                log.delete();
////            }
////        }
//
//        // 调用友盟的方法把错误发送到服务器
//        reportError(mContext, ex);
//    }
//
//    /**
//     * 发送错误报告到服务器
//     *
//     * @param file
//     */
//    private void postReport(File file) {
//        // TODO 发送错误报告到服务器
//        RequestParams params = new RequestParams();
//
//        try {
//            params.put("profile_picture", file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        HttpClient.post("upload", params, new AsyncHttpResponseHandler() {
//
//
//            @Override
//            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//                ToastUtil.show(mContext, "错误报告发送失败！");
//            }
//
//            @Override
//            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//                ToastUtil.show(mContext, "错误报告已提交，我们将尽快处理！");
//            }
//
//        });
//    }
//
//    /**
//     * 获取错误报告文件名
//     *
//     * @param ctx
//     * @return
//     */
//    private String[] getCrashReportFiles(Context ctx) {
//        File filesDir = ctx.getFilesDir();
//        FilenameFilter filter = new FilenameFilter() {
//            public boolean accept(File dir, String name) {
//                return name.endsWith(CRASH_REPORTER_EXTENSION);
//            }
//        };
//        return filesDir.list(filter);
//    }
//
//}
