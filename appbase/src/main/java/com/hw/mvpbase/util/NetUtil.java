package com.hw.mvpbase.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 跟网络相关的工具类
 */
public class NetUtil {

    // 信号强度
    public static final int SIGNAL_HIGH = 0;
    public static final int SIGNAL_MIDDLE = 1;
    public static final int SIGNAL_LOW = 2;

    // wifi的3中模式
    private static final int WIFICIPHER_NOPASS = 0;
    private static final int WIFICIPHER_WEP = 1;
    private static final int WIFICIPHER_WPA = 2;


    /**
     * 是否连接指定的wifi
     * @param context
     * @param wifiname wifi名称
     * @return
     */
    public static boolean connectAppointWifi(Context context, String wifiname) {
        if(null == context){
            return false;
        }
        // 获取系统wifi服务
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(context.WIFI_SERVICE);
        // 获取当前所连接wifi的信息
        WifiInfo wi = wm.getConnectionInfo();
        if(null == wi){
            return false;
        }
        if(TextUtils.isEmpty(wi.getSSID())){
            return false;
        }
        if(wi.getSSID().replaceAll("\"","").equals(wifiname)){
            return true;
        }
        return false;
    }

    /**
     * 切换连接指定wifi
     * @param context
     * @param wifiname 要连接的wifi名称
     * @param wifiPassword 要连接的wifi密码
     * @param wifiType 要连接的wifi类型
     * @return
     */
    public static boolean changeConnectWifi(Context context, String wifiname, String wifiPassword, int wifiType){
        if(null == context){
            return false;
        }
        // 获取系统wifi服务
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(context.WIFI_SERVICE);
        // 获取当前所连接wifi的信息
        WifiInfo wi = wm.getConnectionInfo();
        if(null == wi){
            return false;
        }
        if(TextUtils.isEmpty(wi.getSSID())){
            return false;
        }
        if(wi.getSSID().replaceAll("\"","").equals(wifiname)){
            return false;
        }
        // 获取扫描到的所有wifi信息
        List<ScanResult> scanResults = wm.getScanResults();
        if(scanResults.size() == 0) {// 判断是否因为权限问题导致获取不到
            PackageManager pm = context.getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", context.getPackageName()));
            if (!permission) {
                return false;
            }
        }
        for (ScanResult result : scanResults) {// scanResult.SSID(); scanResult.BSSID(); scanResult.level();    // 信号强度(原始数据)
            if(result.SSID.equals(wifiname)){
                int wcgID = wm.addNetwork(createWifiConfig(wm, result.SSID, wifiPassword, wifiType));
                boolean enable = wm.enableNetwork(wcgID, true);
                if(enable){
                    boolean reconnect = wm.reconnect();
                    return reconnect;
                }
                return false;
            }
        }
        return false;
    }

    /**
     * 创建wifi
     * @param mWifiManager
     * @param ssid
     * @param password
     * @param type
     * @return
     */
    private static WifiConfiguration createWifiConfig(WifiManager mWifiManager, String ssid, String password, int type) {
        //初始化WifiConfiguration
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();

        //指定对应的SSID
        config.SSID = "\"" + ssid + "\"";

        //如果之前有类似的配置
        WifiConfiguration tempConfig = isExist(mWifiManager, ssid);
        if(tempConfig != null) {
            //则清除旧有配置
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        //不需要密码的场景
        if(type == WIFICIPHER_NOPASS) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            //以WEP加密的场景
        } else if(type == WIFICIPHER_WEP) {
            config.hiddenSSID = true;
            config.wepKeys[0]= "\""+password+"\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
            //以WPA加密的场景，自己测试时，发现热点以WPA2建立时，同样可以用这种配置连接
        } else if(type == WIFICIPHER_WPA) {
            config.preSharedKey = "\""+password+"\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }

        return config;
    }

    /**
     * 判断指定wifi是否存在
     * @param mWifiManager
     * @param ssid
     * @return
     */
    private static WifiConfiguration isExist(WifiManager mWifiManager, String ssid) {
        List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();

        for (WifiConfiguration config : configs) {
            if (config.SSID.equals("\""+ssid+"\"")) {
                return config;
            }
        }
        return null;
    }

    /**
     * 判断当前wifi信号强度
     * @param context
     * @return
     */
    public static int getWifiIntensity(Context context){
        if(null == context){
            return SIGNAL_LOW;
        }
        // 获取系统wifi服务
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(context.WIFI_SERVICE);
        // 获取当前所连接wifi的信息
        WifiInfo wi = wm.getConnectionInfo();
        if(null == wi){
            return SIGNAL_LOW;
        }
        if (wi.getRssi() > -50){// 标示信号良好
            return SIGNAL_HIGH;
        }
        if(wi.getRssi() > -70){
            return SIGNAL_MIDDLE;
        }
        return SIGNAL_LOW;
    }

    /**
     * 获取当前连接的wifi名称
     * @param context
     * @return
     */
    public static String getWifiName(Context context) {
        if(null == context){
            return "";
        }
        // 获取系统wifi服务
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(context.WIFI_SERVICE);
        // 获取当前所连接wifi的信息
        WifiInfo wi = wm.getConnectionInfo();
        if(null == wi){
            return "";
        }
        return wi.getSSID().replaceAll("\"","");
    }


    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        if(null == context){
            return false;
        }
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        if(null == context){
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null || null == cm.getActiveNetworkInfo())
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT > 14) {
            try {
                activity.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            } catch (Exception e) {
                activity.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        } else {
            Intent intent = new Intent("/");
            ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
            intent.setComponent(cm);
            intent.setAction("android.intent.action.VIEW");
            activity.startActivityForResult(intent, 0);
        }
    }
    /**
     * 获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     * 自定义
     *
     * @param context
     * @return
     */
    public static int getAPNType(Context context) {
        if(null == context){
            return 0;
        }
        //结果返回值
        int netType = 0;
        //获取手机所有连接管理对象
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //NetworkInfo对象为空 则代表没有网络
        if (networkInfo == null) {
            return netType;
        }
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            //WIFI
            netType = 1;
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 4;
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 3;
                //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 2;
            } else {
                netType = 2;
            }
        }
        return netType;
    }

    public static String getNetType(Context context){
        if(null == context){
            return "offLine";
        }
        //结果返回值
        String netType = "offLine";
        //获取手机所有连接管理对象
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //NetworkInfo对象为空 则代表没有网络
        if (networkInfo == null) {
            return netType;
        }
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            //WIFI
            netType = "wifi";
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming()) {
                netType = "gprs";
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    && !telephonyManager.isNetworkRoaming()) {
                netType = "gprs";
                //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    && !telephonyManager.isNetworkRoaming()) {
                netType = "gprs";
            } else {
                netType = "gprs";
            }
        }
        return netType;
    }

    /**
     * 获取外网的IP(要访问Url，要放到后台线程里处理)
     *
     * @param @return
     * @return String
     * @throws
     * @Title: GetNetIp
     * @Description:
     */
    public static String getNetIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        String ipLine = "";
        HttpURLConnection httpConnection = null;
        try {
            infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
            URLConnection connection = infoUrl.openConnection();
            httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    strber.append(line + "\n");
                }
                Pattern pattern = Pattern
                        .compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
                Matcher matcher = pattern.matcher(strber.toString());
                if (matcher.find()) {
                    ipLine = matcher.group();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
                httpConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return ipLine;
    }
}