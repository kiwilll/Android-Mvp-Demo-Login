package com.hw.mvpbase.basenetwork;


import android.content.Context;
import android.text.TextUtils;


import com.hw.mvpbase.basenetwork.utils.HttpReqParamUtil;
import com.hw.mvpbase.util.Rsa;

import java.util.HashMap;
import java.util.Map;


import retrofit2.Retrofit;

import static com.hw.mvpbase.basenetwork.utils.HttpReqParamUtil.getTimeStemp;


/**
 * Retrofit统一管理
 */
public class HttpReqManager {

    private static HttpReqManager INSTANCE = null;

    /**
     * 初始化
     */
    public static void init(HttpRequestConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("RequestConfig cannot be null.");
        }
        INSTANCE = new HttpReqManager(config);
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static HttpReqManager getInstance() {
        if (INSTANCE == null) {
            throw new IllegalArgumentException("Must call init() method before call this.");
        }
        return INSTANCE;
    }

    private HttpRequestConfig mConfig;
    private Map<String, Object> mServiceMap;

    private Retrofit mRetrofitClient;
    private Retrofit mRetrofitNoneSign;             //不签名以及不验签的服务

    private HttpReqManager(HttpRequestConfig config) {
        mConfig = config;
        mServiceMap = new HashMap<String, Object>();
    }


    public String getVersion() {
        return mConfig.getVersion();
    }

    public Context getContext() {
        return mConfig.getContext();
    }

    /**
     * 创建retrofit api service实例
     *
     * @param serviceClass retrofit service
     * @param <S>
     * @return
     */
    public <S> S getService(Class<S> serviceClass) {
        if (mServiceMap.containsKey(serviceClass.getName())) {
            return (S) mServiceMap.get(serviceClass.getName());
        } else {
            S obj = createService(serviceClass);
            //缓存起来
            mServiceMap.put(serviceClass.getName(), obj);
            return obj;
        }
    }

    /**
     * 获取不需要签名以及不验证签名的retrofit api service实例
     *
     * @param serviceClass retrofit service
     * @param <S>
     * @return
     */
    public <S> S getNoneSignService(Class<S> serviceClass) {
        if (mServiceMap.containsKey(serviceClass.getName())) {
            return (S) mServiceMap.get(serviceClass.getName());
        } else {
            S obj = createNoneSignService(serviceClass);
            //缓存起来
            mServiceMap.put(serviceClass.getName(), obj);
            return obj;
        }
    }

    /**
     * 生成http签名以及加密请求头信息<br/>
     * signature: 对请求body内容进行签名后的值
     * random: 对加密字段采用AES算法进行加密, 将所用的加密key通过RSA进行签名后的值
     *
     * @param bodyJson 待签名的请求body内容
     * @param randomAesKey 针对部分字段采用AES算法加密用的key, 为空则表示没有加密字段
     * @return
     */
    public Map<String, String> genSignAndEncryptInfo(String bodyJson, String randomAesKey) {
        String signature = HttpReqParamUtil.signReqJsonBody(bodyJson, mConfig.getPlatPublicKey());
        Map<String, String> map = new HashMap<String, String>();
        map.put("signature", signature);
        if(!TextUtils.isEmpty(randomAesKey)) {
            map.put("random", Rsa.encryptByPublicKey(randomAesKey, mConfig.getPlatPublicKey()));
        }
        return map;
    }

    /**
     * 生成头部信息
     *
     * @return
     */
    public Map<String, Object> genHeadMap(String userId, String random, String publicKey) {
        String[] arr_time = getTimeStemp();
        String timestamp = arr_time[0];
        String nonce = HttpReqParamUtil.getRandomNumbers();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("timestamp", timestamp);
        map.put("nonce", nonce);
        map.put("random", Rsa.encryptByPublicKey(random, publicKey));
        return map;
    }


    private <S> S createService(Class<S> serviceClass) {
        if (mRetrofitClient == null) {
            mRetrofitClient = RetrofitFactory.createRetrofit(mConfig);
        }
        return mRetrofitClient.create(serviceClass);
    }

    private <S> S createNoneSignService(Class<S> serviceClass) {
        if (mRetrofitNoneSign == null) {
            mRetrofitNoneSign = RetrofitFactory.createNoneSignRetrofit(mConfig);
        }
        return mRetrofitNoneSign.create(serviceClass);
    }

    public void resetConfig(String baseUrl) {
        mServiceMap.clear();
        mRetrofitClient = null;
        mRetrofitNoneSign = null;
        mConfig.setBaseUrl(baseUrl);
    }

}