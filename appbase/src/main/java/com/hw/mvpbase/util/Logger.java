package com.hw.mvpbase.util;

import android.util.Log;

/**
 * Created by hw on 2018/4/25.
 */

public class Logger {

    private static final String TAG = "BwtRideSdk";
    private static boolean mDebug;

    public static void setDebug(boolean debug) {
        mDebug = debug;
    }

    public static void d(String msg) {
        if (mDebug)
            Log.d(TAG, msg);
    }

    public static void i(String msg) {
        if (mDebug)
            Log.i(TAG, msg);
    }

    public static void w(String msg) {
        if (mDebug)
            Log.w(TAG, msg);
    }
}
