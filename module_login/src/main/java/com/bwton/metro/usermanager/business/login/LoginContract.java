package com.bwton.metro.usermanager.business.login;

import com.bwton.metro.base.mvp.AbstractPresenter;
import com.bwton.metro.base.mvp.BaseView;

/**
 * Created by hw on 17/12/6.<br>
 */

public interface LoginContract {

    interface View extends BaseView {

        /**
         * 显示上一次登录的手机号码
         *
         * @param mobile 手机号码
         */
        void showLastLoginMobile(String mobile);

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
         * 在登录前先清除没有删除的老用户信息数据
         */
        public abstract void clearUserInfoBeforeLogin();

        /**
         * 获取上次登录过的手机号码
         */
        public abstract void getLastLoginMobile();

        /**
         * 快速登录
         *
         * @param mobile           手机号
         * @param code             验证码
         * @param agreementChecked 用户协议是否同意
         */
        public abstract void doQuickLogin(String mobile, String code, boolean agreementChecked);

        /**
         * 通过密码登录
         *
         * @param mobile    手机号
         * @param pwd       密码
         */
        public abstract void doLoginByPwd(String mobile, String pwd);

        /**
         * 点击注册
         */
        public abstract void clickRegister();

        /**
         * 点击密码登录
         */
        public abstract void clickPwdLogin();

        /**
         * 点击忘记密码
         */
        public abstract void clickForgetPwd();

        /**
         * 点击用户协议
         */
        public abstract void clickUserAgreement();

        /**
         * 更新登录协议选中状态
         */
        public abstract void getAgreementStatus();

        /**
         * 监听快速登录输入框文本的变化
         *
         * @param mobile 手机号
         * @param code 验证码
         */
        public abstract void onQuickLoginContentChanged(String mobile, String code);

        /**
         * 监听密码登录输入框文本的变化
         *
         * @param mobile 手机号
         * @param pwd   密码
         */
        public abstract void onPwdLoginContentChanged(String mobile, String pwd);
    }

}