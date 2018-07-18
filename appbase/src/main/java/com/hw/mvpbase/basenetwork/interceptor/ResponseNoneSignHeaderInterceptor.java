package com.hw.mvpbase.basenetwork.interceptor;


import com.hw.mvpbase.R;
import com.hw.mvpbase.basenetwork.HttpRequestConfig;
import com.hw.mvpbase.basenetwork.exception.ResponseFailException;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hw on 4/21/17.<br>
 *
 * 不验证签名
 */

public class ResponseNoneSignHeaderInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private HttpRequestConfig mConfig;

    public ResponseNoneSignHeaderInterceptor(HttpRequestConfig config) {
        mConfig = config;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if(!response.isSuccessful()) {
            throw new ResponseFailException(mConfig.getContext().getString(R.string.bwt_error_msg_server_error));
        }

        return response;
    }
}
