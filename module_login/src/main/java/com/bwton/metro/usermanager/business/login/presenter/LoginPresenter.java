package com.bwton.metro.usermanager.business.login.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bwton.metro.base.mvp.consumer.BaseApiErrorConsumer;
import com.bwton.metro.base.mvp.consumer.BaseApiResultConsumer;
import com.bwton.metro.basebiz.CommBizManager;
import com.bwton.metro.network.HttpReqManager;
import com.bwton.metro.network.exception.ApiException;
import com.bwton.metro.sharedata.CityManager;
import com.bwton.metro.sharedata.UserManager;
import com.bwton.metro.sharedata.event.CommBizEvent;
import com.bwton.metro.sharedata.event.LoginSuccEvent;
import com.bwton.metro.sharedata.model.BaseResponse;
import com.bwton.metro.sharedata.sp.SharePreference;
import com.bwton.metro.tools.CommonUtil;
import com.bwton.metro.tools.NetUtil;
import com.bwton.metro.tools.SPUtil;
import com.bwton.metro.usermanager.LoginConstants;
import com.bwton.metro.usermanager.R;
import com.bwton.metro.usermanager.api.ApiConstants;
import com.bwton.metro.usermanager.api.UserApi;
import com.bwton.metro.usermanager.business.login.LoginContract;
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

public class LoginPresenter extends LoginContract.Presenter {

    private Context mContext;

    private Disposable mDisposable;

    public LoginPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void attachView(@NonNull LoginContract.View view) {
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
    public void getLastLoginMobile() {
        String mobile = SPUtil.getString(mContext, LoginConstants.LOGIN_SP_FILE_NAME, LoginConstants.LOGIN_SP_CURR_MOBILE_KEY, "");
        getView().showLastLoginMobile(mobile);
    }

    @Override
    public void clearUserInfoBeforeLogin() {
        HttpReqManager.getInstance().setToken("");
        HttpReqManager.getInstance().setUserId("");
        UserManager.getInstance(mContext).setTokenAndLoginStatus("", false);
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

        String deviceId = SharePreference.getInstance().getDeviceToken();
        String pushToken = SharePreference.getInstance().getPushToken();
        String bundleId = mContext.getPackageName();
        mDisposable = UserApi.doLoginByPwd(mobile, pwd, deviceId, pushToken, bundleId)
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
                        if(t instanceof ApiException) {
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

        String deviceId = SharePreference.getInstance().getDeviceToken();
        String pushToken = SharePreference.getInstance().getPushToken();
        String bundleId = mContext.getPackageName();
        mDisposable = UserApi.doLoginByCode(mobile, code, deviceId, pushToken, bundleId)
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
                            if(t instanceof ApiException) {
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
    private void handleResponse(LoginResultInfo resultInfo) {
        String token = resultInfo != null ? resultInfo.getToken() : null;
        if(TextUtils.isEmpty(token) || resultInfo.getUser() == null) {
            getView().toastMessage("登录失败");
            return;
        }
        if(UserHelper.saveUserInfo(mContext, resultInfo, true) &&
                UserManager.getInstance(mContext).setTokenAndLoginStatus(token, true)) {
            //http请求设置token
            HttpReqManager.getInstance().setToken(token);
            HttpReqManager.getInstance().setUserId(resultInfo.getUser().getUser_id());
            String mobile = resultInfo.getUser().getMobile_phone();
            //保存当前登录的手机号
            SPUtil.put(mContext, LoginConstants.LOGIN_SP_FILE_NAME, LoginConstants.LOGIN_SP_CURR_MOBILE_KEY, mobile);
            //发出登录成功的通知
            EventBus.getDefault().post(new LoginSuccEvent());
            //发出更新公告的通知
            EventBus.getDefault().post(new CommBizEvent("BWTRefreshUnreadMsgCount", ""));
            //登录成功后刷新用户信息
            CommBizManager.getInstance().refreshUserInfoAsync(mContext);

            SPUtil.put(mContext, LoginConstants.LOGIN_SP_FILE_NAME, LoginConstants.LOGIN_SP_AGREEMENT_KEY, true);
            getView().closeCurrPage();
        } else {
            UserManager.getInstance(mContext).setTokenAndLoginStatus("", false);
            getView().toastMessage("保存用户信息出错");
        }
    }

    /**
     * 处理异常
     *
     * @param e 异常
     */
    private void handleException(ApiException e) {
        e.printStackTrace();
        String code = e.getCode();
        if(ApiConstants.HAVE_NO_USER.equals(code) ||
                ApiConstants.DEVICE_LIMIT.equals(code) ||
                ApiConstants.BLACK_LIST_USER.equals(code) ||
                ApiConstants.USER_STATE_ERROR.equals(code) ){
            getView().showAlertDialog(null, e.getMessage(), null, null);
        } else {
            getView().toastMessage(e.getMessage());
        }
    }

    @Override
    public void clickRegister() {
        Router.getInstance().buildWithUrl(ApiConstants.URL_REGISTER)
                .navigation(mContext);
    }

    @Override
    public void clickPwdLogin() {
        Router.getInstance().buildWithUrl(ApiConstants.URL_LOGIN_BY_PWD)
                .navigation(mContext);
    }

    @Override
    public void clickForgetPwd() {
        Router.getInstance().buildWithUrl(ApiConstants.URL_FIND_PWD)
                .navigation(mContext);
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
        if(SPUtil.getBoolean(mContext, LoginConstants.LOGIN_SP_FILE_NAME, LoginConstants.LOGIN_SP_AGREEMENT_KEY, false)){
            getView().updateAgreementStatus(true);
        }
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