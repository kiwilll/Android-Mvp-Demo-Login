package com.hw.mvpbase.basenetwork.converter;

import android.content.Context;


import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by hw on 7/7/16.<br>
 */
public class GsonConverterFactory extends Converter.Factory {

    public static GsonConverterFactory create(Context context) {
        return new GsonConverterFactory(context);
    }

    private final Gson mGson;
    private final Context mContext;

    public GsonConverterFactory(Context context) {
        mContext = context.getApplicationContext();
        mGson = new Gson();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new GsonRequestBodyConverter<>(mGson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new GsonResponseBodyConverter<>(type, mGson, mContext);
    }
}
