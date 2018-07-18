package com.hw.mvpbase.basenetwork.consumer;


import com.hw.mvpbase.basenetwork.exception.ApiException;
import com.hw.mvpbase.entity.BaseResponse;

import io.reactivex.functions.Consumer;

/**
 * Created by hw on 17/11/30.<br>
 *
 * 接口调用里RxJava回调结果处理基类
 */

public class BaseApiResultConsumer<T> implements Consumer<T>{


    public BaseApiResultConsumer() {
    }

    @Override
    public void accept(T t) throws Exception {
        if(t instanceof BaseResponse) {
            BaseResponse response = (BaseResponse) t;
            if(!response.isSuccessfull()) {
                //如果response返回异常, 则全部转到异常处理代码里去
                throw new ApiException(response.getErrcode(), response.getErrmsg());
            }
        }
        handleResult(t);
    }

    /**
     * 处理成功返回的结果
     *
     * @param result
     * @throws Exception
     */
    protected void handleResult(T result) throws Exception{

    }

}
