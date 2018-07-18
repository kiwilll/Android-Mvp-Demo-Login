package com.hw.mvpbase.basenetwork.exception;

/**
 * Created by hw on 4/21/17.<br>
 *
 * 当验签失败时抛出的异常
 */

public class SignInvalidException extends ApiException {


    public SignInvalidException(String detailMessage) {
        super(detailMessage);
    }

}
