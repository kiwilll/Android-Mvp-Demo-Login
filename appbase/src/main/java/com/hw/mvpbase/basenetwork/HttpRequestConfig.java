package com.hw.mvpbase.basenetwork;

import android.content.Context;
import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by hw on 5/10/17.<br>
 * <p>
 * http请求基础配置参数
 */

public class HttpRequestConfig {

    private Context context;
    private String appId;
    private String version;
    private String baseUrl;             //请求base url
    private boolean debug;              //是否debug
    private long readTimeout;
    private TimeUnit readTimeUnit;
    private long connectTimeout;
    private TimeUnit connectTimeUnit;
    private String bundleId;            //区分不同包名的应用
    private String platPublicKey;//平台公钥  验签用

    public HttpRequestConfig(Builder builder) {
        context = builder.context;
        appId = builder.appId;
        version = builder.version;
        baseUrl = builder.baseUrl;
        debug = builder.debug;
        readTimeout = builder.readTimeout;
        readTimeUnit = builder.readTimeUnit;
        connectTimeout = builder.connectTimeout;
        connectTimeUnit = builder.connectTimeUnit;
        bundleId = builder.bundleId;
        platPublicKey = builder.platPublicKey;
    }

    public String getAppId() {
        return appId;
    }

    public Context getContext() {
        return context;
    }

    public boolean isDebug() {
        return debug;
    }


    public String getVersion() {
        return version;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public TimeUnit getConnectTimeUnit() {
        return connectTimeUnit;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public TimeUnit getReadTimeUnit() {
        return readTimeUnit;
    }


    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getBundleId() {
        return bundleId;
    }

    public String getPlatPublicKey() {
        return platPublicKey;
    }

    public static class Builder {

        private Context context;
        private String appId;
        private String version;
        private boolean debug;
        private String baseUrl;
        private long readTimeout;
        private TimeUnit readTimeUnit;
        private long connectTimeout;
        private TimeUnit connectTimeUnit;
        private String bundleId;
        private String platPublicKey;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public Builder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder setPlatPublicKey(String platPublicKey) {
            this.platPublicKey = platPublicKey;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setReadTimeout(long timeout, TimeUnit timeUnit) {
            this.readTimeout = timeout;
            this.readTimeUnit = timeUnit;
            return this;
        }

        public Builder setConnectTimeout(long timeout, TimeUnit timeUnit) {
            this.connectTimeout = timeout;
            this.connectTimeUnit = timeUnit;
            return this;
        }

        public Builder setBundleId(String bundleId) {
            this.bundleId = bundleId;
            return this;
        }

        public HttpRequestConfig build() {
            initEmptyInitialValues();
            return new HttpRequestConfig(this);
        }

        private void initEmptyInitialValues() {
            if (readTimeout <= 0) {
                readTimeout = 30;
                readTimeUnit = TimeUnit.SECONDS;
            }
            if (connectTimeout <= 0) {
                connectTimeout = 30;
                connectTimeUnit = TimeUnit.SECONDS;
            }
            if (TextUtils.isEmpty(this.baseUrl)) {
                throw new IllegalArgumentException("The base url should not be null.");
            }
        }

    }

}
