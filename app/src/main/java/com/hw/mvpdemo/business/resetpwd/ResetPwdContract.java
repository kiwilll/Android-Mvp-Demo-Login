package com.hw.mvpdemo.business.resetpwd;


import com.hw.mvpbase.baseview.mvp.AbstractPresenter;
import com.hw.mvpbase.baseview.mvp.BaseView;

/**
 * Created by hw on 17/12/8.<br>
 */

public interface ResetPwdContract {

    interface View extends BaseView {

        /**
         * 显示title
         *
         * @param title
         */
        void showTopbarTitle(String title);

        /**
         * 隐藏手机号输入框
         */
        void hideMobileEditText();

        void showMobile(String mobile);

        void updateSubmitButtonStatus(boolean enabled);
    }

    abstract class Presenter extends AbstractPresenter<View> {

        /**
         * 初始化
         *
         * @param action
         */
        public abstract void init(int action);

        /**
         * 充值密码
         *
         * @param mobile 手机号
         * @param code   验证码
         * @param pwd1   密码
         * @param pwd2   重复密码
         */
        public abstract void doResetPwd(String mobile, String code, String pwd1, String pwd2);

        public abstract void onInputContentChanged(String mobile, String code, String pwd1, String pwd2);
    }

}
