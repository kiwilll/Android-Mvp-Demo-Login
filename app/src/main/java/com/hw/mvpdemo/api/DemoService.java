package com.hw.mvpdemo.api;

import com.hw.mvpbase.entity.BaseResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by hw on 2018/7/18.
 */

public interface DemoService {

    @POST("")
    Observable<BaseResponse> postSomething(@HeaderMap Map<String, String> headers, @Body String jsonBody);

    Observable<BaseResponse> postSomething(@Url String url, @HeaderMap Map<String, String> headers, @Body String jsonBody);
}
