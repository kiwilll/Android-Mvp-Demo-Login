package com.hw.mvpdemo;

import android.app.Application;
import android.text.TextUtils;

import com.hw.mvpbase.basenetwork.HttpReqManager;
import com.hw.mvpbase.basenetwork.HttpRequestConfig;


/**
 * Created by hw on 18/1/2.<br>
 */

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //网络框架配置
        HttpRequestConfig config = new HttpRequestConfig.Builder(this)
                .setAppId("")
                .setPlatPublicKey("")
                .setVersion("")
                .setBaseUrl("")
                .setDebug(false)
                .setBundleId("")
                .build();
        HttpReqManager.init(config);
    }
}
