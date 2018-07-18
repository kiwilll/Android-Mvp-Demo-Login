package com.hw.mvpdemo.business.register.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hw.mvpbase.basenetwork.consumer.BaseApiErrorConsumer;
import com.hw.mvpbase.basenetwork.consumer.BaseApiResultConsumer;
import com.hw.mvpbase.entity.BaseResponse;
import com.hw.mvpbase.util.CommonUtil;
import com.hw.mvpdemo.R;
import com.hw.mvpdemo.api.UserApi;
import com.hw.mvpdemo.business.register.RegisterContract;

import io.reactivex.disposables.Disposable;

/**
 * Created by hw on 17/6/20.<br>
 */

public class RegisterPresenter extends RegisterContract.Presenter {

    private Context mContext;

    private Disposable mDisposable;

    public RegisterPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void attachView(@NonNull RegisterContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        mContext = null;
    }

    @Override
    public void doRegister(String mobile, String pwd, String confirmPwd, String code, boolean agreementChecked) {
        if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(confirmPwd) || TextUtils.isEmpty(code)) {
            getView().toastMessage(mContext.getString(R.string.user_login_enter_complete_info));
            return;
        }
        if (!CommonUtil.checkPhoneNumber(mobile)) {
            getView().toastMessage(mContext.getString(R.string.user_login_enter_right_mobile));
            return;
        }
        if (pwd.length() < 6 || pwd.length() > 32 || confirmPwd.length() < 6 || confirmPwd.length() > 32) {
            getView().toastMessage(mContext.getString(R.string.user_register_pwd_invalid));
            return;
        }
        if (!pwd.equals(confirmPwd)) {
            getView().toastMessage(mContext.getString(R.string.user_register_twopwd_not_same));
            return;
        }
        if (code.length() != 4) {
            getView().toastMessage(mContext.getString(R.string.user_login_enter_right_code));
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
        mDisposable = UserApi.doRegister(mobile, pwd, code, deviceId, pushToken, bundleId)
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
                        if(!handled) {
                            getView().toastMessage(t.getMessage());
                        }
                    }
                });;

        addDisposable(mDisposable);
    }

    /**
     * 处理登录返回的成功结果
     *
     * @param resultInfo
     */
    private void handleResponse(Object resultInfo) {
    //todo  保存或更新用户数据
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