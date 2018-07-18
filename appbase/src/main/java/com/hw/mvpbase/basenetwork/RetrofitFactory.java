package com.hw.mvpbase.basenetwork;

import android.text.TextUtils;

import com.hw.mvpbase.basenetwork.converter.GsonConverterFactory;
import com.hw.mvpbase.basenetwork.interceptor.HttpLoggingInterceptor;
import com.hw.mvpbase.basenetwork.interceptor.RequestInterceptor;
import com.hw.mvpbase.basenetwork.interceptor.ResponseInterceptor;
import com.hw.mvpbase.basenetwork.interceptor.ResponseNoneSignHeaderInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;


/**
 * Created by hw on 5/10/17.<br>
 */

public class RetrofitFactory {

    /**
     * 创建retrofit
     *
     * @param config
     * @return
     */
    public static Retrofit createRetrofit(HttpRequestConfig config) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(config.getConnectTimeout(), config.getConnectTimeUnit())
                .readTimeout(config.getReadTimeout(), config.getReadTimeUnit())
                .addInterceptor(new RequestInterceptor(config))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(config.isDebug() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .addInterceptor(new ResponseInterceptor(config))
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(config.getContext()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        if(!TextUtils.isEmpty(config.getBaseUrl()))
            builder.baseUrl(config.getBaseUrl());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    /**
     * 创建不需要签名、验签的retrofit
     *
     * @param config
     * @return
     */
    public static Retrofit createNoneSignRetrofit(HttpRequestConfig config) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(config.getConnectTimeout(), config.getConnectTimeUnit())
                .readTimeout(config.getReadTimeout(), config.getReadTimeUnit())
                .addInterceptor(new RequestInterceptor(config))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(config.isDebug() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .addInterceptor(new ResponseNoneSignHeaderInterceptor(config))
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(config.getContext()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        if(!TextUtils.isEmpty(config.getBaseUrl()))
            builder.baseUrl(config.getBaseUrl());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

}