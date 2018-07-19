package com.hw.mvpbase.basenetwork.utils;

import com.google.gson.JsonParseException;
import com.hw.mvpbase.basenetwork.exception.NoNetworkException;
import com.hw.mvpbase.basenetwork.exception.ResponseEmptyException;
import com.hw.mvpbase.basenetwork.exception.ResponseFailException;
import com.hw.mvpbase.util.Logger;

import java.security.SignatureException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;


/**
 * Created by hw on 2018/5/18.
 */

public class RetryWithDelay implements
        Function<Observable<? extends Throwable>, Observable<?>> {

    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount;

    public RetryWithDelay(int maxRetries, int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        return observable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                Logger.d("Retry-->重试" + (retryCount + 1) + "次");
                if (throwable instanceof JsonParseException) {
                    return Observable.error(throwable);
                } else if (throwable instanceof NoNetworkException) {
                    return Observable.error(throwable);
                } else if (throwable instanceof ResponseEmptyException) {
                    return Observable.error(throwable);
                } else if (throwable instanceof ResponseFailException) {
                    return Observable.error(throwable);
                } else if (throwable instanceof SignatureException) {
                    return Observable.error(throwable);
                }
                if (++retryCount <= maxRetries) {
                    return Observable.timer(retryDelayMillis,
                            TimeUnit.MILLISECONDS);
                }
                return Observable.error(throwable);
            }
        });
    }
}
