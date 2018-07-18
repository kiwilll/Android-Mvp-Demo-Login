package com.hw.mvpbase.basenetwork.utils;

import android.text.TextUtils;

import com.hw.mvpbase.util.Hash;
import com.hw.mvpbase.util.Rsa;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.hw.mvpbase.util.Rsa.encryptByPublicKey;


/**
 * Created by hw on 4/11/17.<br>
 */

public class HttpReqParamUtil {

    private static final String RANDOM_CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final String RANDOM_NUMBERS = "0123456789";

    /**
     * 随机生成8位数字加密字符码
     *
     * @return AES加密key
     */
    public static String genAesKey() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int random = (int) (Math.random() * RANDOM_CHARS.length());
            sb.append(RANDOM_CHARS.charAt(random));
        }
        return sb.toString();
    }


    public static String genRandom() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int random = (int) (Math.random() * RANDOM_CHARS.length());
            sb.append(RANDOM_CHARS.charAt(random));
        }
        return sb.toString();
    }


    /**
     * 随机生成20位数字加密字符码
     *
     * @return AES加密key
     */
    public static String getRandomNumbers() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String result = formatter.format(curDate);
        StringBuilder sb = new StringBuilder();
        sb.append(result);
        for (int i = 0; i < 3; i++) {
            int random = (int) (Math.random() * RANDOM_NUMBERS.length());
            sb.append(RANDOM_NUMBERS.charAt(random));
        }
        return sb.toString();
    }
    /**
     * 对http请求body体进行签名
     *
     * @param bodyJson 请求body体字符串
     * @param publicKey RSA公钥
     *
     * @return 签名值
     */
    public static String signReqJsonBody(String bodyJson, String publicKey) {
        String jsonStr = bodyJson;
        if(jsonStr == null) {
            jsonStr = "";
        }
        String md5 = Hash.getMD5(jsonStr);
        String signature = encryptByPublicKey(md5, publicKey);
        return signature;
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    /**
     * 得到当前时间戳数组
     */
    public static String[] getTimeStemp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String[] result = new String[2];
        result[0] = formatter.format(curDate);
        result[1] = formatter2.format(curDate);
        return result;
    }
}