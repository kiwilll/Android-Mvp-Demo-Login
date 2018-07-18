package com.bwton.metro.usermanager.api;

import com.bwton.metro.sharedata.model.BaseResponse;
import com.bwton.metro.usermanager.entity.LoginResultInfo;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

/**
 * Created by hw on 7/11/17.<br>
 */

public interface UserService {

    @POST("app/user/register")
    Observable<BaseResponse<LoginResultInfo>> register(@HeaderMap Map<String, String> headers, @Body String jsonBody);

    @POST("app/user/loginByPassword")
    Observable<BaseResponse<LoginResultInfo>> loginByPwd(@HeaderMap Map<String, String> headers, @Body String jsonBody);

    @POST("app/user/loginByCode")
    Observable<BaseResponse<LoginResultInfo>> loginByCode(@HeaderMap Map<String, String> headers, @Body String jsonBody);

    @POST("app/code/send")
    Observable<BaseResponse> sendCode(@HeaderMap Map<String, String> headers, @Body String jsonBody);

    @POST("app/user/loginOut")
    Observable<BaseResponse> logout(@HeaderMap Map<String, String> headers, @Body String jsonBody);

    @POST("app/user/passwordService")
    Observable<BaseResponse> resetPwd(@HeaderMap Map<String, String> headers, @Body String jsonBody);

    @POST("app/user/modifyUserInfo")
    Observable<BaseResponse> modifyUserInfo(@HeaderMap Map<String, String> headers, @Body String jsonBody);

}