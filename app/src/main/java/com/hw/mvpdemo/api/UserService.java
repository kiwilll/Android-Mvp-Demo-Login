package com.hw.mvpdemo.api;

import com.hw.mvpbase.entity.BaseResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by hw on 7/11/17.<br>
 */

public interface UserService {

    @POST("")
    Observable<BaseResponse> register(@HeaderMap Map<String, String> headers, @Body String jsonBody);

    Observable<BaseResponse> register(@Url String url, @HeaderMap Map<String, String> headers, @Body String jsonBody);

    @POST("")
    Observable<BaseResponse> loginByPwd(@HeaderMap Map<String, String> headers, @Body String jsonBody);

    @POST("")
    Observable<BaseResponse> loginByCode(@HeaderMap Map<String, String> headers, @Body String jsonBody);

    @POST("")
    Observable<BaseResponse> sendCode(@HeaderMap Map<String, String> headers, @Body String jsonBody);

    @POST("")
    Observable<BaseResponse> logout(@HeaderMap Map<String, String> headers, @Body String jsonBody);

    @POST("")
    Observable<BaseResponse> resetPwd(@HeaderMap Map<String, String> headers, @Body String jsonBody);

    @POST("")
    Observable<BaseResponse> modifyUserInfo(@HeaderMap Map<String, String> headers, @Body String jsonBody);

}