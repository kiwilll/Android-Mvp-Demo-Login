package com.hw.mvpbase.basenetwork.interceptor;


import com.hw.mvpbase.R;
import com.hw.mvpbase.basenetwork.HttpRequestConfig;
import com.hw.mvpbase.basenetwork.exception.ApiException;
import com.hw.mvpbase.basenetwork.exception.NoNetworkException;
import com.hw.mvpbase.basenetwork.utils.HttpRespUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hw on 7/8/16.<br>
 * 对http请求增加固定的头信息以及其他额外处理操作
 */
public class RequestInterceptor implements Interceptor {

    private HttpRequestConfig mConfig;

    public RequestInterceptor(HttpRequestConfig config) {
        mConfig = config;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //如果没有联网, 则直接抛出异常
        if (!HttpRespUtil.isConnected(mConfig.getContext())) {
            throw new NoNetworkException(mConfig.getContext().getString(R.string.bwt_error_msg_net_error));
        }
        Request.Builder builder = chain.request()
                .newBuilder()
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Content-Type", "application/json")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "application/json");
        Request request = builder.build();
        try {
            return chain.proceed(request);
        } catch (IOException e) {
            throw new ApiException(mConfig.getContext().getString(R.string.bwt_error_msg_server_error));
        }
    }


}
