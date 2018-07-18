package com.hw.mvpbase.basenetwork.consumer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.hw.mvpbase.R;
import com.hw.mvpbase.basenetwork.exception.ApiException;
import com.hw.mvpbase.baseview.mvp.BaseView;

import io.reactivex.functions.Consumer;

/**
 * Created by hw on 17/11/30.<br>
 * <p>
 * 接口调用里RxJava回调错误处理基类
 */

public class BaseApiErrorConsumer<T extends Throwable> implements Consumer<T> {

    private Context mContext;
    private BaseView mView;

    /**
     * 构造函数, 默认这里会处理从网络层抛出的通用异常、业务逻辑里的token失效和0088提示升级、未知异常
     *
     * @param context
     * @param view
     */
    public BaseApiErrorConsumer(Context context, @NonNull BaseView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void accept(Throwable t) throws Exception {
        boolean handled;
        if ((t instanceof ApiException)) {
            ApiException e = (ApiException) t;
            handled = handleApiException(e);
        } else {
            if (null != mView)
                mView.toastMessage(mContext.getString(R.string.base_unknown_error));
            handled = true;
        }
        handleError(t, handled);
    }

    /**
     * 处理ApiException，这里仅仅处理一些常规、全局的异常
     *
     * @param e
     * @return 异常是否被处理掉了，具体业务相关的异常由子类自己去处理
     */
    private boolean handleApiException(ApiException e) {
        //TODO 处理通用错误
        return false;
    }

    /**
     * 异常处理回调方法, 以下3种异常在这里会自动拦截处理<br/>
     * 1.token过期与0088提示升级<br/>
     * 2.网络层抛出来的通用异常, 如无网络、超时、验签失败、服务请求失败等<br/>
     * 3.未知异常<br/>
     * 其他异常由子类自行决定需不需要处理
     *
     * @param throwable 异常
     * @param handled   异常是否已经处理
     */
    protected void handleError(Throwable throwable, boolean handled) throws Exception {

    }

}