package com.hw.mvpbase.basenetwork.exception;

/**
 * Created by hw on 5/10/17.<br>
 *
 * 当json解析错误时抛出异常
 */

public class JsonParseException extends ApiException {

    public JsonParseException(String message) {
        super(message);
    }
}
