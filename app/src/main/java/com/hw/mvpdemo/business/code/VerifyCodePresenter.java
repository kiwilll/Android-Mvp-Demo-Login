package com.hw.mvpdemo.business.code;

import android.content.Context;
import android.os.UserManager;


import com.hw.mvpbase.basenetwork.consumer.BaseApiErrorConsumer;
import com.hw.mvpbase.basenetwork.consumer.BaseApiResultConsumer;
import com.hw.mvpbase.entity.BaseResponse;
import com.hw.mvpbase.util.CommonUtil;
import com.hw.mvpdemo.R;
import com.hw.mvpdemo.api.UserApi;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hw on 17/6/20.<br>
 */

public class VerifyCodePresenter extends VerifyCodeContract.Presenter {

    private Context mContext;

    private Disposable mSendCodeDisposable;
    private Disposable mCountDownDisposable;

    private boolean mCodeCountDown = false;

    public VerifyCodePresenter(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void onMobileTextChanged(String mobile) {
        if (mCodeCountDown) {
            return;
        }
        if (CommonUtil.checkPhoneNumber(mobile)) {
            getView().enableSendCodeBtn();
        } else {
            getView().disableSendCodeBtn();
        }
    }

    @Override
    public void sendVerifyCode(String flag, String mobile) {
        if (!CommonUtil.checkPhoneNumber(mobile)) {
            getView().toastMessage(mContext.getString(R.string.user_login_enter_right_mobile));
            return;
        }

        //如果是修改手机号，则要修改的手机号不能与原手机号相同
        //todo

        removeDisposable(mSendCodeDisposable);
        getView().showLoadingDialog(null, mContext.getString(R.string.user_login_getting_code));
        String userId = "";
        mSendCodeDisposable = UserApi.sendVerifyCode(userId, mobile, flag, "")
                .subscribe(new BaseApiResultConsumer<BaseResponse>() {
                    @Override
                    protected void handleResult(BaseResponse result) throws Exception {
                        super.handleResult(result);
                        getView().dismissLoadingDialog();
                        onSendCodeSuccess();
                    }
                }, new BaseApiErrorConsumer<Throwable>(mContext, getView()) {
                    @Override
                    protected void handleError(Throwable t, boolean handled) throws Exception {
                        super.handleError(t, handled);
                        t.printStackTrace();
                        mCodeCountDown = false;
                        getView().dismissLoadingDialog();
                        getView().enableSendCodeBtn();
                        if (!handled) {
                            getView().toastMessage(t.getMessage());
                        }
                    }
                });
        addDisposable(mSendCodeDisposable);
    }

    /**
     * 验证码发送成功
     */
    private void onSendCodeSuccess() {
        mCodeCountDown = true;
        getView().toastMessage(mContext.getString(R.string.user_login_code_has_sent));
        getView().disableSendCodeBtn();
        getView().focusCodeEditText();
        getView().showSoftKeyboard();
        //开始倒计时
        startCountDown();
    }

    /**
     * 开始倒计时
     */
    public void startCountDown() {
        removeDisposable(mCountDownDisposable);
        mCountDownDisposable = Observable.intervalRange(0, 61, 0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        int c = (int) (60 - aLong);
                        boolean finish = (c == 0);
                        if (finish) {
                            mCodeCountDown = false;
                            getView().enableSendCodeBtn();
                            getView().showSendCodeBtnTickText(mContext.getString(R.string.user_get_code));
                        } else {
                            getView().disableSendCodeBtn();
                            getView().showSendCodeBtnTickText(mContext.getString(R.string.user_login_resend_after_sec, c));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
        addDisposable(mCountDownDisposable);
    }

}