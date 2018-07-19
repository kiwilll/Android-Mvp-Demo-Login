package com.hw.mvpdemo.api;

import com.hw.mvpbase.basenetwork.BaseApi;
import com.hw.mvpbase.basenetwork.utils.HttpReqParamUtil;
import com.hw.mvpbase.entity.BaseResponse;
import com.hw.mvpbase.util.Rsa;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hw on 2018/7/18.
 */

public class DemoApi extends BaseApi {

    private static DemoService getDemoService() {
        return getService(DemoService.class);
    }

    public static Observable<BaseResponse> postSomething(String something) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("something", something);

        String bodyJson = strMapToJson(map);
        Map<String, String> headerMap = getHeaderMap(bodyJson, null);

        Observable<BaseResponse> observable = getDemoService().postSomething(headerMap, bodyJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }
}
