package com.hw.mvpdemo.business.register;


import com.hw.mvpbase.baseview.mvp.AbstractPresenter;
import com.hw.mvpbase.baseview.mvp.BaseView;

/**
 * Created by hw on 17/12/6.<br>
 */

public interface RegisterContract {

    interface View extends BaseView {

        void updateAgreementStatus(boolean checked);

        /**
         * 更新提交按钮的点击状态
         *
         * @param enabled
         */
        void updateSubmitButtonStatus(boolean enabled);
    }

    abstract class Presenter extends AbstractPresenter<View> {

        /**
         * 注册
         *
         * @param mobile           手机号
         * @param pwd              密码
         * @param confirmPwd       确认密码
         * @param code             验证码
         * @param agreementChecked 用户协议是否同意
         */
        public abstract void doRegister(String mobile, String pwd, String confirmPwd, String code, boolean agreementChecked);

        /**
         * 点击用户协议
         */
        public abstract void clickUserAgreement();

        /**
         * 更新登录协议选中状态
         */
        public abstract void getAgreementStatus();

        /**
         * 监听输入框里文本变化
         *
         * @param mobile 手机号
         * @param code 验证码
         * @param pwd1 密码
         * @param pwd2 确认密码
         */
        public abstract void onInputContentChanged(String mobile, String code, String pwd1, String pwd2);
    }

}