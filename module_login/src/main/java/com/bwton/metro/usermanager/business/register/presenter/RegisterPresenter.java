package com.bwton.metro.usermanager.business.register.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bwton.metro.base.mvp.consumer.BaseApiErrorConsumer;
import com.bwton.metro.base.mvp.consumer.BaseApiResultConsumer;
import com.bwton.metro.network.HttpReqManager;
import com.bwton.metro.sharedata.CityManager;
import com.bwton.metro.sharedata.UserManager;
import com.bwton.metro.sharedata.event.CommBizEvent;
import com.bwton.metro.sharedata.event.LoginSuccEvent;
import com.bwton.metro.sharedata.model.BaseResponse;
import com.bwton.metro.sharedata.sp.SharePreference;
import com.bwton.metro.tools.CommonUtil;
import com.bwton.metro.tools.SPUtil;
import com.bwton.metro.usermanager.LoginConstants;
import com.bwton.metro.usermanager.R;
import com.bwton.metro.usermanager.api.ApiConstants;
import com.bwton.metro.usermanager.api.UserApi;
import com.bwton.metro.usermanager.business.register.RegisterContract;
import com.bwton.metro.usermanager.entity.LoginResultInfo;
import com.bwton.metro.usermanager.utils.UserHelper;
import com.bwton.router.Router;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachView() {
        super.detachView();
        EventBus.getDefault().unregister(this);
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

        String deviceId = SharePreference.getInstance().getDeviceToken();
        String pushToken = SharePreference.getInstance().getPushToken();
        String bundleId = mContext.getPackageName();
        mDisposable = UserApi.doRegister(mobile, pwd, code, deviceId, pushToken, bundleId)
                .subscribe(new BaseApiResultConsumer<BaseResponse<LoginResultInfo>>() {
                    @Override
                    protected void handleResult(BaseResponse<LoginResultInfo> result) throws Exception {
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
    private void handleResponse(LoginResultInfo resultInfo) {
        String token = resultInfo != null ? resultInfo.getToken() : null;
        if(TextUtils.isEmpty(token) || resultInfo.getUser() == null) {
            getView().toastMessage("处理失败");
            return;
        }
        if(UserHelper.saveUserInfo(mContext, resultInfo, true) &&
                UserManager.getInstance(mContext).setTokenAndLoginStatus(token, true)) {
            HttpReqManager.getInstance().setToken(token);
            HttpReqManager.getInstance().setUserId(resultInfo.getUser().getUser_id());
            String mobile = resultInfo.getUser().getMobile_phone();
            SPUtil.put(mContext, LoginConstants.LOGIN_SP_FILE_NAME, LoginConstants.LOGIN_SP_CURR_MOBILE_KEY, mobile);
            //发出登录成功的通知事件
            EventBus.getDefault().post(new LoginSuccEvent());
            //发出更新公告的通知
            EventBus.getDefault().post(new CommBizEvent("BWTRefreshUnreadMsgCount", ""));
            SPUtil.put(mContext, LoginConstants.LOGIN_SP_FILE_NAME, LoginConstants.LOGIN_SP_AGREEMENT_KEY_REGISTER, true);
            getView().closeCurrPage();
        } else {
            UserManager.getInstance(mContext).setTokenAndLoginStatus("", false);
            getView().toastMessage("保存用户信息出错");
        }
    }

    @Override
    public void clickUserAgreement() {
        Router.getInstance().buildWithUrl(ApiConstants.URL_WEBVIEW)
                .withString("url", ApiConstants.getYongHuXieYi(CityManager.getCurrCityId() + ""))
                .withString("title", "用户协议")
                .navigation(mContext);
    }

    @Override
    public void getAgreementStatus() {
        if(SPUtil.getBoolean(mContext, LoginConstants.LOGIN_SP_FILE_NAME, LoginConstants.LOGIN_SP_AGREEMENT_KEY_REGISTER, false)){
            getView().updateAgreementStatus(true);
        }
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

    /**
     * 收到登录成功的消息
     *
     * @param event 登录成功事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccEvent(LoginSuccEvent event) {
        getView().closeCurrPage();
    }

}