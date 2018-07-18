package com.hw.mvpbase.entity;

/**
 * Created by hasee on 2018/3/26.
 */

public class BaseResponse<T> {
    private String errcode;

    private String errmsg;

    private T result;

    public boolean isSuccessfull() {
        if ("0000".equals(errcode))
            return true;
        return false;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
