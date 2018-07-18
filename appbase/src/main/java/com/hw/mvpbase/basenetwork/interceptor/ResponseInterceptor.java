package com.hw.mvpbase.basenetwork.interceptor;

import android.text.TextUtils;


import com.hw.mvpbase.R;
import com.hw.mvpbase.basenetwork.HttpRequestConfig;
import com.hw.mvpbase.basenetwork.exception.ApiException;
import com.hw.mvpbase.basenetwork.exception.ResponseEmptyException;
import com.hw.mvpbase.basenetwork.exception.ResponseFailException;
import com.hw.mvpbase.basenetwork.exception.SignInvalidException;
import com.hw.mvpbase.basenetwork.utils.HttpRespUtil;
import com.hw.mvpbase.entity.BaseResponse;
import com.hw.mvpbase.util.Logger;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by hw on 4/21/17.<br>
 * 对http返回结果进行拦截做额外处理
 */

public class ResponseInterceptor implements Interceptor {

    private HttpRequestConfig mConfig;

    public ResponseInterceptor(HttpRequestConfig config) {
        mConfig = config;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (IOException e) {
            Logger.d("-->response IOException:" + e.getMessage());
            throw new ApiException(mConfig.getContext().getString(R.string.bwt_error_msg_server_error));
        }

        if (!response.isSuccessful()) {
            Logger.d("-->response is not 'success'");
            throw new ResponseFailException(mConfig.getContext().getString(R.string.bwt_error_msg_server_error));
        }

        Headers headers = response.headers();
        String signature = headers.get("signature");
        String nonce = headers.get("nonce");
        String timestamp = headers.get("timestamp");
        String sequence = headers.get("sequence");
        ResponseBody body = response.body();
        BufferedSource source = body.source();
        source.request(Long.MAX_VALUE);

        Buffer buffer = source.buffer().clone();
        String bodyStr = buffer.readUtf8();
        buffer.close();

        if (TextUtils.isEmpty(bodyStr)) {
            Logger.d("-->response bodyStr is empty");
            throw new ResponseEmptyException(mConfig.getContext().getString(R.string.bwt_error_msg_server_error));
        }

        boolean isValid = HttpRespUtil.checkResponse(mConfig.getAppId(), timestamp, nonce, sequence, signature, bodyStr, mConfig.getPlatPublicKey());
        if (!isValid) {
            Logger.d("-->response Valid Fail");
            throw new SignInvalidException(mConfig.getContext().getString(R.string.bwt_error_msg_server_error));
        } else {
            Logger.d("-->response success");
            return response;
        }
    }
}
