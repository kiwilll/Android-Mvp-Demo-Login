package com.hw.mvpdemo.business.code;


import com.hw.mvpbase.baseview.mvp.AbstractPresenter;
import com.hw.mvpbase.baseview.mvp.BaseView;

/**
 * Created by hw on 2017/6/20.
 */

public interface VerifyCodeContract {

    interface View extends BaseView {

        /**
         * 设置发送验证码按钮可点击
         */
        void enableSendCodeBtn();

        /**
         * 设置发送验证码按钮不可点击
         */
        void disableSendCodeBtn();

        /**
         * 发送验证码输入框获取焦点
         */
        void focusCodeEditText();

        /**
         * 设置发送验证码的文字
         *
         * @param tickText
         */
        void showSendCodeBtnTickText(String tickText);

    }

    abstract class Presenter extends AbstractPresenter<View> {

        /**
         * 发送验证码
         *
         * @param flag 验证码类型
         * @param mobile 手机号
         */
        public abstract void sendVerifyCode(String flag, String mobile);

        /**
         * 手机号输入框内容发生变化
         *
         * @param mobile
         */
        public abstract void onMobileTextChanged(String mobile);
    }

}