package com.hw.mvpbase.basenetwork.converter;

import android.content.Context;


import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.hw.mvpbase.R;
import com.hw.mvpbase.util.Logger;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

/**
 * Created by hw on 7/7/16.<br>
 */
public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Type mType;
    private final Gson mGson;
    private final Context mContext;

    public GsonResponseBodyConverter(Type type, Gson gson, Context context) {
        mType = type;
        mGson = gson;
        mContext = context;
    }


    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            BufferedSource bufferedSource = Okio.buffer(value.source());
            String tempStr = bufferedSource.readUtf8();
            bufferedSource.close();
            if (mType == String.class) {
                Logger.d("-->response convert success");
                return (T) tempStr;
            }
            try {
                T obj = mGson.fromJson(tempStr, mType);
                return obj;
            } catch (JsonSyntaxException e) {
                Logger.d("-->response JsonSyntaxException:" + e.getMessage());
                throw new JsonParseException(mContext.getString(R.string.bwt_error_msg_server_error));
            }
        } finally {
            value.close();
        }
    }
}
