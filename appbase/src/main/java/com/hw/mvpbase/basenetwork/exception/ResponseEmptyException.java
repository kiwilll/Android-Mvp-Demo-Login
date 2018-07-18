package com.hw.mvpbase.basenetwork.exception;

/**
 * Created by hw on 17/11/28.<br>
 *
 * 请求返回结果为空时抛出的异常
 */

public class ResponseEmptyException extends ApiException {

    public ResponseEmptyException(String detailMessage) {
        super(detailMessage);
    }
}
