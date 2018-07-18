package com.hw.mvpbase.basenetwork.exception;

import java.io.IOException;

/**
 * Created by hw on 4/21/17.<br>
 */

public class ApiException extends IOException {

    private String code;

    public ApiException(String detailMessage) {
        this(null, detailMessage);
    }

    public ApiException(String code, String detailMessage) {
        super(detailMessage);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
