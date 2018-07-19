package com.hw.mvpdemo.business.demo;

import android.app.Activity;

import com.hw.mvpbase.baseview.BaseActivity;

/**
 * Created by hw on 2018/7/18.
 */

public class DemoActivity extends BaseActivity implements DemoContract.View {

    @Override
    public void showSomething(String something) {

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public String getPageTitle() {
        return null;
    }
}
