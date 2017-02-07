/**
 * Created on 2012-9-7, Logger.java
 *
 * @Author: XuGuobiao
 * @Comment:
 */

package com.everlastxgb.beautyshow.util;

import android.os.Environment;
import android.util.Log;

import com.everlastxgb.beautyshow.BuildConfig;

import java.io.File;

public class Logger {

    private static final String TAG = "BeautyShow";
    private static final String LOG_ENABLE_MARK_FILE_NAME = "com.beautyshow.log.enable";

    private static boolean sIsLogEnabled = true;

    private static LogLevel sLogLevel = LogLevel.VERBOSE;

    static {
        initByBuildType(); // 默认情况下根据buildType预先设置
    }

    /**
     * 日志输出条件 <br> <li>ALWAYS - 总是输出</li> <li>NEVER - 不输出</li> <li>ACCORDING_SD_FILE -
     * 根据SDCard底下是否有LOG_ENABLE_MARK_FILE_NAME文件/文件夹，若有则输出，若为文件夹则取其里面第一个文件的名字转换成Log级别</li>
     */
    public static enum LogCondition {
        ALWAYS,
        NEVER,
        ACCORDING_SD_FILE, // 注：此处需要SD读权限，否则此功能不会生效
    }

    /**
     * 日志输出级别。此处类似Logcat的Log level
     */
    public static enum LogLevel {
        ASSERT(0),
        ERROR(1),
        WARN(2),
        INFO(3),
        DEBUG(4),
        VERBOSE(5);

        private int value = 0;

        private LogLevel(int value) {
            this.value = value;
        }

        public static LogLevel valueOf(int value) {
            switch (value) {
                case 0:
                    return ASSERT;
                case 1:
                    return ERROR;
                case 2:
                    return WARN;
                case 3:
                    return INFO;
                case 4:
                    return DEBUG;
                case 5:
                    return VERBOSE;
                default:
                    return null;
            }
        }

    }

    /**
     * 若log的输出条件设置为ACCORDING_SD_FILE，则根据文件是否存在决定是否输出log，存在则输出。
     */
    private static boolean hasLogMarkFile() {
        File logMarkFile = new File(Environment.getExternalStorageDirectory(), LOG_ENABLE_MARK_FILE_NAME);
        return logMarkFile.exists();
    }

    private static boolean canILog(LogLevel logLevel) {
        return sIsLogEnabled && sLogLevel.value >= logLevel.value;
    }

    public static boolean isLogEnabled() {
        return sIsLogEnabled;
    }


    /**
     * 若buildType是debug模式，日志条件就设置为总是输出；否则设置为ACCORDING_SD_FILE
     */
    public static void initByBuildType() {
        if (BuildConfig.DEBUG) {
            init(LogCondition.ALWAYS, LogLevel.VERBOSE);
        } else {
            init(LogCondition.ACCORDING_SD_FILE, LogLevel.VERBOSE);
        }
    }

    /**
     * 初始化输出日志的条件和日志输出级别。<br> <li>若logRange为LogRange.ALWAYS，则表示根据LogLevel总是输出</li>
     * <li>若logRange为LogRange.NEVER，则表示不输出，忽略后面的logLevel</li> <li>若logRange为LogRange.ACCORDING_SD_FILE，则根据SDCard底下是否有LOG_ENABLE_MARK_FILE_NAME文件/文件夹，若有则输出，若为文件夹则优先取其里面第一个文件的名字转换成Log级别，其次取logLevel参数(默认情况为Verbose级别)。注：此处需要SD读权限，否则不会生效</li>
     *
     * @param logCondition 日志的输出条件
     * @param logLevel     日志的输出级别
     */
    public static void init(LogCondition logCondition, LogLevel logLevel) {
        try {
            switch (logCondition) {
                case ALWAYS:
                    sIsLogEnabled = true;
                    sLogLevel = logLevel != null ? logLevel : LogLevel.VERBOSE;
                    break;
                case NEVER:
                    sIsLogEnabled = false;
                    break;
                case ACCORDING_SD_FILE:
                    File logMarkFile = new File(Environment.getExternalStorageDirectory(), LOG_ENABLE_MARK_FILE_NAME);
                    sIsLogEnabled = logMarkFile.exists();
                    if (sIsLogEnabled && logMarkFile.isDirectory()) {
                        File[] files = logMarkFile.listFiles();
                        if (files != null && files.length > 0) {
                            int name = Integer.parseInt(files[0].getName());
                            LogLevel level = LogLevel.valueOf(name);
                            sLogLevel = level != null ? level : logLevel != null ? logLevel : LogLevel.VERBOSE; // 优先级：文件名->参数logLevel->VERBOSE
                        }
                    }
                    break;
            }
        } catch (Exception e) {

        }

    }


    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (canILog(LogLevel.ERROR)) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (canILog(LogLevel.ERROR)) {
            Log.e(tag, msg, tr);
        }
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void w(String tag, String msg) {
        if (canILog(LogLevel.WARN)) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (canILog(LogLevel.WARN)) {
            Log.w(tag, msg, tr);
        }
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (canILog(LogLevel.INFO)) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (canILog(LogLevel.INFO)) {
            Log.i(tag, msg, tr);
        }
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (canILog(LogLevel.DEBUG)) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (canILog(LogLevel.DEBUG)) {
            Log.d(tag, msg);
        }
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void v(String tag, String msg) {
        if (canILog(LogLevel.VERBOSE)) {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (canILog(LogLevel.VERBOSE)) {
            Log.v(tag, msg);
        }
    }


    /**
     * 使用默认TAG，INFO级别输出
     */
    public static void log(String msg) {
        i(TAG, msg);
    }

    /**
     * 输出异常信息，WARN级别
     */
    public static void print(Exception e) {
        if (e == null) {
            return;
        }

        if (sIsLogEnabled && sLogLevel.value >= LogLevel.WARN.value) {
            e.printStackTrace();
        }
    }


}
