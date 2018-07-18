package com.hw.mvpbase.basenetwork;

import android.content.Context;

import java.util.concurrent.TimeUnit;

/**
 * Created by hw on 5/10/17.<br>
 */

public class RequestConfig {

    private Context context;
    private String appId;
    private String version;
    private String publicKey;           //http请求时RSA公钥
    private String baseUrl;
    private boolean debug;              //是否debug

    private long readTimeout;
    private TimeUnit readTimeUnit;
    private long connectTimeout;
    private TimeUnit connectTimeUnit;

    private String baseUrlOld;          //老协议接口地址
    private String publicKeyOld;        //老协议rsa 公钥
    private String apiVersion;          //协议版本号

    private String deviceId;
    private String bundleId;            //区分不同包名的应用

    private String token;               //登录后的token
    private String cityId;

    public RequestConfig(Builder builder) {
        context = builder.context;
        appId = builder.appId;
        version = builder.version;
        publicKey = builder.publicKey;
        baseUrl = builder.baseUrl;
        debug = builder.debug;
        readTimeout = builder.readTimeout;
        readTimeUnit = builder.readTimeUnit;
        connectTimeout = builder.connectTimeout;
        connectTimeUnit = builder.connectTimeUnit;

        baseUrlOld = builder.baseUrlOld;
        publicKeyOld = builder.publicKeyOld;
        apiVersion = builder.apiVersion;

        deviceId = builder.deviceId;
        cityId = builder.cityId;
        bundleId = builder.bundleId;
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

    public String getPublicKey() {
        return publicKey;
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

    public String getBaseUrlOld() {
        return baseUrlOld;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getPublicKeyOld() {
        return publicKeyOld;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setV2BaseUrl(String url) {
        this.baseUrlOld = url;
    }

    public void setV3BaseUrl(String url) {
        this.baseUrl = url;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getBundleId() {
        return bundleId;
    }

    public static class Builder {

        private Context context;
        private String appId;
        private String version;
        private String publicKey;
        private boolean debug;
        private String baseUrl;

        private long readTimeout;
        private TimeUnit readTimeUnit;
        private long connectTimeout;
        private TimeUnit connectTimeUnit;


        private String baseUrlOld;          //老接口协议
        private String publicKeyOld;        //老协议rsa 公钥
        private String apiVersion;          //协议版本号

        private String deviceId;
        private String cityId;

        private String bundleId;

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

        public Builder setPublickKey(String publicKey) {
            this.publicKey = publicKey;
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

        public Builder setOldBaseUrl(String url) {
            this.baseUrlOld = url;
            return this;
        }

        public Builder setOldPublicKey(String rsaKey) {
            this.publicKeyOld = rsaKey;
            return this;
        }

        public Builder setApiVersion(String version) {
            this.apiVersion = version;
            return this;
        }

        public Builder setDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder setCityId(String cityId) {
            this.cityId = cityId;
            return this;
        }

        public Builder setBundleId(String bundleId) {
            this.bundleId = bundleId;
            return this;
        }

        public RequestConfig build() {
            initEmptyInitialValues();
            return new RequestConfig(this);
        }

        private void initEmptyInitialValues() {
            if(readTimeout <= 0) {
                readTimeout = 30;
                readTimeUnit = TimeUnit.SECONDS;
            }
            if(connectTimeout <= 0) {
                connectTimeout = 30;
                connectTimeUnit = TimeUnit.SECONDS;
            }
        }

    }

}
