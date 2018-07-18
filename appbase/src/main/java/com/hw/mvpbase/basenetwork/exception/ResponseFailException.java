package com.hw.mvpbase.basenetwork.exception;

/**
 * Created by hw on 5/5/17.<br>
 *
 * 当http请求返回的状态码不为[200, 300)时抛出的异常
 */

public class ResponseFailException extends ApiException {

    public ResponseFailException(String message) {
        super(message);
    }
}
