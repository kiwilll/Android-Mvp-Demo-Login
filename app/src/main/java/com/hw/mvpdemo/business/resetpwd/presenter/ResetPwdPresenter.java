package com.hw.mvpdemo.business.resetpwd.presenter;

import android.content.Context;
import android.os.UserManager;
import android.text.TextUtils;


import com.hw.mvpbase.basenetwork.consumer.BaseApiErrorConsumer;
import com.hw.mvpbase.basenetwork.consumer.BaseApiResultConsumer;
import com.hw.mvpbase.entity.BaseResponse;
import com.hw.mvpbase.util.CommonUtil;
import com.hw.mvpdemo.R;
import com.hw.mvpdemo.api.UserApi;
import com.hw.mvpdemo.business.resetpwd.ResetPwdContract;
import com.hw.mvpdemo.business.resetpwd.views.ResetPwdActivity;

import io.reactivex.disposables.Disposable;

/**
 * Created by hw on 17/6/20.<br>
 */

public class ResetPwdPresenter extends ResetPwdContract.Presenter {

    private Context mContext;

    //0-找回密码 1-修改密码
    private int mAction;

    private Disposable mDisposable;

    public ResetPwdPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void init(int action) {
        mAction = action;
        getView().showMobile("");
        if (mAction == ResetPwdActivity.ACTION_RESET_PWD) {              //忘记密码
            getView().showTopbarTitle(mContext.getString(R.string.user_find_pwd));
        } else if (mAction == ResetPwdActivity.ACTION_MODIFY_PWD) {       //修改密码
            getView().showTopbarTitle(mContext.getString(R.string.user_modify_pwd));
            //隐藏手机号输入框
            getView().hideMobileEditText();
            //显示当前登录的手机号
            getView().showMobile("");
        }
    }

    @Override
    public void doResetPwd(String mobile, String code, String pwd1, String pwd2) {
        if (mAction == ResetPwdActivity.ACTION_RESET_PWD) {
         //todo
        } else if (mAction == ResetPwdActivity.ACTION_MODIFY_PWD) {
         //todo
        }
        modifyPwd(mobile, code, pwd1, pwd2);
    }

    /**
     * 修改或充值密码
     *
     * @param mobile 手机号
     * @param code   验证码
     * @param pwd1   密码
     * @param pwd2   重复密码
     */
    private void modifyPwd(String mobile, String code, String pwd1, String pwd2) {
        if (!CommonUtil.checkPhoneNumber(mobile)) {
            getView().toastMessage(mContext.getString(R.string.user_login_enter_right_mobile));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            getView().toastMessage(mContext.getString(R.string.user_login_enter_code));
            return;
        }
        if (code.length() != 4) {
            getView().toastMessage(mContext.getString(R.string.user_login_enter_right_code));
            return;
        }
        if (pwd1.length() < 6 || pwd1.length() > 32) {
            getView().toastMessage(mContext.getString(R.string.user_register_pwd_invalid));
            return;
        }
        if (TextUtils.isEmpty(pwd2)) {
            getView().toastMessage(mContext.getString(R.string.user_login_enter_confirm_pwd));
            return;
        }
        if (!pwd1.equals(pwd2)) {
            getView().toastMessage(mContext.getString(R.string.user_register_twopwd_not_same));
            return;
        }

        removeDisposable(mDisposable);
        getView().showLoadingDialog(null, mContext.getString(R.string.user_login_loading));
        mDisposable = UserApi.changePwd(mobile, pwd1, code)
                .subscribe(new BaseApiResultConsumer<BaseResponse>() {
                    @Override
                    protected void handleResult(BaseResponse result) throws Exception {
                        super.handleResult(result);
                        getView().dismissLoadingDialog();

                        if (mAction == ResetPwdActivity.ACTION_RESET_PWD) {
                            getView().toastMessage(mContext.getString(R.string.user_reset_succ));
                        } else {
                            getView().toastMessage(mContext.getString(R.string.user_modify_succ));
                        }
                        getView().closeCurrPage();
                    }
                }, new BaseApiErrorConsumer<Throwable>(mContext, getView()) {
                    @Override
                    protected void handleError(Throwable throwable, boolean handled) throws Exception {
                        super.handleError(throwable, handled);
                        getView().dismissLoadingDialog();
                        if (!handled) {
                            getView().toastMessage(throwable.getMessage());
                        }
                    }
                });
        addDisposable(mDisposable);
    }

    @Override
    public void onInputContentChanged(String mobile, String code, String pwd1, String pwd2) {
        if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(code) || TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2)) {
            getView().updateSubmitButtonStatus(false);
            return;
        }
        if (mobile.trim().length() == 11 && code.trim().length() == 4 && pwd1.trim().length() >= 6 && pwd2.trim().length() >= 6) {
            getView().updateSubmitButtonStatus(true);
            return;
        }
        getView().updateSubmitButtonStatus(false);
    }
}