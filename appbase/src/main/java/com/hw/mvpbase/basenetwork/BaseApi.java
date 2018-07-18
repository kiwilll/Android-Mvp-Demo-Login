package com.hw.mvpbase.basenetwork;



import com.google.gson.Gson;
import com.hw.mvpbase.R;
import com.hw.mvpbase.basenetwork.exception.ApiException;

import java.io.IOException;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hw on 5/11/17.<br>
 */

public abstract class BaseApi {

    protected static final Gson GSON = new Gson();

    /**
     * 生成http签名以及加密请求头信息<br/>
     *
     * @param bodyJson 待签名的请求body内容
     * @return 返回的Map里包含2个字段
     * signature: 对请求body内容进行签名后的值
     * random: 对加密字段采用AES算法进行加密, 将所用的加密key通过RSA进行加密后的值
     */
    public static Map<String, String> getHeaderMap(String bodyJson, String random) {
        return HttpReqManager.getInstance().genSignAndEncryptInfo(bodyJson, random);
    }

    /**
     * 获取retrofit service
     *
     * @param serviceClass retrofit service
     * @param <T>
     * @return
     */
    public static <T> T getService(Class<T> serviceClass) {
        return HttpReqManager.getInstance().getService(serviceClass);
    }

    /**
     * 获取不验证签名的retrofit service
     *
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T getNoneSignService(Class<T> serviceClass) {
        return HttpReqManager.getInstance().getNoneSignService(serviceClass);
    }

    /**
     * 针对RxJava2的subscribe方法统一处理成功的回调
     *
     * @param clazz
     * @param callback
     * @param <T>
     * @return
     */
    public static <T> Consumer<T> createSuccConsumer(Class<T> clazz, final ApiCallback<T> callback) {
        return new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                callback.onSucc(t);
            }
        };
    }

    /**
     * 针对RxJava2的subscribe方法统一处理失败的回调
     *
     * @param callback
     * @return
     */
    public static Consumer<Throwable> createErrorConsumer(final ApiCallback callback) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (throwable instanceof ApiException) {
                    callback.onError(throwable);
                } else if (throwable instanceof IOException) {
                    //如果是未知IOException, 则重新包装错误信息
                    callback.onError(new ApiException(HttpReqManager.getInstance().getContext().getString(R.string.bwt_error_msg_server_error)));
                } else {
                    callback.onError(throwable);
                }
            }
        };
    }

    public static <T> Disposable toSubscribe(Observable<T> observable, Class<T> clazz, ApiCallback callback) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createSuccConsumer(clazz, callback), createErrorConsumer(callback));
    }

    /**
     * 将map转换成json格式字符串
     *
     * @param map key: String类型, value: Object类型
     * @return
     */
    public static String mapToJson(Map<String, Object> map) {
        if (map == null) {
            return "{}";
        }
        return GSON.toJson(map);
    }

    /**
     * 将map转换成json格式字符串
     *
     * @param map key, value均为String类型
     * @return
     */
    public static String strMapToJson(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }
        return GSON.toJson(map);
    }

}
