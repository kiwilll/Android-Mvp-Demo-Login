package com.hw.mvpbase.basenetwork.exception;

/**
 * Created by hw on 5/11/17.<br>
 *
 * 当没有网络连接时抛出的异常
 */

public class NoNetworkException extends ApiException {

    public NoNetworkException(String message) {
        super(message);
    }

}
