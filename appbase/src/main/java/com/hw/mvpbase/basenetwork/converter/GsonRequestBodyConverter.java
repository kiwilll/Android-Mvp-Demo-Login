package com.hw.mvpbase.basenetwork.converter;


import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by hw on 7/7/16.<br>
 */
public class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    private final Gson mGson;

    public GsonRequestBodyConverter(Gson gson) {
        mGson = gson;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        //如果提交的直接是字符串, 则不需要json转换
        if(value instanceof String) {
            return RequestBody.create(MEDIA_TYPE, ((String) value).getBytes("UTF-8"));
        } else if(value instanceof RequestBody) {
            return (RequestBody) value;
        }
        return RequestBody.create(MEDIA_TYPE, mGson.toJson(value));
    }
}
