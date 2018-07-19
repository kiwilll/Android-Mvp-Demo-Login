package com.hw.mvpdemo.business.demo;

import com.hw.mvpbase.baseview.mvp.AbstractPresenter;
import com.hw.mvpbase.baseview.mvp.BaseView;
import com.hw.mvpdemo.business.login.LoginContract;

/**
 * Created by hw on 2018/7/18.
 */

public interface DemoContract {

    interface View extends BaseView {
        /**
         * 页面处理
         *
         * @param something 待显示的内容
         */
        void showSomething(String something);
    }

    abstract class Presenter extends AbstractPresenter<DemoContract.View> {

        /**
         * 业务功能
         */
        public abstract void doSomething();

    }

}
