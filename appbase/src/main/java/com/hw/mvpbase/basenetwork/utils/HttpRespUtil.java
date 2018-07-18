package com.hw.mvpbase.basenetwork.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.hw.mvpbase.util.Rsa;


/**
 * Created by hw on 4/11/17.<br>
 */

public class HttpRespUtil {

    /**
     * 验证http response签名是否正确
     *
     * @param appid
     * @param timestamp
     * @param nonce
     * @param sequence
     * @param signature
     * @param respBody
     * @param accessPublicKey
     * @return
     */
    public static boolean checkResponse(String appid, String timestamp, String nonce, String sequence, String signature, String respBody, String accessPublicKey) {
        if (TextUtils.isEmpty(signature))
            return false;
        String toBeSigned = "appid=" + appid + "&message=" + respBody + "&nonce=" + nonce + "&sequence=" + sequence + "&timestamp=" + timestamp;
        boolean result = false;

        result = Rsa.doCheck256(toBeSigned, signature, accessPublicKey);
        return result;
    }

    public static boolean checkResponseRSA(String appid, String timestamp, String nonce, String sequence, String signature, String respBody, String accessPublicKey) {
        if (TextUtils.isEmpty(signature))
            return false;
        String toBeSigned = "appid=" + appid + "&message=" + respBody + "&nonce=" + nonce + "&sequence=" + sequence + "&timestamp=" + timestamp;
        boolean result = false;
        result = Rsa.doCheck(toBeSigned, signature, accessPublicKey);
        return result;
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            try {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (null != info && info.isConnected()) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                //如果因为权限问题抛出异常, 这里正常返回
                return true;
            }
        }
        return false;
    }
}
