package com.hw.mvpdemo.business.login.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.hw.mvpbase.basenetwork.consumer.BaseApiErrorConsumer;
import com.hw.mvpbase.basenetwork.consumer.BaseApiResultConsumer;
import com.hw.mvpbase.basenetwork.exception.ApiException;
import com.hw.mvpbase.entity.BaseResponse;
import com.hw.mvpbase.util.CommonUtil;
import com.hw.mvpbase.util.NetUtil;
import com.hw.mvpdemo.R;
import com.hw.mvpdemo.api.ApiConstants;
import com.hw.mvpdemo.api.UserApi;
import com.hw.mvpdemo.business.login.LoginContract;

import io.reactivex.disposables.Disposable;

/**
 * Created by hw on 17/6/20.<br>
 */

public class LoginPresenter extends LoginContract.Presenter {

    private Context mContext;

    private Disposable mDisposable;

    public LoginPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void attachView(@NonNull LoginContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        mContext = null;
    }

    @Override
    public void getLastLoginMobile() {
        String mobile = "";
        getView().showLastLoginMobile(mobile);
    }

    @Override
    public void clearUserInfoBeforeLogin() {

    }

    @Override
    public void doLoginByPwd(final String mobile, final String pwd) {
        if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(pwd)) {
            getView().toastMessage(mContext.getString(R.string.user_login_enter_mobile_pwd));
            return;
        }
        if (!CommonUtil.checkPhoneNumber(mobile)) {
            getView().toastMessage(mContext.getString(R.string.user_login_enter_right_mobile));
            return;
        }
        if (pwd.length() < 6 || pwd.length() > 32) {
            getView().toastMessage(mContext.getString(R.string.user_login_enter_right_pwd));
            return;
        }
        if (!NetUtil.isConnected(mContext)) {
            getView().toastMessage(mContext.getString(R.string.user_login_pls_connect_net));
            return;
        }

        removeDisposable(mDisposable);
        getView().showLoadingDialog(null, mContext.getString(R.string.user_login_loading));

        String deviceId = "";
        String pushToken = "";
        String bundleId = mContext.getPackageName();
        mDisposable = UserApi.doLoginByPwd(mobile, pwd, deviceId, pushToken, bundleId)
                .subscribe(new BaseApiResultConsumer<BaseResponse>() {
                    @Override
                    protected void handleResult(BaseResponse result) throws Exception {
                        super.handleResult(result);
                        getView().dismissLoadingDialog();
                        handleResponse(result.getResult());
                    }
                }, new BaseApiErrorConsumer<Throwable>(mContext, getView()) {
                    @Override
                    public void handleError(Throwable t, boolean handled) throws Exception {
                        super.handleError(t, handled);
                        getView().dismissLoadingDialog();
                        if (!handled) {
                            if (t instanceof ApiException) {
                                handleException((ApiException) t);
                            } else {
                                getView().toastMessage(t.getMessage());
                            }
                        }
                    }
                });

        addDisposable(mDisposable);
    }

    @Override
    public void doQuickLogin(final String mobile, final String code, boolean agreementChecked) {
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
        if (!NetUtil.isConnected(mContext)) {
            getView().toastMessage(mContext.getString(R.string.user_login_pls_connect_net));
            return;
        }
        if (!agreementChecked) {
            getView().toastMessage(mContext.getString(R.string.user_login_agreement));
            getView().hideSoftKeyboard();
            return;
        }

        removeDisposable(mDisposable);
        getView().showLoadingDialog(null, mContext.getString(R.string.user_login_loading));

        String deviceId = "";
        String pushToken = "";
        String bundleId = mContext.getPackageName();
        mDisposable = UserApi.doLoginByCode(mobile, code, deviceId, pushToken, bundleId)
                .subscribe(new BaseApiResultConsumer<BaseResponse>() {
                    @Override
                    protected void handleResult(BaseResponse result) throws Exception {
                        super.handleResult(result);
                        getView().dismissLoadingDialog();
                        handleResponse(result.getResult());
                    }
                }, new BaseApiErrorConsumer<Throwable>(mContext, getView()) {
                    @Override
                    public void handleError(Throwable t, boolean handled) throws Exception {
                        super.handleError(t, handled);
                        getView().dismissLoadingDialog();
                        if (!handled) {
                            if (t instanceof ApiException) {
                                handleException((ApiException) t);
                            } else {
                                getView().toastMessage(t.getMessage());
                            }
                        }
                    }
                });

        addDisposable(mDisposable);
    }

    /**
     * 处理登录返回的成功结果
     *
     * @param resultInfo 登录返回结果
     */
    private void handleResponse(Object resultInfo) {
        //todo 保存用户数据
    }

    /**
     * 处理异常
     *
     * @param e 异常
     */
    private void handleException(ApiException e) {
        e.printStackTrace();
        String code = e.getCode();
        if (ApiConstants.HAVE_NO_USER.equals(code) ||
                ApiConstants.DEVICE_LIMIT.equals(code) ||
                ApiConstants.BLACK_LIST_USER.equals(code) ||
                ApiConstants.USER_STATE_ERROR.equals(code)) {
            getView().showAlertDialog(null, e.getMessage(), null, null);
        } else {
            getView().toastMessage(e.getMessage());
        }
    }

    @Override
    public void clickRegister() {
        //todo
    }

    @Override
    public void clickPwdLogin() {
        //todo
    }

    @Override
    public void clickForgetPwd() {
        //todo
    }

    @Override
    public void clickUserAgreement() {
        //todo
    }

    @Override
    public void getAgreementStatus() {
        //todo
    }

    @Override
    public void onQuickLoginContentChanged(String mobile, String code) {
        if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(code)) {
            getView().updateSubmitButtonStatus(false);
            return;
        }
        if (mobile.trim().length() == 11 && code.trim().length() == 4) {
            getView().updateSubmitButtonStatus(true);
            return;
        }
        getView().updateSubmitButtonStatus(false);
    }

    @Override
    public void onPwdLoginContentChanged(String mobile, String pwd) {
        if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(pwd)) {
            getView().updateSubmitButtonStatus(false);
            return;
        }
        if (mobile.trim().length() == 11 && pwd.trim().length() >= 6 && pwd.trim().length() <= 32) {
            getView().updateSubmitButtonStatus(true);
            return;
        }
        getView().updateSubmitButtonStatus(false);
    }

}