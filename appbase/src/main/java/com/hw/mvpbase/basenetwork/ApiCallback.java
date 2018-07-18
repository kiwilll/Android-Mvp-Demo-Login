package com.hw.mvpbase.basenetwork;

/**
 * Created by hw on 5/10/17.<br>
 */

public interface ApiCallback<T> {

    void onSucc(T data);

    void onError(Throwable throwable);

}