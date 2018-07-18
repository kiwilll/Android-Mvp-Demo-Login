package com.hw.mvpbase.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.os.Build.VERSION_CODES.M;
import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by hasee on 2017/3/28.
 */

public class CommonUtil {

    /**
     * 获取sim卡iccid
     *
     * @return
     */
    public static String getIccid(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String iccid = "N/A";
        if(null == context){
            return iccid;
        }
        int res = context.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE);
        if (res == PackageManager.PERMISSION_GRANTED) {
            iccid = telephonyManager.getSimSerialNumber();
        }
        if(TextUtils.isEmpty(iccid)){
            iccid = "N/A";
        }
        return iccid;
    }

    /**
     * 获取sim卡数量
     *
     * @return
     */
    public static int getSimNum(Context context) {
        if(null == context){
            return 0;
        }
        int res = context.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE);
        if (Build.VERSION.SDK_INT >= M && res == PackageManager.PERMISSION_GRANTED) {
            return SubscriptionManager.from(context).getActiveSubscriptionInfoCount();
        }
        return 0;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return Build.MODEL; // 设备型号
    }

    // 打开系统设置页面
    public static void openSetting(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }


    /**
     * 通过反射的方式获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return 0;
        }
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 沉浸式状态栏
     */
    public static void initState(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    // 打开系统浏览器
    public static void launchBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            intent.setData(Uri.parse(url));
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     * @Description: 验证手机号
     */
    public static boolean checkPhoneNumber(String phoneNumber) {
        String value = phoneNumber;
        String regExp = "^1[0-9]{10}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(value);
        return m.matches();
    }


    // 获取当前手机蓝牙功能状态
    public static boolean bluetoothStatus() {
        BluetoothAdapter mBadapter = BluetoothAdapter.getDefaultAdapter();
        return null != mBadapter && mBadapter.isEnabled();
    }


    //该手机是否支持蓝牙功能
    public static boolean blueToothIsEnable() {
        BluetoothAdapter mBadapter = BluetoothAdapter.getDefaultAdapter();
        return null != mBadapter;
    }

    /**
     * 强制开启或关闭当前 Android 设备的 Bluetooth
     *
     * @return true：强制打开\关闭 Bluetooth　成功　false：强制打开\关闭 Bluetooth 失败
     */
    public static boolean switchBlueTooth(boolean openOrClose) {
        BluetoothAdapter mBadapter = BluetoothAdapter.getDefaultAdapter();
        if (null != mBadapter) {
            if (openOrClose) {
                return mBadapter.enable();
            } else {
                return mBadapter.disable();
            }
        }
        return false;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // 货币分转元
    public static String getFen2Yuan(int s) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format((float) s / 100.0f);
    }

    public static String getMoneyFormat(float money) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(money);
    }

    /**
     * 文件存储根目录, 该目录会随着应用卸载而卸载掉
     *
     * @param context
     * @return
     */
    public static String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 如果读写SD卡权限存在, 获取SD卡上的指定的缓存目录, 否则直接获取当前应用的缓存文件目录。
     * 如果是SD卡上的目录, 不会随着应用卸载而卸载
     *
     * @param context
     * @param cacheDir
     * @return
     */
    public static String getOwnCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
        }
        if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
            appCacheDir = context.getFilesDir();
        }
        if (!appCacheDir.exists())
            appCacheDir.mkdirs();
        return appCacheDir.getAbsolutePath();
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    // 获取设备的abi版本列表
    public static String getBrandAndAbi() {
        String[] abis;
        String abil = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            abis = Build.SUPPORTED_ABIS;
        } else {
            abis = new String[]{Build.CPU_ABI, Build.CPU_ABI2};
        }

        for (String abi : abis) {
            abil += abi + ",";
        }
        return abil + "-" + Build.BRAND + "-" + Build.MODEL + "-" + Build.VERSION.RELEASE;
    }

    public static boolean isAppRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = activityManager.getRunningTasks(1);
        if(list != null && !list.isEmpty()) {
            ComponentName component = list.get(0).topActivity;
            return context.getPackageName().equals(component.getPackageName());
        }
        return true;
    }

    public static String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager =
                (ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null && !runningTaskInfos.isEmpty()) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        return topActivityClassName;
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 获取七牛图片拼接的地址，按指定宽高缩放
     *
     * @param width  指定图片宽度
     * @param height 指定图片高度
     * @return
     */
    public static String getQiNiuTail(int width, int height) {
        if (width > 0) {
            if (height > 0)
                return "?imageView2/2/w/" + width + "/h/" + height + "/format/jpg";
            else
                return "?imageView2/2/w/" + width + "/format/jpg";
        } else {
            if (height > 0)
                return "?imageView2/2/h/" + height + "/format/jpg";
            else
                return "";
        }
    }

    public static void showSoftKeyboard(final Context context, final EditText editText) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);
    }

    public static void closeSoftKeyboard(Activity activity) {
        View currentFocusView = activity.getCurrentFocus();
        if (currentFocusView != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);
        }
    }

    /**
     * 设置输入框是否可以编辑
     */
    public static void isEditable(boolean value, EditText editText) {
        if (value) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.setFilters(new InputFilter[]{new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    return null;
                }
            }});
        } else {
            //设置不可获取焦点
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            //输入框无法输入新的内容
            editText.setFilters(new InputFilter[]{new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    return source.length() < 1 ? dest.subSequence(dstart, dend) : "";
                }
            }});
        }
    }

    //获取距离
    public static String getDistance(double meter) {
        if (meter > 4999)
            return new DecimalFormat("0.0").format(meter / 1000f) + "千米";
        else
            return new DecimalFormat("0").format(meter) + "米";
    }

    //获取绿色出行里程
    public static String getStepAll(double meter) {
        if (meter > 1000)
            return new DecimalFormat("0.0").format(meter / 1000f) + " km";
        else
            return new DecimalFormat("0").format(meter) + " m";
    }

    /**
     * 获取application中配置的meta-data值
     *
     * @param context
     * @param key
     * @return
     */
    public static String getMetadata(Context context, String key) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String value = appInfo.metaData.getString(key);
            return value;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return getIntMetadata(context, key);
        }
    }

    public static String getIntMetadata(Context context, String key) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            int value = appInfo.metaData.getInt(key);
            return String.valueOf(value);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 判断app是否已安装
     *
     * @param context
     * @param packageName 包名
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    /**
     * 判断手机是否ROOT
     */
    public static boolean isRoot() {
        boolean root = false;
        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                root = false;
            } else {
                root = true;
            }
        } catch (Exception e) {
        }
        return root;
    }

}
